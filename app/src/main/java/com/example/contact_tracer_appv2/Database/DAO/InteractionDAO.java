//package com.example.conttracerappdbtest.Database.DAO;
package com.example.contact_tracer_appv2.Database.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contact_tracer_appv2.Database.Model.Interaction;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.Interaction;
//import com.example.conttracerappdbtest.Database.Model.Interaction;
import java.util.List;

@Dao
public interface InteractionDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInteraction(Interaction interaction);

    @Delete
    void deleteInteraction(Interaction interaction);

    @Update
    void updateInteraction(Interaction interaction);

    @Query("SELECT * from interactions_table")
    List<Interaction> getAllInteractions();

    @Query("DELETE FROM interactions_table")
    void deleteAllInteractions();

    @Query("SELECT * FROM interactions_table WHERE EphSK=:ephSK")
    List<Interaction> getInteractionByEphSK(String ephSK);

    @Query("SELECT EphSK FROM interactions_table")
    List<String> getAllInteractionsKeys();
}
