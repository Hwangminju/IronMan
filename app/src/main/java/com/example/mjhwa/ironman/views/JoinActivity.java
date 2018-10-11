package com.example.mjhwa.ironman.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mjhwa.ironman.MainActivity;
import com.example.mjhwa.ironman.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {

    EditText et_id, et_pw, et_pw_chk;
    String sId, sPw, sPw_chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Class.forName("android.os.AsyncTask");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        et_id = (EditText) findViewById(R.id.et_Id);
        et_pw = (EditText) findViewById(R.id.et_Password);
        et_pw_chk = (EditText) findViewById(R.id.et_Password_chk);

    }

    /* onClick에서 정의한 이름과 똑같은 이름으로 생성 */
    public void bt_Join(View view) {
        /* 버튼을 눌렀을 때 동작하는 소스 */
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

        if(sPw.equals(sPw_chk))
        {
            /* 패스워드 확인이 정상적으로 됨 */
            registDB rdb = new registDB();
            rdb.execute();
        }
        else
        {
            /* 패스워드 확인이 불일치 함 */
            Toast.makeText(this, "PW is not same with PW chk.",Toast.LENGTH_SHORT).show();;
        }
    }

    public class registDB extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://ec2-18-224-155-219.us-east-2.compute.amazonaws.com/join.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                try {
                    conn.setRequestProperty("Content-Type", "application/x-w ww-form-urlencoded");
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    /* 안드로이드 -> 서버 파라메터값 전달 */
                    OutputStream outs = conn.getOutputStream();
                    outs.write(param.getBytes("UTF-8"));
                    outs.flush();
                    outs.close();

                    /* 서버 -> 안드로이드 파라메터값 전달 */
                    InputStream is = null;
                    BufferedReader in = null;

                    is = conn.getInputStream();

                    in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                    String line = null;
                    StringBuffer buff = new StringBuffer();

                    while ((line = in.readLine()) != null) {
                        buff.append(line + "\n");
                    }
                    data = buff.toString().trim();
                    Log.e("RECV DATA", data);
                    in.close();

                } finally {
                    conn.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e)  {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /* 서버에서 응답 */
            Log.e("RECV DATA",data);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(JoinActivity.this);

            if(data.equals("0"))
            {
                Log.e("RESULT","성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("성공적으로 등록되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 다시 로그인으로 돌아옴
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }

    }

}