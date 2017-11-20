package co.edu.javeriana.bikewars.Logic.Entities;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbRouteMarker {
    private String title;
    private double latitude;
    private double longirude;

    public dbRouteMarker() {
    }

    public dbRouteMarker(String title, double latitude, double longirude) {
        this.title = title;
        this.latitude = latitude;
        this.longirude = longirude;
    }

    public dbRouteMarker(MarkerOptions mark){
        this.title = mark.getTitle();
        this.latitude = mark.getPosition().latitude;
        this.longirude = mark.getPosition().longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongirude() {
        return longirude;
    }

    public void setLongirude(double longirude) {
        this.longirude = longirude;
    }
}
