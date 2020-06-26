package com.example.contact_tracer_appv2.Database.DataSource;

import com.example.contact_tracer_appv2.Database.DAO.InteractionDAO;
import com.example.contact_tracer_appv2.Database.Model.Interaction;

import java.util.List;

public class InteractionDataSource implements InteractionDataSourceInterface {

    private InteractionDAO interactionDAO;
    private static InteractionDataSource mInstance;

    public InteractionDataSource(InteractionDAO interactionDAO) {
        this.interactionDAO = interactionDAO;
    }

    public static InteractionDataSource getInstance(InteractionDAO interactionDAO) {
        if(mInstance == null)
            mInstance = new InteractionDataSource(interactionDAO);
        return mInstance;
    }


    public List<Interaction> getInteractionByEphSK(String ephSK) {
        return interactionDAO.getInteractionByEphSK(ephSK);
    }

    public List<Interaction> getAllInteractions() {
        return interactionDAO.getAllInteractions();
    }

    public void insertInteraction(Interaction interaction) {
        interactionDAO.insertInteraction(interaction);
    }

    public void updateInteraction(Interaction interaction) {
        interactionDAO.updateInteraction(interaction);
    }

    public void deleteInteraction(Interaction interaction) {
        interactionDAO.deleteInteraction(interaction);
    }

    public void deleteAllInteractions() {
        interactionDAO.deleteAllInteractions();
    }

}

