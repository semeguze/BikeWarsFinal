package co.edu.javeriana.bikewars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.AddFriendAdapter;
import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.Entities.dbUser;

public class SearchFriendsView extends AppCompatActivity{

    private EditText serachTxt;
    private ListView list;
    private List<dbObservable> listData;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends_view);
        list = findViewById(R.id.searchFriendsList);

    }

    private void populateList(final dbUser user){
        listData = new ArrayList<>();
        final List<String> friends = user.getFriends();
        FirebaseDatabase.getInstance().getReference(Constants.observablesRoot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbObservable friend = snapshot.getValue(dbObservable.class);
                    if(!friends.contains(friend.getUserID()) && !friend.getUserID().equals(user.getUserID())){
                        listData.add(friend);
                    }
                }
                list.setAdapter(new AddFriendAdapter(getBaseContext(), R.layout.add_friend_layout, listData));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference(Constants.usersRoot + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateList(dataSnapshot.getValue(dbUser.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        super.onResume();
    }
}
