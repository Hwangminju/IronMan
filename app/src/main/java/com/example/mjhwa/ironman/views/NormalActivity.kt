package com.example.mjhwa.ironman.views

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.view.View
import android.widget.Toast

import com.example.mjhwa.ironman.R
import com.example.mjhwa.ironman.R.id.*
import com.example.mjhwa.ironman.bluetooth.BluetoothManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_normal.*

class NormalActivity : Activity() {

    // 이메일, 비밀번호 로그인 모듈 변수
    private var mAuth: FirebaseAuth? = null
    // 현재 로그인된 유저 정보를 담을 변수
    private val currentUser: FirebaseUser? = null

    private val mBluetoothManager: BluetoothManager = BluetoothManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal)

        val intent = getIntent()
        val sLR:String = intent.getStringExtra("LR")

        mAuth = FirebaseAuth.getInstance()

        val listener = View.OnClickListener { view ->
            val no = when (view.id) {
                R.id.norm1 -> 1
                R.id.norm2 -> 2
                R.id.norm3 -> 3
                R.id.norm4 -> 4
                R.id.norm5 -> 5
                R.id.norm6 -> 6
                R.id.norm7 -> 7
                R.id.norm8 -> 8
                R.id.norm9 -> 9
                R.id.norm10 -> 10
                else -> 0
            }
            val intent = Intent(this, LearnActivity::class.java)
            mBluetoothManager.write(no.toString().toByteArray())
            intent.putExtra("LR", sLR)
            Toast.makeText(this,"LR is " + sLR, Toast.LENGTH_LONG).show()
            intent.putExtra("NO", no)
            startActivity(intent)
        }

        norm1.setOnClickListener(listener)
        norm2.setOnClickListener(listener)
        norm3.setOnClickListener(listener)
        norm4.setOnClickListener(listener)
        norm5.setOnClickListener(listener)
        norm6.setOnClickListener(listener)
        norm7.setOnClickListener(listener)
        norm8.setOnClickListener(listener)
        norm9.setOnClickListener(listener)
        norm10.setOnClickListener(listener)
    }

}
