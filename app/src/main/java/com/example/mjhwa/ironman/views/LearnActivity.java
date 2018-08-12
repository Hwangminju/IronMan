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
                pic.setImageResource(R.drawable.hand1);
                name.setText("주먹 쥐기");
                guide.setText("주먹을 꽉 쥔다는 생각으로, 5초 동안 근육을 움직이십시오.");
                guide.setGravity(Gravity.CENTER);
                break;
            case 2:
                pic.setImageResource(R.drawable.hand2);
                name.setText("엄지 접기");
                guide.setText("엄지를 접고 나머지 네 손가락은 편다는 생각으로, 5초 동안 근육을 움직이십시오.");
                guide.setGravity(Gravity.CENTER);
                break;
            case 3:
                pic.setImageResource(R.drawable.hand3);
                name.setText("C자 만들기");
                guide.setText("엄지와 나머지 네 손가락이 알파벳 C를 만든다는 생각으로, 5초 동안 근육을 움직이십시오.");
                guide.setGravity(Gravity.CENTER);
                break;
            case 4:
                pic.setImageResource(R.drawable.hand4);
                name.setText("검지 펴기");
                guide.setText("검지만 쭉 편다는 생각으로, 5초 동안 근육을 움직이십시오.");
                guide.setGravity(Gravity.CENTER);
                break;
            default:
                break;
        }
    }
}
