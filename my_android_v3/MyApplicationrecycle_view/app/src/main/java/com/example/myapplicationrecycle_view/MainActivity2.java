package com.example.myapplicationrecycle_view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        // logout button
        final Button logout = (Button)findViewById(R.id.logout_button4);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, login_main.class);
                startActivity(intent);
            }
        });

        //傳姓名進來
        Intent intent = getIntent();
        String name = intent.getStringExtra("name_data");
        String id = intent.getStringExtra("id_data");//傳id
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

        Call<List<Chol>> chol_call = myAPI.getChol(real_id);
        chol_call.enqueue(new Callback<List<Chol>>() {
            @Override
            public void onResponse(Call<List<Chol>> call, Response<List<Chol>> response) {
                int size = response.body().size();
                chol_list = new int[size];
                for(int i=0; i<size; i++)
                    chol_list[i] = response.body().get(i).getChol();
            }

            @Override
            public void onFailure(Call<List<Chol>> call, Throwable t) {

            }
        });

        Call<List<BloodPressure>> bps_call = myAPI.getBloodPressure(real_id);
        bps_call.enqueue(new Callback<List<BloodPressure>>() {
            @Override
            public void onResponse(Call<List<BloodPressure>> call, Response<List<BloodPressure>> response) {
                int size = response.body().size();
                blood_pressure_list = new int[size];
                for(int i=0; i<size; i++)
                    blood_pressure_list[i] = response.body().get(i).getBps();
            }

            @Override
            public void onFailure(Call<List<BloodPressure>> call, Throwable t) {

            }
        });

        Call<List<Thalach>> thalach_call = myAPI.getThalach(real_id);
        thalach_call.enqueue(new Callback<List<Thalach>>() {
            @Override
            public void onResponse(Call<List<Thalach>> call, Response<List<Thalach>> response) {
                int size = response.body().size();
                thalach_list = new int[size];
                for(int i=0; i<size; i++)
                    thalach_list[i] = response.body().get(i).getThalach();
            }

            @Override
            public void onFailure(Call<List<Thalach>> call, Throwable t) {

            }
        });

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

