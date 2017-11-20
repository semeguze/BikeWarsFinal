package co.edu.javeriana.bikewars.Logic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Interfaces.LocationListener;
import co.edu.javeriana.bikewars.Logic.Entities.dbCommercialMarker;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.Entities.dbTravel;
import co.edu.javeriana.bikewars.RouteLobbyView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * Created by jairo on 28/10/17.
 */

public class MapData {
    //Aux
    private static MapData instance = null;
    public static final LatLng bogotaMark = new LatLng(4.624335, -74.063644);
    private static ReactiveLocationProvider provider;
    private static Disposable subscription;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Attributes
    private dbObservable ubication;
    private List<MarkerOptions> markers;
    private List<MarkerOptions> globalMarkers;
    private List<LocationListener> listeners;
    private Route route;
    private dbTravel travel;

    private MapData() {
        markers = new ArrayList<>();
        listeners = new ArrayList<>();
        provider = new ReactiveLocationProvider(RouteLobbyView.context);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        loadGlobalMarkers();
    }

    public static MapData getInstance() {
        if (instance == null) {
            instance = new MapData();
        }
        return instance;
    }

    private void loadGlobalMarkers() {
        globalMarkers = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(UserData.markersRoot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot marker : dataSnapshot.getChildren()) {
                    dbCommercialMarker markerData = marker.getValue(dbCommercialMarker.class);
                    globalMarkers.add(new MarkerOptions()
                            .title(markerData.getTitle())
                            .snippet(markerData.getDescription())
                            .position(new LatLng(markerData.getLatitude(), markerData.getLongitude()))
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public dbObservable getUbication() {
        synchronized (ubication) {
            return ubication;
        }
    }

    public Marker addMarker(GoogleMap map, MarkerOptions options) {
        markers.add(options);
        return map.addMarker(options);
    }

    public List<MarkerOptions> getMarkers() {
        return markers;
    }

    public void addListener(LocationListener listener) {
        synchronized (listeners) {
            if (listeners.isEmpty()) {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                if (ActivityCompat.checkSelfPermission(RouteLobbyView.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                subscription = provider.getUpdatedLocation(mLocationRequest).subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                    }
                });
                listeners.add(listener);
            }else{
                listeners.add(listener);
            }
        }
    }

    public void unSuscribe(LocationListener client){
        synchronized (listeners){
            listeners.remove(client);
            if(listeners.isEmpty() && subscription != null){
                subscription.dispose();
            }
        }
    }

    private MarkerOptions observableMark(dbObservable source){
        return new MarkerOptions().position(new LatLng(source.getLatitude(), source.getLongitude())).title(source.getDisplayName());
    }

    private void updateListeners(){
        synchronized (listeners){
                for(LocationListener listener: listeners){
                    synchronized (ubication){
                        listener.updateLocation(observableMark(ubication), markers, route, globalMarkers);
                    }
                }
        }
    }

    public void setRoute(Route route){
        this.route = route;
        this.travel = new dbTravel(route.getDbRef(), System.currentTimeMillis(), "Lluvia");
        updateListeners();
    }

    public void endRoute(){
        long endTime = System.currentTimeMillis();
        this.travel.setTimeElapsed(endTime-this.travel.getDate());
        UserData.getInstance().addHistoric(travel);
        travel=null;
        markers.clear();
        route=null;
        updateListeners();
        Toast.makeText(RouteLobbyView.context, "Recorrido Terminado", Toast.LENGTH_SHORT).show();
    }
}
