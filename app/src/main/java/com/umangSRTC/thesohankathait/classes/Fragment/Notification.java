package com.umangSRTC.thesohankathait.classes.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.umangSRTC.thesohankathait.classes.Utill.DeleteFromFirebaseStorage;
import com.umangSRTC.thesohankathait.classes.Utill.DownloadTask;
import com.umangSRTC.thesohankathait.classes.database.DbHelper;
import com.umangSRTC.thesohankathait.classes.model.NoticeRequest;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.Utill.Equals;
import com.umangSRTC.thesohankathait.classes.ViewHolders.NoticesViewHolder;
import com.umangSRTC.thesohankathait.classes.model.Notices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Notification extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar allNotificationProgressbar;
    private FirebaseRecyclerAdapter<Notices,NoticesViewHolder> firebaseRecyclerAdapter;
   // private InterstitialAd mInterstitialAd;  //ads

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_all_notification,container,false);
        recyclerView=view.findViewById(R.id.allNotificationRecyclerView);
        allNotificationProgressbar=view.findViewById(R.id.allNotificationProgressbar);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);//it will set the recycler view to show the elements in bottom up manner.
        linearLayoutManager.setStackFromEnd(true);//it will show the last element first.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetchDataFromFirebase("Notification");


        //for displaying ads
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //initialising
//        MobileAds.initialize(getContext(),"ca-app-pub-3940256099942544~3347511713");
//        mInterstitialAd = new InterstitialAd(getContext());
//        mInterstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));//modify the ad id from string resources
//        //loading  an ad
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        return view;
    }
    private void fetchDataFromFirebase(final String schoolName) {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notices, NoticesViewHolder>(Notices.class, R.layout.all_notification_row, NoticesViewHolder.class, FirebaseDatabase.getInstance().getReference("Category").child(schoolName)) {
            @Override
            protected void populateViewHolder(NoticesViewHolder noticesViewHolder, final Notices notices, final int postion) {

                allNotificationProgressbar.setVisibility(View.GONE);
                noticesViewHolder.allNoticeTitleTextView.setText(notices.getTitle());
//                noticesViewHolder.allNoticeDescriptionTextView.setText(notices.getDescription());
                Glide.with(getContext()).load(notices.getImageUrl()).into(noticesViewHolder.allNoticeImageView);

                String sender="- "+notices.getSender();
                noticesViewHolder.allNoticeSenderTextview.setText(sender);

                noticesViewHolder.allNoticeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFullNotices(schoolName,notices);
                    }
                });

                noticesViewHolder.allNoticeTitleTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFullNotices(schoolName,notices);
                    }
                });

                if (Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    noticesViewHolder.allNotificationlinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            deleteNotificationWarning(schoolName, notices);
                            return true;
                        }
                    });
                }
//               noticesViewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DbHelper dbHelper=new DbHelper(getContext());
//                        NoticeRequest noticeRequest=new NoticeRequest(schoolName,notices);
//                        dbHelper.saveNotice(noticeRequest);
//                        dbHelper.close();
//                        // Toast.makeText(getContext(), "Notice saved", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void showFullNotices(String schoolName, Notices notices) {


        //show full dialog fragment

        FragmentManager fragmentManager = getFragmentManager();
        FullScreenDialogFragment fullScreenDialogFragment = FullScreenDialogFragment.newInstance(schoolName,notices);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, fullScreenDialogFragment).addToBackStack("faf").commit();

    }

    private void deleteNotificationWarning(final String schoolName, final Notices notices) {


        AlertDialog builder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Do you really want to delete this notice?")
                .setTitle("Delete Notice")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("imageurlbeforedelete",notices.getImageUrl());

                        deleteNotificaitonFromFirebase(schoolName, notices);
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
                if (Equals.BothEquals(notices, dataSnapshot.getValue(Notices.class))) {
                    dataSnapshot.getRef().removeValue();
  //                  Log.d("justve",notices.getImageUrl());
                    if(notices.getImageUrl()!=null)
                        DeleteFromFirebaseStorage.deleteByDownloadUrl(getContext(),notices.getImageUrl());
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
//
//    private void    showFullImage(final String schoolName, final Notices notices) {
//
//     View view = LayoutInflater.from(getContext()).inflate(R.layout.full_image, null, false);
//        ImageView imageView = view.findViewById(R.id.allNoticeImageView);
//        Button imageDownloadButton=view.findViewById(R.id.imageDownloadButton);
//        imageDownloadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DownloadTask downloadTask=new DownloadTask(getContext(),notices.getImageUrl(),notices.getTitle(),schoolName,notices.getFileExtension());
//                downloadTask.DownloadData();
//
//                //if ad is loaded than show it if user download a pdf
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    // Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }
//
//            }
//        });
//        Glide.with(getContext()).load(notices.getImageUrl()).into(imageView);
//        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
//                .setView(view)
//                .show();
//
//    }

    public static Notification newInstance() {

        Bundle args = new Bundle();

        Notification fragment = new Notification();
        fragment.setArguments(args);
        return fragment;
    }


}
