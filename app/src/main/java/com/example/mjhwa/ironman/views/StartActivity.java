package com.example.mjhwa.ironman.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.mjhwa.ironman.R;
import com.example.mjhwa.ironman.ble.BluetoothLeService;

public class StartActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView txt_login;

    private Button btn_connect;
    private TextView txt_connect;

    Button btn_main;
    private BluetoothLeService btService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

}

