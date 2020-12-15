package com.example.myapplicationrecycle_view.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitClient {
    private static Retrofit instance;


    public static Retrofit getInstance(){
        if(instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl("http://193.42.40.110:3000/") // In Enulator, 127.0.0.1 will change to 193.42.40.110
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
