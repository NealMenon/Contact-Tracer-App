//package com.example.conttracerappdbtest.Database.DataSource;
package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource;

import  com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO.EphSecretKeyDAO;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;

//import com.example.conttracerappdbtest.Database.DAO.EphSecretKeyDAO;
//import com.example.conttracerappdbtest.Database.Model.EphSecretKey;

import java.util.List;

public class EphSecretKeyDataSource implements EphSecretKeyDataSourceInterface {

    private EphSecretKeyDAO ephSecretKeyDAO;
    private static EphSecretKeyDataSource mInstance;

    public EphSecretKeyDataSource(EphSecretKeyDAO ephSecretKeyDAO) {
        this.ephSecretKeyDAO = ephSecretKeyDAO;
    }

    public static EphSecretKeyDataSource getInstance(EphSecretKeyDAO ephSecretKeyDAO) {
        if(mInstance == null)
            mInstance = new EphSecretKeyDataSource(ephSecretKeyDAO);
        return mInstance;
    }

    @Override
    public void insertEphSecretKey(EphSecretKey ephSecretKeys) {
        ephSecretKeyDAO.insertEphSecretKey(ephSecretKeys);
    }

    @Override
    public void updateEphSecretKey(EphSecretKey ephSecretKey) {
//        ephSecretKeys.up
    }

    @Override
    public void deleteEphSecretKey(EphSecretKey ephSecretKey) {

    }

    @Override
    public List<EphSecretKey> getEphSecretKeyById(int ephSKID) {
        return null;
    }

    @Override
    public List<EphSecretKey> getAllEphSecretKeys() {
        return null;
    }

    @Override
    public void deleteAllEphSecretKeys() {

    }
}
