package com.google.android.gms.nearby.messages.sachin.nearbydevices;

import android.util.Log;

import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.Interaction;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.TracerDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CentralChecker {

    private TracerDatabase tracerDB;
    private List<String> centralRepo;
//    private List<Interaction> interactionsList;
//    private List<String> interactionKeys;

    public CentralChecker(TracerDatabase tracerDB, List<String> centralRepo) {
        this.tracerDB = tracerDB;
        this.centralRepo = centralRepo;
//        interactionKeys = tracerDB.interactionDAO().getAllInteractionsKeys();
//        interactionList = tracerDB.interactionDAO().getAllInteractions();
    }

    public void runThrough() {
        for(String key : centralRepo) {
            if(decodeAndCheck(key)) {
                // EPHSK HIT
                Log.d("Data", "EphSK HIT CONFIRMED");
                // doSomething()
                break;
            }
        }
    }

    private boolean decodeAndCheck(String key) {
        for(int i = 0; i < 14; i++) { // 14 days
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
        // check whether any ephKeys generated are in interactionKeys;
        for(String eSK : ephKeys) {
            List<Interaction> rec = tracerDB.interactionDAO().getInteractionByEphSK(eSK);
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
