package com.umangSRTC.thesohankathait.umang.javas.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.umang.javas.Fragment.PdfNotice;
import com.umangSRTC.thesohankathait.umang.javas.Utill.Initialisation;
import com.umangSRTC.thesohankathait.umang.javas.Utill.PdfDownloadTask;
import com.umangSRTC.thesohankathait.umang.javas.model.NoticeRequest;
import com.umangSRTC.thesohankathait.umang.javas.model.Notices;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class RequestPdfNoticeArrayAdapter extends ArrayAdapter{

        private ArrayList<NoticeRequest> pdfNoticeRequestsArrayList=new ArrayList<>();
        private Context context;
        private ImageView pdfnoticeImageView;
        private TextView pdfsenderNameTextView,pdftitleTextView,pdfdescriptionTextView,pdfschoolNameTextView;
        private Button editButton,allowButton,denyButton;
        private LinearLayout imageAndTitleLinearLayout;
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.pdf_notification_request_row,parent,false);

            pdfsenderNameTextView=view.findViewById(R.id.pdfsenderTextView);
            pdftitleTextView=view.findViewById(R.id.pdfNoiceTitleTextView);
            pdfdescriptionTextView=view.findViewById(R.id.pdfNoticedescriptionTextView);
            pdfschoolNameTextView=view.findViewById(R.id.pdfschoolNameTextView);
            editButton=view.findViewById(R.id.editButton);
            allowButton=view.findViewById(R.id.AlowButton);
            denyButton=view.findViewById(R.id.DenyButton);
            imageAndTitleLinearLayout=view.findViewById(R.id.imageandTitleLinearLayout);
            imageAndTitleLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PdfDownloadTask.DownloadData(context,pdfNoticeRequestsArrayList.get(position).
                            getNotices().getImageUrl(),pdfNoticeRequestsArrayList.get(position).getNotices().getTitle());
                    return true;
                }
            });
            pdfnoticeImageView=view.findViewById(R.id.pdfNoticeImageView);
            final Notices notices=pdfNoticeRequestsArrayList.get(position).getNotices();

            pdfsenderNameTextView.setText(notices.getSender());
            pdftitleTextView.setText(notices.getTitle());
            pdfdescriptionTextView.setText(notices.getDescription());
            pdfschoolNameTextView.setText(pdfNoticeRequestsArrayList.get(position).getSchoolName());
            Picasso.get().load(R.drawable.pdf).into(pdfnoticeImageView);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "edit ", Toast.LENGTH_SHORT).show();
                    showEditBoxForNotice(pdfNoticeRequestsArrayList.get(position).getSchoolName(),notices);
                }
            });

            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adminIsSureforDeny(pdfNoticeRequestsArrayList.get(position).getSchoolName(),notices);

                }
            });

            allowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "allow", Toast.LENGTH_SHORT).show();
                    adminIsSureforAllow(pdfNoticeRequestsArrayList.get(position).getSchoolName(),notices);
                }
            });



            return view;
        }
//*********************** for edit a notice************************

    private void showEditBoxForNotice(final String schoolName, final Notices notices) {

            View view=LayoutInflater.from(context).inflate(R.layout.edit_notice,null,false);
            final EditText titleEditText=view.findViewById(R.id.editTitleEditText);
            final EditText descriptionEditText=view.findViewById(R.id.editDescriptionEditText);
           final Spinner schoolNameSpinner=view.findViewById(R.id.editSchoolSpinner);
       SpinnerAdapter spinnerArrayAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item,
                        Initialisation.schools); //selected item will look like a spinner set from XML
        ((ArrayAdapter) spinnerArrayAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolNameSpinner.setAdapter(spinnerArrayAdapter);

        titleEditText.setText(notices.getTitle());
            descriptionEditText.setText(notices.getDescription());

            AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setView(view)
                    .setCancelable(false)
                .setTitle("Edit Notice")
                .setMessage("If you press Yes Notice will be available for all users")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!schoolNameSpinner.getSelectedItem().equals("Select Schools")) {
                            deleteNoticeFromFireBase(schoolName, notices); //deleting notice from request reference
                            Notices newNotice=new Notices(descriptionEditText.getText().toString(),titleEditText.getText().toString(),
                                    notices.getSender(),notices.getImageUrl());
                            alloWNotice(schoolNameSpinner.getSelectedItem().toString(),newNotice);   //adding notice into category.
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(context, "Select School first!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No",null)
                .show();


    }

    // //********************for allow a notice******************


    private void adminIsSureforAllow(final String schoolName, final Notices notices) {

        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Allow Notice")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNoticeFromFireBase(schoolName,notices); //deleting notice from request reference
                        alloWNotice(schoolName,notices);   //adding notice into category.
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",null)
                .show();


    }

    private void alloWNotice(String schoolName, Notices notices) {

            FirebaseDatabase.getInstance().getReference("PdfCategory").child(schoolName).push().setValue(notices).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(context, "You allowed ths notice", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    @Override
    public int getCount() {
        return pdfNoticeRequestsArrayList.size();
    }

    public RequestPdfNoticeArrayAdapter(Context context, ArrayList<NoticeRequest> noticeRequestsArrayList) {
        super(context,R.layout.pdf_notification_request_row,noticeRequestsArrayList);
        this.pdfNoticeRequestsArrayList=noticeRequestsArrayList;
        this.context=context;
    }



    private boolean BothEquals(Notices currentNotice, Notices notices) {

            return currentNotice.getDescription().equals(notices.getDescription()) &&
                    currentNotice.getImageUrl().equals(notices.getImageUrl()) &&
                    currentNotice.getSender().equals(notices.getSender()) &&
                    currentNotice.getTitle().equals(notices.getTitle());

        }



        //********************for deny a notice******************


    private void adminIsSureforDeny(final String schoolName, final Notices notices) {

        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete Notice")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNoticeFromFireBase(schoolName,notices);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",null)
                .show();


    }


    private void deleteNoticeFromFireBase(String schoolName, final Notices notices) {

        FirebaseDatabase.getInstance().getReference("PdfRequests").child(schoolName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot finalDataSnapshot:dataSnapshot.getChildren()){
                    Notices currentNotice=finalDataSnapshot.getValue(Notices.class);
                    if(BothEquals(currentNotice,notices)){
                        finalDataSnapshot.getRef().removeValue();
                        for(int i = 0; i<PdfNotice.pdfNoticeRequestList.size(); i++){
                            if(BothEquals(PdfNotice.pdfNoticeRequestList.get(i).getNotices(),notices)){
                                PdfNotice.pdfNoticeRequestList.remove(i);
                                break;
                            }
                        }
                        PdfNotice.requestPdfNoticeArrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}
