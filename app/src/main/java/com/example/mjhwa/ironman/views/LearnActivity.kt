package com.example.mjhwa.ironman.views

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.os.AsyncTask
import android.os.Message
import android.speech.SpeechRecognizer
import android.util.Log
import android.Manifest;
import android.R.attr.bottom
import android.content.Intent

import com.example.mjhwa.ironman.R
import com.example.mjhwa.ironman.bluetooth.BluetoothManager
import kotlinx.android.synthetic.main.activity_learn_left.*
import kotlinx.android.synthetic.main.activity_learn_right.*
import java.nio.charset.Charset
import android.widget.*
import com.example.mjhwa.ironman.EMGData
import com.example.mjhwa.ironman.R.layout.activity_learn_left
import com.example.mjhwa.ironman.R.layout.activity_learn_right
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import android.R.attr.x
import android.annotation.SuppressLint
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask


class LearnActivity : AppCompatActivity() {

    private val mBtHandler = BluetoothHandler()
    private val mBluetoothManager: BluetoothManager = BluetoothManager.getInstance()
    private var mPrevUpdateTime = 0L

    private val TAG = "phptest"
    private val TAG_BT = "BluetoothClient"
    private val ON_TAG = "LearnActivity"

    private var timer: Timer? = null
    private var mToolbar: Toolbar? = null

    init {
        Manifest.permission.RECORD_AUDIO
    }

    var id : String? = null
    var lr : String? = null
    var num : Int? = 0

    private var time = 0
    private var timerTask: Timer? = null

    private var emgData = ArrayList<EMGData>()

    var text: String? = null // 음성 인식 텍스트

    internal var i: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBluetoothManager.setHandler(mBtHandler)

        val intent = getIntent()
        id = intent.getStringExtra("ID")
        lr = intent.getStringExtra("LR")

        if (lr == "left") {
            setContentView(activity_learn_left)
        } else if (lr == "right") {
            setContentView(activity_learn_right)
        }

