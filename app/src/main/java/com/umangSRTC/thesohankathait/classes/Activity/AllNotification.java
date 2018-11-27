package com.umangSRTC.thesohankathait.classes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.umangSRTC.thesohankathait.classes.Fragment.FullScreenDialogFragment;
import com.umangSRTC.thesohankathait.classes.Utill.DeleteFromFirebaseStorage;
import com.umangSRTC.thesohankathait.classes.Utill.DownloadTask;
import com.umangSRTC.thesohankathait.classes.Utill.Equals;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.ViewHolders.NoticesViewHolder;
import com.umangSRTC.thesohankathait.classes.model.Notices;

public class AllNotification extends AppCompatActivity {

    private  RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Notices,NoticesViewHolder> firebaseRecyclerAdapter;
    private ProgressBar allNotificationProgressbar;
    private InterstitialAd mInterstitialAd;  //ads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
        Intent intent=getIntent();
        String schoolName=intent.getStringExtra("SCHOOL");


        allNotificationProgressbar=findViewById(R.id.allNotificationProgressbar);
        recyclerView=findViewById(R.id.allNotificationRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);//it will set the recycler view to show the elements in bottom up manner.
        linearLayoutManager.setStackFromEnd(true);//it will show the last element first.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetchDataFromFirebase(schoolName);

        //for displaying ads
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //initialising
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));//modify the ad id from string resources
        //loading  an ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    private void fetchDataFromFirebase(final String schoolName) {
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Notices, NoticesViewHolder>(Notices.class,R.layout.all_notification_row,NoticesViewHolder.class,FirebaseDatabase.getInstance().getReference("Category").child(schoolName)) {
            @Override
            protected void populateViewHolder(final NoticesViewHolder noticesViewHolder, final Notices notices, final int postion) {

                allNotificationProgressbar.setVisibility(View.GONE);
                noticesViewHolder.allNoticeTitleTextView.setText(notices.getTitle());
//                noticesViewHolder.allNoticeDescriptionTextView.setText(notices.getDescription());
                Glide.with(getApplicationContext()).load(notices.getImageUrl()).into(noticesViewHolder.allNoticeImageView);

                String sender="- "+notices.getSender();
                noticesViewHolder.allNoticeSenderTextview.setText(sender);

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

                noticesViewHolder.allNoticeTitleTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFullNotices(schoolName,notices);
                    }
                });



            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    private void showFullNotices(String schoolName, Notices notices) {



        //show full dialog fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FullScreenDialogFragment fullScreenDialogFragment = FullScreenDialogFragment.newInstance(schoolName,notices);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, fullScreenDialogFragment).addToBackStack("faf").commit();

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
                    if(Equals.BothEquals(notices,dataSnapshot.getValue(Notices.class))){
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


    private void showFullImage(final String schoolName, final Notices notices) {
        View view=LayoutInflater.from(this).inflate(R.layout.full_image,null,false);
        ImageView imageView=view.findViewById(R.id.allNoticeImageView);
        Glide.with(getApplicationContext()).load(notices.getImageUrl()).into(imageView);
        Button imageDownloadButton=view.findViewById(R.id.imageDownloadButton);
        imageDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask downloadTask=new DownloadTask(getApplicationContext(),notices.getImageUrl(),notices.getTitle(),schoolName,notices.getFileExtension());
                downloadTask.DownloadData();

                //if ad is loaded than show it if user download a pdf
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    // Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });


        AlertDialog alertDialog= new AlertDialog.Builder(this)
                .setView(view)
                .show();

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()!=0) {
            getSupportFragmentManager().popBackStack();
        }
        else{
            finish();

        }
    }
}
