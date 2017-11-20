package co.edu.javeriana.bikewars.Logic.Entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbCommercialMarker {
    private String markerID;
    private String ownerID;
    private String title;
    private String description;
    private String validUntil;
    private String url;
    private double latitude;
    private double longitude;
    private Marker marker;

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public dbCommercialMarker(String markerID, String ownerID, String title, String description, String validUntil, String url, double latitude, double longitude) {

        this.markerID = markerID;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.validUntil = validUntil;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public dbCommercialMarker() {

    }

    public void setMarker(Marker marker){
        //TODO: mejorar la presentacion y el diseño grafico de los marcadores
        this.marker = marker;
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setTitle(title);
        marker.setSnippet(description);
        marker.setVisible(true);
    }

    public void uptdateMarker(dbCommercialMarker newMarker){
        newMarker.setMarker(marker);
    }

    public void removeMarker(){
        marker.remove();
    }
}
