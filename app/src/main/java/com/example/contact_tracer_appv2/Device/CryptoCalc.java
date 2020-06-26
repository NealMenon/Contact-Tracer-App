package com.example.contact_tracer_appv2.Device;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/*
 * Class that contains all crypto functions for SecretKey, TracerDatabase as well as CentralChecker classes
 */
public final class CryptoCalc {

    public CryptoCalc() {}

    /*
     * Takes input key and returns next key (for the next day)
     */
    public static String generateKey(String key) {
        String ret = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(key.getBytes());
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

    /*
     * Takes a day's secretkey and returns List<String> of EphSK for that day
     */
    public static List<String> genEphKeys(String seed) {
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

    /*
     * Helper function for genEPhKeys that generates longer length string
     * Note: 36 means all lower case chars + 10 numerals will be produced as output of hash
     */
    private static String genEphKeysHash(String seed) {
        String ret = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(seed.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            ret = no.toString(36);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ret ;
    }

}
