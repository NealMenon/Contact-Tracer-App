//package com.example.conttracerappdbtest.Database.DAO;
package com.example.contact_tracer_appv2.Database.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contact_tracer_appv2.Database.Model.EphSecretKey;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;

import java.util.List;

@Dao
public interface EphSecretKeyDAO {



    @Insert
    void insertEphSecretKey(EphSecretKey... ephSecretKeys);

    @Update
    void updateEphSecretKeys(EphSecretKey... ephSecretKeys);


    @Delete
    void deleteEphSecretKey(EphSecretKey ephSecretKey);

    @Query("SELECT EphSecretKey  FROM ephsecretkeys_table WHERE id=:ephSKID")
    String getEphSecretKeyById(int ephSKID);

    @Query("SELECT * FROM ephsecretkeys_table")
    List<EphSecretKey> getAllEphSecretKeys();

    @Query("DELETE FROM ephsecretkeys_table")
    void deleteAllEphSecretKeys();

//    @Query("SELECT EphSecretKey FROM ephsecretkeys_table WHERE id=(SELECT MAX(id) from ephsecretkeys_table)")
//    String getRandomEphSK();

    @Query("SELECT EphSecretKey FROM ephsecretkeys_table ORDER BY RANDOM() LIMIT 1")
    String getRandomEphSK();

    @Query("DELETE FROM ephsecretkeys_table WHERE EphSecretKey=:value")
    void deleteEphSecretKeyByValue(String value);
}
