package com.example.contact_tracer_appv2.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;


@Entity(tableName = "interactions_table")
public class Interaction {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;


    @ColumnInfo(name="EphSK")
    @NonNull
    private String ephSK;

    @ColumnInfo(name="Duration")
    @NonNull
    private int duration;

    @ColumnInfo(name="Proximity")
    @NonNull
    private int proximity;

    @ColumnInfo(name="TimeStamp")
    @NonNull
    private String time;

    public Interaction(String ephSK, int duration, int proximity, String time) {
        this.ephSK = ephSK;
        this.duration = duration;
        this.proximity = proximity;
        this.time = time;
    }

    public Interaction(String ephSK) {
        this.ephSK = ephSK;
        this.duration = 100;
        this.proximity = 5;
        this.time = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEphSK() {
        return ephSK;
    }

    public void setEphSK(String ephSK) {
        this.ephSK = ephSK;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", ephSK='" + ephSK + '\'' +
                ", duration=" + duration +
                ", proximity=" + proximity +
                ", time=" + time +
                '}';
    }
}
