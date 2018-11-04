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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mjhwa.ironman.MainActivity;
import com.example.mjhwa.ironman.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

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
    String sId, sPw, sPw_chk, sLR;
    Button bt_Join;
    RadioGroup rg_lr;
    RadioButton rb_left, rb_right;
    private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        et_id = (EditText) findViewById(R.id.et_Id);
        et_pw = (EditText) findViewById(R.id.et_Password);
        et_pw_chk = (EditText) findViewById(R.id.et_Password_chk);
        rg_lr = (RadioGroup) findViewById(R.id.rg_lr);
        rb_left = (RadioButton) findViewById(R.id.rb_left);
        rb_right = (RadioButton) findViewById(R.id.rb_right);
        bt_Join = (Button) findViewById(R.id.bt_Join);

        bt_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();
                    sPw_chk = et_pw_chk.getText().toString();
                    if (rb_left.isChecked()) {
                        sLR = "left";
                    } else if (rb_right.isChecked()) {
                        sLR = "right";
                    }

                    if(sPw.equals(sPw_chk))
                    {
                        /* 패스워드 확인이 정상적으로 됨 */
                        registDB rdb = new registDB();
                        rdb.execute("http://ec2-18-224-155-219.us-east-2.compute.amazonaws.com/join.php", sId, sPw, sLR);
                    }
                    else
                    {
                        /* 패스워드 확인이 불일치 함 */
                        Toast.makeText(JoinActivity.this, "PW is not same with PW chk.",Toast.LENGTH_SHORT).show();
                    }
                }

                catch (NullPointerException e)
                {
                    Log.e("err",e.getMessage());
                }
            }
        });
    }

    public class MyInfo
    {
        public String id;
        public String data;
        public String lr;
    }

    public class registDB extends AsyncTask<String, Integer, MyInfo> {

        String errorString = null;

        @Override
        protected MyInfo doInBackground(String... params) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "" + "&u_lr=" + sLR;
            String serverURL = params[0];
            String postParameters = "u_id=" + params[1] + "&u_pw=" + params[2] + "&u_lr=" + params[3] + "";
            Log.e("POST", postParameters);

            try {
                /* 서버연결 */
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                sendObject();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                String data = sb.toString().trim();
                MyInfo myInfo = new MyInfo();
                myInfo.data = data;
                myInfo.id = params[1];
                myInfo.lr = params[2];

                Log.e("myInfo", myInfo.id + " / " + myInfo.data + " / " + myInfo.lr);
                if (myInfo.data.equals("1")) {
                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생!");
                }

                return myInfo;


            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }

        private void sendObject(){
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("u_id", sId);
                jsonObject.put("u_pw", sPw);
                jsonObject.put("u_lr", sLR);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... params) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final MyInfo myInfo) {
            super.onPostExecute(myInfo);

            /* 서버에서 응답 */
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(JoinActivity.this);

            if(myInfo.data.equals("1"))
            {
                Log.e("RESULT","회원가입 성공!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("회원가입 성공!")
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
            else if(myInfo.data.equals("0"))
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + myInfo.data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ myInfo.data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 다시 돌아옴
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 다시 돌아옴
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }

    }

}