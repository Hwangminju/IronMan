package com.example.mjhwa.ironman.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.mjhwa.ironman.R;

public class LearnActivity extends AppCompatActivity {

    Intent intent;
    ImageView pic;
    TextView name;
    TextView guide;
    int getNo;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        mToolbar = (Toolbar) findViewById(R.id.toolbar); // 상단 틀바
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        pic = (ImageView) findViewById(R.id.pic); // 동작 이미지
        name = (TextView) findViewById(R.id.name); // 동작 이름
        intent = getIntent();
        getNo = intent.getIntExtra("NO",0);

        switch(getNo) { // intent로 받아온 동작 num case
            case 1:
                pic.setImageResource(R.drawable.norm_1);
                name.setText(R.string.opt1);
                break;
            case 2:
                pic.setImageResource(R.drawable.norm_2);
                name.setText(R.string.opt2);
                break;
            case 3:
                pic.setImageResource(R.drawable.norm_3);
                name.setText(R.string.opt3);
                break;
            case 4:
                pic.setImageResource(R.drawable.tap);
                name.setText("엄지와 새끼 손가락 펴기");
                guide.setText("5초 동안 엄지와 새끼 손가락만 편다는 생각으로 근육을 움직이세요.");
                guide.setGravity(Gravity.CENTER);
                break;
            default:
                break;
        }
    }
}
