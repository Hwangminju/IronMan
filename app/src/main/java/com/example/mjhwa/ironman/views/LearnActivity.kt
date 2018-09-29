package com.example.mjhwa.ironman.views

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import java.util.Timer

import android.widget.ViewFlipper

import com.example.mjhwa.ironman.R
import kotlinx.android.synthetic.main.activity_learn.*

class LearnActivity : AppCompatActivity() {

    internal var getNo: Int = 0

    private val timer: Timer? = null
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar // 상단 틀바
        mToolbar!!.setTitleTextColor(Color.parseColor("white"))
        setSupportActionBar(mToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        // 이미지들이 담긴 컨테이너
        val vf = findViewById<View>(R.id.view_flipper) as ViewFlipper
        vf.visibility = View.INVISIBLE
        vf.setFlipInterval(1000)

        intent = getIntent()
        getNo = intent.getIntExtra("NO", 0)

        btn_start.setOnClickListener {
            btn_start.visibility = View.GONE // start 버튼은 없어지고
            vf.isEnabled = true
            vf.visibility = View.VISIBLE // timer는 나타내기
            vf.startFlipping()

            val displayedChild = vf.displayedChild
            val childCount = vf.childCount

            Handler().postDelayed({
                vf.stopFlipping()
                vf.isEnabled = false
                vf.visibility = View.GONE
                btn_start.visibility = View.VISIBLE
            },10000)
        }

        when (getNo) {
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
}
