package com.sinch.messagingtutorial.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

    private Button b2;
    private Button loginButton;
    private Button signupbutton;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Intent intent;
    private Intent serviceIntent;
    private Intent newintent;
    private Intent displayintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(getApplicationContext(), ListUsersActivity.class);
        serviceIntent = new Intent(getApplicationContext(), MessageService.class);
        newintent = new Intent(getApplicationContext(),SignUpActivity.class);
        displayintent = new Intent(getApplicationContext(),DisplayActivity.class);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            startService(serviceIntent);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupbutton = (Button) findViewById(R.id.signup);
        //b2= (Button) findViewById(R.id.button2);
        usernameField = (EditText) findViewById(R.id.loginUsername);
        passwordField = (EditText) findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            startService(serviceIntent);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                "Wrong username/password combo",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                       public void onClick(View view){
                                               startActivity(newintent);

                                           }
                                        });

       // b2.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
             //   startActivity(displayintent);




  //          }
//        });



    }
    //public void bu(View vu){
      // startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    //}
    @Override
    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }
}
