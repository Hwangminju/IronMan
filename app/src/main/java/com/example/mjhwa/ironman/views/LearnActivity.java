package com.example.mjhwa.ironman.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.ViewFlipper;

import com.example.mjhwa.ironman.R;
import com.example.mjhwa.ironman.bluetooth.BluetoothManager;

import static java.sql.Types.NULL;

public class LearnActivity extends AppCompatActivity {

    Intent intent;
    ImageView pic;
    TextView name;
    int getNo;

    private Timer timer;
    private Toolbar mToolbar;

    ImageButton btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        mToolbar = (Toolbar) findViewById(R.id.toolbar); // 상단 틀바
        mToolbar.setTitleTextColor(Color.parseColor("#fff"));
        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        btn_start = (ImageButton) findViewById(R.id.btn_start);

        // 이미지들이 담긴 컨테이너
        final ViewFlipper vf = (ViewFlipper) findViewById(R.id.view_flipper);
        vf.setVisibility(View.INVISIBLE);
        vf.setFlipInterval(1000);

        pic = (ImageView) findViewById(R.id.pic); // 동작 이미지
        name = (TextView) findViewById(R.id.name); // 동작 이름
        intent = getIntent();
        getNo = intent.getIntExtra("NO",0);


        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.GONE); // start 버튼은 없어지고
                vf.setVisibility(View.VISIBLE); // timer는 나타내기
                vf.startFlipping();

                int displayedChild = vf.getDisplayedChild();
                int childCount = vf.getChildCount();

                if (displayedChild == childCount - 1) {
                    vf.stopFlipping();
                    vf.setVisibility(View.GONE);
                    btn_start.setVisibility(View.VISIBLE);
                }

            };

        });

        switch(getNo) {
            // intent로 받아온 동작 num case
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
                pic.setImageResource(R.drawable.norm_4);
                name.setText(R.string.opt4);
                break;
            case 5:
                pic.setImageResource(R.drawable.norm_5);
                name.setText(R.string.opt5);
                break;
            case 6:
                pic.setImageResource(R.drawable.norm_6);
                name.setText(R.string.opt6);
                break;
            case 7:
                pic.setImageResource(R.drawable.norm_7);
                name.setText(R.string.opt7);
                break;
            case 8:
                pic.setImageResource(R.drawable.norm_8);
                name.setText(R.string.opt8);
                break;
            case 9:
                pic.setImageResource(R.drawable.norm_9);
                name.setText(R.string.opt9);
                break;
            case 10:
                pic.setImageResource(R.drawable.norm_10);
                name.setText(R.string.opt10);
                break;
            default:
                break;
        }
    }
}
