package com.example.mjhwa.ironman.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.mjhwa.ironman.R;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton hand1;
    ImageButton hand2;
    ImageButton hand3;
    ImageButton hand4;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        intent = getIntent();

        hand1 = (ImageButton) findViewById(R.id.hand1);
        hand1.setOnClickListener(this);

        hand2 = (ImageButton) findViewById(R.id.hand2);
        hand2.setOnClickListener(this);

        hand3 = (ImageButton) findViewById(R.id.hand3);
        hand3.setOnClickListener(this);

        hand4 = (ImageButton) findViewById(R.id.hand4);
        hand4.setOnClickListener(this);
    }

    public void onClick(View v) {
        int no = 0;
        Intent intent = new Intent(v.getContext(), LearnActivity.class);
        switch (v.getId()) {
            case R.id.hand1:
                no = 1;
                break;
            case R.id.hand2:
                no = 2;
                break;
            case R.id.hand3:
                no = 3;
                break;
            case R.id.hand4:
                no = 4;
                break;
            default:
                break;
        }
        intent.putExtra("NO",no);
        startActivity(intent);
    }

}
