package co.edu.javeriana.bikewars;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.RouteAdapter;
import co.edu.javeriana.bikewars.Logic.Entities.dbRoute;
import co.edu.javeriana.bikewars.Logic.Route;
import co.edu.javeriana.bikewars.Logic.UserData;

public class SearchView extends AppCompatActivity {

    private ListView resultados;
    private EditText searchTxt;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private dbRoute seleccionado;
    private View selecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        resultados = findViewById(R.id.searchResultList);
        searchTxt = findViewById(R.id.searchSearchTxt);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(UserData.routesRoot);
        FirebaseListAdapter<dbRoute> adapter = new FirebaseListAdapter<dbRoute>(this, dbRoute.class, R.layout.route_layout,ref.orderByKey()) {

            @Override
            protected void populateView(View v, dbRoute model, int position) {
                TextView name, from, to, author;
                name = v.findViewById(R.id.routeName);
                from = v.findViewById(R.id.travelFrom);
                to = v.findViewById(R.id.routeTo);
                author = v.findViewById(R.id.travelAuthor);
                name.setText(model.getDisplayName());
                from.setText(model.getStart().getTitle());
                to.setText(model.getEnd().getTitle());
                author.setText(model.getOwnerName());
            }
        };
        resultados.setAdapter(adapter);
        resultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selecView==null){
                    selecView = resultados.getChildAt(position);
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionado = (dbRoute) resultados.getItemAtPosition(position);
                }else{
                    selecView.setBackgroundColor(Color.WHITE);
                    selecView = resultados.getChildAt(position);
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionado = (dbRoute) resultados.getItemAtPosition(position);
                }
            }
        });
    }

    public void search(View view){
        final String busqueda = searchTxt.getText().toString();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<dbRoute> rutas = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbRoute ruta = snapshot.getValue(dbRoute.class);
                    if(ruta.getDisplayName().contains(busqueda)){
                        rutas.add(ruta);
                    }
                }
                resultados.setAdapter(new RouteAdapter(getBaseContext(), R.layout.route_layout, rutas));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void select(View view){
        if(seleccionado!=null){
            new Route(seleccionado);
            finish();
        }else{
            Toast.makeText(this, "Seleccion invalida, intente de nuevo", Toast.LENGTH_SHORT).show();
        }
    }
}
