package com.umangSRTC.thesohankathait.umang.classes.Utill;

import android.app.Application;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Initialisation extends Application {
    public static ArrayList<String> schools;
    public static ArrayList<String> schoolArrayList;

    @Override
    public void onCreate() {
        super.onCreate();

        schools=new ArrayList<>();
        schools.add("Select Schools");
        schoolArrayList=new ArrayList<>();
        getSchools();

    }

    private void getSchools() {


        FirebaseDatabase.getInstance().getReference("Schools").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(!schoolArrayList.contains(dataSnapshot.getValue().toString())) {
                        schools.add(dataSnapshot.getValue().toString());
                        schoolArrayList.add(dataSnapshot.getValue().toString());
                        Collections.sort(schoolArrayList);
                        Collections.sort(schools.subList(1, schools.size()));
                    }
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
