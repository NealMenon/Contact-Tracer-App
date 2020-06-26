package com.example.contact_tracer_appv2.Database.DataSource;

import com.example.contact_tracer_appv2.Database.Model.EphSecretKey;

import java.util.List;

public interface EphSecretKeyDataSourceInterface {

    void insertEphSecretKey(EphSecretKey ephSecretKey);
    void updateEphSecretKey(EphSecretKey ephSecretKey);
    void deleteEphSecretKey(EphSecretKey ephSecretKey);
    String getEphSecretKeyById(int ephSKID);
    String getRandomEphSK();
    void deleteEphSecretKeyByValue(String value);
    List<EphSecretKey> getAllEphSecretKeys();
    void deleteAllEphSecretKeys();

}
