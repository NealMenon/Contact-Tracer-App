package com.example.contact_tracer_appv2.Device;
import android.os.Build;
import android.util.Log;

import com.example.contact_tracer_appv2.Database.Repository.EphSecretKeyRepository;
import com.example.contact_tracer_appv2.Database.TracerDatabase;
import com.example.contact_tracer_appv2.MainActivity;
import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;

import java.nio.charset.Charset;
public class DeviceMessage {
    private static final Gson gson = new Gson();

    private final String mUUID;
    private String mMessageBody;
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
        mMessageBody = MainActivity.tracerDB.ephSecretKeyDAO().getRandomEphSK();
    }

    public String getMessageBody(EphSecretKeyRepository ephSecretKeyRepository) {
        return mMessageBody;
    }
}