        if (lr == "left")
            run_left()
        else if (lr == "right")
            run_right()
    }

    fun run_left(): Boolean {

        tv_voice_left.setText("10초 동안 그림 속 손동작을 유지해 주세요.")
        tv_emg_left.setText("\n\nEMG 데이터가 이곳에 입력됩니다.")

        btn_learning_left.visibility = View.GONE
        mToolbar = findViewById<View>(R.id.toolbar_left) as Toolbar // 상단 틀바
        mToolbar!!.setTitleTextColor(Color.parseColor("white"))
        setSupportActionBar(mToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        // 이미지들이 담긴 컨테이너
        val vf = findViewById<View>(R.id.view_flipper_left) as ViewFlipper
        vf.isEnabled = false
        vf.setFlipInterval(1000)

        num = intent.getIntExtra("NO",0)

        btn_start_left.setOnClickListener {

            btn_start_left.visibility = View.GONE // start 버튼은 없어지고
            btn_learning_left.visibility = View.VISIBLE
            vf.isEnabled = true

            vf.startFlipping()
            tv_emg_left.setText("")

            try {
                val sendMessage = "start"
                if (sendMessage.length > 0) {
                    mBluetoothManager.write(sendMessage.toByteArray())
                }
            } catch (e: IOException) {
                Log.e(TAG, "Message cannot be sent", e);
            }

            Handler().postDelayed({
                vf.stopFlipping()
                vf.displayedChild = 0
                vf.isEnabled = false
                btn_learning_left.visibility = View.GONE
                btn_start_left.visibility = View.VISIBLE
            },10000)

            ParseTask().execute()
        }

        when (num) {
            // intent로 받아온 동작 num case
            1 -> {
                pic_left.setImageResource(R.drawable.norm1)
                name_left.setText(R.string.opt1)
            }
            2 -> {
                pic_left.setImageResource(R.drawable.norm2)
                name_left.setText(R.string.opt2)
            }
            3 -> {
                pic_left.setImageResource(R.drawable.norm3)
                name_left.setText(R.string.opt3)
            }
            4 -> {
                pic_left.setImageResource(R.drawable.norm4)
                name_left.setText(R.string.opt4)
            }
            5 -> {
                pic_left.setImageResource(R.drawable.norm5)
                name_left.setText(R.string.opt5)
            }
            6 -> {
                pic_left.setImageResource(R.drawable.norm6)
                name_left.setText(R.string.opt6)
            }
            7 -> {
                pic_left.setImageResource(R.drawable.norm7)
                name_left.setText(R.string.opt7)
            }
            8 -> {
                pic_left.setImageResource(R.drawable.norm8)
                name_left.setText(R.string.opt8)
            }
            9 -> {
                pic_left.setImageResource(R.drawable.norm9)
                name_left.setText(R.string.opt9)
            }
            10 -> {
                pic_left.setImageResource(R.drawable.norm10)
                name_left.setText(R.string.opt10)
            }
            11 -> {
                pic_left.setImageResource(R.drawable.norm11)
                name_left.setText(R.string.opt11)
            }
            12 -> {
                pic_left.setImageResource(R.drawable.norm12)
                name_left.setText(R.string.opt12)
            }
            13 -> {
                pic_left.setImageResource(R.drawable.norm13)
                name_left.setText(R.string.opt13)
            }
            14 -> {
                pic_left.setImageResource(R.drawable.norm14)
                name_left.setText(R.string.opt14)
            }
            15 -> {
                pic_left.setImageResource(R.drawable.norm15)
                name_left.setText(R.string.opt15)
            }
            16 -> {
                pic_left.setImageResource(R.drawable.bari1)
                name_left.setText(R.string.b_opt1)
            }
            17 -> {
                pic_left.setImageResource(R.drawable.bari2)
                name_left.setText(R.string.b_opt2)
            }
            18 -> {
                pic_left.setImageResource(R.drawable.bari3)
                name_left.setText(R.string.b_opt3)
            }
            19 -> {
                pic_left.setImageResource(R.drawable.bari4)
                name_left.setText(R.string.b_opt4)
            }
            20 -> {
                pic_left.setImageResource(R.drawable.bari5)
                name_left.setText(R.string.b_opt5)
            }
            21 -> {
                pic_left.setImageResource(R.drawable.bari6)
                name_left.setText(R.string.b_opt6)
            }
            22 -> {
                pic_left.setImageResource(R.drawable.bari7)
                name_left.setText(R.string.b_opt7)
            }
            23 -> {
                pic_left.setImageResource(R.drawable.bari8)
                name_left.setText(R.string.b_opt8)
            }
            24 -> {
                pic_left.setImageResource(R.drawable.bari9)
                name_left.setText(R.string.b_opt9)
            }
            25 -> {
                pic_left.setImageResource(R.drawable.bari10)
                name_left.setText(R.string.b_opt10)
            }
            26 -> {
                pic_left.setImageResource(R.drawable.bari11)
                name_left.setText(R.string.b_opt11)
            }
            27 -> {
                pic_left.setImageResource(R.drawable.bari12)
                name_left.setText(R.string.b_opt12)
            }
            28 -> {
                pic_left.setImageResource(R.drawable.bari13)
                name_left.setText(R.string.b_opt13)
            }
            29 -> {
                pic_left.setImageResource(R.drawable.bari14)
                name_left.setText(R.string.b_opt14)
            }
            30 -> {
                pic_left.setImageResource(R.drawable.bari15)
                name_left.setText(R.string.b_opt15)
            }
            else -> {
            }
        }
        return true
    }

    inner class ParseTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...
            tv_emg_left.setText("\n\nEMG 데이터 수집 중입니다.")

            val res = getResources()

            val ges1 = res.openRawResource(R.raw.gesture1) // 1번 제스처 csv 파일
            val ges2 = res.openRawResource(R.raw.gesture2) // 2번 제스처 csv 파일
            val ges3 = res.openRawResource(R.raw.gesture3) // 3번 제스처 csv 파일
            val ges4 = res.openRawResource(R.raw.gesture4) // 4번 제스처 csv 파일
            val ges5 = res.openRawResource(R.raw.gesture5) // 5번 제스처 csv 파일

            var reader: BufferedReader? = null
            var gesNum: Int? = num!! % 5

            when (gesNum) {
                // normal 1~5
                0 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))

                1 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
                2 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
                3 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
                4 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
                5 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))

                // barista 1~5
                16 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
                17 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
                18 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
                19 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
                20 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
            }

            var line : String?
            var emgList : String? = ""
            reader!!.readLine() // [﻿A, B, C]

            line = reader.readLine()
            // update ui here
            while (line != null) {

                var tokens: List<String>
                tokens = line.split(",")
                Log.d("line is what", tokens.toString())

                // tv_emg_left.append(tokens[0] + " | " + tokens[1] + " | " + tokens[2] + "\n")
                emgList += (tokens[0] + " | " + tokens[1] + " | " + tokens[2] + "\n")

                line = reader.readLine()
            }
            return emgList
        }

        override fun onPostExecute(emgList: String?) {
            Handler().postDelayed({
                tv_emg_left.append(emgList)
                sv_emg_left.post {
                    sv_emg_left.fullScroll(View.FOCUS_DOWN) // 스크롤 맨 밑으로
                }
                Toast.makeText(applicationContext, "EMG 데이터 수집 완료!", Toast.LENGTH_LONG).show()
            },9000)

        }
    }

    fun parsing_data_left() {

        sv_emg_left.setSmoothScrollingEnabled(true)
        sv_emg_left.smoothScrollTo(0, tv_emg_left.getBaseline())

        tv_emg_left.setText("")

        val res = getResources()

        val ges1 = res.openRawResource(R.raw.gesture1) // 1번 제스처 csv 파일
        val ges2 = res.openRawResource(R.raw.gesture2) // 2번 제스처 csv 파일
        val ges3 = res.openRawResource(R.raw.gesture3) // 3번 제스처 csv 파일
        val ges4 = res.openRawResource(R.raw.gesture4) // 4번 제스처 csv 파일
        val ges5 = res.openRawResource(R.raw.gesture5) // 5번 제스처 csv 파일

        var reader: BufferedReader? = null
        var gesNum: Int? = null

        when (num) {
            // normal 1~5
            1 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
            2 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
            3 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
            4 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
            5 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
            // barista 1~5
            16 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
            17 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
            18 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
            19 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
            20 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
        }

        // txt 출력 반복 시작

        runOnUiThread { // runOnUiThread를 사용하지 않으면 Main Thread에서 UI 변경 불가
            var line:String?
            reader!!.readLine() // [﻿A, B, C]

            line = reader.readLine()
            // update ui here
            while (line != null) {
                var tokens: List<String>
                tokens = line.split(",")
                Log.d("line is what", tokens.toString())

                tv_emg_left.append(tokens[0] + " | " + tokens[1] + " | " + tokens[2] + "\n")

                sv_emg_left.post {
                    sv_emg_left.fullScroll(View.FOCUS_DOWN) // 스크롤 맨 밑으로
                }

                line = reader.readLine()

            }
        }


    }


    fun run_right(): Boolean {

        tv_voice_right.setText("10초 동안 그림 속 손동작을 유지해 주세요.")
        tv_emg_right.setText("\n\nEMG 데이터가 이곳에 입력됩니다.")

        btn_learning_right.visibility = View.GONE
        mToolbar = findViewById<View>(R.id.toolbar_right) as Toolbar // 상단 틀바
        mToolbar!!.setTitleTextColor(Color.parseColor("white"))
        setSupportActionBar(mToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        // 이미지들이 담긴 컨테이너
        val vf = findViewById<View>(R.id.view_flipper_right) as ViewFlipper
        vf.isEnabled = false
        vf.setFlipInterval(1000)

        num = intent.getIntExtra("NO",0)
        // mBluetoothManager.setHandler(mBtHandler)

        btn_start_right.setOnClickListener {
            btn_start_right.visibility = View.GONE // start 버튼은 없어지고
            btn_learning_right.visibility = View.VISIBLE
            vf.isEnabled = true
            vf.visibility = View.VISIBLE // timer는 나타내기
            vf.startFlipping()
            tv_emg_right.setText("")

            try {
                val sendMessage = "start"
                if (sendMessage.length > 0) {
                    mBluetoothManager.write(sendMessage.toByteArray())
                }
            } catch (e: IOException) {
                Log.e(TAG, "Message cannot be sent", e);
            }

            val displayedChild = vf.displayedChild
            val childCount = vf.childCount

            Handler().postDelayed({
                vf.stopFlipping()
                vf.displayedChild = 0
                vf.isEnabled = false
                vf.visibility = View.INVISIBLE
                btn_learning_right.visibility = View.GONE
                btn_start_right.visibility = View.VISIBLE

            },10000)

            ParserTask().execute()
        }

        when (num) {
            // intent로 받아온 동작 num case
            1 -> {
                pic_right.setImageResource(R.drawable.norm1)
                name_right.setText(R.string.opt1)
            }
            2 -> {
                pic_right.setImageResource(R.drawable.norm2)
                name_right.setText(R.string.opt2)
            }
            3 -> {
                pic_right.setImageResource(R.drawable.norm3)
                name_right.setText(R.string.opt3)
            }
            4 -> {
                pic_right.setImageResource(R.drawable.norm4)
                name_right.setText(R.string.opt4)
            }
            5 -> {
                pic_right.setImageResource(R.drawable.norm5)
                name_right.setText(R.string.opt5)
            }
            6 -> {
                pic_right.setImageResource(R.drawable.norm6)
                name_right.setText(R.string.opt6)
            }
            7 -> {
                pic_right.setImageResource(R.drawable.norm7)
                name_right.setText(R.string.opt7)
            }
            8 -> {
                pic_right.setImageResource(R.drawable.norm8)
                name_right.setText(R.string.opt8)
            }
            9 -> {
                pic_right.setImageResource(R.drawable.norm9)
                name_right.setText(R.string.opt9)
            }
            10 -> {
                pic_right.setImageResource(R.drawable.norm10)
                name_right.setText(R.string.opt10)
            }
            11 -> {
                pic_right.setImageResource(R.drawable.norm11)
                name_right.setText(R.string.opt11)
            }
            12 -> {
                pic_right.setImageResource(R.drawable.norm12)
                name_right.setText(R.string.opt12)
            }
            13 -> {
                pic_right.setImageResource(R.drawable.norm13)
                name_right.setText(R.string.opt13)
            }
            14 -> {
                pic_right.setImageResource(R.drawable.norm14)
                name_right.setText(R.string.opt14)
            }
            15 -> {
                pic_right.setImageResource(R.drawable.norm15)
                name_right.setText(R.string.opt15)
            }
            16 -> {
                pic_right.setImageResource(R.drawable.bari1)
                name_right.setText(R.string.b_opt1)
            }
            17 -> {
                pic_right.setImageResource(R.drawable.bari2)
                name_right.setText(R.string.b_opt2)
            }
            18 -> {
                pic_right.setImageResource(R.drawable.bari3)
                name_right.setText(R.string.b_opt3)
            }
            19 -> {
                pic_right.setImageResource(R.drawable.bari4)
                name_right.setText(R.string.b_opt4)
            }
            20 -> {
                pic_right.setImageResource(R.drawable.bari5)
                name_right.setText(R.string.b_opt5)
            }
            21 -> {
                pic_right.setImageResource(R.drawable.bari6)
                name_right.setText(R.string.b_opt6)
            }
            22 -> {
                pic_right.setImageResource(R.drawable.bari7)
                name_right.setText(R.string.b_opt7)
            }
            23 -> {
                pic_right.setImageResource(R.drawable.bari8)
                name_right.setText(R.string.b_opt8)
            }
            24 -> {
                pic_right.setImageResource(R.drawable.bari9)
                name_right.setText(R.string.b_opt9)
            }
            25 -> {
                pic_right.setImageResource(R.drawable.bari10)
                name_right.setText(R.string.b_opt10)
            }
            26 -> {
                pic_right.setImageResource(R.drawable.bari11)
                name_right.setText(R.string.b_opt11)
            }
            27 -> {
                pic_right.setImageResource(R.drawable.bari12)
                name_right.setText(R.string.b_opt12)
            }
            28 -> {
                pic_right.setImageResource(R.drawable.bari13)
                name_right.setText(R.string.b_opt13)
            }
            29 -> {
                pic_right.setImageResource(R.drawable.bari14)
                name_right.setText(R.string.b_opt14)
            }
            30 -> {
                pic_right.setImageResource(R.drawable.bari15)
                name_right.setText(R.string.b_opt15)
            }
            else -> {
            }
        }
        return true
    }

    inner class ParserTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...
            tv_emg_right.setText("\n\nEMG 데이터 수집 중입니다.")

            val res = getResources()

            val ges1 = res.openRawResource(R.raw.gesture1) // 1번 제스처 csv 파일
            val ges2 = res.openRawResource(R.raw.gesture2) // 2번 제스처 csv 파일
            val ges3 = res.openRawResource(R.raw.gesture3) // 3번 제스처 csv 파일
            val ges4 = res.openRawResource(R.raw.gesture4) // 4번 제스처 csv 파일
            val ges5 = res.openRawResource(R.raw.gesture5) // 5번 제스처 csv 파일

            var reader: BufferedReader? = null
            var gesNum: Int? = num!! % 5

            when (gesNum) {
                // normal 1~5
                0 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))

                1 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
                2 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
                3 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
                4 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
                5 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
                // barista 1~5
                16 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
                17 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
                18 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
                19 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
                20 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
            }

            var line : String?
            var emgList : String? = ""
            reader!!.readLine() // [﻿A, B, C]

            line = reader.readLine()
            // update ui here
            while (line != null) {

                var tokens: List<String>
                tokens = line.split(",")
                Log.d("line is what", tokens.toString())

                // tv_emg_right.append(tokens[0] + " | " + tokens[1] + " | " + tokens[2] + "\n")
                emgList += (tokens[0] + " | " + tokens[1] + " | " + tokens[2] + "\n")

                line = reader.readLine()
            }
            return emgList
        }

        override fun onPostExecute(emgList: String?) {
            Handler().postDelayed({
                tv_emg_right.append(emgList)
                sv_emg_right.post {
                    sv_emg_right.fullScroll(View.FOCUS_DOWN) // 스크롤 맨 밑으로
                }
                Toast.makeText(applicationContext, "EMG 데이터 수집 완료!", Toast.LENGTH_LONG).show()
            },9000)

        }
    }

    fun parsing_data_right() {

        sv_emg_right.setSmoothScrollingEnabled(true)
        sv_emg_right.smoothScrollTo(0, tv_emg_right.getBaseline())

        tv_emg_right.setText("")

        val res = getResources()

        val ges1 = res.openRawResource(R.raw.gesture1) // 1번 제스처 csv 파일
        val ges2 = res.openRawResource(R.raw.gesture2) // 2번 제스처 csv 파일
        val ges3 = res.openRawResource(R.raw.gesture3) // 3번 제스처 csv 파일
        val ges4 = res.openRawResource(R.raw.gesture4) // 4번 제스처 csv 파일
        val ges5 = res.openRawResource(R.raw.gesture5) // 5번 제스처 csv 파일

        var reader: BufferedReader? = null
        var gesNum: Int? = null

        when (num) {
            1 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
            2 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
            3 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
            4 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
            5 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
            // barista 1~5
            16 -> reader = BufferedReader(InputStreamReader(ges3, Charset.forName("UTF-8")))
            17 -> reader = BufferedReader(InputStreamReader(ges2, Charset.forName("UTF-8")))
            18 -> reader = BufferedReader(InputStreamReader(ges1, Charset.forName("UTF-8")))
            19 -> reader = BufferedReader(InputStreamReader(ges5, Charset.forName("UTF-8")))
            20 -> reader = BufferedReader(InputStreamReader(ges4, Charset.forName("UTF-8")))
        }

        // txt 출력 반복 시작

        timerTask = timer(period = 100) {

            runOnUiThread { // runOnUiThread를 사용하지 않으면 Main Thread에서 UI 변경 불가
                var line:String?
                reader!!.readLine() // [﻿A, B, C]

                line = reader.readLine()
                // update ui here
                while (line != null) {
                    var tokens: List<String>
                    tokens = line.split(",")
                    Log.d("line is what", tokens.toString())

                    tv_emg_right.append(tokens[0] + " | " + tokens[1] + " | " + tokens[2] + "\n")

                    sv_emg_right.post {
                        sv_emg_right.fullScroll(View.FOCUS_DOWN) // 스크롤 맨 밑으로
                    }

                    line = reader.readLine()
                }
            }
        }

    }

    inner class BluetoothHandler : Handler() { // emg 값 수신
        override fun handleMessage(msg: Message) {

            var st : String? = null
            var emg1 : Int? = null
            var emg2 : Int? = null
            var emg3 : Int? = null

            when (msg.what) {
                BluetoothManager.MESSAGE_READ -> {

                    if (msg.obj != null) {
                        if (lr == "left") {
                            val currentTime = System.currentTimeMillis()
                            if (mPrevUpdateTime + 10 <= currentTime) {
                                Log.e("Read EMG", "Reading EMG Sensors")
                                tv_emg_left.append("\n")
                                mPrevUpdateTime = currentTime
                            }
                            tv_emg_left.append((msg.obj as ByteArray).toString(Charset.defaultCharset()))

                            st = (msg.obj as ByteArray).toString()
                            var emg_list = st.split(" ")

                        } else if (lr == "right") {
                            val currentTime = System.currentTimeMillis()
                            if (mPrevUpdateTime + 10 <= currentTime) {
                                Log.e("Read EMG", "Reading EMG Sensors")
                                tv_emg_right.append("\n")
                                mPrevUpdateTime = currentTime
                            }
                            tv_emg_right.append((msg.obj as ByteArray).toString(Charset.defaultCharset()))
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

    override fun onStart() {
        super.onStart()
        Log.i(ON_TAG, "onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.i(ON_TAG, "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.i(ON_TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(ON_TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(ON_TAG, "onDestory")
    }
}
