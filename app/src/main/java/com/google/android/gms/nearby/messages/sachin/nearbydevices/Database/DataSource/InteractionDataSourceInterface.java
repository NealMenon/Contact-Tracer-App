//package com.example.conttracerappdbtest.Database.DataSource;
package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DataSource;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.Interaction;
//import com.example.conttracerappdbtest.Database.Model.Interaction;

import java.util.List;

public interface InteractionDataSourceInterface {

    List<Interaction> getInteractionByEphSK(String ephSK);
    List<Interaction> getAllInteractions();
    void insertInteraction(Interaction interaction);
    void updateInteraction(Interaction interaction);
    void deleteInteraction(Interaction interaction);
    void deleteAllInteractions();

}
