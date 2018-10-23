package com.example.mjhwa.ironman.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mjhwa.ironman.MainActivity;
import com.example.mjhwa.ironman.R;
import com.example.mjhwa.ironman.RequestHttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class LoginActivity extends AppCompatActivity {

    EditText etId, etPw;
    String sId, sPw;
    Button bt_login;
    private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        bt_login = (Button) findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sId = etId.getText().toString();
                    sPw = etPw.getText().toString();

                    loginDB lDB = new loginDB();
                    lDB.execute("http://ec2-18-224-155-219.us-east-2.compute.amazonaws.com/login.php", sId, sPw);
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
    }

    public class loginDB extends AsyncTask<String, Integer, MyInfo> {

        String errorString = null;

        @Override
        protected MyInfo doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "u_id=" + params[1] + "&u_pw=" + params[2] + "";

            try {
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                /* 인풋 파라메터값 생성 */
                String param = "u_id=" + sId + "&u_pw=" + sPw + "";

                // Log.e("POST",param);
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();

                /*
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String data = "";

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                */

                sendObject();

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                /* 안드로이드 -> 서버 파라메터값 전달 */
                /* outs.write(param.getBytes("UTF-8"));
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(param);
                bw.flush();
                osw.close();
                bw.close();*/

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }
                /* 서버 -> 안드로이드 파라메터값 전달 */
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

                if (myInfo.data.equals("1")) {
                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = " + myInfo.data);
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
            } catch (JSONException e){
                e.printStackTrace();
            }
        }


        @Override
        protected void onProgressUpdate(Integer... params) {

        }

        @Override
        protected void onPostExecute(final MyInfo myInfo) {
            super.onPostExecute(myInfo);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);

            if(myInfo.data.equals("1"))
            {
                Log.e("RESULT","로그인 성공!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("로그인 성공!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog.dismiss();
                                Log.e("RESULT","Log In With ID : " + myInfo.id);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("ID", myInfo.id);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else if(myInfo.data.equals("0"))
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + myInfo.data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("로그인 중 에러가 발생했습니다! errcode : "+ myInfo.data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog.dismiss();
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }
}
