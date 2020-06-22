//package com.example.conttracerappdbtest.Database.Repository;
package com.example.contact_tracer_appv2.Database.Repository;

//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource.InteractionDataSource;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource.InteractionDataSourceInterface;
//import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.Interaction;

//import com.example.conttracerappdbtest.Database.DataSource.InteractionDataSource;
//import com.example.conttracerappdbtest.Database.DataSource.InteractionDataSourceInterface;
//import com.example.conttracerappdbtest.Database.Model.Interaction;

import com.example.contact_tracer_appv2.Database.DataSource.InteractionDataSource;
import com.example.contact_tracer_appv2.Database.DataSource.InteractionDataSourceInterface;
import com.example.contact_tracer_appv2.Database.Model.Interaction;

import java.util.List;

public class InteractionRepository implements InteractionDataSourceInterface {



    private InteractionDataSource mLocalDataSource;
    private static InteractionRepository mInstance;

    public InteractionRepository(InteractionDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static InteractionRepository getInstance(InteractionDataSource mLocalDataSource) {
        if(mInstance == null)
            mInstance = new InteractionRepository(mLocalDataSource);
        return mInstance;
    }

    @Override
    public List<Interaction> getInteractionByEphSK(String ephSK) {
        return mLocalDataSource.getInteractionByEphSK(ephSK);
    }

    @Override
    public List<Interaction> getAllInteractions() {
        return mLocalDataSource.getAllInteractions();
    }

    @Override
    public void insertInteraction(Interaction interaction) {
        mLocalDataSource.insertInteraction(interaction);
    }

    @Override
    public void updateInteraction(Interaction interaction) {
        mLocalDataSource.updateInteraction(interaction);
    }

    @Override
    public void deleteInteraction(Interaction interaction) {
        mLocalDataSource.deleteInteraction(interaction);
    }

    @Override
    public void deleteAllInteractions() {
        mLocalDataSource.deleteAllInteractions();
    }
}
