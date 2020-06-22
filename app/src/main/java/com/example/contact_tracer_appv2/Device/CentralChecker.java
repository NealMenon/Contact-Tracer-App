package com.example.contact_tracer_appv2.Device;

import android.util.Log;


import com.example.contact_tracer_appv2.Database.Model.Interaction;
import com.example.contact_tracer_appv2.Database.TracerDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CentralChecker {

    private TracerDatabase tracerDB;
    private List<String> centralRepo;

    private String TAG = "CentralCheckerLogs";


    public CentralChecker(TracerDatabase tracerDB, List<String> centralRepo) {
        this.tracerDB = tracerDB;
        this.centralRepo = centralRepo;

    }

    public int runThrough(int threatLevel) {
        Log.d(TAG, "Inside runThrough");
        for(String key : centralRepo) {
            Log.d(TAG, "Checking for key: " + key);
            if(decodeAndCheck(key)) {
                // EPHSK HIT
                Log.d("Data", "EphSK HIT CONFIRMED");
                threatLevel++;
                // doSomething()
            }
        }
        return threatLevel;
    }

    private boolean decodeAndCheck(String key) {
        Log.d(TAG, "Inside decodeAndCheck with key = " + key );
        for(int i = 0; i < 14; i++) { // 14 days
            Log.d(TAG, "Day " + i);
            if(checkEphSecretKeys(key)) {
                return true;
            }
            key = nextKey(key);
        }
        return false;
    }

    private String nextKey(String seed) {
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
        return ret ;
    }

    private boolean checkEphSecretKeys(String seed) {
        List<String> ephKeys = genEphKeys(seed);
        Log.d(TAG, "Inside checkEphSK and list of EphSk is " + ephKeys.toString());
        // check whether any ephKeys generated are in interactions_table;
        for(String eSK : ephKeys) {
            List<Interaction> rec = tracerDB.interactionDAO().getInteractionByEphSK(eSK);
            Log.d(TAG, "Rec = " + rec.toString());
            if(!rec.isEmpty()) {
                // hit
                return true;
            }
        }
        return  false;
    }

    private List<String> genEphKeys(String seed) {
        String holder[] = new String[5];
        holder[0] = "";

        holder[0] = genEphKeysHash(seed);
        holder[1] = holder[0].substring(0, 24);
        holder[2] = holder[0].substring(24, 48);
        holder[3] = holder[0].substring(48, 72);
        holder[4] = holder[0].substring(72);

        List<String> keys = new ArrayList<String>();

        for(int i = 1; i < 5; i++) {
            holder[0] = genEphKeysHash(holder[i]);
            keys.add(holder[0].substring(0, 16));
            keys.add(holder[0].substring(16, 32));
            keys.add(holder[0].substring(32, 48));
            keys.add(holder[0].substring(48, 64));
            keys.add(holder[0].substring(64, 80));
            keys.add(holder[0].substring(80, 96));
        }
        return keys;
    }

    private String genEphKeysHash(String seed) {
        String ret = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(seed.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            ret = no.toString(36);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ret ;
    }


}
