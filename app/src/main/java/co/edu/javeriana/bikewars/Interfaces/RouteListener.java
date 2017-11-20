package co.edu.javeriana.bikewars.Interfaces;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Todesser on 29/10/2017.
 */

public interface RouteListener {
    void routeFinished(PolylineOptions polyOpt, String distance);
}
