package com.example.graduationproject.Remote;

import com.example.graduationproject.Data.Chat;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IChatBotApi {
    @FormUrlEncoded
    @POST("chat")
    Call<Chat> fetchIlnessName (@Field("chatInput") String chatText );

}
