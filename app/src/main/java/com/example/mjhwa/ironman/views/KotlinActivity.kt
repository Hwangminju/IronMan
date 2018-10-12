package com.example.mjhwa.ironman.views

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText

import com.example.mjhwa.ironman.R

class KotlinActivity : Activity() {

    internal var et_id: EditText? = null
    internal var et_pw: EditText? = null
    internal var et_pw_chk: EditText? = null
    internal var sId: String? = null
    internal var sPw: String? = null
    internal var sPw_chk: String? = null

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        val intent = intent
        val data = application as Data
        data.id = intent.getStringExtra("ID")
    }

    inner class Data : Application() {
        var id: String? = null
        var num: Int = 0
    }

}
