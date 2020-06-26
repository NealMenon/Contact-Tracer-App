package com.example.contact_tracer_appv2.Device;

import com.example.contact_tracer_appv2.Database.Model.Interaction;
import com.example.contact_tracer_appv2.Database.TracerDatabase;

import java.util.List;

public class CentralChecker {

    private static final int NUMBER_OF_DAYS = 14;
    private TracerDatabase tracerDB; // database object
    private List<String> centralRepo; // passed string of confirmed SKs

    public CentralChecker(TracerDatabase tracerDB, List<String> centralRepo) {
        this.tracerDB = tracerDB;
        this.centralRepo = centralRepo;
    }

    /*
     * runs through each key in confirmed list and returns increase in threatlevel, if any
     */
    public int runThrough(int threatLevel) {
        for(String key : centralRepo) {
            if(decodeAndCheck(key)) {
                threatLevel++; // EphSK HIT
            }
        }
        return threatLevel;
    }

    /*
     * check for for all secretkeys for a period of NUMBER_OF_DAYS, starting from String key
     */
    private boolean decodeAndCheck(String key) {
        for(int i = 0; i < NUMBER_OF_DAYS; i++) { // NUMBER_OF_DAYS by default 14 days
            if(checkEphSecretKeys(key)) {
                return true;
            }
            key = CryptoCalc.generateKey(key);
        }
        return false;
    }

    /*
     * Gets list of ephSKs from a certain day's keys and queried interaction_table for any match
     */
    private boolean checkEphSecretKeys(String key) {
        List<String> ephKeys = CryptoCalc.genEphKeys(key);
        for(String eSK : ephKeys) {
            List<Interaction> rec = tracerDB.interactionDAO().getInteractionByEphSK(eSK);
            if(!rec.isEmpty()) {
                return true; // match found
            }
        }
        return  false;
    }
}
