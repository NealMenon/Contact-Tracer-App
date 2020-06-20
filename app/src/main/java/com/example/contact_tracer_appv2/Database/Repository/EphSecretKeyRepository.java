package com.example.contact_tracer_appv2.Database.Repository;

//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource.EphSecretKeyDataSource;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource.EphSecretKeyDataSourceInterface;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;

import com.example.contact_tracer_appv2.Database.DataSource.EphSecretKeyDataSource;
import com.example.contact_tracer_appv2.Database.DataSource.EphSecretKeyDataSourceInterface;
import com.example.contact_tracer_appv2.Database.Model.EphSecretKey;

import java.util.List;

public class EphSecretKeyRepository implements EphSecretKeyDataSourceInterface {

    private EphSecretKeyDataSource mLocalDataSource;
    private static EphSecretKeyRepository mInstance;

    public EphSecretKeyRepository(EphSecretKeyDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static EphSecretKeyRepository getInstance(EphSecretKeyDataSource mLocalDataSource) {
        if(mInstance == null)
            mInstance = new EphSecretKeyRepository(mLocalDataSource);
        return mInstance;
    }



    @Override
    public void insertEphSecretKey(EphSecretKey ephSecretKey) {
        mLocalDataSource.insertEphSecretKey(ephSecretKey);
    }

    @Override
    public void updateEphSecretKey(EphSecretKey ephSecretKey) {
        mLocalDataSource.updateEphSecretKey(ephSecretKey);
    }

    @Override
    public void deleteEphSecretKey(EphSecretKey ephSecretKey) {
        mLocalDataSource.deleteEphSecretKey(ephSecretKey);
    }

    @Override
    public String getEphSecretKeyById(int ephSKID) {
        return mLocalDataSource.getEphSecretKeyById(ephSKID);
    }

    @Override
    public String getRandomEphSK() {
        return mLocalDataSource.getRandomEphSK();
    }

    @Override
    public void deleteEphSecretKeyByValue(String value) {
        mLocalDataSource.deleteEphSecretKeyByValue(value);
    }

    @Override
    public List<EphSecretKey> getAllEphSecretKeys() {
        return mLocalDataSource.getAllEphSecretKeys();
    }

    @Override
    public void deleteAllEphSecretKeys() {
        mLocalDataSource.deleteAllEphSecretKeys();
    }
}
