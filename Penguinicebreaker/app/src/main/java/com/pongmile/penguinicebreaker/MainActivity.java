package com.pongmile.penguinicebreaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 0;

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public static final String TAG = "MainActivity";
    public CallbackManager mCallbackManager;
    public AccessToken fbAccessToken;
    public LoginButton loginButton;
    public ImageView mProfilePictureView;
    public Uri userPic;
    public String userName;
    public TextView textView;
    public Profile profile;
    public String userEmail;
    public String uid;
    public EditText etInviteEMail;
    public String MySample;
    public TextView textcolor;
    public String PlayerSession = "";
    public String PlayerGameID;
    public int ActivePlayer = 1;


    public ArrayList<Integer> Player1= new ArrayList<Integer>();// hold player 1 data
    public ArrayList<Integer> Player2= new ArrayList<Integer>();// hold player 2 data

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        InitializeFacebook();
        currentUser = mAuth.getCurrentUser();
        myRef.setValue("Hello, World!");

        textcolor = findViewById(R.id.textColor);
        textcolor.setText("Wait");

        etInviteEMail = (EditText)findViewById(R.id.etInviteEmal);

    }


    public void getInfo(){
        userName = currentUser.getDisplayName();
        textView = findViewById(R.id.nameUser);
        textView.setText(userName);

        uid = currentUser.getUid();
        userEmail = currentUser.getEmail();
        myRef.child("Users").child(BeforeAt(userEmail)).child("Req").setValue(uid);


        userPic = currentUser.getPhotoUrl();
        mProfilePictureView = findViewById(R.id.imageFacebook);
        Picasso.with(this).load(userPic).resize(150, 150)
                .centerCrop().into(mProfilePictureView);



    }

    String BeforeAt(String Email){
        String[] split= Email.split("@");
        return split[0];
    }


    @Override
    protected void onStart() {
        currentUser = mAuth.getCurrentUser();
        super.onStart();
        profile = Profile.getCurrentProfile();
        if (profile != null && currentUser != null) {

            userEmail = currentUser.getEmail();
            IncommingRequest();
            getInfo();

        } else {
            // user has not logged in
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);

            textView = findViewById(R.id.nameUser);
            textView.setText("Please Login");

            mProfilePictureView = findViewById(R.id.imageFacebook);
            Picasso.with(this).load("https://i.imgur.com/FdrUogq.jpg").resize(150, 150)
                    .centerCrop().into(mProfilePictureView);

        }

    }


        private void InitializeFacebook () {
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();

                IncommingRequest();
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }


        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            currentUser = mAuth.getCurrentUser();
                            getInfo();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void BuInvite(View view) {
        Log.d("Invate",etInviteEMail.getText().toString());
        myRef.child("Users")
                .child(BeforeAt(etInviteEMail.getText().toString())).child("Req").push().setValue(userEmail);

        // Jena //Laya  ="Laya:Jena"
        StartGame(BeforeAt(etInviteEMail.getText().toString()) +":"+ BeforeAt(userEmail));
        MySample = "X";

    }

    public void BuAccept(View view) {
        Log.d("Accept",etInviteEMail.getText().toString());
        myRef.child("Users")
                .child(BeforeAt(etInviteEMail.getText().toString())).child("Req").push().setValue(userEmail);

        //Laya// Jena  ="Laya:Jena"
        StartGame(BeforeAt( BeforeAt(userEmail) + ":"+ etInviteEMail.getText().toString()));
        MySample = "O";
    }

    public void NewGame(View view){

        ButtonColorDef();
        StopGame(BeforeAt(etInviteEMail.getText().toString()) +":"+ BeforeAt(userEmail));

    }


    public void IncommingRequest(){

        // Read from the database
        userEmail = currentUser.getEmail();
        myRef.child("Users").child(BeforeAt(userEmail)).child("Req")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try{
                            HashMap<String,Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                            if (td!=null){

                                String value;
                                for(String key:td.keySet()){
                                    value=(String) td.get(key);
                                    Log.d("User request",value);
                                    etInviteEMail.setText(value);
                                    ButtonColorInvite();
                                    myRef.child("Users").child(BeforeAt(userEmail)).child("Req").setValue(uid);
                                    break;
                                }
                            }
                            else{
                                ButtonColorDef();
                            }

                        }catch (Exception ex){}
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

    }

    private void ButtonColorInvite(){
        etInviteEMail.setBackgroundColor(Color.RED);
        Toast.makeText(MainActivity.this, "You Have Invite to Play !!",
                Toast.LENGTH_SHORT).show();
    }

    private void ButtonColorDef(){
        etInviteEMail.setBackgroundColor(Color.GREEN);
    }

    private void StartGame(String PlayerGameID) {
        PlayerSession = PlayerGameID;

        myRef.child("Playing").child(PlayerGameID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try{
                            Player1.clear();
                            Player2.clear();
                            ActivePlayer = 2;
                            HashMap<String,Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                            if (td!=null){

                                String value;

                                for(String key:td.keySet()){
                                    value=(String) td.get(key);
                                    if(!value.equals(BeforeAt(userEmail)))
                                        ActivePlayer = MySample == "X"?1:2;
                                    else
                                        ActivePlayer = MySample == "X"?2:1;

                                    String[] splitID = key.split(":");
                                    AutoPlay(Integer.parseInt(splitID[1]));

                                }
                            }


                        }catch (Exception ex){}
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }

    private void StopGame(String PlayerGameID){
        Player1.clear();
        Player2.clear();

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void BuClick(View view) {
        // game not started yet
        if (PlayerSession.length()<=0)
            return;

        Button buSelected = (Button) view;
        int CellID = 0;
        switch ((buSelected.getId())){

            case R.id.bu1:
                CellID=1;
                break;

            case R.id.bu2:
                CellID=2;
                break;

            case R.id.bu3:
                CellID=3;
                break;

            case R.id.bu4:
                CellID=4;
                break;

            case R.id.bu5:
                CellID=5;
                break;

            case R.id.bu6:
                CellID=6;
                break;

            case R.id.bu7:
                CellID=7;
                break;

            case R.id.bu8:
                CellID=8;
                break;

            case R.id.bu9:
                CellID=9;
                break;
        }


        myRef.child("Playing").child(PlayerSession).child("CellID:"+CellID).setValue(BeforeAt(userEmail));


        if (MySample == "O"){
            textcolor.setText("Your Color is Green");
            textcolor.setTextColor(Color.GREEN);
        }
        else {
            textcolor.setText("Your Color is Blue");
            textcolor.setTextColor(Color.BLUE);
        }


    }



    private void PlayGame(int CellID,Button buSelected){

        Log.d("Player:",String.valueOf(CellID));

        if (ActivePlayer==1){
            buSelected.setText("X");
            buSelected.setBackgroundColor(Color.GREEN);
            Player1.add(CellID);

        }
        else if (ActivePlayer==2){
            buSelected.setText("O");
            buSelected.setBackgroundColor(Color.BLUE);
            Player2.add(CellID);
        }

        buSelected.setClickable(false);
        CheckWiner();
    }


    private void CheckWiner(){
        int Winer = -1;
        //row 1
        if (Player1.contains(1) && Player1.contains(2)  && Player1.contains(3))  {
            Winer=1 ;
        }
        if (Player2.contains(1) && Player2.contains(2)  && Player2.contains(3))  {
            Winer=2 ;
        }

        //row 2
        if (Player1.contains(4) && Player1.contains(5)  && Player1.contains(6))  {
            Winer=1 ;
        }
        if (Player2.contains(4) && Player2.contains(5)  && Player2.contains(6))  {
            Winer=2 ;
        }

        //row 3
        if (Player1.contains(7) && Player1.contains(8)  && Player1.contains(9))  {
            Winer=1 ;
        }
        if (Player2.contains(7) && Player2.contains(8)  && Player2.contains(9))  {
            Winer=2 ;
        }


        //col 1
        if (Player1.contains(1) && Player1.contains(4)  && Player1.contains(7))  {
            Winer=1 ;
        }
        if (Player2.contains(1) && Player2.contains(4)  && Player2.contains(7))  {
            Winer=2 ;
        }

        //col 2
        if (Player1.contains(2) && Player1.contains(5)  && Player1.contains(8))  {
            Winer=1 ;
        }
        if (Player2.contains(2) && Player2.contains(5)  && Player2.contains(8))  {
            Winer=2 ;
        }


        //col 3
        if (Player1.contains(3) && Player1.contains(6)  && Player1.contains(9))  {
            Winer=1 ;
        }
        if (Player2.contains(3) && Player2.contains(6)  && Player2.contains(9))  {
            Winer=2 ;
        }

        if (Player1.contains(3) && Player1.contains(5)  && Player1.contains(7))  {
            Winer=1 ;
        }
        if (Player2.contains(3) && Player2.contains(5)  && Player2.contains(7))  {
            Winer=2 ;
        }

        if (Player1.contains(1) && Player1.contains(5)  && Player1.contains(9))  {
            Winer=1 ;
        }
        if (Player2.contains(1) && Player2.contains(5)  && Player2.contains(9))  {
            Winer=2 ;
        }


        if ( Winer !=-1){
            // We have winer

            if (Winer==1){
                Toast.makeText(this,"Player 1 is winner",Toast.LENGTH_LONG).show();
            }

            if (Winer==2){
                Toast.makeText(this,"Player 2 is winner",Toast.LENGTH_LONG).show();
            }

        }

    }


    private void AutoPlay(int CellID){

        Button buSelected;
        switch (CellID){

            case 1 :
                buSelected=(Button) findViewById(R.id.bu1);
                break;

            case 2:
                buSelected=(Button) findViewById(R.id.bu2);
                break;

            case 3:
                buSelected=(Button) findViewById(R.id.bu3);
                break;

            case 4:
                buSelected=(Button) findViewById(R.id.bu4);
                break;

            case 5:
                buSelected=(Button) findViewById(R.id.bu5);
                break;

            case 6:
                buSelected=(Button) findViewById(R.id.bu6);
                break;

            case 7:
                buSelected=(Button) findViewById(R.id.bu7);
                break;

            case 8:
                buSelected=(Button) findViewById(R.id.bu8);
                break;

            case 9:
                buSelected=(Button) findViewById(R.id.bu9);
                break;
            default:
                buSelected=(Button) findViewById(R.id.bu1);
                break;

        }
        PlayGame(CellID, buSelected);
    }

}
