//package com.example.conttracerappdbtest.Database;
package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database;
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

import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO.EphSecretKeyDAO;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO.InteractionDAO;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO.SecretKeyDAO;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.Interaction;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.SecretKey;
//import com.example.conttracerappdbtest.Database.DAO.EphSecretKeyDAO;
//import com.example.conttracerappdbtest.Database.DAO.InteractionDAO;
//import com.example.conttracerappdbtest.Database.DAO.SecretKeyDAO;
//import com.example.conttracerappdbtest.Database.Model.EphSecretKey;
///import com.example.conttracerappdbtest.Database.Model.Interaction;
//import com.example.conttracerappdbtest.Database.Model.SecretKey;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Interaction.class, SecretKey.class, EphSecretKey.class}, version = 1, exportSchema = false)
public abstract class TracerDatabase extends RoomDatabase {

    public abstract InteractionDAO interactionDAO();
    public abstract SecretKeyDAO secretKeyDAO();
    public abstract EphSecretKeyDAO ephSecretKeyDAO();

    private static TracerDatabase tracerDB;
    private static final int NUMBER_OF_THREADS = 12;
    public static boolean locked = false;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TracerDatabase getInstance(Context context) {
        if (null == tracerDB) {
            tracerDB = buildDatabaseInstance(context);
        }
        return tracerDB;
    }



    private static TracerDatabase buildDatabaseInstance(Context context) {
        Log.d("DatabaseTest", "In buildDB");
//        RoomDatabase.Builder roombuilder = Room.databaseBuilder(context, TracerDatabase.class, "tracer_database.db");
//        roombuilder.addCallback(new RoomDatabase.Callback() {
//            @Override
//            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                super.onCreate(db);
//                Log.d("DatabaseTest", "In onCreate");
//            }
//            @Override
//            public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                super.onOpen(db);
//                Log.d("DatabaseTest", "In onOpenBEG0");
//                SecretKeyDAO dao = tracerDB.secretKeyDAO();
//
//                Log.d("DatabaseTest", "In onOpenBEG1");
//                dao.insertSecretKey(new SecretKey(1, "First", "Date1"));
//
//                Log.d("DatabaseTest", "In onOpenMID");
//                dao.insertSecretKey(new SecretKey(1, "Second", "Date2"));
//                Log.d("DatabaseTest", "In onOpenEND");
//            }
//        });
//        tracerDB = (TracerDatabase) roombuilder.build();
        if(tracerDB == null) {
            synchronized (TracerDatabase.class) {
                if(tracerDB == null) {
                    Log.d("DatabaseTest", "CreatingDB1");
                    tracerDB =  Room.databaseBuilder(context,
                            TracerDatabase.class, "tracer_database.db")
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        Log.d("DatabaseTest", "Build done");
        return tracerDB;
    }

    public void cleanUpDB() {
        tracerDB = null;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            locked = true;
            Log.d("DatabaseTest", "Inside onCreate");
            databaseWriteExecutor.execute(() -> {
                Log.d("DatabaseTest", "Inside execute");

                SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
                secretKeyDao.insertSecretKey(new SecretKey());

                tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
                Log.d("DatabaseTest", "End of onCreate");
                locked = false;
//                tracerDB.sleeperFunction();

            });
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d("DatabaseTest", "Inside onOpen");
            databaseWriteExecutor.execute(() -> {
                final Handler handler = new Handler(Looper.getMainLooper());
                SecretKeyDAO secretKeyDao = tracerDB.secretKeyDAO();
//                EphSecretKeyDAO ephSKdao = tracerDB.ephSecretKeyDAO();

                secretKeyDao.insertSecretKey(new SecretKey());
//                ephSKdao.insertEphSecretKey(new EphSecretKey("FirstSecretKeyTestValue"));
                tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());

                Runnable runnable = new Runnable() {
                    public void run() {

                        Log.d("Data", "Reached the handler function");
                        locked = true;
                        secretKeyDao.insertSecretKey(new SecretKey(secretKeyDao.getLastSecretKey()));
                        tracerDB.generateEphSecretKeys(secretKeyDao.getLastSecretKey().getSecretKey());
                        locked = false;
                        handler.postDelayed(this, 30000);
                    }
                };
//                runnable.run(); // Uncomment this to generate newer keys
            });
        }
    };

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

}