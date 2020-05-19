package com.example.smartfareadmin.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServices {

    @Headers({
            "Content-Type:Application/json",
            "Authorization:key=AAAAJ8DqZdY:APA91bF0SpCHwegdxbBzjFoR_eUZdMemOwwj5aIgwjdctG4Dz0NieDFlVyoXbnLTUJCtVFYiqjgZh5MBxfic0gQVcQtFfmcHzZ4sgbdShLUV64dGkamPq0dZe3TYWm-9c88ySRPuOz4T"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
