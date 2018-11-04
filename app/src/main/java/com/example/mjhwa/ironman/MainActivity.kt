package com.example.mjhwa.ironman

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.mjhwa.ironman.R.layout.activity_main
import com.example.mjhwa.ironman.bluetooth.BluetoothManager
import com.example.mjhwa.ironman.utils.Const
import com.example.mjhwa.ironman.utils.Logs
import com.example.mjhwa.ironman.views.BaristaActivity
import com.example.mjhwa.ironman.views.MenuActivity
import com.example.mjhwa.ironman.views.NormalActivity
import com.example.mjhwa.ironman.views.SignActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {
    companion object {
        val TAG = "MainActivity"
    }

    // 이메일, 비밀번호 로그인 모듈 변수
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    // 현재 로그인된 유저 정보를 담을 변수
    val currentUser: FirebaseUser? = mAuth.currentUser
    private val mBtHandler = BluetoothHandler()
    private val mBluetoothManager: BluetoothManager = BluetoothManager.getInstance()
    private var mPrevUpdateTime = 0L
    private var mIsConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        val intent = getIntent()
        val sId:String = intent.getStringExtra("ID")
        val sLR:String = intent.getStringExtra("LR")

        Logs.d(TAG, "# MainActivity - onCreate()")
        if(sId != null) {
            Toast.makeText(this, sId + "님, 환영합니다!",Toast.LENGTH_SHORT).show()
        } // 로그인했을 때 토스트 메세지

        if(!checkBluetooth()) {
            scan_device.isEnabled = false
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, Const.REQUEST_ENABLE_BT)
        } else {
            scan_device.isEnabled = true
        }
        mBluetoothManager.setHandler(mBtHandler)

        // Register for broadcasts when a device is discovered
        var filter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        this.registerReceiver(mReceiver, filter)

        initialize()

        btNormal.setOnClickListener {
            val intent = Intent(this, NormalActivity::class.java)
            intent.putExtra("ID", sId)
            intent.putExtra("LR", sLR)
            startActivity(intent)
            Toast.makeText(this, "일반 모드를 시작합니다.", Toast.LENGTH_SHORT).show()
        }

        btBarista.setOnClickListener {
            val intent = Intent(this, BaristaActivity::class.java)
            intent.putExtra("ID", sId)
            intent.putExtra("LR", sLR)
            startActivity(intent)
            Toast.makeText(this, "바리스타 모드를 시작합니다.", Toast.LENGTH_SHORT).show()
        }

        btSignout.setOnClickListener{ // 로그아웃 버튼
            val alertDiag = AlertDialog.Builder(this)
            alertDiag.setMessage("로그아웃 하시겠습니까?") // alert 다이얼로그 메세지
            alertDiag.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                mAuth!!.signOut()
                finish()
                startActivity(Intent(this, MenuActivity::class.java))
            }
            alertDiag.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

            val alert = alertDiag.create()
            alert.setTitle("Log Out")
            alert.show()
        }
    }

    override fun onBackPressed() {
        // Exit dialog
        val alertDiag = AlertDialog.Builder(this)
        alertDiag.setMessage("Exit app?")
        alertDiag.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            // finish app
            finishApp()
        }
        alertDiag.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        val alert = alertDiag.create()
        alert.setTitle("Exit")
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        finalizeActivity()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        finalizeActivity()
    }

    private fun initialize() {
        // initialize UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bt_image.setImageDrawable(resources.getDrawable(R.drawable.bt, null))
        } else {
            bt_image.setImageDrawable(resources.getDrawable(R.drawable.bt))
        }
        bt_status.text = resources.getString(R.string.bt_wait)

        scan_device.setOnClickListener {
            if(mIsConnected)
                disconnect()
            else
                doScan()
        }

        /* btn_send.setOnClickListener {
            if(edit_chat.text.isNotEmpty()) {
                mBluetoothManager.write(edit_chat.text.toString().toByteArray())
            }
            edit_chat.text.clear()
        }*/

        scanned.setOnClickListener {
            ensureDiscoverable()
        }
    }

    private fun finalizeActivity() {
        Logs.d(TAG, "# Activity - finalizeActivity()")

        // Close bluetooth connection
        mBluetoothManager.stop()
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver)
    }

    private fun finishApp() {
        ActivityCompat.finishAffinity(this);
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    private fun checkBluetooth(): Boolean {
        return mBluetoothManager.isBluetoothEnabled()
    }

    /**
     * Launch the DeviceListActivity to see devices and do scan
     */
    private fun doScan() {
        val intent = Intent(this, DeviceListActivity::class.java)
        startActivityForResult(intent, Const.REQUEST_CONNECT_DEVICE)
    }

    /**
     * Ensure this device is discoverable by others
     */
    private fun ensureDiscoverable() {
        if (mBluetoothManager.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30)
            startActivityForResult(intent, Const.REQUEST_DISCOVERABLE)

            // text_chat.append("\n")
            // text_chat.append(resources.getText(R.string.discoverable_desc))
        } else {
            // text_chat.append("\n")
            // text_chat.append(resources.getText(R.string.discoverable_fail_desc))
        }
    }

    private fun setConnected(connected: Boolean) {
        mIsConnected = connected
        if(connected) { // 블루투스 연결되었을 때
            // btn_send.isEnabled = true
            scan_device.text = resources.getText(R.string.disconnect)  // 블루투스 끊기
        } else { // 블루투스 연결 안 되었을 때
            // btn_send.isEnabled = false
            scan_device.text = resources.getText(R.string.scan_device)
        }
    }

    private fun disconnect() {
        mBluetoothManager.stop()
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            // When discovery finds a device
            if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED == action) {
                val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1)
                val prevMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, -1)
                when(scanMode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                        scan_device.isEnabled = false // 직접 스캔
                        scanned.isEnabled = false // 스캔 가능 모드
                        // text_chat.append("\nSCAN_MODE_CONNECTABLE_DISCOVERABLE")
                        // text_chat.append("\nMake server socket")

                        mBluetoothManager.start()
                    }
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                        scan_device.isEnabled = true
                        scanned.isEnabled = true
                        // text_chat.append("\nSCAN_MODE_CONNECTABLE")
                    }
                    BluetoothAdapter.SCAN_MODE_NONE -> {
                        // Bluetooth is not enabled
                        scan_device.isEnabled = false
                        scanned.isEnabled = false
                        // text_chat.append("\nBluetooth is not enabled!!")
                    }
                }
            }
        }
    }

    inner class BluetoothHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                /*BluetoothManager.MESSAGE_READ -> {
                    if (msg.obj != null) {
                        val currentTime = System.currentTimeMillis()
                        if(mPrevUpdateTime + 1000 < currentTime) {
                            text_chat.append("\n")
                            mPrevUpdateTime = currentTime
                        }
                        if(text_chat.text.length > 10000) {
                            text_chat.text = ""
                        }
                        text_chat.append((msg.obj as ByteArray).toString(Charset.defaultCharset()))
                    }
                }*/
                BluetoothManager.MESSAGE_STATE_CHANGE -> {
                    when(msg.arg1) {
                        BluetoothManager.STATE_NONE -> {    // we're doing nothing
                            bt_status.text = resources.getString(R.string.bt_disconnected)
                            bt_image.setImageDrawable(resources.getDrawable(R.drawable.bt))
                            setConnected(false)
                        }
                        BluetoothManager.STATE_LISTEN -> {  // now listening for incoming connections
                            bt_status.text = resources.getString(R.string.bt_wait)
                            bt_image.setImageDrawable(resources.getDrawable(R.drawable.bt))
                            setConnected(false)
                        }
                        BluetoothManager.STATE_CONNECTING -> {  // connecting to remote
                            bt_status.text = resources.getString(R.string.bt_connecting)
                            bt_image.setImageDrawable(resources.getDrawable(R.drawable.bt_connecting))
                        }
                        BluetoothManager.STATE_CONNECTED -> {   // now connected to a remote device
                            bt_status.text = resources.getString(R.string.bt_connected)
                            bt_image.setImageDrawable(resources.getDrawable(R.drawable.bt_connected))
                            setConnected(true)
                        }
                    }
                }
                BluetoothManager.MESSAGE_DEVICE_NAME -> {
                    if(msg.data != null) {
                        val deviceName = msg.data.getString(BluetoothManager.MSG_DEVICE_NAME)
                        val deviceAddr = msg.data.getString(BluetoothManager.MSG_DEVICE_ADDRESS)
                        bt_status.append(" to ")
                        bt_status.append(deviceName)
                        bt_status.append(", ")
                        bt_status.append(deviceAddr)
                    }
                }
            }

            super.handleMessage(msg)
        }
    }

    /**
     * Receives result from external activity
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Logs.d(TAG, "onActivityResult $resultCode")

        when (requestCode) {
            Const.REQUEST_CONNECT_DEVICE -> {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    val address = data?.extras?.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS)
                    // Attempt to connect to the device
                    mBluetoothManager.connect(address)
                }
            }
            Const.REQUEST_ENABLE_BT -> {
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a BT session
                    scan_device.isEnabled = true
                } else {
                    // User did not enable Bluetooth or an error occured
                    Logs.e(TAG, "BT is not enabled")
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show()
                    scan_device.isEnabled = false
                }
            }
            Const.REQUEST_DISCOVERABLE -> {
                // resultCode is always false
            }
        }    // End of switch(requestCode)
    }
}
