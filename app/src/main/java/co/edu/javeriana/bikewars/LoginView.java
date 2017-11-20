package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.util.Arrays;
import java.util.regex.Pattern;

public class LoginView extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    //GUI
    private EditText user, pass;
    //Regex
    private Pattern emailRegex;
    //RedesSociales
    private TwitterAuthClient client;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializar SDK's redes sociales__
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginView.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginView.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        //__Inicializar SDK's

        setContentView(R.layout.activity_login_view);

        //CE: CAMBIOS ESTÉTICOS__
        //CE. Barra de estado translucida
        setStatusBarTranslucent(true);
        //CE. Esconder Action bar
        try {
            getActionBar().hide();
        }catch (Exception e){
            getSupportActionBar().hide();
        }
        //CE. Esconder teclado al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //__CAMBIOS ESTÉTICOS

        user = findViewById(R.id.loginUserTxt);
        pass = findViewById(R.id.loginPassTxt);
        emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getBaseContext(), RouteLobbyView.class));
                    finish();
                }
            }
        });

        //TWITTER__
        ImageButton customLoginButton = (ImageButton) findViewById(R.id.loginBtnTwitter);
        customLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new TwitterAuthClient();
                client.authorize(LoginView.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Do something with result, which provides a TwitterSession for making API calls
                        handleTwitterSession(twitterSessionResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(LoginView.this,"Autenticación con Twitter fallida", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //__TWITTER

        //FACEBOOK__
        ImageButton btn_fb_login = (ImageButton) findViewById(R.id.loginBtnFb);

        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginView.this, Arrays.asList("public_profile", "user_friends"));
            }
        });
        //__FACEBOOK

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            client.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e){
            Log.i("ErrorAR", "No se pudo con el cliente de Twitter");
        }
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e){
            Log.i("ErrorAR", "No se pudo con el cliente de Facebook");
        }

    }

    public void login(View context){
        String userTxt = user.getText().toString().trim(),
        passTxt = pass.getText().toString().trim();
        if(!userTxt.isEmpty() && !passTxt.isEmpty()){
            Boolean validMail = emailRegex.matcher(userTxt).matches();
            if(validMail){
                mAuth.signInWithEmailAndPassword(userTxt, passTxt).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pass.setText("");
                        Toast.makeText(LoginView.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            Toast.makeText(this, "Ingrese los datos completos", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleTwitterSession(TwitterSession session) {
        Log.d("TAG", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginView.this, "Autenticación correcta",Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginView.this, "Autenticación fallida",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginView.this, "Autenticación correcta",Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginView.this, "Autenticación fallida",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


    public void newUserLaunch(View view){
        startActivity(new Intent(getBaseContext(), NewUserView.class));
    }

    //Método para hacer barra de estado translucida
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
