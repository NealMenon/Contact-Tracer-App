package com.example.contact_tracer_appv2.Database.Model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;


import javax.crypto.KeyGenerator;

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
        Log.d("Data", "New FIRST SecretKey");
        this.secretKey = this.generateKey(generateSeed());
//        this.dateString = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
        this.dateString = new java.util.Date().toString();
    }

    public SecretKey(SecretKey old) { // subsequent new SKs
        this.secretKey = this.generateKey(old.getSecretKey());
        Log.d("Data", "New SK");
//        this.dateString = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
        this.dateString = new java.util.Date().toString();
    }

    private String generateSeed() {
//        String chrs = "0123456789abcdefghijklmnopqrstuvwxyz-_!@#$%^&*+ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        SecureRandom secureRandom = null;
//        try {
//            secureRandom = SecureRandom.getInstanceStrong();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        String seed = secureRandom.ints(32, 0, chrs.length()).mapToObj(i -> chrs.charAt(i)).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
////        Log.d("Database", "Seed: " + seed);
//        return seed;
////        return "AAA";
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    private String generateKey(String seed) {
        String ret = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(seed.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            ret = no.toString(16);
            while (ret.length() < 32) {
                ret = "0" + ret;
            }
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        Log.d("Database", "Final hash: " + ret);
//        ret += seed + "B";
        return ret ;
    }



    @NonNull
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
