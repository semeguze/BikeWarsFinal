package co.edu.javeriana.bikewars.Logic.Entities;

import java.util.List;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbRoute {
    private String routeID;
    private String displayName;
    private String ownerID;
    private String ownerName;
    private dbRouteMarker start;
    private dbRouteMarker end;
    private List<String> members;

    public dbRoute() {
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public dbRoute(String routeID, String displayName, String ownerID, String ownerName, dbRouteMarker start, dbRouteMarker end, List<String> members) {
        this.routeID = routeID;
        this.displayName = displayName;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.start = start;
        this.end = end;
        this.members = members;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public dbRouteMarker getStart() {
        return start;
    }

    public void setStart(dbRouteMarker start) {
        this.start = start;
    }

    public dbRouteMarker getEnd() {
        return end;
    }

    public void setEnd(dbRouteMarker end) {
        this.end = end;
    }
}
