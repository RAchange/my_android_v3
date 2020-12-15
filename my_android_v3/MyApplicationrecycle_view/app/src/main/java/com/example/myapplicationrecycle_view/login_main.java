package com.example.myapplicationrecycle_view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class login_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        // login button
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final Button login = (Button)findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                animAlpha.setAnimationListener(animationListener);
            }
        });

        // Create text
        final TextView create = (TextView)findViewById(R.id.create_acc);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_main.this, sign_up.class);
                startActivity(intent);
            }
        });

        // Forgot text
        final TextView forgot = (TextView)findViewById(R.id.forgot_password);

        forgot.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View view) {
                Intent intent = new Intent(login_main.this, forgot_password.class);
                startActivity(intent);
            }
        });
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            // default username, password
            final String user = "admin";
            final String pass = "hello";

            // get input username, password
            String username = "";
            String password = "";
            EditText editText1 = (EditText)findViewById(R.id.inputusrname);
            username = editText1.getText().toString();
            EditText editText2 = (EditText)findViewById(R.id.inputPassword);
            password = editText2.getText().toString();

            editText1.setText(null);
            editText2.setText(null);

            // check valid or not
            if(username.equals(user) & password.equals(pass)){
                Intent intent = new Intent(login_main.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                new AlertDialog.Builder(login_main.this).setTitle("Error!")
                        .setMessage("Wrong Username or Password.")
                        .setNegativeButton("OK", null).show();
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}