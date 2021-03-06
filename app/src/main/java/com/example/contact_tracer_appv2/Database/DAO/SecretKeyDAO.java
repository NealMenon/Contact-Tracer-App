package com.example.contact_tracer_appv2.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contact_tracer_appv2.Database.Model.SecretKey;

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

