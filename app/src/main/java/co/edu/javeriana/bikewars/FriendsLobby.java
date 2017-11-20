package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.FriendAdapter;
import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbGroup;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.Entities.dbUser;

public class FriendsLobby extends AppCompatActivity{

    private ListView listViewFriends, listViewGroups;
    private List<dbObservable> friendsList;
    private List<String> groupsList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_lobby);
        listViewFriends = findViewById(R.id.friendsViewList);
        listViewGroups = findViewById(R.id.friendsViewGroups);
    }

    private void populateList(dbUser user){
        final List<String> friends = user.getFriends();
        final List<String> groups = user.getGroups();
        friendsList = new ArrayList<>();
        groupsList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Constants.observablesRoot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbObservable friend = snapshot.getValue(dbObservable.class);
                    if(friends.contains(friend.getUserID())){
                        friendsList.add(friend);
                    }
                }
                listViewFriends.setAdapter(new FriendAdapter(FriendsLobby.this, R.layout.friend_layout, friendsList));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        FirebaseDatabase.getInstance().getReference(Constants.groupsRoot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbGroup group = snapshot.getValue(dbGroup.class);
                    if(groups.contains(group.getGroupID())){
                        groupsList.add(group.getName());
                    }
                }
                listViewGroups.setAdapter(new ArrayAdapter<String>(FriendsLobby.this, android.R.layout.simple_list_item_1, groupsList));
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

    public void newGroup(View view){
        startActivity(new Intent(this, NewGroupView.class));
    }

    public void searchFriends(View view){
        startActivity(new Intent(this, SearchFriendsView.class));
    }
}
