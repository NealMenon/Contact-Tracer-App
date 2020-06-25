package com.example.contact_tracer_appv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;


import com.example.contact_tracer_appv2.Database.DataSource.EphSecretKeyDataSource;
import com.example.contact_tracer_appv2.Database.DataSource.InteractionDataSource;
import com.example.contact_tracer_appv2.Database.Model.Interaction;
import com.example.contact_tracer_appv2.Database.Repository.EphSecretKeyRepository;
import com.example.contact_tracer_appv2.Database.Repository.InteractionRepository;
import com.example.contact_tracer_appv2.Database.TracerDatabase;
import com.example.contact_tracer_appv2.Device.CentralChecker;
import com.example.contact_tracer_appv2.Device.DeviceMessage;
import com.example.contact_tracer_appv2.Device.covidnotsafe;
import com.example.contact_tracer_appv2.Device.covidpositive;
import com.example.contact_tracer_appv2.Device.JsonPlaceHolderApi;
import com.example.contact_tracer_appv2.Device.Post;
import com.example.contact_tracer_appv2.Device.covidsafe;
import com.example.contact_tracer_appv2.Jobs.DBUpdateWorker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

// server api imports
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView textViewResult;                                                //object for api response debug purpose
    public int threatLevel = 0;                                                    //ThreatLevel
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button covidpositive;
    private Button covid_status;
    private static final int TTL_IN_SECONDS = 3*60*60;                              // Maximum connection response time
    private static final String KEY_UUID = "key_uuid";                              // Key used by google for creating tokens
    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).setDistanceType(Strategy.DISTANCE_TYPE_EARSHOT).build();  ///Earshot Strategy so we can limit communicstion distance to 5-10 feets
    //// Genrating TOKEN FOR COMMUNICATION TO SERVER ///
    private static String getUUID(SharedPreferences sharedPreferences) {
        String uuid = sharedPreferences.getString(KEY_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(KEY_UUID, uuid).apply();
        }
        return uuid;
    }
    public static TracerDatabase tracerDB;
    private InteractionRepository interactionRepository;
    private EphSecretKeyRepository ephSecretKeyRepository;


    private GoogleApiClient mGoogleApiClient;   ////Google Client needed for communication between google server and Phone
    private SwitchCompat mPublishSwitch;        ////Publisher Switch needed for connection establishment
    private SwitchCompat mSubscribeSwitch;      /// Subscriber switch needed for connection establishment
    private Message mPubMessage;                //// PubMessage which will be sent to other devices
    private MessageListener mMessageListener;   /// MessageListner object which will recive mPubMessage
    private ArrayAdapter<String> mNearbyDevicesArrayAdapter;  //// most important part of our code this array of string contains All recived strings which we got during communication
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(threatLevel != 0) {
            setTheme(R.style.HighThreatTheme);
        }
        tracerDB = TracerDatabase.getInstance(MainActivity.this);  // THIS BLOCK CREATES THE DATABASE. INCLUDE IN MAIN ACTIVITY
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(DBUpdateWorker.class, 1, TimeUnit.HOURS)
                .build();
        WorkManager mWorkManager = WorkManager.getInstance();
        mWorkManager.enqueueUniquePeriodicWork("Updating_SK_and_ESK", ExistingPeriodicWorkPolicy.KEEP, workRequest);

        interactionRepository = InteractionRepository.getInstance(InteractionDataSource.getInstance(tracerDB.interactionDAO()));
        interactionRepository.getAllInteractions(); // This actually confirms build of DB
        ephSecretKeyRepository = EphSecretKeyRepository.getInstance(EphSecretKeyDataSource.getInstance(tracerDB.ephSecretKeyDAO()));

        /////////////////// function for calling response of API from SERVER /////////////////
        textViewResult = findViewById(R.id.text_view_result);       ///////// Inserted here for Debuging purpose so can check if response is here
        /// creating object of retrofit according to standard JSon format implementation
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://40.81.226.196/contact-tracer-server/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ///////////////////////////////////////////////////
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class); //Calling the response format class
        Call<List<Post>> call = jsonPlaceHolderApi.getposts(); /// List of posts are getting in CALL
        /////////////Background Thread For our Api Execution /////////////
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: "+ response.code()); ///API Response Code needs 200 for success
                }
                List<Post> posts = response.body(); ///response body format is list
                List<String> centralRepo = new ArrayList<String>();
                for(Post post: posts) {
                    String content = "";
                    String secKey = post.getSecret_key();
                    centralRepo.add(secKey);
                    content += "secret_key" + secKey + "\n";
                    content += "timestamp" + post.getTimestamp() + "\n";
                    textViewResult.append(content); /// For UI showcase purpose
                }
                CentralChecker ck = new CentralChecker(tracerDB, centralRepo);
                threatLevel = ck.runThrough(threatLevel);
                logAndShowSnackbar("ThreatLevel is " + threatLevel);
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) { //////throwing  exception failure response so our app will not crash
                textViewResult.setText(t.getMessage());
            }
        });
        ///////////////Background Thread Function Completed /////////////

        /////Covid Button which will redirect us to new activity ////////
        covidpositive = (Button) findViewById(R.id.covidpositive);
        covidpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        covid_status = (Button) findViewById(R.id.covid_status);
        covid_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(threatLevel==0)
                {
                    openActivity3();
                }
                else
                {
                    openActivity4();
                }
            }
        });
        //////////////


        Log.d("DatabaseTest", "GOt key2: " + ephSecretKeyRepository.getRandomEphSK());
        mSubscribeSwitch = (SwitchCompat) findViewById(R.id.subscribe_switch);          //switch of subscriber permission
        mPublishSwitch = (SwitchCompat) findViewById(R.id.publish_switch);               //switch of publisher permission
        mSubscribeSwitch.setChecked(true);                                               //making state to true so that publish and subscribe will always be ON
        mPublishSwitch.setChecked(true);                                                  //making state to true for always true
        //Publisher message is getting it's response from Device message class
        mPubMessage = DeviceMessage.newNearbyMessage(getUUID(getSharedPreferences(
                getApplicationContext().getPackageName(), Context.MODE_PRIVATE)));
        //mMessageListner is object intialization

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////// NearBy API Functions are Starting from HERE  Can be modified according to required USAGE //////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message)
            {
                String secKey = DeviceMessage.fromNearbyMessage(message).getMessageBody(ephSecretKeyRepository);
                interactionRepository.insertInteraction(new Interaction(secKey));
                mNearbyDevicesArrayAdapter.add(secKey);
            }
            @Override
            public void onLost(final Message message) {
                mNearbyDevicesArrayAdapter.remove(
                        DeviceMessage.fromNearbyMessage(message).getMessageBody(ephSecretKeyRepository));
            }
        };
        mSubscribeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    if (isChecked) {
                        subscribe();
                    } else {
                        subscribe();
                    }
                }
            }
        });
        mPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    if (isChecked) {
                        publish();
                    } else {
                        publish();
                    }
                }
            }
        });
        ///// Getting List of ALL nearby devices KEYS or DATA Which They are Publishing
        final List<String> nearbyDevicesArrayList = new ArrayList<>();
        mNearbyDevicesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                nearbyDevicesArrayList);
        final ListView nearbyDevicesListView = (ListView) findViewById(
                R.id.nearby_devices_list_view);
        if (nearbyDevicesListView != null) {
            nearbyDevicesListView.setAdapter(mNearbyDevicesArrayAdapter);
        }
        buildGoogleApiClient();
    }
    ///Building API Client for Token Sending To Server
    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mPublishSwitch.setEnabled(true);
        mSubscribeSwitch.setEnabled(true);
        logAndShowSnackbar("Exception while connecting to Google Play services: " +
                connectionResult.getErrorMessage());
    }
    @Override
    public void onConnectionSuspended(int i) {
        logAndShowSnackbar("Connection suspended. Error code: " + i);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        if (mPublishSwitch.isChecked()) {                           ////// Function is Handling Data Communication when devices are connected
            publish();                                              //////  devices are in both publish and subscribe mode
        }
        if (mSubscribeSwitch.isChecked()) {
            subscribe();
        }
    }
    private void subscribe() {                                  //// Subscriber Function device is set to be always in subscribe mode only
        Log.i(TAG, "Subscribing");
        //mNearbyDevicesArrayAdapter.clear();
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new SubscribeCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer subscribing");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSubscribeSwitch.setChecked(true);
                            }
                        });
                    }
                }).build();
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Subscribed successfully.");
                        } else {
                            logAndShowSnackbar("Could not subscribe, status = " + status);
                            mSubscribeSwitch.setChecked(true);
                        }
                    }
                });
    }
    private void publish() {                                  ////Publisher Function always in Published State only
        Log.i(TAG, "Publishing");
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new PublishCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer publishing");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPublishSwitch.setChecked(true);
                            }
                        });
                    }
                }).build();
        Nearby.Messages.publish(mGoogleApiClient, mPubMessage, options)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Published successfully.");
                        } else {
                            logAndShowSnackbar("Could not publish, status = " + status);
                            mPublishSwitch.setChecked(true);
                        }
                    }
                });
    }
    private void unsubscribe() {             /////////////Breaking connection between devices when not needed
        Log.i(TAG, "Unsubscribing.");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }
    private void unpublish() {             ///////////////Breaking Connection for publisher side
        Log.i(TAG, "Unpublishing.");
        Nearby.Messages.unpublish(mGoogleApiClient, mPubMessage);
    }
    private void logAndShowSnackbar(final String text) {       ////////If internet is not connected then this will pop up connection error message
        Log.w(TAG, text);
        View container = findViewById(R.id.activity_main_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////// NearBYAPI FUNCTIONS END HERE ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void openActivity2() {   ///////////////Activity of Covid positive state
        Intent intent = new Intent(this,covidpositive.class);
        startActivity(intent);
    }
    public void openActivity3()
    {
        Intent intent = new Intent(this, covidsafe.class);
        startActivity(intent);
    }
    public void openActivity4()
    {
        Intent intent = new Intent(this, covidnotsafe.class);
        startActivity(intent);
    }

}