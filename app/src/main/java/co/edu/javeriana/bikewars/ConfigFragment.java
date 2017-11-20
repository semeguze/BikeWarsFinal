package co.edu.javeriana.bikewars;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Todesser on 27/08/2017.
 */

public class ConfigFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
