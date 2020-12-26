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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationrecycle_view.Crypto.Base64Coder;
import com.example.myapplicationrecycle_view.Crypto.PKCS1ToSubjectPublicKeyInfo;
import com.example.myapplicationrecycle_view.Crypto.RSACoder;
import com.example.myapplicationrecycle_view.Retrofit.INodeJS;
import com.example.myapplicationrecycle_view.Retrofit.RetrofitClient;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Base64;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class login_main extends AppCompatActivity {

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
        setContentView(R.layout.activity_login_main);

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);


        // View
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final Button login = (Button)findViewById(R.id.btnlogin);
        final TextView create = (TextView)findViewById(R.id.create_acc);

        EditText editText1 = (EditText)findViewById(R.id.inputusrname);
        EditText editText2 = (EditText)findViewById(R.id.inputPassword);

        // Event
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginUser(editText1.getText().toString(), editText2.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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

    private void loginUser(String email, String password) throws Exception {

        Call<String> getPublicKeyAPI = myAPI.getPublicKey(email);
        getPublicKeyAPI.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String pubKey = response.body();
                    if(pubKey.contains("error")) {
                        Toast.makeText(login_main.this, ""+pubKey, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    byte[] snsPublicKey = PKCS1ToSubjectPublicKeyInfo.createSubjectPublicKeyInfoEncoding(Base64.getDecoder().decode(response.body())) ;
                    String encryptedPassword = Base64.getEncoder().encodeToString(RSACoder.encryptByPublicKey(password.getBytes(), snsPublicKey));
                    compositeDisposable.add(myAPI.loginUser(email, encryptedPassword)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    if(s.contains("successful")) {
                                        Toast.makeText(login_main.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(login_main.this, MainActivity.class);
                                        intent.putExtra("snsPublicKey", snsPublicKey);
                                        intent.putExtra("password", password);
                                        intent.putExtra("user", email);
                                        startActivity(intent);
                                    }
                                    else
                                        Toast.makeText(login_main.this, ""+s, Toast.LENGTH_SHORT).show(); // Else just show error from API
                                }
                            }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}