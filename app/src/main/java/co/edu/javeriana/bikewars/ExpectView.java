package co.edu.javeriana.bikewars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ExpectView extends AppCompatActivity implements OnMapReadyCallback {

    List<String> top;
    ListView list;
    TextView banner;
    MapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expect_view);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.expectMap);
        mapFragment.getMapAsync(this);
        banner = findViewById(R.id.expectBanner);
        banner.setText(getIntent().getStringExtra("name"));
        top = new ArrayList<>();
        top.add("Daniela");
        top.add("Sebastian");
        top.add("David");
        list = findViewById(R.id.expectTopList);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, top));
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
