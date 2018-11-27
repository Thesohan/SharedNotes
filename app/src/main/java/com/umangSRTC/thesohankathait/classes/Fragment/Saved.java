package com.umangSRTC.thesohankathait.classes.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.umangSRTC.thesohankathait.classes.Adapter.SavedNoticeRecyclerAdapter;
import com.umangSRTC.thesohankathait.classes.database.DbHelper;
import com.umangSRTC.thesohankathait.classes.model.NoticeRequest;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Saved extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar allNotificationProgressbar;
    private ArrayList<NoticeRequest> noticeRequests;
    public static SavedNoticeRecyclerAdapter savedNoticeRecyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //since we need the same layout as for all_notification ..
        View view=inflater.inflate(R.layout.activity_all_notification,container,false);
        recyclerView=view.findViewById(R.id.allNotificationRecyclerView);
        allNotificationProgressbar=view.findViewById(R.id.allNotificationProgressbar);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);//it will set the recycler view to show the elements in bottom up manner.
        linearLayoutManager.setStackFromEnd(true);//it will show the last element first.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        noticeRequests =new ArrayList<>();

        //for displaying ads
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        fetchNoticeFromDatabase();
        allNotificationProgressbar.setVisibility(View.GONE);
        savedNoticeRecyclerAdapter=new SavedNoticeRecyclerAdapter(noticeRequests,getContext());
        recyclerView.setAdapter(savedNoticeRecyclerAdapter);

        return view;
    }

    private void fetchNoticeFromDatabase() {

        DbHelper dbHelper=new DbHelper(getContext());
        noticeRequests=dbHelper.readNoticeList();
//        savedNoticeRecyclerAdapter.notifyDataSetChanged();
        dbHelper.close();
        //pass list to adapter.

    }

    public static Saved newInstance() {
        
        Bundle args = new Bundle();
        
        Saved fragment = new Saved();
        fragment.setArguments(args);
        return fragment;
    }
}
