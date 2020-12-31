package com.example.myapplicationrecycle_view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationrecycle_view.Crypto.AESCoder;
import com.example.myapplicationrecycle_view.Crypto.RSACoder;
import com.example.myapplicationrecycle_view.Retrofit.INodeJS;
import com.example.myapplicationrecycle_view.Retrofit.RetrofitClient;
import com.example.myapplicationrecycle_view.Retrofit.User;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePage extends AppCompatActivity{

    RecyclerView recyclerView;
    MyAdapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerviewLayoutManager;

    List<UserData> userDataList;
    EditText searchView;
    CharSequence searchString = "";

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
        setContentView(R.layout.activity_home_page);
        userDataList = new ArrayList<UserData>();
        searchView = findViewById(R.id.search_bar);

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        snsPublicKey = intent.getByteArrayExtra("snsPublicKey");
        user = intent.getStringExtra("user");
        password = intent.getStringExtra("password");

        try{
            getPatientList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getPatientList() throws Exception {

        byte[] aesKey = AESCoder.initKey();
        String buf = new String(user+":"+password+":"+ Base64.getEncoder().encodeToString(aesKey));
        String token = Base64.getEncoder().encodeToString(RSACoder.encryptByPublicKey(buf.getBytes(), snsPublicKey));
        Call<String> call = myAPI.getPatientList(user, token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String jsonString = AESCoder.decrypt(response.body(), aesKey);
                    JSONArray userList = new JSONArray(jsonString);
                    if(userList.length() == 0) {
                        Toast.makeText(HomePage.this, "憑證過期: 請重新登入", Toast.LENGTH_SHORT).show();
                        HomePage.super.finish();
                    }
                    for(int i=0; i<userList.length(); i++)
                        userDataList.add(new UserData(userList.getJSONObject(i).getString("name")));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                recyclerView = findViewById(R.id.RecyclerView);
                recyclerAdapter = new MyAdapter(userDataList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerviewLayoutManager = new LinearLayoutManager(HomePage.this);
                recyclerView.setLayoutManager(recyclerviewLayoutManager);
                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        recyclerAdapter.getFilter().filter(charSequence);
                        searchString = charSequence;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(HomePage.this, "Server Internel Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable{

        //private RecyclerView.OnClickListener itemView_onClickListener;
        List<UserData> filtereduserDataList;
        List<UserData> AlluserDataList;

        public MyAdapter(List<UserData> userDataList){
           this.AlluserDataList = userDataList;
           filtereduserDataList = AlluserDataList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public View itemView;
            public TextView Username;

            public  MyViewHolder(View v){
                super(v);
                itemView = v;
                Username = itemView.findViewById(R.id.userName);
            }

            @Override
            public void onClick(View view) {

            }
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_row_item,parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            holder.Username.setText(filtereduserDataList.get(position).getUsername());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent GotoUserInfo = new Intent();
                    GotoUserInfo.setClass(HomePage.this, UserInfo.class);
                    GotoUserInfo.putExtra("password", password);
                    GotoUserInfo.putExtra("user", user);
                    GotoUserInfo.putExtra("snsPublicKey", snsPublicKey);
                    GotoUserInfo.putExtra("userName",AlluserDataList.get(position).getUsername());
                    String S_position = Integer.toString(position);
                    GotoUserInfo.putExtra("userId",S_position);
                    Toast.makeText(HomePage.this, AlluserDataList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                    startActivity(GotoUserInfo);
                }
            });
        }

        @Override
        public int getItemCount() {
            return filtereduserDataList.size();
        }

        @Override
        public Filter getFilter() {
            return myFilter;
        }

        Filter myFilter = new Filter() {

            //Automatic on background thread
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<String> filteredList = new ArrayList<>();
                String Key = charSequence.toString();

                if (Key.isEmpty()) {
                    filtereduserDataList = AlluserDataList;
                } else {
                    List<UserData> lstFiltered = new ArrayList<>();
                    for (UserData row : AlluserDataList) {
                        if (row.getUsername().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }
                    }

                    filtereduserDataList = lstFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtereduserDataList;
                return filterResults;
            }

            //Automatic on UI thread
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtereduserDataList = (List<UserData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}