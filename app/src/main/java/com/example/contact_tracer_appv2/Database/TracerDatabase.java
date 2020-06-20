//package com.example.conttracerappdbtest.Database;
package com.example.contact_tracer_appv2.Database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contact_tracer_appv2.Database.DAO.EphSecretKeyDAO;
import com.example.contact_tracer_appv2.Database.DAO.InteractionDAO;
import com.example.contact_tracer_appv2.Database.DAO.SecretKeyDAO;
import com.example.contact_tracer_appv2.Database.Model.EphSecretKey;
import com.example.contact_tracer_appv2.Database.Model.Interaction;
import com.example.contact_tracer_appv2.Database.Model.SecretKey;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Interaction.class, SecretKey.class, EphSecretKey.class}, version = 1, exportSchema = false)
public abstract class TracerDatabase extends RoomDatabase {
    // version at 1152 jun 19
    public abstract InteractionDAO interactionDAO();
    public abstract SecretKeyDAO secretKeyDAO();
    public abstract EphSecretKeyDAO ephSecretKeyDAO();

    private static TracerDatabase tracerDB;
    private static final int NUMBER_OF_THREADS = 1;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TracerDatabase getInstance(Context context) {
        if (null == tracerDB) {
            tracerDB = buildDatabaseInstance(context);
        }
        return tracerDB;
    }



    private static TracerDatabase buildDatabaseInstance(Context context) {
        Log.d("DatabaseTest", "In buildDB");
        if(tracerDB == null) {
            synchronized (TracerDatabase.class) {
                if(tracerDB == null) {
                    Log.d("DatabaseTest", "CreatingDB1");
                    tracerDB =  Room.databaseBuilder(context,
                            TracerDatabase.class, "tracer_database.db")
                            .allowMainThreadQueries()
                            /*.addCallback(sRoomDatabaseCallback)*/
                            .build();
                    SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
                    secretKeyDao.insertSecretKey(new SecretKey());
                    tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
                }
            }
        }
        Log.d("DatabaseTest", "Build done");
        return tracerDB;
    }

    public void cleanUpDB() {
        tracerDB = null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sleeperFunction() {
        final Handler handler = new Handler(Looper.getMainLooper());
        SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
        EphSecretKeyDAO ephSKdao = tracerDB.ephSecretKeyDAO();

        secretKeyDao.insertSecretKey(new SecretKey());
//        ephSKdao.insertEphSecretKey(new EphSecretKey("FirstSecretKeyTestValue"));
        generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());

        Runnable runnable = new Runnable() {
            public void run() {
                handler.postDelayed(this, 30000);
                Log.d("Data", "Reached the handler function");
                secretKeyDao.insertSecretKey(new SecretKey(secretKeyDao.getLastSecretKey()));
                generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());

            }
        };
        runnable.run(); // Uncomment this to generate newer keys
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void newDay() {
        SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
        secretKeyDao.insertSecretKey(new SecretKey(secretKeyDao.getLastSecretKey()));
        generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
    }

    protected void generateEphSecretKeys(String seed) {
        EphSecretKeyDAO ephSKdao = tracerDB.ephSecretKeyDAO();
        ephSKdao.deleteAllEphSecretKeys();
        String holder[] = new String[5];
        holder[0] = "";

        holder[0] = hash(seed);
        holder[1] = holder[0].substring(0, 24);
        holder[2] = holder[0].substring(24, 48);
        holder[3] = holder[0].substring(48, 72);
        holder[4] = holder[0].substring(72);

        List<String> keys = new ArrayList<String>();

        for(int i = 1; i < 5; i++) {
            holder[0] = hash(holder[i]);

            keys.add(holder[0].substring(0, 16));
            keys.add(holder[0].substring(16, 32));
            keys.add(holder[0].substring(32, 48));
            keys.add(holder[0].substring(48, 64));
            keys.add(holder[0].substring(64, 80));
            keys.add(holder[0].substring(80, 96));


        }
        for(String key : keys) {
            ephSKdao.insertEphSecretKey(new EphSecretKey(key));
        }

    }

    private String hash(String seed) {
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

//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            databaseWriteExecutor.execute(() -> {
//                Log.d("DatabaseTest", "Inside onCreate");
//                SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
//                secretKeyDao.insertSecretKey(new SecretKey());
//                tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
//                Log.d("DatabaseTest", "End of onCreate");
//            });
//        }
//            databaseWriteExecutor.execute(() -> {
//                Log.d("DatabaseTest", "Inside execute");
//
//                SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
//                secretKeyDao.insertSecretKey(new SecretKey());
//
//                tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
//                Log.d("DatabaseTest", "End of onCreate");
////                tracerDB.sleeperFunction();
//
//            });
//        }
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            Log.d("DatabaseTest", "Inside onOpen");
//            databaseWriteExecutor.execute(() -> {
//                final Handler handler = new Handler(Looper.getMainLooper());
//                SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
////                EphSecretKeyDAO ephSKdao = tracerDB.ephSecretKeyDAO();
//
//                secretKeyDao.insertSecretKey(new SecretKey());
////                ephSKdao.insertEphSecretKey(new EphSecretKey("FirstSecretKeyTestValue"));
//                tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
//
//                Runnable runnable = new Runnable() {
//                    public void run() {
//
//                        Log.d("Data", "Reached the handler function");
//                        secretKeyDao.insertSecretKey(new SecretKey(secretKeyDao.getLastSecretKey()));
//                        tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
//                        handler.postDelayed(this, 30000);
//                    }
//                };
////                runnable.run(); // Uncomment this to generate newer keys
//            });
//        }
//    };

}