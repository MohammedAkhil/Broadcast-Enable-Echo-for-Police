package com.sinch.messagingtutorial.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignUpActivity extends Activity {
    private Button verify;
    private EditText name;
    private EditText dob;
    private EditText number;
    String strname;
    String strdob;
    String strno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        verify = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);
        dob = (EditText) findViewById(R.id.dob);
        number = (EditText) findViewById(R.id.number);
        //strname = name.toString();
        //strdob = dob.toString();
        //strno = number.toString();

    }

    public void new_button_click(View vie){
        strname = name.getText().toString();
        strdob = dob.getText().toString();
        strno = number.getText().toString();
        Intent intnt = new Intent(SignUpActivity.this,CameraTestActivity.class);
        intnt.putExtra("txtname",strname);
        intnt.putExtra("txtdob",strdob);
        intnt.putExtra("txtno",strno);
        startActivity(intnt);
    }



}
