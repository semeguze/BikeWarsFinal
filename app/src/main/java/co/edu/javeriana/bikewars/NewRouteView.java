package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import co.edu.javeriana.bikewars.Interfaces.RouteListener;
import co.edu.javeriana.bikewars.Logic.MapData;
import co.edu.javeriana.bikewars.Logic.Route;

public class NewRouteView extends AppCompatActivity implements OnMapReadyCallback, RouteListener {

    private MapFragment mapFragment;
    private GoogleMap map;
    private Address salida, llegada;
    private MarkerOptions salidaMarker, llegadaMarker;
    private Marker salidaMark, llegadaMark;
    private PolylineOptions poli;
    private Polyline poliRuta;
    private Button salidaBtn, llegadaBtn;
    private EditText routeName;
    private Route ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route_view);
        salidaBtn = findViewById(R.id.newRouteStartBtn);
        llegadaBtn = findViewById(R.id.newRouteEndBtn);
        routeName = findViewById(R.id.newRouteName);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.newRouteMap);
        mapFragment.getMapAsync(this);
    }

    public void selectionSalidaLaunch(View view){
        startActivityForResult(new Intent(getBaseContext(), SelectionView.class), 1);
    }
    public void selectionLlegadaLaunch(View view){
        startActivityForResult(new Intent(getBaseContext(), SelectionView.class), 2);
    }

    public void mainLaunch(View view){
        ruta.register(routeName.getText().toString());
        MapData.getInstance().setRoute(ruta);
        finish();
    }

    public void shareLaunch(View view){
        startActivity(new Intent(this, ShareView.class));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng ubicacion = new LatLng(4.6275604, -74.0640883);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 11));
        map.setIndoorEnabled(false);
        salidaBtn.setEnabled(true);
        llegadaBtn.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == 1){
                    if(salidaMark!=null){
                        salidaMark.remove();
                    }
                    salida = (Address)data.getExtras().get("endPoint");
                    salidaBtn.setText(salida.getFeatureName());
                    salidaMarker = new MarkerOptions().position(new LatLng(salida.getLatitude(), salida.getLongitude())).title(salida.getFeatureName());
                    salidaMark = map.addMarker(salidaMarker);
                    if(llegada!=null){
                        trazarRuta();
                    }
                }else{
                    Toast.makeText(this, "Debe seleccionar una opcion valida", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(resultCode == 1){
                    if(llegadaMark!=null){
                        llegadaMark.remove();
                    }
                    llegada = (Address)data.getExtras().get("endPoint");
                    llegadaBtn.setText(llegada.getFeatureName());
                    llegadaMarker = new MarkerOptions().position(new LatLng(llegada.getLatitude(), llegada.getLongitude())).title(llegada.getFeatureName());
                    llegadaMark = map.addMarker(llegadaMarker);
                    if(salida!=null){
                        trazarRuta();
                    }
                }
                else{
                    Toast.makeText(this, "Debe seleccionar una opcion valida", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void trazarRuta(){
        ruta = new Route(salidaMarker, llegadaMarker);
        ruta.calcularRuta(this);
    }

    @Override
    public void routeFinished(PolylineOptions polyOpt, String distance) {
        if(poliRuta!=null){
            poliRuta.remove();
        }
        poliRuta = map.addPolyline(polyOpt);
        Toast.makeText(getBaseContext(), "La distancia es de: " + distance, Toast.LENGTH_SHORT).show();
    }
}
