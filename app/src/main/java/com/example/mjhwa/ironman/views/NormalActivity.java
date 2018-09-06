package com.example.mjhwa.ironman.views;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mjhwa.ironman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NormalActivity extends Activity implements View.OnClickListener {

    // 이메일, 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    // 현재 로그인된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    ImageButton n1,n2,n3,n4,n5,n6,n7,n8,n9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        mAuth = FirebaseAuth.getInstance();

        n1 = (ImageButton) findViewById(R.id.norm1);
        n2 = (ImageButton) findViewById(R.id.norm2);
        n3 = (ImageButton) findViewById(R.id.norm3);
        n4 = (ImageButton) findViewById(R.id.norm4);
        n5 = (ImageButton) findViewById(R.id.norm5);
        n6 = (ImageButton) findViewById(R.id.norm6);
        n7 = (ImageButton) findViewById(R.id.norm7);
        n8 = (ImageButton) findViewById(R.id.norm8);
        n9 = (ImageButton) findViewById(R.id.norm9);

        n1.setOnClickListener(this);
        n2.setOnClickListener(this);
        n3.setOnClickListener(this);
        n4.setOnClickListener(this);
        n5.setOnClickListener(this);
        n6.setOnClickListener(this);
        n7.setOnClickListener(this);
        n8.setOnClickListener(this);
        n9.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.norm1:

        }
    }

}
