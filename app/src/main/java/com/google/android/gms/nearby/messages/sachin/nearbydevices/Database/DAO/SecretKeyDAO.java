//package com.example.conttracerappdbtest.Database.DAO;
package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.SecretKey;
//import com.example.conttracerappdbtest.Database.Model.SecretKey;

import java.util.Date;
import java.util.List;

@Dao
public interface SecretKeyDAO {

    @Insert
    void insertSecretKey(SecretKey secretKey);

    @Update
    void updateSecretKey(SecretKey secretKey);

    @Delete
    void deleteSecretKey(SecretKey secretKey);

    @Query("SELECT * FROM secretkeys_table WHERE Date=:secretKeyDate")
    SecretKey getSecretKeyByDate(String secretKeyDate);

    @Query("SELECT * FROM secretkeys_table")
    List<SecretKey> getAllSecretKeys();

    @Query("DELETE FROM secretkeys_table")
    void deleteAllSecretKeys();

    @Query("SELECT * FROM secretkeys_table WHERE id=(SELECT MAX(id) from secretkeys_table)")
    SecretKey getLastSecretKey();
}

