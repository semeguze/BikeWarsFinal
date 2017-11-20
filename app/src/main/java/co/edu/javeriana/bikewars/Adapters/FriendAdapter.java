package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.ChatView;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 30/10/2017.
 */

public class FriendAdapter extends ArrayAdapter<dbObservable>{

    private Context cache;

    public FriendAdapter(@NonNull Context context, int resource, @NonNull List<dbObservable> objects) {
        super(context, resource, objects);
        cache = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.friend_layout, parent, false);
        }
        final ImageView photo = convertView.findViewById(R.id.friendPhoto);
        TextView name = convertView.findViewById(R.id.friendName);
        ImageButton sendMessage = convertView.findViewById(R.id.friendSendMessage);
        ImageButton removeFriend = convertView.findViewById(R.id.friendRemove);
        final dbObservable model = getItem(position);
        photo.setImageResource(R.drawable.ic_account);
        model.setContainer(photo);
        name.setText(model.getDisplayName());
        sendMessage.setImageResource(R.drawable.ic_envelope);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(cache, ChatView.class);
                chatIntent.putExtra("photo", model.getPhoto());
                chatIntent.putExtra("name", model.getDisplayName());
                chatIntent.putExtra("userID", model.getUserID());
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cache.startActivity(chatIntent);
            }
        });
        removeFriend.setImageResource(R.drawable.cancel);
        removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.usersRoot + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/friends/");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        List<String> friends = dataSnapshot.getValue(t);
                        friends.remove(model.getUserID());
                        ref.setValue(friends);
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
