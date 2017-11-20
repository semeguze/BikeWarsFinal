package co.edu.javeriana.bikewars;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import co.edu.javeriana.bikewars.Dialogs.CustomDatePickerDialog;

public class NewRaceView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_race_view);
    }

    public void selectDate(View view){
        DialogFragment picker = new CustomDatePickerDialog();
        picker.show(getFragmentManager(), "datePicker");
    }

    public void historic(View view){
        startActivity(new Intent(this, HistoricView.class));
    }

    public void newRoute(View view){
        startActivity(new Intent(this, NewRouteView.class));
    }

    public void done(View view){
        finish();
    }
}
