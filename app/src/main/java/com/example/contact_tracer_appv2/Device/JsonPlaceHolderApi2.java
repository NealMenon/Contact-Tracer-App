package com.example.contact_tracer_appv2.Device;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi2 {

    @POST("report_positive")
    Call<Post2> createPost2(@Body Post2 post2);
}
