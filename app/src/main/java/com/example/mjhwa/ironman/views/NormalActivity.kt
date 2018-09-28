package com.example.mjhwa.ironman.views

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar

import com.example.mjhwa.ironman.R
import com.example.mjhwa.ironman.R.drawable.norm1
import com.example.mjhwa.ironman.bluetooth.BluetoothManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class NormalActivity : Activity(), View.OnClickListener {

    // 이메일, 비밀번호 로그인 모듈 변수
    private var mAuth: FirebaseAuth? = null
    // 현재 로그인된 유저 정보를 담을 변수
    private val currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal)

        mAuth = FirebaseAuth.getInstance()

    }

    override fun onClick(v: View) {
        var no = 0
        val intent = Intent(this, LearnActivity::class.java)

        // BluetoothAdapter 인스턴스를 얻는다
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val mBluetoothManager = BluetoothManager.getInstance()


        when (v.id) {
            R.id.norm1 -> no = 1
            R.id.norm2 -> no = 2
            R.id.norm3 -> no = 3
            R.id.norm4 -> no = 4
            R.id.norm5 -> no = 5
            R.id.norm6 -> no = 6
            R.id.norm7 -> no = 7
            R.id.norm8 -> no = 8
            R.id.norm9 -> no = 9
            R.id.norm10 -> no = 10
            else -> {
            }

        }
        mBluetoothManager.write(no.toString().toByteArray())
        intent.putExtra("NO", no)
        startActivity(intent)
    }

}
