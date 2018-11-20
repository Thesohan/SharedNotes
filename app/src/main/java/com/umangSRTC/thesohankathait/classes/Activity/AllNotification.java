package com.umangSRTC.thesohankathait.classes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.umangSRTC.thesohankathait.classes.Utill.DeleteFromFirebaseStorage;
import com.umangSRTC.thesohankathait.classes.Utill.DownloadTask;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.ViewHolders.NoticesViewHolder;
import com.umangSRTC.thesohankathait.classes.model.Notices;

public class AllNotification extends AppCompatActivity {

    private  RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Notices,NoticesViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
        Intent intent=getIntent();
        String schoolName=intent.getStringExtra("SCHOOL");


        recyclerView=findViewById(R.id.allNotificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        fetchDataFromFirebase(schoolName);

    }

    private void fetchDataFromFirebase(final String schoolName) {
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Notices, NoticesViewHolder>(Notices.class,R.layout.all_notification_row,NoticesViewHolder.class,FirebaseDatabase.getInstance().getReference("Category").child(schoolName)) {
            @Override
            protected void populateViewHolder(final NoticesViewHolder noticesViewHolder, final Notices notices, final int postion) {
                noticesViewHolder.allNoticeTitleTextView.setText(notices.getTitle());
                noticesViewHolder.allNoticeDescriptionTextView.setText(notices.getDescription());
                Picasso.get().load(notices.getImageUrl()).into(noticesViewHolder.allNoticeImageView);
                noticesViewHolder.allNoticeSenderTextview.setText(notices.getSender());

                noticesViewHolder.allNoticeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFullImage(schoolName,notices);
                    }
                });
                if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    noticesViewHolder.allNotificationlinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            deleteNotificationWarning(schoolName,notices);
                            return true;
                        }
                    });
                }
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void deleteNotificationWarning(final String schoolName, final Notices notices) {


        AlertDialog builder=new AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Do you really want to delete this notification?")
                .setTitle("Delete Notification")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteNotificaitonFromFirebase(schoolName,notices);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteNotificaitonFromFirebase(String schoolName, final Notices notices) {

        FirebaseDatabase.getInstance().getReference("Category").child(schoolName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(BothEquals(notices,dataSnapshot.getValue(Notices.class))){
                        dataSnapshot.getRef().removeValue();
                        DeleteFromFirebaseStorage.deleteByDownloadUrl(AllNotification.this, notices.getImageUrl());
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

    private boolean BothEquals(Notices notices, Notices currentNotice) {


        return notices.getTitle().equals(currentNotice.getTitle()) &&
                notices.getSender().equals(currentNotice.getSender())&&
                notices.getImageUrl().equals(currentNotice.getImageUrl())&&
                notices.getDescription().equals(currentNotice.getDescription());
    }

    private void showFullImage(final String schoolName, final Notices notices) {
        View view=LayoutInflater.from(this).inflate(R.layout.full_screen_notification,null,false);
        ImageView imageView=view.findViewById(R.id.allNoticeImageView);
        Picasso.get().load(notices.getImageUrl()).into(imageView);
        Button imageDownloadButton=view.findViewById(R.id.imageDownloadButton);
        imageDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask downloadTask=new DownloadTask(getApplicationContext(),notices.getImageUrl(),notices.getTitle(),schoolName,notices.getFileExtension());
                downloadTask.DownloadData();
            }
        });


        AlertDialog alertDialog= new AlertDialog.Builder(this)
                .setView(view)
                .show();

    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
