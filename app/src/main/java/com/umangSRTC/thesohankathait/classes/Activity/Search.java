package com.umangSRTC.thesohankathait.classes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.umangSRTC.thesohankathait.classes.Adapter.SearchListAdapter;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private SearchListAdapter searchListAdapter;
    private ListView searchListview;
    private List<Notices> searchList;
    private androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView=findViewById(R.id.searchView);
        searchListview=findViewById(R.id.searchListView);
        searchList=new ArrayList<>();
        searchListAdapter=new SearchListAdapter(this,searchList);
        searchListview.setAdapter(searchListAdapter);

        searchView.setActivated(true);
        searchView.setQueryHint("Type here");
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchedDataFromFireBase(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getSearchedDataFromFireBase(String query) {

        FirebaseDatabase.getInstance().getReference().child(Initialisation.selectedCollege).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                searchList.add(dataSnapshot.getValue(Notices.class));
                searchListAdapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
    }
}
