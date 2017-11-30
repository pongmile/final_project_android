package com.pongmile.penguinicebreaker;

import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pongmile on 11/30/2017.
 */

public class startGame{
 /*   public String useEmail;
    public String MSample;
    public String PlayeSession = "";
    public String PlayeGameID;
    public int ActivPlayer;

    MainActivity mainActivity = new MainActivity();

    public ArrayList<Integer> Playe1= new ArrayList<Integer>();// hold player 1 data
    public ArrayList<Integer> Playe2= new ArrayList<Integer>();// hold player 2 data

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    public void startG(String PlayerGameID, String PlayerSession, final String MySample, final String userEmail, int ActivePlayer, ArrayList<Integer> Player1,ArrayList<Integer> Player2){

        this.PlayeGameID = PlayerGameID;
        this.useEmail = userEmail;
        this.MSample = MySample;
        this.PlayeSession = PlayerSession;
        this.ActivPlayer = ActivePlayer;
        this.Playe1 = Player1;
        this.Playe2 = Player2;

        PlayeSession = PlayeGameID;
        myRef.child("Playing").child(PlayeGameID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try{
                            Playe1.clear();
                            Playe2.clear();
                            ActivPlayer = 2;
                            HashMap<String,Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                            if (td!=null){
                                String value;
                                for(String key:td.keySet()){
                                    value=(String) td.get(key);
                                    if(!value.equals(BeforeAt(useEmail)))
                                        ActivPlayer = MSample == "X"?1:2;

                                    else
                                        ActivPlayer = MSample== "X"?2:1;

                                    String[] splitID = key.split(":");
                                    mainActivity.AutoPlay(Integer.parseInt(splitID[1]));

                                }
                            }

                        }catch (Exception ex){}
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }
    String BeforeAt(String Email){
        String[] split= Email.split("@");
        return split[0];
    }*/
}
