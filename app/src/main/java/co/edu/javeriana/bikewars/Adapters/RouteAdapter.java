package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.javeriana.bikewars.Logic.Entities.dbRoute;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 29/10/2017.
 */

public class RouteAdapter extends ArrayAdapter<dbRoute>{
    public RouteAdapter(@NonNull Context context, int resource, @NonNull List<dbRoute> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.route_layout, parent, false);
        }
        TextView name, from, to, author;
        ImageView arrow = convertView.findViewById(R.id.travelFromTo);
        name = convertView.findViewById(R.id.routeName);
        from = convertView.findViewById(R.id.travelFrom);
        to = convertView.findViewById(R.id.routeTo);
        author = convertView.findViewById(R.id.travelAuthor);
        dbRoute model = getItem(position);
        name.setText(model.getDisplayName());
        from.setText(model.getStart().getTitle());
        to.setText(model.getEnd().getTitle());
        author.setText(model.getOwnerName());
        arrow.setImageResource(R.drawable.ic_arrow);
        return convertView;
    }
}
