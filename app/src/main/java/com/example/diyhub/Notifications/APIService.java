package com.example.diyhub.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAOLF03HA:APA91bE-VQjLgyJ2Pf9MuMmrbqXE-ZD9S5-o56qkwgoo7CW8VO7qnRvhMHG8KZf5eSZa_8KHZvSoleNdQyyORRKiBbwnqxkh1l_URVcLQ1bWIyTDhtQUdLVN6HPlX4oO2cvMy08HVtmg"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
