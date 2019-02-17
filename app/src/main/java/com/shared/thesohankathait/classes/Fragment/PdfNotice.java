package com.shared.thesohankathait.classes.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.shared.thesohankathait.classes.Utill.DeleteFromFirebaseStorage;
import com.shared.thesohankathait.classes.Utill.DownloadTask;
import com.shared.thesohankathait.classes.ViewHolders.PdfNoticesViewHolder;
import com.shared.thesohankathait.classes.model.Notices;
import com.shared.thesohankathait.notices.R;
import com.shared.thesohankathait.classes.Utill.Admin;
import com.shared.thesohankathait.classes.Utill.Equals;
import com.shared.thesohankathait.classes.Utill.Initialisation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PdfNotice extends Fragment {

    private RecyclerView pdfRecyclerView;
  //  private ProgressDialog progressDialog;

    private LinearLayout twoButtonLinearLayout;
    private Button pdfuploadButton,pdfRequestButton;
    private Button addPdfImageButton;

    //private String selectedSchool;
   // private Uri pdfUri=null;
    //private String pdfUrl;
//    private int PICK_PDF_CODE = 10000;
//    private  EditText titleEditText,
//            descriptionEditText;

//    private ImageView imageView;
//    private  Button uploadButton;
//    private  Spinner spinner;
//    private SpinnerAdapter spinnerArrayAdapter;
//
//    private String fileExtension="pdf";
//    private AlertDialog uploadDialogBuilder;
//    private  ListView requestPdfListView;
//    public static ArrayList<NoticeRequest> pdfNoticeRequestList;
    private FirebaseRecyclerAdapter<Notices,PdfNoticesViewHolder> firebaseRecyclerAdapter;
//    public static RequestPdfNoticeArrayAdapter requestPdfNoticeArrayAdapter;

    private ProgressBar pdfProgressbar,pdfRequestProgressbar;
    private Context context;

    private InterstitialAd mInterstitialAd;  //ads


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_notice, container, false);

        context=getContext();

        pdfProgressbar=view.findViewById(R.id.pdfProgressbar);

      //initialising
            MobileAds.initialize(getContext(),"ca-app-pub-3940256099942544~3347511713");
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));//modify the ad id from string resources
            //loading  an ad
           mInterstitialAd.loadAd(new AdRequest.Builder().build());

        pdfRecyclerView = view.findViewById(R.id.pdfRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);//it will set the recycler view to show the elements in bottom up manner.
        linearLayoutManager.setStackFromEnd(true);//it will show the last element first.
        pdfRecyclerView.setLayoutManager(linearLayoutManager);
        pdfRecyclerView.setHasFixedSize(true);

        addPdfImageButton = view.findViewById(R.id.addPdfFloatingActionButton);
        twoButtonLinearLayout=view.findViewById(R.id.twoButtonsLinearLayout);
        pdfRequestButton=view.findViewById(R.id.pdfRequestButton);
        pdfuploadButton=view.findViewById(R.id.pdfuploadButton);

        String schoolName=getArguments().getString("SCHOOL_NAME");

        if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            addPdfImageButton.setVisibility(View.GONE);
        }
        else{
            twoButtonLinearLayout.setVisibility(View.GONE);
        }

        addPdfImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showUploadDialogForPdf();
            }
        });

        pdfuploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadDialogForPdf();
            }
        });

        pdfRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPdfPostRequest();
            }
        });

        FetchPdfNoticeFromFirebase(schoolName);

        return view;
    }

    private void FetchPdfNoticeFromFirebase(final String schoolName) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notices, PdfNoticesViewHolder>(Notices.class, R.layout.pdf_notification_row, PdfNoticesViewHolder.class, FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfCategory").child(schoolName)) {
            @Override
            protected void populateViewHolder(PdfNoticesViewHolder pdfNoticesViewHolder, final Notices notices, int i) {

//                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;//to generate random colors
//                pdfNoticesViewHolder.pdfNoticeTitleTextView.setTextColor(colorGenerator.getRandomColor());

                pdfProgressbar.setVisibility(View.GONE);
                pdfNoticesViewHolder.pdfNoticeTitleTextView.setText(notices.getTitle());
                pdfNoticesViewHolder.pdfNoticeDescriptionTextView.setText(notices.getDescription());
                Glide.with(context).load(R.drawable.pdf).into(pdfNoticesViewHolder.pdfNoticeImageView);


                String sender="- "+notices.getSender();
                pdfNoticesViewHolder.pdfNoticeSenderTextview.setText(sender);

                pdfNoticesViewHolder.pdfSchoolNameTextView.setText(schoolName);

                if(!Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    pdfNoticesViewHolder.pdfDeleteButton.setVisibility(View.GONE);

                pdfNoticesViewHolder.pdfDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteWarning(schoolName,notices);
                    }
                });

                pdfNoticesViewHolder.pdfNotificationlinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DownloadTask downloadTask =new DownloadTask(context,notices.getImageUrl(),notices.getTitle(),schoolName,notices.getFileExtension());
                        downloadTask.DownloadData();


                        //if ad is loaded than show it if user download a pdf
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                           // Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
//
                        //download pdf
                    }
                });


            }
        };
        pdfRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDeleteWarning(final String schoolName, final Notices notices) {

        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(context)
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Do you really want to delete this notice?")
                .setTitle("Delete Notice")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfCategory").child(schoolName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (Equals.BothEquals(notices, dataSnapshot.getValue(Notices.class))) {
                    dataSnapshot.getRef().removeValue();
                    DeleteFromFirebaseStorage.deleteByDownloadUrl(context,notices.getImageUrl());
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
    private void showPdfPostRequest() {

        FragmentManager fragmentManager = getFragmentManager();
        PdfPostRequest pdfPostRequest=PdfPostRequest.newInstance();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content,pdfPostRequest).addToBackStack("faf").commit();


      /*  View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pdf_notice_request, null, false);
        ArrayList<NoticeRequest> pdfNoticeRequests=new ArrayList<>();
        requestPdfListView = view.findViewById(R.id.requestPdfListView);
        pdfRequestProgressbar=view.findViewById(R.id.pdfNoticeProgressbar);


        pdfNoticeRequestList=new ArrayList<>();
        requestPdfNoticeArrayAdapter=new RequestPdfNoticeArrayAdapter(getContext(),pdfNoticeRequestList);
        requestPdfListView.setAdapter(requestPdfNoticeArrayAdapter);


        AlertDialog builder=new AlertDialog.Builder(getContext())
                .setView(view).show();

        fetchAllPdfNoticeRequest();
*/


    }

    /*

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
    */

    private void showUploadDialogForPdf() {

        FragmentManager fragmentManager = getFragmentManager();
        UploadPdf uploadPdf=UploadPdf.newInstance();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content,uploadPdf).addToBackStack("faf").commit();


        /*
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pdf_upload_fragment, null, false);

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


        uploadDialogBuilder=new AlertDialog.Builder(context)
                .setView(view).show();

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
        });*/

    }

