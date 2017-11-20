package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.edu.javeriana.bikewars.Logic.Entities.dbMail;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 30/10/2017.
 */

public class ChatAdapter extends ArrayAdapter<dbMail>{
    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<dbMail> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.mail_layout, parent, false);
        }
        LinearLayout linear = convertView.findViewById(R.id.mailLinear);
        TextView name = convertView.findViewById(R.id.mailMessage),
        date = convertView.findViewById(R.id.mailDate);
        dbMail model = getItem(position);
        name.setText(model.getMessage());
        date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date(model.getDate())));
        if(!model.getTo().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            linear.setGravity(Gravity.RIGHT);
            linear.setBackground(null);
        }
        return convertView;
    }
}
