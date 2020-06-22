package com.example.contact_tracer_appv2.Device;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.contact_tracer_appv2.Database.Model.SecretKey;
import com.example.contact_tracer_appv2.Database.TracerDatabase;
import com.example.contact_tracer_appv2.R;

import java.text.SimpleDateFormat;

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
<<<<<<< HEAD
                .baseUrl("http://40.81.226.196/contact-tracer-server/")
=======
                .baseUrl("https://40.81.226.196/contact-tracer-server/")
>>>>>>> e055b75338190d186660ee1667d0ff2ae1be0b57
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        createPost2();


    }
    private void createPost2() {

        String mySecretKey = TracerDatabase.getInstance(getApplicationContext()).secretKeyDAO()
                .getSecretKeyByDate(new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date())).getSecretKey();

        Log.d("pushcovid","i am passing"+mySecretKey);
        Post2 post2 = new Post2(mySecretKey);

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