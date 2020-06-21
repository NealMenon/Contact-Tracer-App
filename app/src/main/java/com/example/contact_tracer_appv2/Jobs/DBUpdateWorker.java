package com.example.contact_tracer_appv2.Jobs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.contact_tracer_appv2.Database.TracerDatabase;

public class DBUpdateWorker extends Worker {

    TracerDatabase tracerDB;

    public DBUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        tracerDB = TracerDatabase.getInstance(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        tracerDB.newDay();
        Log.d("DatabaseTest", "Inside doWork");



        return Result.success();
    }
}
