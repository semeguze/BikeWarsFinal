package co.edu.javeriana.bikewars.Logic.Entities;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbUser {
    private String userID;
    private String displayName;
    private String photo;
    private List<String> friends;
    private List<String> routes;
    private List<String> groups;
    private List<dbTravel> historic;
    private List<String> commercialMarkers;

    public dbUser() {
    }

    public dbUser(String userID, String displayName, Uri photo) {
        this.userID = userID;
        this.displayName = displayName;
        this.photo = photo.toString();
        friends = new ArrayList<>();
        routes = new ArrayList<>();
        groups = new ArrayList<>();
        historic = new ArrayList<>();
        commercialMarkers = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getFriends() {
        if(friends==null){
            friends = new ArrayList<>();
        }
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public List<String> getGroups() {
        if(groups==null){
            groups = new ArrayList<>();
        }
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<dbTravel> getHistoric() {
        if(historic==null){
               historic = new ArrayList<>();
        }
        return historic;
    }

    public void setHistoric(List<dbTravel> historic) {
        this.historic = historic;
    }

    public List<String> getCommercialMarkers() {
        return commercialMarkers;
    }

    public void setCommercialMarkers(List<String> commercialMarkers) {
        this.commercialMarkers = commercialMarkers;
    }

    @Override
    public String toString() {
        return "dbUser{" +
                "userID='" + userID + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photo='" + photo + '\'' +
                ", friends=" + friends +
                ", routes=" + routes +
                ", groups=" + groups +
                ", historic=" + historic +
                ", commercialMarkers=" + commercialMarkers +
                '}';
    }
}
