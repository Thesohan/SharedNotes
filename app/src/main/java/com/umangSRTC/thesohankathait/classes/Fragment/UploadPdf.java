package com.umangSRTC.thesohankathait.classes.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.umangSRTC.thesohankathait.classes.Adapter.RequestPdfNoticeArrayAdapter;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.Utill.FileExtension;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
import com.umangSRTC.thesohankathait.classes.ViewHolders.PdfNoticesViewHolder;
import com.umangSRTC.thesohankathait.classes.model.AdminProfile;
import com.umangSRTC.thesohankathait.classes.model.NoticeRequest;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class UploadPdf extends DialogFragment {


    private ProgressDialog progressDialog;


    private String selectedSchool;
    private Uri pdfUri=null;
    private String pdfUrl;
    private int PICK_PDF_CODE = 10000;
    private EditText titleEditText,
            descriptionEditText;

    private ImageView imageView;
    private  Button uploadButton;
    private Spinner spinner;
    private SpinnerAdapter spinnerArrayAdapter;

    private String fileExtension="pdf";
    private Context context;
    private ImageView myImage;
    private TextView myName,myDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view =inflater.inflate(R.layout.pdf_upload_fragment, null, false);
        context=getContext();

        myImage=view.findViewById(R.id.myImage);
        myName=view.findViewById(R.id.myName);
        myDescription=view.findViewById(R.id.myDescription);
        imageView=view.findViewById(R.id.uploadImageView);
        setMyprofile();


        TextView selectAPdf =view.findViewById(R.id.selectApdf);
        selectAPdf.setText("Select A pdf");

        imageView = view.findViewById(R.id.pdfuploadImageView);
        titleEditText= view.findViewById(R.id.pdftitleEditText);
        descriptionEditText = view.findViewById(R.id.pdfdescriptionEditText);
        uploadButton = view.findViewById(R.id.pdfuploadButton);
        spinner = view.findViewById(R.id.pdfspinner);

        // Collections.sort(Initialisation.schools.subList(1,Initialisation.schools.size()));
        spinnerArrayAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item,
                        Initialisation.schools); //selected item will look like a spinner set from XML
        ((ArrayAdapter) spinnerArrayAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPdf();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allSet()) {
                    selectedSchool = spinner.getSelectedItem().toString();
                    showProgressDialog();
                    uploadPdf(pdfUri);

                } else {
                    Toast.makeText(context, "Please fill all fields first!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void setMyprofile() {

        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege).child("AdminProfile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdminProfile adminProfile=dataSnapshot.getValue(AdminProfile.class);
//                Log.d("name",adminProfile.getName());

                if(adminProfile!=null){
                Glide.with(context).load(adminProfile.getImageUrl()).into(myImage);
                myName.setText(adminProfile.getName());
                myDescription.setText(adminProfile.getDescription());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadPdf(Uri pdfUri) {

        UUID uuid=UUID.randomUUID();
        FirebaseStorage.getInstance().getReference().child(uuid.toString()).putFile(pdfUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setMessage("please wait..."+((100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount())+"%");

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(context, "file uploaded", Toast.LENGTH_SHORT).show();
                                pdfUrl =task.getResult().toString();
                                uploadintoFirebase();

                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(context, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadintoFirebase() {
        progressDialog.dismiss();
        Notices notices=new Notices(descriptionEditText.getText().toString(),titleEditText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),pdfUrl);
        notices.setFileExtension(fileExtension);
        if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfCategory").child(selectedSchool).push().setValue(notices).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "notice send", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }
        else{
            FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfRequests").child(selectedSchool).push().setValue(notices).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Your NoticeRequest is send to admin, thankyou!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }

//        uploadDialogBuilder.dismiss();
    }

    private boolean allSet() {
        if(!spinner.getSelectedItem().toString().equals("Select Schools") &&
                !titleEditText.getText().toString().trim().equals("") &&
                !descriptionEditText.getText().toString().trim().equals("")&&
                pdfUri!=null)
            return true;
        else
            return false;

    }
    private void showProgressDialog() {
        progressDialog =new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(R.style.Animation_Design_BottomSheetDialog);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();


    }
    private void getPdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        //need to send mimetypes for some devices (read from stackoverflow not sure

        String[] mimetypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain",
                "application/pdf",
                "application/zip"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        try {
            startActivityForResult(intent, PICK_PDF_CODE);

        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog

            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                pdfUri=data.getData();
                fileExtension=FileExtension.getMimeType(getContext(),pdfUri);
            } else {
                Toast.makeText(context, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public static UploadPdf newInstance() {
        
        Bundle args = new Bundle();
        UploadPdf fragment = new UploadPdf();
        fragment.setArguments(args);
        return fragment;
    }

}