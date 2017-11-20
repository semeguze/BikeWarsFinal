package co.edu.javeriana.bikewars;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.bikewars.Auxiliar.Constants;

public class SelectionView extends AppCompatActivity {

    private PermissionListener permisos;
    private EditText busqueda;
    private ListView resultados;
    private Geocoder mGeocoder;
    private Map<String, Address> resultadosLatLon;
    private Address seleccionada;
    private View selecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_view);
        busqueda = findViewById(R.id.selectionSearchTxt);
        resultados = findViewById(R.id.selectionResultList);
        permisos = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                mGeocoder = new Geocoder(getBaseContext());
                String addressString = busqueda.getText().toString();
                if (!addressString.isEmpty()) {
                    try {
                        List<Address> addresses = mGeocoder.getFromLocationName(
                                addressString, 5,
                                Constants.lowerLeftLatitude,
                                Constants.lowerLeftLongitude,
                                Constants.upperRightLatitude,
                                Constants.upperRigthLongitude);
                        if (addresses != null && !addresses.isEmpty()) {
                            String[] resultadosTxt = new String[addresses.size()];
                            resultadosLatLon = new HashMap<>();
                            int i=0;
                            for(Address address: addresses){
                                resultadosTxt[i] = address.getAddressLine(0);
                                resultadosLatLon.put(resultadosTxt[i], address);
                                i++;
                            }
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, resultadosTxt);
                            resultados.setAdapter(adaptador);
                        } else {
                            Toast.makeText(getBaseContext(), "Dirección no encontrada", Toast.LENGTH_SHORT).show();}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {Toast.makeText(getBaseContext(), "La dirección esta vacía", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        resultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selecView==null){
                    selecView = view;
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionada = resultadosLatLon.get(resultados.getItemAtPosition(position));
                }else{
                    selecView.setBackgroundColor(Color.WHITE);
                    selecView = view;
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionada = resultadosLatLon.get(resultados.getItemAtPosition(position));
                }
            }
        });
    }

    private void checkPermissions(){
        TedPermission.with(getBaseContext())
                .setPermissionListener(permisos)
                .setDeniedMessage("Se necesita acceso a la ubicacion")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public void search(View v){
        checkPermissions();
    }

    public void select(View v){
        if(seleccionada != null){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("endPoint", seleccionada);
            Log.i("Retorno", seleccionada.getFeatureName());
            setResult(1, returnIntent);
            finish();
        }else{
            Toast.makeText(getBaseContext(), "No ha seleccionado una opcion valida", Toast.LENGTH_SHORT).show();
        }
    }
}
