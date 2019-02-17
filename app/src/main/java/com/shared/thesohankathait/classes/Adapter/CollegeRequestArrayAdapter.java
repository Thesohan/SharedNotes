package com.shared.thesohankathait.classes.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.shared.thesohankathait.classes.Fragment.AllRequestedColleges;
import com.shared.thesohankathait.classes.Utill.Admin;
import com.shared.thesohankathait.classes.Utill.Equals;
import com.shared.thesohankathait.classes.model.College;
import com.shared.thesohankathait.notices.R;

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
                    Toast.makeText(context, ""+collegeRequestsArrayList.get(position).getCollegeName(), Toast.LENGTH_SHORT).show();

                     showAlertDialog(collegeRequestsArrayList.get(position));
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
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCollegeFromFireBase(college);
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

    // //********************for allow a notice******************


    private void adminIsSureforAllow(final College college) {

            FirebaseDatabase.getInstance().getReference("CollegeRequests").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    College currentCollege=dataSnapshot.getValue(College.class);
                    if(Equals.BothEqual(college,currentCollege)){
                        FirebaseDatabase.getInstance().getReference(Admin.modifyCollege(college.getCollegeName())).child("AdminEmail").push().setValue(college.getAdminEmail());
                        FirebaseDatabase.getInstance().getReference("Colleges").push().setValue(Admin.modifyCollege(college.getCollegeName()));
                        FirebaseDatabase.getInstance().getReference(Admin.modifyCollege(college.getCollegeName())).child("Schools").push().setValue("Notices");//since i have created this app for notice saring so every college must use it for notice sharing
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
        super(context,R.layout.college_request_row,collegeRequestsArrayList);
        this.collegeRequestsArrayList=collegeRequestsArrayList;
        this.context=context;
    }

    private void deleteCollegeFromFireBase(final College college) {

        FirebaseDatabase.getInstance().getReference("CollegeRequests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

}
