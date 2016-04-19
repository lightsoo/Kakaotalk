package com.sample.lightsoo.kakaotalk.RestAPI;


import com.sample.lightsoo.kakaotalk.Data.Message;
import com.sample.lightsoo.kakaotalk.Data.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by LG on 2016-04-18.
 */
public interface LoginAPI {

    @POST("/login")
    Call<Message> login(@Body User user);

}
