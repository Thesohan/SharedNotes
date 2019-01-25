package com.umangSRTC.thesohankathait.classes.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.classes.Adapter.RequestPdfNoticeArrayAdapter;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
import com.umangSRTC.thesohankathait.classes.ViewHolders.PdfNoticesViewHolder;
import com.umangSRTC.thesohankathait.classes.model.College;
import com.umangSRTC.thesohankathait.classes.model.NoticeRequest;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PdfPostRequest extends DialogFragment {

    private ListView requestPdfListView;
    public static ArrayList<NoticeRequest> pdfNoticeRequestList;
    private FirebaseRecyclerAdapter<Notices,PdfNoticesViewHolder> firebaseRecyclerAdapter;
    public static RequestPdfNoticeArrayAdapter requestPdfNoticeArrayAdapter;

    private ProgressBar pdfProgressbar,pdfRequestProgressbar;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context=getContext();
        View view = inflater.inflate(R.layout.fragment_pdf_notice_request, null, false);
        ArrayList<NoticeRequest> pdfNoticeRequests=new ArrayList<>();
        requestPdfListView = view.findViewById(R.id.requestPdfListView);
        pdfRequestProgressbar=view.findViewById(R.id.pdfNoticeProgressbar);


        pdfNoticeRequestList=new ArrayList<>();
        requestPdfNoticeArrayAdapter=new RequestPdfNoticeArrayAdapter(getContext(),pdfNoticeRequestList);
        requestPdfListView.setAdapter(requestPdfNoticeArrayAdapter);


        fetchAllPdfNoticeRequest();

        return view;
    }

    private void fetchAllPdfNoticeRequest() {


        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfRequests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.d("cild", dataSnapshot.getValue().toString());
                for (DataSnapshot finalDataSnapshot : dataSnapshot.getChildren()) {
                    Notices notices = finalDataSnapshot.getValue(Notices.class);
                    NoticeRequest noticeRequest = new NoticeRequest(dataSnapshot.getKey(), notices);
                    pdfNoticeRequestList.add(noticeRequest);
                    requestPdfNoticeArrayAdapter.notifyDataSetChanged();
                    pdfRequestProgressbar.setVisibility(View.GONE);

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


    public static PdfPostRequest newInstance() {
        
        Bundle args = new Bundle();
        PdfPostRequest fragment = new PdfPostRequest();
        fragment.setArguments(args);
        return fragment;
    }

}