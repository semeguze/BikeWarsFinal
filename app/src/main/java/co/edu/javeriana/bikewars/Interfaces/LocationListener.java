package co.edu.javeriana.bikewars.Interfaces;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import co.edu.javeriana.bikewars.Logic.Route;

/**
 * Created by jairo on 28/10/17.
 */

public interface LocationListener {
    void updateLocation(MarkerOptions location, List<MarkerOptions> markers, Route route, List<MarkerOptions> globals);
}
