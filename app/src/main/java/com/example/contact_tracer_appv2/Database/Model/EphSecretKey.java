//package com.example.conttracerappdbtest.Database.Model;
package com.example.contact_tracer_appv2.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "ephsecretkeys_table")
public class EphSecretKey {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="EphSecretKey")
    @NonNull
    private String secretKey;

    public EphSecretKey(@NonNull String secretKey) {
        this.secretKey = secretKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(@NonNull String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "EphSecretKey{" +
                "id=" + id +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}


