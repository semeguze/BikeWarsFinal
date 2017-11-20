package co.edu.javeriana.bikewars.Logic.Entities;

import android.location.Location;

/**
 * Created by Todesser on 30/10/2017.
 */

public class dbTravel {
    private dbRoute route;
    private long timeElapsed;
    private long date;
    private long kilometersCovered;
    private String weather;
    private Location anterior;

    public dbTravel() {
    }

    public dbTravel(dbRoute route, long timeElapsed, long date, long kilometersCovered, String weather) {
        this.route = route;
        this.timeElapsed = timeElapsed;
        this.date = date;
        this.kilometersCovered = kilometersCovered;
        this.weather = weather;
    }

    public dbTravel(dbRoute route, long date, String weather) {
        this.route = route;
        this.date = date;
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public dbRoute getRoute() {
        return route;
    }

    public void setRoute(dbRoute route) {
        this.route = route;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getKilometersCovered() {
        return kilometersCovered;
    }

    public void setKilometersCovered(long kilometersCovered) {
        this.kilometersCovered = kilometersCovered;
    }

    public void addMeters(Location nuevo){
        if(anterior!=null){
            this.kilometersCovered += nuevo.distanceTo(anterior);
        }
        anterior = nuevo;
    }
}
