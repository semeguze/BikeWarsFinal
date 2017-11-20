package co.edu.javeriana.bikewars.Logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Interfaces.FriendListener;
import co.edu.javeriana.bikewars.Logic.Entities.dbGroup;
import co.edu.javeriana.bikewars.Logic.Entities.dbTravel;
import co.edu.javeriana.bikewars.Logic.Entities.dbUser;

/**
 * Created by Todesser on 28/10/2017.
 */

public class UserData {
    private static final UserData ourInstance = new UserData();
    public static final String observablesRoot = "db/observables/";
    public static final String usersRoot = "db/users/";
    public static final String racesRoot = "db/races/";
    public static final String routesRoot = "db/routes/";
    public static final String groupsRoot = "db/groupes/";
    public static final String markersRoot = "db/markers/";
    public static final String mailBoxRoot = "db/mailBox/";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference refUser;
    private dbUser user;
    private List<FriendListener> friendListeners;


    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        friendListeners = new ArrayList<>();
    }

    public void initialize(){
        mUser = mAuth.getCurrentUser();
        if(mUser!=null){
            refUser = db.getReference(usersRoot+mUser.getUid());
            refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(dbUser.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public DatabaseReference getRefUser() {
        return refUser;
    }

    public FirebaseUser getmUser(){
        return mUser;
    }

    public dbUser getUser(){
        return user;
    }

    public void setNewUser(dbUser newUser){
        refUser.setValue(newUser);
    }

    public void addHistoric(dbTravel travel){
        List<dbTravel> historic = user.getHistoric();
        if(historic!=null && historic.size()>5){
            historic.remove(0);
            historic.add(travel);
        }else{
            if(historic==null){
                historic = new ArrayList<>();
                user.setHistoric(historic);
            }
            historic.add(travel);
        }
        refUser.child("historic").setValue(historic);
    }

    public void addFriendListener(FriendListener listener){
        friendListeners.add(listener);
    }

    public void removeFriendListener(FriendListener listener){
        friendListeners.remove(listener);
    }

    public void addFriend(String friend){
        user.getFriends().add(friend);
        refUser.child("friends").setValue(user.getFriends());
        for(FriendListener listener: friendListeners){
            listener.UpdateFriends(user.getFriends());
        }
    }

    public void removeFriend(String userID){
        user.getFriends().remove(userID);
        refUser.child("friends").setValue(user.getFriends());
        for(FriendListener listener: friendListeners){
            listener.UpdateFriends(user.getFriends());
        }
    }

    public void addGroup(dbGroup grupo){
        DatabaseReference ref = db.getReference(groupsRoot).push();
        String groupID = ref.getKey();
        ref.setValue(grupo);
        user.getGroups().add(groupID);
        refUser.child("groups").setValue(user.getGroups());
    }
}
