package com.example.contact_tracer_appv2.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.contact_tracer_appv2.Database.DAO.EphSecretKeyDAO;
import com.example.contact_tracer_appv2.Database.DAO.InteractionDAO;
import com.example.contact_tracer_appv2.Database.DAO.SecretKeyDAO;
import com.example.contact_tracer_appv2.Database.Model.EphSecretKey;
import com.example.contact_tracer_appv2.Database.Model.Interaction;
import com.example.contact_tracer_appv2.Database.Model.SecretKey;
import com.example.contact_tracer_appv2.Device.CryptoCalc;

import java.util.List;


@Database(entities = {Interaction.class, SecretKey.class, EphSecretKey.class}, version = 1, exportSchema = false)
public abstract class TracerDatabase extends RoomDatabase {

    // following DAOs allow access to each table
    public abstract InteractionDAO interactionDAO();
    public abstract SecretKeyDAO secretKeyDAO();
    public abstract EphSecretKeyDAO ephSecretKeyDAO();

    private static TracerDatabase tracerDB;

    public static TracerDatabase getInstance(Context context) {
        if (null == tracerDB) {
            tracerDB = buildDatabaseInstance(context);
        }
        return tracerDB;
    }


    private static TracerDatabase buildDatabaseInstance(Context context) {
        if (tracerDB == null) {
            synchronized (TracerDatabase.class) {
                if (tracerDB == null) {
                    tracerDB = Room.databaseBuilder(context,
                            TracerDatabase.class, "tracer_database.db")
                            .allowMainThreadQueries()
                            .build();
                    SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
                    secretKeyDao.insertSecretKey(new SecretKey());
                    tracerDB.insertEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
                }
            }
        }
        return tracerDB;
    }

    private void insertEphSecretKeys(String secretKey) {
        EphSecretKeyDAO ephSKdao = tracerDB.ephSecretKeyDAO();
        List<String> ephKeys = CryptoCalc.genEphKeys(secretKey);
        ephSKdao.deleteAllEphSecretKeys();
        for(String key : ephKeys) {
            ephSKdao.insertEphSecretKey(new EphSecretKey(key));
        }
    }

    public void cleanUpDB() {
        tracerDB = null;
    }

    public void newDay() {
        SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
        secretKeyDao.insertSecretKey(new SecretKey(secretKeyDao.getLastSecretKey()));
        insertEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
    }

}
