package com.example.mjhwa.ironman.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.mjhwa.ironman.R;
import com.example.mjhwa.ironman.ble.BluetoothLeService;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);

            }
        }, 2000);
    }

}

