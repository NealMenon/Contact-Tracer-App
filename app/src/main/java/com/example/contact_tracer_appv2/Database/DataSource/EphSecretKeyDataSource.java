package com.example.contact_tracer_appv2.Database.DataSource;

import com.example.contact_tracer_appv2.Database.DAO.EphSecretKeyDAO;
import com.example.contact_tracer_appv2.Database.Model.EphSecretKey;

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
        ephSecretKeyDAO.updateEphSecretKeys(ephSecretKey);
    }

    @Override
    public void deleteEphSecretKey(EphSecretKey ephSecretKey) {
        ephSecretKeyDAO.deleteEphSecretKey(ephSecretKey);
    }

    @Override
    public String getEphSecretKeyById(int ephSKID) {
        return ephSecretKeyDAO.getEphSecretKeyById(ephSKID);
    }

    @Override
    public String getRandomEphSK() {
        return ephSecretKeyDAO.getRandomEphSK();
    }

    @Override
    public void deleteEphSecretKeyByValue(String value) {
        ephSecretKeyDAO.deleteEphSecretKeyByValue(value);
    }

    @Override
    public List<EphSecretKey> getAllEphSecretKeys() {
        return ephSecretKeyDAO.getAllEphSecretKeys();
    }



    @Override
    public void deleteAllEphSecretKeys() {
        ephSecretKeyDAO.deleteAllEphSecretKeys();
    }
}
