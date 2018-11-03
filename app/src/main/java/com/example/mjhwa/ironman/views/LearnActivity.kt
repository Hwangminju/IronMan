package com.example.mjhwa.ironman.views

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.bluetooth.BluetoothAdapter
import android.os.AsyncTask
import android.os.Message
import android.util.Log

import com.example.mjhwa.ironman.R
import com.example.mjhwa.ironman.bluetooth.BluetoothManager
import kotlinx.android.synthetic.main.activity_learn.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.*
import android.widget.*
import java.io.*
import java.util.*
import kotlin.text.Charsets.UTF_8

class LearnActivity : AppCompatActivity() {

    private val mBtHandler = BluetoothHandler()
    private val mBluetoothManager: BluetoothManager = BluetoothManager.getInstance()
    private var mPrevUpdateTime = 0L
    internal var isConnectionError = false

    private val TAG = "phptest"
    private val TAG_BT = "BluetoothClient"
    private var mConversationArrayAdapter: ArrayAdapter<String>? = null

    private val timer: Timer? = null
    private var mToolbar: Toolbar? = null

    var id : String? = null
    var num : Int? = 0

    internal val uploadFilePath = "/data/data/com.example.mjhwa.ironman/databases/"
    internal val uploadFileName = "data.txt" // 전송하고자 하는 파일 이름

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        mBluetoothManager.setHandler(mBtHandler)

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar // 상단 틀바
        mToolbar!!.setTitleTextColor(Color.parseColor("white"))
        setSupportActionBar(mToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        val mMessageListview = findViewById(R.id.message_listview) as ListView

        mConversationArrayAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1)
        mMessageListview.adapter = mConversationArrayAdapter

        // 이미지들이 담긴 컨테이너
        val vf = findViewById<View>(R.id.view_flipper) as ViewFlipper
        vf.visibility = View.INVISIBLE
        vf.setFlipInterval(1000)

        val intent = getIntent()
        id = intent.getStringExtra("ID")

        num = intent.getIntExtra("NO",0)
        // mBluetoothManager.setHandler(mBtHandler)

        btn_start.setOnClickListener {
            btn_start.visibility = View.INVISIBLE // start 버튼은 없어지고
            vf.isEnabled = true
            vf.visibility = View.VISIBLE // timer는 나타내기
            vf.startFlipping()

            val sendMessage = "start"
            if (sendMessage.length > 0) {
                mBluetoothManager.write(sendMessage.toByteArray())
            }

            // mBluetoothManager.write("0".toByteArray())
            // mBluetoothManager.setHandler(mBtHandler)

            /*
            try {
                val lDB = InsertDB()
                lDB.execute("http://ec2-18-224-155-219.us-east-2.compute.amazonaws.com/login.php",
                        158, 200, 25, num.toString(), id)
            } catch (e: NullPointerException) {
                Log.e("err", e.message)
            }*/

            val displayedChild = vf.displayedChild
            val childCount = vf.childCount

            Handler().postDelayed({

                vf.stopFlipping()
                vf.displayedChild = 0
                vf.isEnabled = false
                vf.visibility = View.INVISIBLE
                btn_start.visibility = View.VISIBLE

            },10000)

            val mMessageListview = findViewById(R.id.message_listview) as ListView

            mConversationArrayAdapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1)
            mMessageListview.adapter = mConversationArrayAdapter
        }

        when (num) {
            // intent로 받아온 동작 num case
            1 -> {
                pic.setImageResource(R.drawable.norm_1)
                name.setText(R.string.opt1)
            }
            2 -> {
                pic.setImageResource(R.drawable.norm_2)
                name.setText(R.string.opt2)
            }
            3 -> {
                pic.setImageResource(R.drawable.norm_3)
                name.setText(R.string.opt3)
            }
            4 -> {
                pic.setImageResource(R.drawable.norm_4)
                name.setText(R.string.opt4)
            }
            5 -> {
                pic.setImageResource(R.drawable.norm_5)
                name.setText(R.string.opt5)
            }
            6 -> {
                pic.setImageResource(R.drawable.norm_6)
                name.setText(R.string.opt6)
            }
            7 -> {
                pic.setImageResource(R.drawable.norm_7)
                name.setText(R.string.opt7)
            }
            8 -> {
                pic.setImageResource(R.drawable.norm_8)
                name.setText(R.string.opt8)
            }
            9 -> {
                pic.setImageResource(R.drawable.norm_9)
                name.setText(R.string.opt9)
            }
            10 -> {
                pic.setImageResource(R.drawable.norm_10)
                name.setText(R.string.opt10)
            }
            else -> {
            }
        }
    }

    inner class BluetoothHandler : Handler() { // emg 값 수신
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BluetoothManager.MESSAGE_READ -> {

                    if (msg.obj != null) {
                        val currentTime = System.currentTimeMillis()
                        if (mPrevUpdateTime + 10 <= currentTime) {
                            // tv_emg.append("\n")
                            Log.e("Read EMG", "Reading EMG Sensors")
                            mConversationArrayAdapter!!.insert((msg.obj as ByteArray).toString(Charset.defaultCharset()),0)
                            mPrevUpdateTime = currentTime
                        }
                    }
                }
            }
            super.handleMessage(msg)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //return super.onCreateOptionsMenu(menu);
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> { // toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class InsertDB : AsyncTask<Any, Int, String>() {

        internal var errorString: String? = null

        override fun doInBackground(vararg params: Any): String? {

            val serverURL = params[0]
            val postParameters = "emg1=" + params[1] + "&emg2=" + params[2] +
                    "&emg3=" + params[3] + "&gesture=" + params[4] + "&u_id=" + params[5] + ""

            try {
                val url = URL(serverURL.toString())
                val conn = url.openConnection() as HttpURLConnection

                conn.readTimeout = 5000
                conn.connectTimeout = 5000
                conn.setRequestProperty("Content-Type", "application/x-w ww-form-urlencoded")
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true
                conn.connect()

                /* 안드로이드 -> 서버 파라메터값 전달 */
                val outputStream = conn.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode = conn.responseCode
                Log.d(TAG, "response code - $responseStatusCode")

                val inputStream: InputStream
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.inputStream
                } else {
                    inputStream = conn.errorStream
                }
                /* 서버 -> 안드로이드 파라메터값 전달 */
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                val sb = StringBuilder()
                var line: String

                while (bufferedReader.readLine() != null) {
                    sb.append(bufferedReader.readLine())
                }

                bufferedReader.close()
                val data = sb.toString().trim { it <= ' ' }

                if (data == "1") {
                    Log.e("RESULT", "성공적으로 처리되었습니다!")
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = $data")
                }

                return data

            } catch (e: Exception) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null
            }

        }

        override fun onPostExecute(data: String?) {
            super.onPostExecute(data)
            this@LearnActivity

            /* 서버에서 응답 */
            Log.e("RECV DATA", data)

            if (data == "1") {
                Log.e("RESULT", "성공적으로 처리되었습니다!")
            } else {
                Log.e("RESULT", "에러 발생! ERRCODE = $data")
            }

        }

    }

}
