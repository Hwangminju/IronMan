package com.example.mjhwa.ironman.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mjhwa.ironman.R;

public class LearnActivity extends AppCompatActivity {

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
                pic.setImageResource(R.drawable.thumbs_up);
                name.setText("엄지 올리기");
                guide.setText("5초 동안 엄지를 올린다는 생각으로 근육을 움직이세요.");
                guide.setGravity(Gravity.CENTER);
                break;
            case 2:
                pic.setImageResource(R.drawable.ok);
                name.setText("OK 동작");
                guide.setText("5초 동안 엄지와 검지를 동그랗게 붙인다는 생각으로 근육을 움직이세요.");
                guide.setGravity(Gravity.CENTER);
                break;
            case 3:
                pic.setImageResource(R.drawable.victory);
                name.setText("V자 동작");
                guide.setText("엄지와 중지만 펴서 V자를 그린다는 생각으로 5초 동안 근육을 움직이세요.");
                guide.setGravity(Gravity.CENTER);
                break;
            case 4:
                pic.setImageResource(R.drawable.tap);
                name.setText("검지 펴기");
                guide.setText("검지만 펴고 나머지 손가락들은 접어 준다는 생각으로, 5초 동안 근육을 움직이세요.");
                guide.setGravity(Gravity.CENTER);
                break;
            default:
                break;
        }
    }
}
