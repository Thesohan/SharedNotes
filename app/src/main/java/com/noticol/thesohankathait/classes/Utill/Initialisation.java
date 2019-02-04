package com.noticol.thesohankathait.classes.Utill;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noticol.thesohankathait.classes.Fragment.Schools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

public class Initialisation extends Application {
    public static ArrayList<String> schools;//this list is for spinner since we have to add first element as "select schools
    public static ArrayList<String> schoolArrayList;
    public static ArrayList<String> adminList;
    public static String selectedCollege=null;
    
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();



        getCollegeName();
        sharedPreferences=getSharedPreferences("ADMIN",MODE_PRIVATE);
        editor=sharedPreferences.edit();


        adminList=new ArrayList<>();


        schools=new ArrayList<>();
        schools.add("Select Schools");
        schoolArrayList=new ArrayList<>();

        getSavedSchoolFromSharedPreferences();
        getSavedAdminFromSharedPreferences();

        fetchAdminListFromFirebase();

        getSchools();


    }

    private void getCollegeName() {

        sharedPreferences=getSharedPreferences("COLLEGE",MODE_PRIVATE);
        selectedCollege=sharedPreferences.getString("SELECTEDCOLLEGE",null);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void getSavedAdminFromSharedPreferences() {

        Set<String> stringSet=new HashSet<>();
        stringSet=sharedPreferences.getStringSet("ADMINLIST",null);
        if(stringSet!=null) {
            for (String admin : stringSet) {
                adminList.add(admin);

            }
        }
    }

    private void getSavedSchoolFromSharedPreferences() {

        Set<String> stringSet=new HashSet<>();
        stringSet=sharedPreferences.getStringSet("SCHOOLLIST",null);
        if(stringSet!=null) {
            for (String school : stringSet) {
                    schools.add(school);
                    schoolArrayList.add(school);

            }
            Collections.sort(schoolArrayList);
            Collections.sort(schools.subList(1, schools.size()));

        }

    }

    private void fetchAdminListFromFirebase() {

        if (selectedCollege != null) {
            FirebaseDatabase.getInstance().getReference(selectedCollege).child("AdminEmail").addChildEventListener(new ChildEventListener() {
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
            FirebaseDatabase.getInstance().getReference(selectedCollege).child("AdminEmail").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    updateAdminlListInSharedPreferance();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateAdminlListInSharedPreferance() {

        Set<String> stringSet=new HashSet<>();
        for(String admin:adminList){
            stringSet.add(admin);
        }

        editor.putStringSet("ADMINLIST",stringSet);
        editor.commit();


    }

    private void getSchools() {


        if(selectedCollege!=null) {
            FirebaseDatabase.getInstance().getReference(selectedCollege).child("Schools").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (!schoolArrayList.contains(dataSnapshot.getValue().toString())) {
                        schools.add(dataSnapshot.getValue().toString());
                        schoolArrayList.add(dataSnapshot.getValue().toString());
                        Collections.sort(schoolArrayList);
                        Collections.sort(schools.subList(1, schools.size()));
                        updateSchoolListInSharedPreferance();

                        // Refresh list on addition for instant effect
                        if (Schools.schoolsFragmentInstance != null) {
                            Log.i("refreshlist", "onChildRemoved: " + "list refereshed");
                            Schools.schoolsFragmentInstance.schoolsArrayAdapter.notifyDataSetChanged();
                        }

                        if (Schools.schoolProgressbar != null)
                            Schools.schoolProgressbar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    // get the removed school
                    String removedSchool = dataSnapshot.getValue().toString();
                    Log.i("School removed", "onChildRemoved: " + removedSchool);

                    // Remove the school from the local arraylists 'School' and 'schoolArrayList' whenever the child is removed from
                    // firebase , to keep things syncronised
                    schools.remove(removedSchool);
                    schoolArrayList.remove(removedSchool);

                    // Notify dataset change in schools list otherwise it could get index out of bounds
                    // due to deletion without notifying deletion
                    if (Schools.schoolsFragmentInstance != null) {
                        Log.i("refreshlist", "onChildRemoved: " + "list refereshed");
                        Schools.schoolsFragmentInstance.schoolsArrayAdapter.notifyDataSetChanged();
                    }

                    //since we have to update sharedPrefeances
                    updateSchoolListInSharedPreferance();

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

    private void updateSchoolListInSharedPreferance() {


        Set<String> stringSet=new HashSet<>();
        for(String school:schoolArrayList){
            stringSet.add(school);
        }

        editor.putStringSet("SCHOOLLIST",stringSet);
        editor.commit();

    }
}
