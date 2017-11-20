package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import co.edu.javeriana.bikewars.ExpectView;
import co.edu.javeriana.bikewars.Items.RaceItem;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 04/09/2017.
 */

public class RaceItemAdapter extends ArrayAdapter<RaceItem>{


    public RaceItemAdapter(Context context, List<RaceItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.race_item_layout, parent, false);
        }
        final TextView name = convertView.findViewById(R.id.race_item_name);
        TextView date = convertView.findViewById(R.id.race_item_date);
        ImageButton expect = convertView.findViewById(R.id.race_item_expect);
        ImageButton join = convertView.findViewById(R.id.race_item_join);
        RaceItem race = getItem(position);
        name.setText(race.getName());
        date.setText(new SimpleDateFormat("dd-MM-yyyy").format(race.getDate()));
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join(v);
            }
        });
        expect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expect(v, name.getText().toString());
            }
        });
        return convertView;
    }

    public void join(View view){

    }

    public void expect(View view, String name){
        Intent intent = new Intent(getContext(), ExpectView.class);
        intent.putExtra("name", name);
        getContext().startActivity(intent);
    }
}
