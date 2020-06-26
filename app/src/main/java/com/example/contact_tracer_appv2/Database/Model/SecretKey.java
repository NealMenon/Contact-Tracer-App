package com.example.contact_tracer_appv2.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.contact_tracer_appv2.Device.CryptoCalc;

import java.text.SimpleDateFormat;

@Entity(tableName = "secretkeys_table")
public class SecretKey {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="SecretKey")
    private String secretKey;

    @ColumnInfo(name="Date")
    @NonNull
    private String dateString;



    public SecretKey() { // only on db create
        this.secretKey = CryptoCalc.generateKey(generateSeed());
        this.dateString = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
    }

    public SecretKey(SecretKey old) { // subsequent new SKs
        this.secretKey = CryptoCalc.generateKey(old.getSecretKey());
        this.dateString = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
    }


    /*
     * This is called only on installation, creates a unique key based on which all subsequent keys are generated
     * Increase from 32 for longer seed string
     */
    private String generateSeed() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }


    public String getDateString() {
        return dateString;
    }
    public void setDateString(@NonNull String dateString) {
        this.dateString = dateString;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "SecretKey{" +
                "id=" + id +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}
