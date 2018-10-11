package com.example.mjhwa.ironman.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mjhwa.ironman.R;

public class KotlinActivity extends Activity {

    EditText et_id, et_pw, et_pw_chk;
    String sId, sPw, sPw_chk;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        et_id = (EditText) findViewById(R.id.et_Id);
        et_pw = (EditText) findViewById(R.id.et_Password);
        et_pw_chk = (EditText) findViewById(R.id.et_Password_chk);

        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

    }

    /* onClick에서 정의한 이름과 똑같은 이름으로 생성 */
    public void bt_Join(View view)
    {
        /* 버튼을 눌렀을 때 동작하는 소스 */
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

        if(sPw.equals(sPw_chk))
        {
            /* 패스워드 확인이 정상적으로 됨 */

        }
        else
        {
            /* 패스워드 확인이 불일치 함 */

        }
    }


}
