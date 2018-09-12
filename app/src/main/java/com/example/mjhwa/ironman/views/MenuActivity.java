package com.example.mjhwa.ironman.views;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mjhwa.ironman.R;
import com.example.mjhwa.ironman.bluetooth.DeviceListActivity;
import com.example.mjhwa.ironman.service.BTCTemplateService;
import com.example.mjhwa.ironman.utils.AppSettings;
import com.example.mjhwa.ironman.utils.Constants;
import com.example.mjhwa.ironman.utils.Logs;
import com.example.mjhwa.ironman.utils.RecycleUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.mjhwa.ironman.fragments.FragmentAdapter.TAG;

public class MenuActivity extends Activity {

    // 이메일, 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    // 현재 로그인된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    private TextView mTextMessage;

    // Debugging
    private static final String TAG = "BLEActivity";

    Intent intent = getIntent();

    // Context, System
    private Context mContext;
    private BTCTemplateService mService;
    private MenuActivity.ActivityHandler mActivityHandler;

    private ImageView bleImage = null;
    private TextView bleStatus = null;

    TextView scan_device; // ble 스캔 클릭
    Button btNormal, btBarista;

    // Refresh timer
    private Timer mRefreshTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();

        bleImage = (ImageView) findViewById(R.id.ble_image);
        bleImage.setImageDrawable(getResources().getDrawable(R.drawable.no_bluetooth));
        bleStatus = (TextView) findViewById(R.id.ble_status);
        bleStatus.setText(getResources().getString(R.string.ble_disconnected));

        scan_device = (TextView) findViewById(R.id.scan_device);
        btNormal = (Button) findViewById(R.id.btNormal);
        btBarista = (Button) findViewById(R.id.btBarista);

        scan_device.setOnClickListener(mClickListener);
        btNormal.setOnClickListener(mClickListener);
        btBarista.setOnClickListener(mClickListener);

    }

    @Override
    public synchronized void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        // Stop the timer
        if(mRefreshTimer != null) {
            mRefreshTimer.cancel();
            mRefreshTimer = null;
        }
        super.onStop();
    }

    private Button.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scan_device:
                    // DeviceListActivity로 넘어가서 장치 스캔 시작
                    doScan();
                    break;
                case R.id.btNormal:
                    startActivity(new Intent(MenuActivity.this, NormalActivity.class));
                    Toast.makeText(MenuActivity.this, "일반 모드를 시작합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btBarista:
                    startActivity(new Intent(MenuActivity.this, BaristaActivity.class));
                    Toast.makeText(MenuActivity.this, "바리스타 모드를 시작합니다.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * Service connection
     */
    private ServiceConnection mServiceConn = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, "Activity - Service connected");

            mService = ((BTCTemplateService.ServiceBinder) binder).getService();

            // Activity couldn't work with mService until connections are made
            // So initialize parameters and settings here. Do not initialize while running onCreate()
            initialize();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    /**
     * Initialization / Finalization
     */
    private void initialize() {
        Logs.d(TAG, "# Activity - initialize()");

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.bt_ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        mService.setupService(mActivityHandler);

        // If BT is not on, request that it be enabled.
        // RetroWatchService.setupBT() will then be called during onActivityResult
        if(!mService.isBluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
        }

        // Load activity reports and display
        if(mRefreshTimer != null) {
            mRefreshTimer.cancel();
        }

        // Use below timer if you want scheduled job
        //mRefreshTimer = new Timer();
        //mRefreshTimer.schedule(new RefreshTimerTask(), 5*1000);
    }

    private void finalizeActivity() {
        Logs.d(TAG, "# Activity - finalizeActivity()");

        if (!AppSettings.getBgService()) {
            doStopService();
        } else {
        }

        // Clean used resources
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    /**
     * Start service if it's not running
     */
    private void doStartService() {
        Log.d(TAG, "# Activity - doStartService()");
        startService(new Intent(this, BTCTemplateService.class));
        bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stop the service
     */
    private void doStopService() {
        Log.d(TAG, "# Activity - doStopService()");
        mService.finalizeService();
        stopService(new Intent(this, BTCTemplateService.class));
    }

    /**
     * Launch the DeviceListActivity to see devices and do scan
     */
    private void doScan() {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CONNECT_DEVICE);
    }

    /**
     * Ensure this device is discoverable by others
     */
    private void ensureDiscoverable() {
        if (mService.getBluetoothScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
    }

    /*****************************************************
     *	Public classes
     ******************************************************/

    /**
     * Receives result from external activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logs.d(TAG, "onActivityResult " + resultCode);

        switch(requestCode) {
            case Constants.REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Attempt to connect to the device
                    if(address != null && mService != null)
                        mService.connectDevice(address);
                }
                break;

            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a BT session
                    mService.setupBLE();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Logs.e(TAG, "BT is not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                }
                break;
        }	// End of switch(requestCode)
    }

    /*****************************************************
     *	Handler, Callback, Sub-classes
     ******************************************************/

    public class ActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what) {
                // 블루투스 연결 상태에 따라 UI 변경
                case Constants.MESSAGE_BT_STATE_INITIALIZED:
                    bleStatus.setText(getResources().getString(R.string.ble_disconnected));
                    bleImage.setImageDrawable(getResources().getDrawable(R.drawable.no_bluetooth));
                    break;
                case Constants.MESSAGE_BT_STATE_LISTENING:
                    bleStatus.setText(getResources().getString(R.string.ble_wait));
                    bleImage.setImageDrawable(getResources().getDrawable(R.drawable.no_bluetooth));
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTING:
                    bleStatus.setText(getResources().getString(R.string.ble_connecting));
                    bleImage.setImageDrawable(getResources().getDrawable(R.drawable.ing_bluetooth));
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTED:
                    if(mService != null) {
                        String deviceName = mService.getDeviceName();
                        if(deviceName != null) {
                            bleStatus.setText(getResources().getString(R.string.ble_connected) + " : " + deviceName);
                            Toast.makeText(MenuActivity.this, deviceName + "에 연결 성공", Toast.LENGTH_SHORT).show();
                            bleImage.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
                        } else {
                            bleStatus.setText(getResources().getString(R.string.ble_connected) + " : no name");
                            bleImage.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
                        }
                    }
                    break;
                case Constants.MESSAGE_BT_STATE_ERROR:
                    bleStatus.setText(getResources().getString(R.string.ble_error));
                    bleImage.setImageDrawable(getResources().getDrawable(R.drawable.disabled));
                    break;

                // BT Command status
                case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
                    bleStatus.setText(getResources().getString(R.string.ble_cmd_sending_error));
                    bleImage.setImageDrawable(getResources().getDrawable(R.drawable.disabled));
                    break;

                ///////////////////////////////////////////////
                // When there's incoming packets on bluetooth
                // do the UI works like below
                ///////////////////////////////////////////////

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }	// End of class ActivityHandler

    /**
     * Auto-refresh Timer
     */
    private class RefreshTimerTask extends TimerTask {
        public RefreshTimerTask() {
        }

        public void run() {
            mActivityHandler.post(new Runnable() {
                public void run() {
                    // TODO:
                    mRefreshTimer = null;
                }
            });
        }
    }

}
