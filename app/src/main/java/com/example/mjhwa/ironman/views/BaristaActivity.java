package com.example.mjhwa.ironman.views;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mjhwa.ironman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaristaActivity extends Activity {

    // 이메일, 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    // 현재 로그인된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barista);

        mAuth = FirebaseAuth.getInstance();

    }

}