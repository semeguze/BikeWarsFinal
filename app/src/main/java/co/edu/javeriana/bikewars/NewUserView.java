package co.edu.javeriana.bikewars;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import co.edu.javeriana.bikewars.Logic.Entities.dbUser;
import co.edu.javeriana.bikewars.Logic.UserData;

public class NewUserView extends AppCompatActivity {

    private Bitmap photoBitMap = null;
    private ImageView img;
    private EditText name, email, pass;
    private Pattern emailRegex;
    private FirebaseAuth mAuth;
    private FirebaseStorage bucket;
    private final int MAX_ATTEMPTS = 4;
    private int attempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_view);
        emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        mAuth = FirebaseAuth.getInstance();
        bucket = FirebaseStorage.getInstance();
        img = findViewById(R.id.newUserPhoto);
        name = findViewById(R.id.newUserName);
        email = findViewById(R.id.newUserEmail);
        pass = findViewById(R.id.newUserPass);
    }

    public void createUserBtn(View view){
        if(photoBitMap==null){
            Toast.makeText(this, "Debe seleccionar una foto de la galeria o tomar una nueva", Toast.LENGTH_SHORT).show();
            return;
        }
        String emailTxt = email.getText().toString().trim();
        String passTxt = pass.getText().toString().trim();
        Boolean validMail = emailRegex.matcher(emailTxt).matches();
        if(validMail){
            mAuth.createUserWithEmailAndPassword(emailTxt, passTxt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        updateInfo();
                    }else{
                        Toast.makeText(getBaseContext(), "Registro fallido por:" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Elementos invalidos", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateInfo(){
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            StorageReference photoRef = bucket.getReference("images/"+user.getUid()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask upload = photoRef.putBytes(data);
            upload.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Ocurrio un error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    UserProfileChangeRequest updater = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString())
                            .setPhotoUri(taskSnapshot.getDownloadUrl())
                            .build();
                    user.updateProfile(updater).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference(UserData.usersRoot+user.getUid()).setValue(new dbUser(user.getUid(), user.getDisplayName(), user.getPhotoUrl())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewUserView.this, "Ocurrio un error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                            if(attempts<MAX_ATTEMPTS){
                                attempts++;
                                updateInfo();
                            }else{
                                Toast.makeText(NewUserView.this, "Maximo numero de intentos para actualizar usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    public void cancel(View view){
        finish();
    }

    public void requireCamera(View v){
        TedPermission.with(getBaseContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 2);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage("Se necesita acceso a la camara")
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    public void requireGallery(View v){
        TedPermission.with(getBaseContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent pickImage = new Intent(Intent.ACTION_PICK);
                        pickImage.setType("image/*");
                        startActivityForResult(pickImage, 1);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage("Se necesita acceso a la camara")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:{
                if(resultCode==RESULT_OK){
                    try{
                        final InputStream imageStream = getContentResolver().openInputStream(data.getData());
                        photoBitMap = BitmapFactory.decodeStream(imageStream);
                        img.setImageBitmap(photoBitMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case 2:{
                if(resultCode==RESULT_OK){
                    Bundle extras = data.getExtras();
                    photoBitMap = (Bitmap) extras.get("data");
                    img.setImageBitmap(photoBitMap);
                }
                break;
            }
        }
    }
}
