package com.example.myapplicationrecycle_view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationrecycle_view.Retrofit.INodeJS;
import com.example.myapplicationrecycle_view.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class sign_up extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        // View
        final Button sign_up = (Button)findViewById(R.id.btnsing_up);
        final TextView back = (TextView)findViewById(R.id.back_to_login);
        EditText editText1 = (EditText)findViewById(R.id.input_username);
        EditText editText2 = (EditText)findViewById(R.id.input_email);
        EditText editText3 = (EditText)findViewById(R.id.inputPassword);

        // Event
        sign_up.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View view) {
                registerUser(editText1.getText().toString(), editText2.getText().toString(), editText3.getText().toString());
                Intent intent = new Intent(com.example.myapplicationrecycle_view.sign_up.this, login_main.class);
                startActivity(intent);
            }
        });

        // back to login text

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(com.example.myapplicationrecycle_view.sign_up.this, login_main.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(String username, String email, String password) {
        compositeDisposable.add(myAPI.registerUser(email, username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Toast.makeText(sign_up.this, ""+s, Toast.LENGTH_SHORT).show();
                }
            }));
    }
}