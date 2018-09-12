package com.example.mjhwa.ironman.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mjhwa.ironman.R;

public class LearnActivity extends Activity {

    Intent intent;
    ImageView pic;
    TextView name;
    TextView guide;
    int getNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        pic = (ImageView) findViewById(R.id.pic); // 동작 이미지
        name = (TextView) findViewById(R.id.name); // 동작 이름
        guide = (TextView) findViewById(R.id.guide); // 동작 설명
        intent = getIntent();
        getNo = intent.getIntExtra("NO",0);

        switch(getNo) { // intent로 받아온 동작 num case
            case 1:
                pic.setImageResource(R.drawable.norm1);
                name.setText("검지,중지,약지 펴기");
                guide.setText(R.string.norm1);
                guide.setGravity(Gravity.CENTER);
                break;
            case 2:
                pic.setImageResource(R.drawable.norm2);
                name.setText("엄지,검지,중지 펴기");
                guide.setText(R.string.norm2);
                guide.setGravity(Gravity.CENTER);
                break;
            case 3:
                pic.setImageResource(R.drawable.norm3);
                name.setText("새끼 손가락 펴기");
                guide.setText("5초 동안 새끼 손가락만 편다는 생각으로 근육을 움직이세요.");
                guide.setGravity(Gravity.CENTER);
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
