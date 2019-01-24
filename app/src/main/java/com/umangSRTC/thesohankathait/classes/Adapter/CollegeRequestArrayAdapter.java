package com.umangSRTC.thesohankathait.classes.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umangSRTC.thesohankathait.classes.Fragment.AllRequestedColleges;
import com.umangSRTC.thesohankathait.classes.Fragment.Request;
import com.umangSRTC.thesohankathait.classes.Utill.DeleteFromFirebaseStorage;
import com.umangSRTC.thesohankathait.classes.Utill.DownloadTask;
import com.umangSRTC.thesohankathait.classes.Utill.Equals;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
import com.umangSRTC.thesohankathait.classes.model.College;
import com.umangSRTC.thesohankathait.classes.model.NoticeRequest;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

public class CollegeRequestArrayAdapter extends ArrayAdapter{

        private ArrayList<College> collegeRequestsArrayList;
        private Context context;
        private TextView collegeNameTextView,phoneTextView,emailTextView;
        private CardView cardView;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View view=layoutInflater.inflate(R.layout.college_request_row,parent,false);
            collegeNameTextView=view.findViewById(R.id.collegeNameTextView);
            phoneTextView=view.findViewById(R.id.phoneTextView);
            emailTextView=view.findViewById(R.id.emailTextView);
            cardView=view.findViewById(R.id.rootLayoutCardview);

             collegeNameTextView.setText(collegeRequestsArrayList.get(position).getCollegeName());
             phoneTextView.setText(collegeRequestsArrayList.get(position).getPhoneNo());
             emailTextView.setText(collegeRequestsArrayList.get(position).getAdminEmail());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(new College(collegeNameTextView.getText().toString(),emailTextView.getText().toString(),phoneTextView.getText().toString()));
                }
            });


            return view;
        }

    private void showAlertDialog(final College college) {

            new AlertDialog.Builder(context)
                    .setTitle("If you allow than this college will be added with the listed category of colleges\n")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adminIsSureforAllow(college);
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCollegeFromFireBase(college);
                        }
                    })
                    .show();
        }

    // //********************for allow a notice******************


    private void adminIsSureforAllow(final College college) {

            FirebaseDatabase.getInstance().getReference("CollegeRequests").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(Equals.BothEqual(college,dataSnapshot.getValue(College.class))){
                        FirebaseDatabase.getInstance().getReference(college.getCollegeName()).child("AdminEmail").push().setValue(college.getAdminEmail());
                        FirebaseDatabase.getInstance().getReference("Colleges").push().setValue(college.getCollegeName());
                        deleteCollegeFromFireBase(college);
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
    public int getCount() {
        return collegeRequestsArrayList.size();
    }

    public CollegeRequestArrayAdapter(Context context, ArrayList<College> collegeRequestsArrayList) {
        super(context,R.layout.notification_request_row,collegeRequestsArrayList);
        this.collegeRequestsArrayList=collegeRequestsArrayList;
        this.context=context;
    }

    private void deleteCollegeFromFireBase(final College college) {

        FirebaseDatabase.getInstance().getReference("CollegeRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    College currentCollege=dataSnapshot.getValue(College.class);
                if (currentCollege != null && Equals.BothEqual(currentCollege, college)) {
                    dataSnapshot.getRef().removeValue();

                    for (int i = 0; i < AllRequestedColleges.collegeRequestsArrayList.size(); i++) {
                        if (Equals.BothEqual(AllRequestedColleges.collegeRequestsArrayList.get(i), college)) {
                            AllRequestedColleges.collegeRequestsArrayList.remove(i);
                            break;
                        }
                    }
                    AllRequestedColleges.collegeRequestArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
