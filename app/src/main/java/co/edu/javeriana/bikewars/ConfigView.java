package co.edu.javeriana.bikewars;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Todesser on 27/08/2017.
 */

public class ConfigView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Logger", "Aqui llega");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ConfigFragment())
                .commit();
    }

}
