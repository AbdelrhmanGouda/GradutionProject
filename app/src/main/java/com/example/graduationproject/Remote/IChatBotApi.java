package com.example.graduationproject.Remote;

import com.example.graduationproject.Data.Chat;
import com.example.graduationproject.Data.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IChatBotApi {
    @FormUrlEncoded
    @POST("chat")
    Call<Chat> fetchIlnessName (@Field("chatInput") String chatText );

}
