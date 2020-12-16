package com.example.myapplicationrecycle_view.Retrofit;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password")String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password")String password);

    @GET("getPatientList")
    Call<List<User>> getPatientList();

    @GET("getChol")
    Call<List<Chol>> getChol(@Query("id") int id);

    @GET("getBloodPressure")
    Call<List<BloodPressure>> getBloodPressure(@Query("id") int id);

    @GET("getThalach")
    Call<List<Thalach>> getThalach(@Query("id") int id);
}
