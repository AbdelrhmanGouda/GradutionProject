package com.example.graduationproject.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
        {
            "Content-Type:application/json",
            "Authorization:key=AAAAvkept9I:APA91bGLUASRrfj3pxc97dGKBc31OZldaejWcu0M1Vqk-shRZRGzhbpnO5oRCdBHteEbd05sKa3jOBAplMAUaympIQcIg-XG6PIHhGxFor48I_UvkqEwcth_jo8eFCVhOJT6mbrv6GQM"
        }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
