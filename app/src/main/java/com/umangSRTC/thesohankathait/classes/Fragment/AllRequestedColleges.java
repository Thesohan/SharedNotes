package com.umangSRTC.thesohankathait.classes.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.classes.Adapter.CollegeRequestArrayAdapter;
import com.umangSRTC.thesohankathait.classes.model.College;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AllRequestedColleges extends DialogFragment {

    private Context context;
    private ListView listView;

    public static ArrayList<College> collegeRequestsArrayList;

   public static CollegeRequestArrayAdapter collegeRequestArrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        collegeRequestsArrayList=new ArrayList<>();
        fetchColleges();
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        context=getContext();
        listView=view.findViewById(R.id.requestListView);
        collegeRequestArrayAdapter=new CollegeRequestArrayAdapter(context,collegeRequestsArrayList);
        listView.setAdapter(collegeRequestArrayAdapter);

        return view;
    }

    private void fetchColleges() {

        FirebaseDatabase.getInstance().getReference("CollegeRequests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("ans",dataSnapshot.getValue(College.class).toString());
                if(!collegeRequestsArrayList.contains(dataSnapshot.getValue(College.class))){
                    collegeRequestsArrayList.add(dataSnapshot.getValue(College.class));
                            if(collegeRequestArrayAdapter!=null){
                                collegeRequestArrayAdapter.notifyDataSetChanged();
                            }
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



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public static AllRequestedColleges newInstance() {
        
        Bundle args = new Bundle();
        AllRequestedColleges fragment = new AllRequestedColleges();
        fragment.setArguments(args);
        return fragment;
    }

}