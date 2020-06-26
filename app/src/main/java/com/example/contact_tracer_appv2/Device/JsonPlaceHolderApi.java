package com.example.contact_tracer_appv2.Device;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("positive_user")
    Call<List<Post>> getposts();

}