//    private void uploadPdf(Uri pdfUri) {
//
//        UUID uuid=UUID.randomUUID();
//        FirebaseStorage.getInstance().getReference().child(uuid.toString()).putFile(pdfUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                progressDialog.setMessage("please wait..."+((100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount())+"%");
//
//            }
//        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if(task.isSuccessful()){
//                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if(task.isSuccessful()) {
//                                Toast.makeText(context, "file uploaded", Toast.LENGTH_SHORT).show();
//                                pdfUrl =task.getResult().toString();
//                                uploadintoFirebase();
//
//                            }
//                            else{
//                                progressDialog.dismiss();
//                                Toast.makeText(context, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//                else{
//                    progressDialog.dismiss();
//                    Toast.makeText(context, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    private void uploadintoFirebase() {
//        progressDialog.dismiss();
//        Notices notices=new Notices(descriptionEditText.getText().toString(),titleEditText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),pdfUrl);
//        notices.setFileExtension(fileExtension);
//        if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
//            FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfCategory").child(selectedSchool).push().setValue(notices).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(context, "notice send", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else{
//            FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/PdfRequests").child(selectedSchool).push().setValue(notices).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(context, "Your NoticeRequest is send to admin, thankyou!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        uploadDialogBuilder.dismiss();
//    }
//
//    private boolean allSet() {
//        if(!spinner.getSelectedItem().toString().equals("Select Schools") &&
//                !titleEditText.getText().toString().trim().equals("") &&
//                !descriptionEditText.getText().toString().trim().equals("")&&
//                pdfUri!=null)
//            return true;
//        else
//            return false;
//
//    }
//    private void showProgressDialog() {
//        progressDialog =new ProgressDialog(context);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setProgressStyle(R.style.Animation_Design_BottomSheetDialog);
//        progressDialog.setProgress(0);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//
//    }
//    private void getPdf() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("*/*");
//        //need to send mimetypes for some devices (read from stackoverflow not sure
//
//        String[] mimetypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
//                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
//                "text/plain",
//                "application/pdf",
//                "application/zip"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//        try {
//            startActivityForResult(intent, PICK_PDF_CODE);
//
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//
//            Toast.makeText(getContext(), "Please install a File Manager.",
//                    Toast.LENGTH_SHORT).show();
//
//        }
//    }

 /*   @Override
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

*/

    public static PdfNotice newInstance(String schoolName) {

        Bundle args = new Bundle();
        args.putString("SCHOOL_NAME",schoolName);
        PdfNotice fragment = new PdfNotice();
        fragment.setArguments(args);
        return fragment;
    }

}