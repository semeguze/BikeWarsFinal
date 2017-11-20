package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 30/10/2017.
 */

public class AddFriendAdapter extends ArrayAdapter<dbObservable>{
    public AddFriendAdapter(@NonNull Context context, int resource, @NonNull List<dbObservable> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.add_friend_layout, parent, false);
        }
        final ImageView photo = convertView.findViewById(R.id.newGroupFriendPhoto);
        final Bitmap[] photoBit = new Bitmap[1];
        TextView name = convertView.findViewById(R.id.addFriendName);
        ImageButton addFriend = convertView.findViewById(R.id.addFriendAdd);
        final dbObservable model = getItem(position);
        photo.setImageResource(R.drawable.ic_account);
        model.setContainer(photo);
        name.setText(model.getDisplayName());
        addFriend.setImageResource(R.drawable.add);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.usersRoot + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/friends/");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        List<String> friends = dataSnapshot.getValue(t);
                        if(friends==null){
                            friends = new ArrayList<>();
                        }
                        friends.add(model.getUserID());
                        ref.setValue(friends);
                        Toast.makeText(getContext(), model.getDisplayName() + " ha sido añadido a tus amigos", Toast.LENGTH_SHORT).show();
                        remove(model);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return convertView;
    }
}
