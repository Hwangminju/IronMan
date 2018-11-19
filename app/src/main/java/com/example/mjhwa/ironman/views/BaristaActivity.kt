package com.example.mjhwa.ironman.views

import android.os.Bundle
import android.app.Activity
import android.view.MenuItem
import android.widget.TextView

import com.example.mjhwa.ironman.R
import com.example.mjhwa.ironman.bluetooth.BluetoothManager
import android.content.Intent
import android.bluetooth.BluetoothAdapter
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mjhwa.ironman.MainActivity

import com.example.mjhwa.ironman.R.id.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_barista.*

class BaristaActivity : Activity() {

    private val mTextMessage: TextView? = null

    // 이메일, 비밀번호 로그인 모듈 변수
    private var mAuth: FirebaseAuth? = null
    // 현재 로그인된 유저 정보를 담을 변수
    private val currentUser: FirebaseUser? = null
    val TAG = "BaristaActivity"

    private val mBluetoothManager: BluetoothManager = BluetoothManager.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barista)

        val intent = getIntent()
        val sLR:String = intent.getStringExtra("LR")

        mAuth = FirebaseAuth.getInstance()


        val listener = View.OnClickListener { view ->
            val no = when (view.id) {
                R.id.bari1 -> 16
                R.id.bari2 -> 17
                R.id.bari3 -> 18
                R.id.bari4 -> 19
                R.id.bari5 -> 20
                R.id.bari6 -> 21
                R.id.bari7 -> 22
                R.id.bari8 -> 23
                R.id.bari9 -> 24
                R.id.bari10 -> 25
                R.id.bari11 -> 26
                R.id.bari12 -> 27
                R.id.bari13 -> 28
                R.id.bari14 -> 29
                R.id.bari15 -> 30
                else -> 0
            }
            val intent = Intent(this, LearnActivity::class.java)
            mBluetoothManager.write(no.toString().toByteArray())
            intent.putExtra("LR", sLR)
            intent.putExtra("NO", no)
            startActivity(intent)
        }

        bari1.setOnClickListener(listener)
        bari2.setOnClickListener(listener)
        bari3.setOnClickListener(listener)
        bari4.setOnClickListener(listener)
        bari5.setOnClickListener(listener)
        bari6.setOnClickListener(listener)
        bari7.setOnClickListener(listener)
        bari8.setOnClickListener(listener)
        bari9.setOnClickListener(listener)
        bari10.setOnClickListener(listener)
        bari11.setOnClickListener(listener)
        bari12.setOnClickListener(listener)
        bari13.setOnClickListener(listener)
        bari14.setOnClickListener(listener)
        bari15.setOnClickListener(listener)
    }

    override fun onStart() {
        super.onStart()
        Log.i(MainActivity.TAG, "onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.i(MainActivity.TAG, "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.i(MainActivity.TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(MainActivity.TAG, "onStop")
    }


}