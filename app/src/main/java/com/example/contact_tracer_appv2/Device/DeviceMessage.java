package com.example.contact_tracer_appv2.Device;
import android.os.Build;
import android.util.Log;

import com.example.contact_tracer_appv2.Database.Repository.EphSecretKeyRepository;
import com.google.android.gms.nearby.messages.Message;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO.EphSecretKeyDAO;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Repository.EphSecretKeyRepository;
import com.google.gson.Gson;

import java.nio.charset.Charset;

/**
 * Major class which will take input from database of EPH-Key
 * used com.google.android.gms.nearby.messages.Message
 */
public class DeviceMessage {
    private static final Gson gson = new Gson();

    private final String mUUID;
    private final String mMessageBody;
    public static Message newNearbyMessage(String instanceId) {
        DeviceMessage deviceMessage = new DeviceMessage(instanceId);
        return new Message(gson.toJson(deviceMessage).getBytes(Charset.forName("UTF-8")));
    }
    public static DeviceMessage fromNearbyMessage(Message message) {
        String nearbyMessageString = new String(message.getContent()).trim();
        return gson.fromJson(
                (new String(nearbyMessageString.getBytes(Charset.forName("UTF-8")))),
                DeviceMessage.class);
    }

    private DeviceMessage(String uuid) {
        mUUID = uuid;
        mMessageBody = Build.MODEL;
    }

    public String getMessageBody(EphSecretKeyRepository ephSecretKeyRepository) {

        ephSecretKeyRepository.getAllEphSecretKeys();
//        ephSecretKeyRepository.insertEphSecretKey(new EphSecretKey("Friday Jun19DM"));
        String randomKey = ephSecretKeyRepository.getRandomEphSK();
        Log.d("DatabaseTest", "The return EphSK in DevMess is " + randomKey);
        ephSecretKeyRepository.deleteEphSecretKeyByValue(randomKey);
        return randomKey;
    }
}