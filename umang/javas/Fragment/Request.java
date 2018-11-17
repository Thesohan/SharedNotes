package com.umangSRTC.thesohankathait.umang.javas.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.umang.javas.Adapter.RequestArrayAdapter;
import com.umangSRTC.thesohankathait.umang.javas.model.NoticeRequest;
import com.umangSRTC.thesohankathait.umang.javas.model.Notices;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Request extends Fragment {

    private ListView requestListView;
    public static RequestArrayAdapter requestArrayAdapter;
    public static ArrayList<NoticeRequest> noticeRequestArrayList;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        context = getContext();
        noticeRequestArrayList = new ArrayList<>();
        requestListView = view.findViewById(R.id.requestListView);
        requestArrayAdapter = new RequestArrayAdapter(context, noticeRequestArrayList);
        requestListView.setAdapter(requestArrayAdapter);

        fetchRequestedNotice();
        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "list item clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void fetchRequestedNotice() {
        FirebaseDatabase.getInstance().getReference("Requests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("cild", dataSnapshot.getValue().toString());
                for (DataSnapshot finalDataSnapshot : dataSnapshot.getChildren()) {
                    Notices notices = finalDataSnapshot.getValue(Notices.class);
                    NoticeRequest noticeRequest = new NoticeRequest(dataSnapshot.getKey(), notices);
                    noticeRequestArrayList.add(noticeRequest);
                    requestArrayAdapter.notifyDataSetChanged();

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

    public static Request newInstance() {
        
        Bundle args = new Bundle();
        
        Request fragment = new Request();
        fragment.setArguments(args);
        return fragment;
    }
}