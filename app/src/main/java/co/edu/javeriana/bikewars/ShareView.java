package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShareView extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_view);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.shareMap);
        mapFragment.getMapAsync(this);
    }

    public void mainLaunch(View view){
        startActivity(new Intent(this, MainView.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void newGroupLaunch(View view){
        startActivity(new Intent(this, NewGroupView.class));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng ubicacion = new LatLng(4.6275604, -74.0640883);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        map.addMarker(new MarkerOptions()
                .position(ubicacion)
                .title("Marker"));
        map.setIndoorEnabled(false);
    }

}
