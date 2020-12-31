package com.example.myapplicationrecycle_view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.myapplicationrecycle_view.Retrofit.User;

import org.json.JSONArray;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserInfo extends AppCompatActivity {

    String diagram_type, data_type, date_type;
    Button Choose_Diagram, Choose_Data, Choose_Date, Send_button, Us_pre_page_Button, Logout_Button;
    TextView Name;
    HashMap<String, Data_Information> Datas;
    Data_Information Blood_Pressure, Blood_Sugar, Cholesterol;
    int StartIndex, EndIndex;
    int real_id;

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    byte[] snsPublicKey = null;
    String user = "", password = "";
    String PreChartChoose, PreDataChoose, PreDateChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Random rand = new Random();
        Datas = new HashMap<>();
        Blood_Pressure = new Data_Information("血壓", "mmHg", 140);
        Blood_Sugar = new Data_Information("血糖", "mm/dl", 126);
        Cholesterol = new Data_Information("膽固醇", "mg/dl", 240);

        Datas.put("血壓", Blood_Pressure);
        Datas.put("血糖", Blood_Sugar);
        Datas.put("膽固醇", Cholesterol);

        String[] diagram_Type = {"長條圖" , "折線圖", "面積圖"};
        String[] data_Type = {"血壓", "膽固醇", "血糖"};
        String[] date_Type = {"近一個月", "近一周"};

        Intent intent = getIntent();
        snsPublicKey = intent.getByteArrayExtra("snsPublicKey");
        user = intent.getStringExtra("user");
        password = intent.getStringExtra("password");
        String userName = intent.getStringExtra("userName");
        String userId = intent.getStringExtra("userId");//傳id
        real_id = Integer.parseInt(userId) + 1; //真正的id
        System.out.println("id=" + userId);

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
                            Toast.makeText(UserInfo.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                            UserInfo.super.finish();
                        }

                        for(int i=0; i<userList.length(); i++)
                            Cholesterol.add("12/" + Integer.toString(i + 1), userList.getJSONObject(i).getInt("chol"));

                        if(userList.length() < 30) {
                            for (int i = userList.length(); i < 30; i++) {
                                Cholesterol.add("12/" + Integer.toString(i + 1), (int) (Math.random() * (280 - 126) + 126));
                            }
                        }

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
                            Toast.makeText(UserInfo.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                            UserInfo.super.finish();
                        }

                        for(int i=0; i<userList.length(); i++) {
                            Blood_Pressure.add("12/" + Integer.toString(i + 1), userList.getJSONObject(i).getInt("trestbps"));
                        }
                        if(userList.length() < 30) {
                            for (int i = userList.length(); i < 30; i++) {
                                Blood_Pressure.add("12/" + Integer.toString(i + 1), (int)(Math.random() * (160 - 94) + 94));
                            }
                        }
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
                            Toast.makeText(UserInfo.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                            UserInfo.super.finish();
                        }

                        for(int i=0; i<userList.length(); i++)
                            Blood_Sugar.add("12/" + Integer.toString(i + 1), userList.getJSONObject(i).getInt("thalach"));

                        if(userList.length() < 30) {
                            for (int i = userList.length(); i < 30; i++) {
                                Blood_Sugar.add("12/" + Integer.toString(i + 1), (int) (Math.random() * (165 - 71) + 71));
                            }
                        }
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

        Choose_Diagram = findViewById(R.id.Choose_Diagram);
        Choose_Data = findViewById(R.id.Choose_Data);
        Choose_Date = findViewById(R.id.Choose_Date);
        Send_button = findViewById(R.id.Send);
        Us_pre_page_Button = findViewById(R.id.Return_to_HomePage);
        Logout_Button = findViewById(R.id.Us_Logout);
        Name = findViewById(R.id.name);

        Choose_Diagram.setText("選擇圖表類型");
        Choose_Data.setText("選擇資料類型");
        Choose_Date.setText("選擇時間區段");

        diagram_type = "選擇圖表類型";
        data_type = "選擇資料類型";
        date_type = "選擇時間區段";
        PreChartChoose = "選擇圖表類型";
        PreDataChoose = "選擇資料類型";
        PreDateChoose = "選擇時間區段";

        Name.setText(userName);

        Choose_Diagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog_Builder = new AlertDialog.Builder(UserInfo.this);
                alertdialog_Builder.setTitle("選擇圖表類型")
                        .setSingleChoiceItems(diagram_Type, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                PreChartChoose = diagram_type;
                                diagram_type = diagram_Type[which];
                                Choose_Diagram.setText(diagram_Type[which]);
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Choose_Diagram.setText(PreChartChoose);
                                diagram_type = PreChartChoose;
                            }
                        });
                alertdialog_Builder.create();
                alertdialog_Builder.show();
            }
        });

        Choose_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog_Builder = new AlertDialog.Builder(UserInfo.this);
                alertdialog_Builder.setTitle("選擇資料類型")
                        .setSingleChoiceItems(data_Type, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                PreDataChoose = data_type;
                                data_type = data_Type[which];
                                Choose_Data.setText(data_Type[which]);
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Choose_Data.setText(PreDataChoose);
                                data_type = PreDataChoose;
                            }
                        });
                alertdialog_Builder.create();
                alertdialog_Builder.show();
            }
        });

        Choose_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog_Builder = new AlertDialog.Builder(UserInfo.this);
                alertdialog_Builder.setTitle("選擇時間區段")
                        .setSingleChoiceItems(date_Type, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if(date_Type[which] == "近一個月"){
                                    StartIndex = 0;
                                    EndIndex = 30;
                                }else if (date_Type[which] == "近一周"){
                                    StartIndex = 24;
                                    EndIndex = 30;
                                }
                                PreDateChoose = date_type;
                                date_type = date_Type[which];
                                Choose_Date.setText(date_Type[which]);
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Choose_Date.setText(PreDateChoose);
                                date_type = PreDateChoose;
                            }
                        });
                alertdialog_Builder.create();
                alertdialog_Builder.show();
            }
        });

        Send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(UserInfo.this, "Data Size: " + Integer.toString(Datas.get(data_type).list.size()) + " " + Integer.toString(StartIndex) + "  " +Integer.toString(EndIndex), Toast.LENGTH_SHORT).show();
                Intent GoToVisualizationPage = new Intent();
                GoToVisualizationPage.setClass(UserInfo.this, Visualization_Page.class);
                if(diagram_type == "選擇圖表類型"){
                    Toast.makeText(UserInfo.this, "尚未選擇圖表類型", Toast.LENGTH_SHORT).show();
                }else if(data_type == "選擇資料類型"){
                    Toast.makeText(UserInfo.this, "尚未選擇資料類型", Toast.LENGTH_SHORT).show();
                }else if(date_type == "選擇時間區段"){
                    Toast.makeText(UserInfo.this, "尚未選擇時間區段", Toast.LENGTH_SHORT).show();
                }else {
                    GoToVisualizationPage.putExtra("Data", Datas.get(data_type));
                    GoToVisualizationPage.putExtra("ChartType", diagram_type);
                    GoToVisualizationPage.putExtra("StartIndex", StartIndex);
                    GoToVisualizationPage.putExtra("EndIndex", EndIndex);

                    startActivity(GoToVisualizationPage);
                }
            }
        });

        Us_pre_page_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( UserInfo.this, login_main.class);
                startActivity(intent);
            }
        });
    }
}