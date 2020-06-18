//package com.example.conttracerappdbtest.Database.DataSource;
package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource;

import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;
//import com.example.conttracerappdbtest.Database.Model.EphSecretKey;

import java.util.List;

public interface EphSecretKeyDataSourceInterface {

    void insertEphSecretKey(EphSecretKey ephSecretKey);
    void updateEphSecretKey(EphSecretKey ephSecretKey);
    void deleteEphSecretKey(EphSecretKey ephSecretKey);
    List<EphSecretKey> getEphSecretKeyById(int ephSKID);
    List<EphSecretKey> getAllEphSecretKeys();
    void deleteAllEphSecretKeys();

}