package co.edu.javeriana.bikewars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.NewGroupFriendAdapter;
import co.edu.javeriana.bikewars.Adapters.NewGroupSelectedAdapter;
import co.edu.javeriana.bikewars.Interfaces.NewGroupListener;
import co.edu.javeriana.bikewars.Logic.Entities.dbGroup;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.UserData;

public class NewGroupView extends AppCompatActivity implements NewGroupListener{

    ListView friends, selected;
    EditText name;
    List<dbObservable> friendsList, selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_view);
        final NewGroupListener listener = this;
        friends = findViewById(R.id.newGroupFriends);
        selected = findViewById(R.id.newGroupSelected);
        name = findViewById(R.id.newGroupName);
        friendsList = new ArrayList<>();
        selectedList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(UserData.observablesRoot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> userFriends = UserData.getInstance().getUser().getFriends();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbObservable friend = snapshot.getValue(dbObservable.class);
                    if(userFriends.contains(friend.getUserID())){
                        friendsList.add(friend);
                    }
                }
                friends.setAdapter(new NewGroupFriendAdapter(getBaseContext(), R.layout.new_group_friend, friendsList, listener));
                selected.setAdapter(new NewGroupSelectedAdapter(getBaseContext(), R.layout.new_group_selected, selectedList, listener));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void friendAdded(dbObservable added) {
        selectedList.add(added);
        friendsList.remove(added);
        updateUI();
    }

    @Override
    public void friendRemoved(dbObservable removed) {
        friendsList.add(removed);
        selectedList.remove(removed);
        updateUI();
    }

    public void updateUI(){
        friends.setAdapter(new NewGroupFriendAdapter(getBaseContext(), R.layout.new_group_friend, friendsList, this));
        selected.setAdapter(new NewGroupSelectedAdapter(getBaseContext(), R.layout.new_group_selected, selectedList, this));
    }

    public void save(View v){
        dbGroup newGroupe = new dbGroup();
        newGroupe.setGroupID(name.getText().toString());
        List<String> ids = new ArrayList<>();
        for(dbObservable selectedID: selectedList){
            ids.add(selectedID.getUserID());
        }
        ids.add(UserData.getInstance().getUser().getUserID());
        newGroupe.setMembers(ids);
        UserData.getInstance().addGroup(newGroupe);
        finish();
    }
}
