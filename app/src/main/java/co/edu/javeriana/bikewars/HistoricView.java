package co.edu.javeriana.bikewars;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.TravelAdapter;
import co.edu.javeriana.bikewars.Logic.Entities.dbTravel;
import co.edu.javeriana.bikewars.Logic.Route;
import co.edu.javeriana.bikewars.Logic.UserData;

public class HistoricView extends AppCompatActivity {

    private ListView list;
    private DatabaseReference ref;
    private View selecView;
    private dbTravel seleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_view);
        list = findViewById(R.id.historicList);
        List<dbTravel> listData = UserData.getInstance().getUser().getHistoric();
        if(listData==null){
            listData = new ArrayList<>();
        }
        Collections.reverse(listData);
        list.setAdapter(new TravelAdapter(getBaseContext(), R.layout.travel_layout, listData));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selecView==null){
                    selecView = list.getChildAt(position);
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionado = (dbTravel) list.getItemAtPosition(position);
                }else{
                    selecView.setBackgroundColor(Color.WHITE);
                    selecView = list.getChildAt(position);
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionado = (dbTravel) list.getItemAtPosition(position);
                }
            }
        });
    }

    public void mainLaunch(View view){
        if(seleccionado!=null){
            new Route(seleccionado.getRoute());
            finish();
        }else{
            Toast.makeText(this, "Seleccion invalida, intente de nuevo", Toast.LENGTH_SHORT).show();
        }
    }
}
