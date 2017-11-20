package co.edu.javeriana.bikewars.Logic.Entities;

import java.util.List;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbRace {
    private String raceID;
    private String routeID;
    //Referencia a los observables de los participantes
    private List<String> participants;

    public dbRace() {
    }

    public dbRace(String raceID, String routeID, List<String> participants) {
        this.raceID = raceID;
        this.routeID = routeID;
        this.participants = participants;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
