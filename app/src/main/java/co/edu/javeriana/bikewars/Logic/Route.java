package co.edu.javeriana.bikewars.Logic;

import android.graphics.Color;
import android.util.Log;

import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Interfaces.RouteListener;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.Entities.dbRoute;
import co.edu.javeriana.bikewars.Logic.Entities.dbRouteMarker;

/**
 * Created by Todesser on 28/10/2017.
 */

public class Route {
    private dbRoute dbRef;
    private String title;
    private MarkerOptions start;
    private MarkerOptions end;
    private PolylineOptions route;
    private List<dbObservable> members;

    public Route(){}

    public dbRoute getDbRef() {
        return dbRef;
    }

    public void setDbRef(dbRoute dbRef) {
        this.dbRef = dbRef;
    }

    public List<dbObservable> getMembers() {
        return members;
    }

    public void setMembers(List<dbObservable> members) {
        this.members = members;
    }

    public Route(dbRoute route){
        dbRef = route;
        this.start = new MarkerOptions().position(new LatLng(route.getStart().getLatitude(), route.getStart().getLongirude())).title(route.getStart().getTitle());
        this.end = new MarkerOptions().position(new LatLng(route.getEnd().getLatitude(), route.getEnd().getLongirude())).title(route.getEnd().getTitle());
        this.title = route.getDisplayName();
        calcularRuta(this);
    }

    public Route(MarkerOptions start, MarkerOptions llegada) {
        this.start = start;
        this.end = llegada;
        members = new ArrayList<>();
        members.add(MapData.getInstance().getUbication());
    }

    public void calcularRuta(final RouteListener caller){
        final Routing calculatedRoute = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(new RoutingListener() {
                    @Override
                    public void onRoutingFailure(RouteException e) {
                        Log.i("Route", "Fallo");
                    }

                    @Override
                    public void onRoutingStart() {
                        Log.i("Route", "Inicio");
                    }

                    @Override
                    public void onRoutingSuccess(ArrayList<com.directions.route.Route> arrayList, int i) {
                        com.directions.route.Route resultRoute = arrayList.get(i);
                        PolylineOptions poly = new PolylineOptions();
                        poly.color(Color.argb(255,171, 85, 251));
                        poly.width(10);
                        poly.addAll(resultRoute.getPoints());
                        route = poly;
                        caller.routeFinished(poly, resultRoute.getDistanceText());
                    }

                    @Override
                    public void onRoutingCancelled() {
                        Log.i("Route", "Cancelo");
                    }
                })
                .waypoints(start.getPosition(), end.getPosition())
                .build();
        calculatedRoute.execute();
    }

    public void calcularRuta(final Route ruta){
        final Routing calculatedRoute = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(new RoutingListener() {
                    @Override
                    public void onRoutingFailure(RouteException e) {
                        Log.i("Route", "Fallo");
                    }

                    @Override
                    public void onRoutingStart() {
                        Log.i("Route", "Inicio");
                    }

                    @Override
                    public void onRoutingSuccess(ArrayList<com.directions.route.Route> arrayList, int i) {
                        com.directions.route.Route resultRoute = arrayList.get(i);
                        PolylineOptions poly = new PolylineOptions();
                        poly.color(Color.argb(255,171, 85, 251));
                        poly.width(10);
                        poly.addAll(resultRoute.getPoints());
                        route = poly;
                        MapData.getInstance().setRoute(ruta);
                    }

                    @Override
                    public void onRoutingCancelled() {
                        Log.i("Route", "Cancelo");
                    }
                })
                .waypoints(start.getPosition(), end.getPosition())
                .build();
        calculatedRoute.execute();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MarkerOptions getStart() {
        return start;
    }

    public void setStart(MarkerOptions start) {
        this.start = start;
    }

    public MarkerOptions getEnd() {
        return end;
    }

    public void setEnd(MarkerOptions end) {
        this.end = end;
    }

    public PolylineOptions getRoute() {
        return route;
    }

    public void setRoute(PolylineOptions route) {
        this.route = route;
    }

    public dbRoute register(String title){
        this.title = title;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(UserData.routesRoot).push();
        List<String> membersCodes = new ArrayList<>();
        for(dbObservable member: members){
            membersCodes.add(member.getUserID());
        }
        FirebaseUser user = UserData.getInstance().getmUser();
        dbRef = new dbRoute(ref.getKey(), title, user.getUid(), user.getDisplayName(),new dbRouteMarker(start), new dbRouteMarker(end), membersCodes);
        ref.setValue(dbRef);
        return dbRef;
    }

}
