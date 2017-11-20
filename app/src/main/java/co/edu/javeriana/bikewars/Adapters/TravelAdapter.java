package co.edu.javeriana.bikewars.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.edu.javeriana.bikewars.Logic.Entities.dbTravel;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 29/10/2017.
 */

public class TravelAdapter extends ArrayAdapter<dbTravel>{
    public TravelAdapter(@NonNull Context context, int resource, @NonNull List<dbTravel> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.travel_layout, parent, false);
        }
        TextView name, from, to, author, date, time, kilometers, weather;
        ImageView arrow = convertView.findViewById(R.id.travelFromTo);
        name = convertView.findViewById(R.id.travelName);
        from = convertView.findViewById(R.id.travelFrom);
        to = convertView.findViewById(R.id.travelTo);
        author = convertView.findViewById(R.id.travelAuthor);
        date = convertView.findViewById(R.id.travelDate);
        time = convertView.findViewById(R.id.travelTime);
        kilometers = convertView.findViewById(R.id.travelKilometers);
        weather = convertView.findViewById(R.id.travelWeather);
        dbTravel model = getItem(position);
        name.setText(model.getRoute().getDisplayName());
        from.setText(model.getRoute().getStart().getTitle());
        to.setText(model.getRoute().getEnd().getTitle());
        author.setText(model.getRoute().getOwnerName());
        arrow.setImageResource(R.drawable.ic_arrow);
        date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date(model.getDate())));
        time.setText(String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(model.getTimeElapsed()),
                TimeUnit.MILLISECONDS.toMinutes(model.getTimeElapsed()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(model.getTimeElapsed())),
                TimeUnit.MILLISECONDS.toSeconds(model.getTimeElapsed()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(model.getTimeElapsed()))));
        kilometers.setText(String.format("%s km",model.getKilometersCovered()/1000.0));
        weather.setText("Lluvia");
        return convertView;
    }
}
