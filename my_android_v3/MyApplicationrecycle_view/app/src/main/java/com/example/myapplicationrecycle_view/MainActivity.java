package com.example.myapplicationrecycle_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplicationrecycle_view.Crypto.AESCoder;
import com.example.myapplicationrecycle_view.Crypto.RSACoder;
import com.example.myapplicationrecycle_view.Retrofit.INodeJS;
import com.example.myapplicationrecycle_view.Retrofit.RetrofitClient;
import com.example.myapplicationrecycle_view.Retrofit.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnclickListener {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> moviesList;
    int student_number=0;
    String[] name_list = new String[80];

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    byte[] snsPublicKey = null;
    String user = "", password = "";

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
        setContentView(R.layout.activity_main);

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        snsPublicKey = intent.getByteArrayExtra("snsPublicKey");
        user = intent.getStringExtra("user");
        password = intent.getStringExtra("password");


        try {
            getPatientList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPatientList() throws Exception {
        moviesList = new ArrayList<String>();
        String aesKey = AESCoder.initKeyString();
        String buf = new String(user+":"+password+":"+aesKey);
        String token = Base64.getEncoder().encodeToString(RSACoder.encryptByPublicKey(buf.getBytes(), snsPublicKey));
        Call<List<User>> call = myAPI.getPatientList(user, token);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                try {
                    List<User> userList = response.body();
                    if(userList.size() == 0) {
                        Toast.makeText(MainActivity.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                        MainActivity.super.finish();
                    }
                    for(User user: userList)
                        moviesList.add(user.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                recyclerView = findViewById(R.id.recyclerView);
                recyclerAdapter = new RecyclerAdapter(moviesList, MainActivity.this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(recyclerAdapter);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Server Internel Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onclick(int position) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("name_data",moviesList.get(position));
        intent.putExtra("password", password);
        intent.putExtra("user", user);
        intent.putExtra("snsPublicKey", snsPublicKey);
        String S_position = Integer.toString(position);
        intent.putExtra("id_data",S_position);
        startActivity(intent);
    }
}