package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.ChatAdapter;
import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbMail;

public class ChatView extends AppCompatActivity {

    private ListView chatList;
    private TextView name;
    private EditText message;
    private ImageView photo;
    private List<dbMail> mailList;
    private String friendID, userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        mailList = new ArrayList<>();
        chatList = findViewById(R.id.chatList);
        name = findViewById(R.id.chatName);
        message = findViewById(R.id.chatMessage);
        photo = findViewById(R.id.chatPhoto);
        Intent intent = getIntent();
        name.setText((String)intent.getExtras().get("name"));
        String photoTxt = (String)intent.getExtras().get("photo");
        FirebaseStorage.getInstance().getReferenceFromUrl(photoTxt).getBytes(Constants.MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photoBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                photo.setImageBitmap(photoBmp);
            }
        });
        friendID = (String)intent.getExtras().get("userID");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.mailBoxRoot);
        final ChatAdapter adapter = new ChatAdapter(this, R.layout.mail_layout, mailList);
        chatList.setAdapter(adapter);
        ref.child(userID + "/" + friendID + "/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dbMail mail = dataSnapshot.getValue(dbMail.class);
                if(mail.getFrom().equals(userID) && mail.getTo().equals(friendID) || mail.getFrom().equals(friendID) && mail.getTo().equals(userID)){
                    mailList.add(mail);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //No se deberian poder modificar los mensajes
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mailList.remove(dataSnapshot.getValue(dbMail.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //No importa para la app
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Manejar el error
            }
        });
    }

    public void sendMessage(View v){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.mailBoxRoot);
        DatabaseReference sender = ref.child("/"+userID+"/"+friendID+"/");
        DatabaseReference receiver = ref.child("/"+friendID+"/"+userID+"/");
        dbMail mail = new dbMail(userID, friendID, message.getText().toString(), System.currentTimeMillis());
        sender.push().setValue(mail);
        receiver.push().setValue(mail);
        message.setText("");
    }
}
