package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.RaceItemAdapter;
import co.edu.javeriana.bikewars.Items.RaceItem;

public class RaceView extends AppCompatActivity {

    List<RaceItem> carreras;
    RaceItemAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_view);
        carreras = new ArrayList<>();
        carreras.add(new RaceItem("Vuelta Colombia", new Date()));
        adapter = new RaceItemAdapter(this, carreras);
        list = findViewById(R.id.raceList);
        list.setAdapter(adapter);
    }

    public void newRace(View view){
        startActivity(new Intent(this, NewRaceView.class));
    }
}
