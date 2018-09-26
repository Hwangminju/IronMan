package com.example.mjhwa.ironman.views;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mjhwa.ironman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NormalActivity extends Activity implements View.OnClickListener {

    // 이메일, 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    // 현재 로그인된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    Button n1,n2,n3,n4,n5,n6,n7,n8,n9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        mAuth = FirebaseAuth.getInstance();

        n1 = (Button) findViewById(R.id.norm1);
        n2 = (Button) findViewById(R.id.norm2);
        n3 = (Button) findViewById(R.id.norm3);
        n4 = (Button) findViewById(R.id.norm4);
        n5 = (Button) findViewById(R.id.norm5);
        n6 = (Button) findViewById(R.id.norm6);
        n7 = (Button) findViewById(R.id.norm7);
        n8 = (Button) findViewById(R.id.norm8);
        n9 = (Button) findViewById(R.id.norm9);

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
        int no = 0;
        Intent intent = new Intent(v.getContext(), LearnActivity.class);

        switch (v.getId()) {
            case R.id.norm1:
                no = 1;
                break;
            case R.id.norm2:
                no = 2;
                break;
            case R.id.norm3:
                no = 3;
                break;
            case R.id.norm4:
                no = 4;
                break;
            case R.id.norm5:
                no = 5;
                break;
            case R.id.norm6:
                no = 6;
                break;
            case R.id.norm7:
                no = 7;
                break;
            case R.id.norm8:
                no = 8;
                break;
            case R.id.norm9:
                no = 9;
                break;
            default:
                break;
        }
        intent.putExtra("NO",no);
        startActivity(intent);
    }

}
