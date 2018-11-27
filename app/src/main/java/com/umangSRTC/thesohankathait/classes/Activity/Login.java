package com.umangSRTC.thesohankathait.classes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.classes.Utill.CheckNetwork;
import com.umangSRTC.thesohankathait.classes.model.User;

import java.util.Random;

public class Login extends AppCompatActivity {


    //facebook sign in
    private CallbackManager callbackManager;
    public static LoginButton facebookloginButton;


    //google sign in
    public GoogleSignInClient googleSignInClient;
    private Button googleSignInButton;
    private static final int SIGNUP_REQUESTCODE = 1;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//                //if network isn't available then retry
//                if(!CheckNetwork.isNetworkAvailable(this))
//                    warningAndExit();


//creating notification channel for higher version of andorid

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.notificationChannelId);//for creating notification channel
            String channelName = getString(R.string.channelName);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));



        }
        //*****************************all about google sign in**********************

        progressDialog = new ProgressDialog(this);
        googleSignInButton = findViewById(R.id.googlesigninbutton);


        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                signIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //*********************** all about facebook sign in***************************
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        facebookloginButton = findViewById(R.id.login_button);
        facebookloginButton.setReadPermissions("email", "public_profile");
        facebookloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("tag", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("tag2", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("tag3", "facebook:onError", error);
                // ...
            }
        });
// ...


//
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        if (isLoggedIn)
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile,email,name"));
//
//        FacebookSdk.isInitialized();
//        callbackManager = CallbackManager.Factory.create();
//
//
//        facebookloginButton = (LoginButton) findViewById(R.id.login_button);
//        facebookloginButton.setReadPermissions(Arrays.asList(EMAIL));
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        facebookloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                setFacebookData(loginResult);
//                // App code
////
////                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
////                        new GraphRequest.GraphJSONObjectCallback() {
////                            @Override
////                            public void onCompleted(JSONObject object, GraphResponse response) {
////                                Log.v("LoginActivity", response.toString());
////
////                                // Application code
////                                try {
////                                    String email = object.getString("email");
////                                    String name = object.getString("name");
////                                    String profileUrl=object.getString("public_profile");
////                                    Log.d("email,name,profile",email+" "+name+" "+profileUrl);
////
////                                } catch (JSONException e) {
////                                    e.printStackTrace();
////                                }
////                                }
////                        });
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//    }
//    private void setFacebookData(final LoginResult loginResult)
//    {
//        GraphRequest request = GraphRequest.newMeRequest(
//                loginResult.getAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        // Application code
//                        try {
//                            Log.i("Response",response.toString());
//
//                            String email = object.getString("email");
//                            String name = object.getString("first_name");
//                            name+=" ";
//                            name+=object.getString("last_name");
////                            String profileUrl=object.getString("public_profile");
//                            Log.d("email,name,profile",email+" "+name);
//                            editor.putString("NAME",name);
//                            editor.putString("EMAIL",email);
//                            editor.apply();
//
////
////
////                            Profile profile = Profile.getCurrentProfile();
////                            String id = profile.getId();
////                            String link = profile.getLinkUri().toString();
////                            Log.i("Link",link);
////                            if (Profile.getCurrentProfile()!=null)
////                            {
////                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
////                            }
////
////                            Log.i("Login" + "Email", email);
////                            Log.i("Login"+ "FirstName", firstName);
////                            Log.i("Login" + "LastName", lastName);
////                            Log.i("Login" + "Gender", gender);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        //this is mendatory inorder to call above callback
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,email,first_name,last_name,gender");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
//
////            private void warningAndExit() {
////                AlertDialog.Builder builder=new AlertDialog.Builder(this);
////                builder.setMessage("Please make sure you have a working internet connection")
////                        .setIcon(R.drawable.ic_warning_black_24dp)
////                        .setPositiveButton("try again", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                recreate();
////                            }
////                        })
////                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                finish();
////                            }
////                        })
////                        .show();
////
////            }
    }

    private void handleFacebookAccessToken(AccessToken token) {
       // Log.d("new2", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("oncomplete", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signfaild", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            editor.putString("NAME", mAuth.getCurrentUser().getDisplayName());
            editor.putString("EMAIL", mAuth.getCurrentUser().getEmail());
            editor.apply();
            User.setUser(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail());
            storeUserInFirebase();
            if (progressDialog != null)
                progressDialog.dismiss();
            moveToFunctionalityActivity();
            finish();
        }
    }


    public void signIn() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            User.setUser(sharedPreferences.getString("NAME", null), sharedPreferences.getString("EMAIL", null));
            Log.i("name", User.currentUser.getName());
            progressDialog.dismiss();
            moveToFunctionalityActivity();
        } else {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, SIGNUP_REQUESTCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.dismiss();
        if (requestCode == SIGNUP_REQUESTCODE && resultCode == RESULT_OK && data != null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "googel sign in failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                         //   Log.d("signInWithCredential", "signInWithCredential:success");
                            //saving user info in shared preferences
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                          //  Log.w("signInFailed", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void storeUserInFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.setValue(User.getCurrentUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "user Saved", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                User.setUser(sharedPreferences.getString("NAME", null), sharedPreferences.getString("EMAIL", null));
              //  Log.i("name", User.currentUser.getName());
                progressDialog.dismiss();
                // Toast.makeText(this, "move to next activity", Toast.LENGTH_SHORT).show();
                moveToFunctionalityActivity();
            }
            else if(!CheckNetwork.isNetworkAvailable(this)){
                warningAndExit();

            }

    }

    private void warningAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please make sure you have a working internet connection")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Network Check")
                .setPositiveButton("try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }


    private void moveToFunctionalityActivity() {


        Intent intent = new Intent(Login.this, Functionality.class);
        startActivity(intent);
        finish();
//        final ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Data");
//        progressDialog.setProgress(0);
//        progressDialog.setMax(100);
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.show();
//        new CountDownTimer(3000, 1) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
////                Toast.makeText(Login.this, ""+millisUntilFinished, Toast.LENGTH_SHORT).show();
//                progressDialog.setProgress((int) ((3000-millisUntilFinished)*100)/3000);
//            }
//
//            @Override
//            public void onFinish() {
//                progressDialog.dismiss();
//
//                finish();
//
//            }
//        }.start();
//
//    }

    }
}
