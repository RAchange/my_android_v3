package com.example.myapplicationrecycle_view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // sign_up button
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final Button sign_up = (Button)findViewById(R.id.btnsing_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                animAlpha.setAnimationListener(animationListener);
            }
        });

        // back to login text
        final TextView back = (TextView)findViewById(R.id.back_to_login);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(com.example.myapplicationrecycle_view.sign_up.this, login_main.class);
                startActivity(intent);
            }
        });
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

            // get input username, email, password
            String username = "";
            String email = "";
            String password = "";
            EditText editText1 = (EditText)findViewById(R.id.input_username);
            username = editText1.getText().toString();
            EditText editText2 = (EditText)findViewById(R.id.input_email);
            email = editText2.getText().toString();
            EditText editText3 = (EditText)findViewById(R.id.inputPassword);
            password = editText3.getText().toString();

            new AlertDialog.Builder(sign_up.this).setTitle("Sign Up")
                    .setMessage("OK!")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        //@Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(sign_up.this, login_main.class);
                            startActivity(intent);
                        }
                    }).show();
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}