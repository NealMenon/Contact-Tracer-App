package com.example.contact_tracer_appv2.Database.DataSource;

import com.example.contact_tracer_appv2.Database.Model.Interaction;

import java.util.List;

public interface InteractionDataSourceInterface {

    List<Interaction> getInteractionByEphSK(String ephSK);
    List<Interaction> getAllInteractions();
    void insertInteraction(Interaction interaction);
    void updateInteraction(Interaction interaction);
    void deleteInteraction(Interaction interaction);
    void deleteAllInteractions();

}
