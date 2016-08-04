package com.sinch.messagingtutorial.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayActivity extends Activity {
    private TextView text;
    private TextView text2;
    private TextView text3;
    private TextView t4;
    //private EditText dob;
    //private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
         text = (TextView) findViewById(R.id.textView);
         text2 = (TextView) findViewById(R.id.textView2);
         text3 = (TextView) findViewById(R.id.textView3);
          t4 = (TextView) findViewById(R.id.textView6);
       // dob = (EditText) findViewById(R.id.editText2);
       // name = (EditText) findViewById(R.id.displayUsername);
        Intent intent = getIntent();
       // String str = intent.getStringExtra("mytext");
        String[] attrNames = intent.getStringArrayExtra("myattrnames");
        text.setText(attrNames[0]);
        String[] attrValues = intent.getStringArrayExtra("myattrvalues");
        text2.setText(attrValues[0]);
        String ano = intent.getStringExtra("myno");
        text3.setText(ano);
        String parse1 = intent.getStringExtra("parse");
        String ver = "verification Success";
        String err = "error";
        String sno = text2.getText().toString();
        Boolean b = ano.equals(sno);
        if(b==true)
        {
            t4.setText(ver);
            Toast.makeText(getApplicationContext(),
                    "Verification Complete",
                    Toast.LENGTH_LONG).show();
            Intent vr = new Intent(DisplayActivity.this,SignUpFinal.class);
            vr.putExtra("aadhaardetails",parse1);
            startActivity(vr);

        }
                else{
            t4.setText(err);
            Toast.makeText(getApplicationContext(),
                    "Wrong Aadhaar Number..Try Again",
                    Toast.LENGTH_LONG).show();
            Intent er = new Intent(DisplayActivity.this,SignUpActivity.class);
            startActivity(er);
        }

        //text.setText(str);
        //dob.setText(attrValues[2]);
        //name.setText(attrValues[3]);
    }




}
