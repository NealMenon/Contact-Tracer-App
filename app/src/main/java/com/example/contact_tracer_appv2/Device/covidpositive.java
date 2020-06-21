package com.example.contact_tracer_appv2.Device;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.contact_tracer_appv2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class covidpositive extends AppCompatActivity {

    private TextView textViewResult2;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covidpositive);

        textViewResult2 = findViewById(R.id.text_view_result2);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" http://2d751f09f8e7.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        createPost2();


    }
    private void createPost2() {
        Post2 post2 = new Post2("defaulttest");

        Call<Post2> call = jsonPlaceHolderApi2.createPost2(post2);

        call.enqueue(new Callback<Post2>() {
            @Override
            public void onResponse(Call<Post2> call, Response<Post2> response) {

                if(!response.isSuccessful()){
                    textViewResult2.setText("Code :" + response.code());
                    return;
                }
                Post2 postResponse = response.body();
                String content = "";
                content += "Code :" + response.code() + "\n";
                content += "Secret_key: " + postResponse.getSecret_key() + "\n";

                textViewResult2.setText(content);
            }

            @Override
            public void onFailure(Call<Post2> call, Throwable t) {
                textViewResult2.setText(t.getMessage());
            }
        });


    }
}