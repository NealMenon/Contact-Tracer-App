package com.google.android.gms.nearby.messages.sachin.nearbydevices;

import android.util.Log;

import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Repository.EphSecretKeyRepository;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Repository.InteractionRepository;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.TracerDatabase;

public class DBTester {

    private TracerDatabase tracerDB;
    private InteractionRepository interactionRepository;
    private EphSecretKeyRepository ephSecretKeyRepository;

    public DBTester(TracerDatabase tracerDB, InteractionRepository interactionRepository, EphSecretKeyRepository ephSecretKeyRepository) {
        this.tracerDB = tracerDB;
        this.interactionRepository = interactionRepository;
        this.ephSecretKeyRepository = ephSecretKeyRepository;
    }

    public String getEphSK() {
        ephSecretKeyRepository.getAllEphSecretKeys();
        ephSecretKeyRepository.insertEphSecretKey(new EphSecretKey("Friday Jun19"));
        String randomKey = ephSecretKeyRepository.getRandomEphSK();
        Log.d("DatabaseTest", "The return EphSK in DevMess is " + randomKey);
        ephSecretKeyRepository.deleteEphSecretKeyByValue(randomKey);
        return randomKey;
    }
}
