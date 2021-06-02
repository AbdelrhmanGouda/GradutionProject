package com.example.graduationproject.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBot {
    private static Retrofit getInstance(){
        return new Retrofit.Builder().baseUrl("http://192.168.1.21:5000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static IChatBotApi cteareAPI(){
        return getInstance().create(IChatBotApi.class);
    }

}
