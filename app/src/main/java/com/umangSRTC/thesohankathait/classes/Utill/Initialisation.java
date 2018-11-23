package com.umangSRTC.thesohankathait.classes.Utill;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.classes.Fragment.Schools;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Initialisation extends Application {
    public static ArrayList<String> schools;
    public static ArrayList<String> schoolArrayList;
    public static ArrayList<String> adminList;

    @Override
    public void onCreate() {
        super.onCreate();

        adminList=new ArrayList<>();
        fetchAdminListFromFirebase();

        schools=new ArrayList<>();
        schools.add("Select Schools");
        schoolArrayList=new ArrayList<>();
        getSchools();


    }

    private void fetchAdminListFromFirebase() {

        FirebaseDatabase.getInstance().getReference("AdminEmail").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!adminList.contains(dataSnapshot.getValue().toString())) {
                    adminList.add(dataSnapshot.getValue().toString());
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

    private void getSchools() {


        FirebaseDatabase.getInstance().getReference("Schools").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(!schoolArrayList.contains(dataSnapshot.getValue().toString())) {
                        schools.add(dataSnapshot.getValue().toString());
                        schoolArrayList.add(dataSnapshot.getValue().toString());
                        Collections.sort(schoolArrayList);
                        Collections.sort(schools.subList(1, schools.size()));


                        // Refresh list on addition for instant effect
                        if(Schools.schoolsFragmentInstance!=null){
                            Log.i("refreshlist", "onChildRemoved: "+"list refereshed");
                            Schools.schoolsFragmentInstance.schoolsArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                // get the removed school
                String removedSchool = dataSnapshot.getValue().toString();
                Log.i("School removed", "onChildRemoved: "+removedSchool);

                // Remove the school from the local arraylists 'School' and 'schoolArrayList' whenever the child is removed from
                // firebase , to keep things syncronised
                schools.remove(removedSchool);
                schoolArrayList.remove(removedSchool);

                // Notify dataset change in schools list otherwise it could get index out of bounds
                // due to deletion without notifying deletion
                if(Schools.schoolsFragmentInstance!=null){
                    Log.i("refreshlist", "onChildRemoved: "+"list refereshed");
                    Schools.schoolsFragmentInstance.schoolsArrayAdapter.notifyDataSetChanged();
                }

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
