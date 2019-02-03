package com.umangSRTC.thesohankathait.classes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.umangSRTC.thesohankathait.classes.Adapter.SearchListAdapter;
import com.umangSRTC.thesohankathait.classes.Utill.Equals;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Search extends AppCompatActivity {

    private SearchListAdapter searchListAdapter;
    private ListView searchListview;
    private List<Notices> searchList;
    private androidx.appcompat.widget.SearchView searchView;
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        getSearchedDataFromFireBase();


       searchListview.setFocusable(View.FOCUSABLE);
       searchListview.setClickable(true);


        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // searchListAdapter.getFilter().filter(query);
               // searchListAdapter.getFilter().filter(query);
                searchListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    searchListAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void getSearchedDataFromFireBase() {

        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege).child("Category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for(DataSnapshot finaDataSnapshot:dataSnapshot.getChildren()) {
                    Log.d("text",finaDataSnapshot.getValue(Notices.class).getTitle());
                            Notices notices = finaDataSnapshot.getValue(Notices.class);
//                    Collections.sort(searchList, new Comparator<Notices>() {
//                                @Override
//                                public int compare(Notices notices1, Notices notices2) {
//                                    return notices1.getTitle().compareTo(notices2.getTitle());
//                                }
//                            }
//                    );

                    if(!Equals.contain(searchList,notices))
                                searchList.add(notices);
                            Log.d("title", notices.getTitle());
                            searchListAdapter.notifyDataSetChanged();
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
      //  Query pdfQuery=     FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege).child("PdfCategory").orderByChild("title").startAt(query).endAt(query+"\uf8ff");

        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege).child("PdfCategory").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for(DataSnapshot finaDataSnapshot:dataSnapshot.getChildren()) {
                    Log.d("text",finaDataSnapshot.getValue(Notices.class).getTitle());
//                    Collections.sort(searchList, new Comparator<Notices>() {
//                                @Override
//                                public int compare(Notices notices1, Notices notices2) {
//                                    return notices1.getTitle().compareTo(notices2.getTitle());
//                                }
//                            }
//                    );
                    Collections.shuffle(searchList);

                    Notices notices = finaDataSnapshot.getValue(Notices.class);
                    if(!Equals.contain(searchList,notices))
                        searchList.add(notices);
                    Log.d("title", notices.getTitle());
                    searchListAdapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
    }
}
