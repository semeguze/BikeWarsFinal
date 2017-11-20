package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainView extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        mAuth = FirebaseAuth.getInstance();
    }

    public void friendsLaunch(View view){
        startActivity(new Intent(this, FriendsLobby.class));
    }

    public void routeLaunch(View view){
        startActivity(new Intent(this, RouteLobbyView.class));
    }

    public void racesLaunch(View view){
        startActivity(new Intent(this, RaceView.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuLogout:
                mAuth.signOut();
                startActivity(new Intent(getBaseContext(), LoginView.class));
                finish();
                return true;
            case R.id.mainMenuConfig:
                // TODO: 27/10/2017 Lanzamiento de configuracion. -- In progress nov 18th
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
