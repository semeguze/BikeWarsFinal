package co.edu.javeriana.bikewars.Logic.Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import co.edu.javeriana.bikewars.Auxiliar.Constants;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbObservable {
    //Firebase members
    private String userID;
    private String displayName;
    private String photo;
    private double latitude;
    private double longitude;
    //Logic members
    private DatabaseReference ref;
    private Bitmap photoBmp;
    private Marker marker;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public Bitmap getPhotoBpm() {
        return photoBmp;
    }

    public dbObservable(String displayName, double latitude, double longitude, String photo) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public dbObservable() {
    }

    public dbObservable(String userID, Marker marker){
        this.marker = marker;
        ref = FirebaseDatabase.getInstance().getReference(Constants.observablesRoot + userID);
        setListener();
    }

    public dbObservable(FirebaseUser user, Location location, Marker marker){
        this.marker = marker;
        userID = user.getUid();
        displayName = user.getDisplayName();
        marker.setTitle(displayName);
        photo = user.getPhotoUrl().toString();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        ref = FirebaseDatabase.getInstance().getReference(Constants.observablesRoot + user.getUid());
        ref.setValue(this);
        downloadPhoto();
    }

    public void setMarker(Marker marker){
        this.marker = marker;
    }

    private Bitmap resizeImageForImageView(Bitmap bitmap, int scaleSize) {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if(originalHeight > originalWidth) {
            newHeight = scaleSize ;
            multFactor = (float) originalWidth/(float) originalHeight;
            newWidth = (int) (newHeight*multFactor);
        } else if(originalWidth > originalHeight) {
            newWidth = scaleSize ;
            multFactor = (float) originalHeight/ (float)originalWidth;
            newHeight = (int) (newWidth*multFactor);
        } else if(originalHeight == originalWidth) {
            newHeight = scaleSize ;
            newWidth = scaleSize ;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private void downloadPhoto(){
        FirebaseStorage.getInstance().getReferenceFromUrl(photo).getBytes(Constants.MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                photoBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap resized = resizeImageForImageView(photoBmp, 150);
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(getRoundedCornerBitmap(resized, 20)));
                    marker.setVisible(true);
            }
        });
    }

    public void setContainer(ImageView container){
        if(photoBmp==null){
            downloadPhoto(container);
        }else{
            container.setImageBitmap(photoBmp);
        }
    }

    private void downloadPhoto(final ImageView container){
        FirebaseStorage.getInstance().getReferenceFromUrl(photo).getBytes(Constants.MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                photoBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                container.setImageBitmap(photoBmp);
            }
        });
    }

    private void setListener(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbObservable returned = dataSnapshot.getValue(dbObservable.class);
                if(userID != returned.getUserID()){
                    userID = returned.getUserID();
                }
                if(displayName!=returned.getDisplayName()) {
                    displayName = returned.getDisplayName();
                    if(marker!=null){
                        marker.setTitle(displayName);
                    }
                }
                latitude = returned.getLatitude();
                longitude = returned.getLongitude();
                    marker.setPosition(new LatLng(latitude, longitude));
                if(photo!=returned.getPhoto()) {
                    photo = returned.getPhoto();
                    downloadPhoto();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateLocation(LatLng location){
        latitude = location.latitude;
        longitude = location.longitude;
        marker.setPosition(location);
        ref.setValue(this);
    }
}
