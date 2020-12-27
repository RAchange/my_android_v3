package com.example.myapplicationrecycle_view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationrecycle_view.Crypto.AESCoder;
import com.example.myapplicationrecycle_view.Crypto.RSACoder;
import com.example.myapplicationrecycle_view.Retrofit.BloodPressure;
import com.example.myapplicationrecycle_view.Retrofit.Chol;
import com.example.myapplicationrecycle_view.Retrofit.INodeJS;
import com.example.myapplicationrecycle_view.Retrofit.RetrofitClient;
import com.example.myapplicationrecycle_view.Retrofit.Thalach;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;

import java.util.Base64;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity2 extends AppCompatActivity {
    private Button blood_pressure;
    private Button chol;
    private Button thalach;
    private Button return_to_up_page;
    private int[] chol_list ;
    private int[] blood_pressure_list ;
    private int[] thalach_list ;
    private int number_of_chol = 0;
    private int real_id  = 0;

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    byte[] snsPublicKey = null;
    String user = "", password = "";

    private void open_object(){
        blood_pressure = findViewById(R.id.blood_pressure);
        chol = findViewById(R.id.chol);
        thalach = findViewById(R.id.Basal_metabolic_rate);
        return_to_up_page = findViewById(R.id.return_to_up_page);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //傳姓名進來
        Intent intent = getIntent();
        String name = intent.getStringExtra("name_data");
        String id = intent.getStringExtra("id_data");//傳id
        snsPublicKey = intent.getByteArrayExtra("snsPublicKey");
        user = intent.getStringExtra("user");
        password = intent.getStringExtra("password");
        real_id = Integer.parseInt(id) + 1; //真正的id
        System.out.println("id=" + id);
        TextView text_name = (TextView) findViewById(R.id.name);
        String people_name = "姓名:";
        people_name = people_name + name;
        text_name.setText(people_name);

        //去資料庫拿資料
        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        try {
            String token = null, encToken = null;
            byte[] key1 = AESCoder.initKey(), key2 = AESCoder.initKey(), key3 = AESCoder.initKey();
            String tokenPrefix = new String(user+":"+password+":"+Integer.toString(real_id)+":");

            token = new String(tokenPrefix + Base64.getEncoder().encodeToString(key1));
            encToken = Base64.getEncoder().encodeToString(RSACoder.encryptByPublicKey(token.getBytes(), snsPublicKey));
            Call<String> chol_call = myAPI.getChol(user, encToken);
            chol_call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().contains("error")){
                        System.out.println(response.body());
                        return;
                    }
                    try {
                        System.out.println(response.body());
                        String jsonString = AESCoder.decrypt(response.body(), key1);
                        JSONArray userList = new JSONArray(jsonString);
                        if(userList.length() == 0) {
                            Toast.makeText(MainActivity2.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                            MainActivity2.super.finish();
                        }
                        chol_list = new int[userList.length()];
                        for(int i=0; i<userList.length(); i++)
                            chol_list[i]  = userList.getJSONObject(i).getInt("chol");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

            token = new String(tokenPrefix + Base64.getEncoder().encodeToString(key2));
            encToken = Base64.getEncoder().encodeToString(RSACoder.encryptByPublicKey(token.getBytes(), snsPublicKey));
            Call<String> bps_call = myAPI.getBloodPressure(user, encToken);
            bps_call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().contains("error")){
                        System.out.println(response.body());
                        return;
                    }
                    try {
                        System.out.println(response.body());
                        String jsonString = AESCoder.decrypt(response.body(), key2);
                        JSONArray userList = new JSONArray(jsonString);
                        if(userList.length() == 0) {
                            Toast.makeText(MainActivity2.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                            MainActivity2.super.finish();
                        }
                        blood_pressure_list = new int[userList.length()];
                        for(int i=0; i<userList.length(); i++)
                            blood_pressure_list[i]  = userList.getJSONObject(i).getInt("trestbps");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

            token = new String(tokenPrefix + Base64.getEncoder().encodeToString(key3));
            encToken = Base64.getEncoder().encodeToString(RSACoder.encryptByPublicKey(token.getBytes(), snsPublicKey));
            Call<String> thalach_call = myAPI.getThalach(user, encToken);
            thalach_call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().contains("error")){
                        System.out.println(response.body());
                        return;
                    }
                    try {
                        System.out.println(response.body());
                        String jsonString = AESCoder.decrypt(response.body(), key3);
                        JSONArray userList = new JSONArray(jsonString);
                        if(userList.length() == 0) {
                            Toast.makeText(MainActivity2.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                            MainActivity2.super.finish();
                        }
                        thalach_list = new int[userList.length()];
                        for(int i=0; i<userList.length(); i++)
                            thalach_list[i]  = userList.getJSONObject(i).getInt("thalach");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



        /*for(int i=0;i<50;i++){
            if(blood_pressure_list[i] == null){
                System.out.println(i+1 + "\n");
                number_of_chol = i+1;
                break;
            }
            System.out.println(blood_pressure_list[i] + "\n");
        }*/



        open_object();
        blood_pressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,BloodPressureActivityActivity.class);
                intent.putExtra("blood_data",blood_pressure_list);
                intent.putExtra("id_data",id);
                intent.putExtra("name_data",name);
                startActivity(intent);
            }
        });
        chol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,chol.class);
                intent.putExtra("extra_data",chol_list);
                intent.putExtra("id_data",id);
                intent.putExtra("name_data",name);
                startActivity(intent);
            }
        });
        thalach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,basic.class);
                intent.putExtra("extra_data",thalach_list);
                intent.putExtra("id_data",id);
                intent.putExtra("name_data",name);
                startActivity(intent);
            }
        });
        return_to_up_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

}