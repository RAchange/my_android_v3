package com.example.myapplicationrecycle_view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity2 extends AppCompatActivity {
    private Button blood_pressure;
    private Button chol;
    private Button thalach;
    private Button return_to_up_page;
    private String[] chol_list = new String[100];
    private String[] blood_pressure_list = new String[100];
    private String[] thalach_list = new String[100];
    private int number_of_chol = 0;
    private int real_id  = 0;
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
        real_id = Integer.parseInt(id) + 1; //真正的id
        System.out.println("id=" + id);
        TextView text_name = (TextView) findViewById(R.id.name);
        String people_name = "姓名:";
        people_name = people_name + name;
        text_name.setText(people_name);

        //去資料庫拿資料
        thread_mysql a = new thread_mysql();
        a.start();
        a.give_id(Integer.toString(real_id));//把id傳給後端
        try {//parent要等資料抓回來才能用資料
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chol_list = a.get_chol_list();
        blood_pressure_list = a.getBlood_pressure_list();
        thalach_list = a.getThalach_list();

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

