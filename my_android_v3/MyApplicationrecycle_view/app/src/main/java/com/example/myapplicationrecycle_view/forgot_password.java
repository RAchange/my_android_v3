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

public class forgot_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // forgot button
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final Button reset = (Button)findViewById(R.id.btnreset);

        reset.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(forgot_password.this, login_main.class);
                startActivity(intent);
            }
        });
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

            // get input email
            String email = "";
            EditText editText1 = (EditText)findViewById(R.id.input_email);
            email = editText1.getText().toString();

            new AlertDialog.Builder(forgot_password.this).setTitle("Reset")
                    .setMessage("Check your email.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        //@Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(forgot_password.this, login_main.class);
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