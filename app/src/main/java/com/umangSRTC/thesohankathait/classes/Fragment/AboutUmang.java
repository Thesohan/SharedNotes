package com.umangSRTC.thesohankathait.classes.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.Utill.CompressImages;
import com.umangSRTC.thesohankathait.classes.Utill.DeleteFromFirebaseStorage;
import com.umangSRTC.thesohankathait.classes.Utill.DownloadTask;
import com.umangSRTC.thesohankathait.classes.Utill.Equals;
import com.umangSRTC.thesohankathait.classes.ViewHolders.AboutUmangViewHolder;
import com.umangSRTC.thesohankathait.classes.model.AboutUmang_model;
import com.umangSRTC.thesohankathait.umang.R;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class AboutUmang extends Fragment {
    private RecyclerView aboutUmnagRecyclerView;
    private FirebaseRecyclerAdapter<AboutUmang_model,AboutUmangViewHolder> firebaseRecyclerAdapter;
    private Button addAboutButton,uploadButton;
    private ImageView uploadAboutImageView;
    private EditText uploadAboutEditText;
    private Uri imageUri;
    private String imageUrl;
    private ProgressDialog progressDialog;
    private AlertDialog builder;
    private ProgressBar aboutUmangProgressbar;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.about_umang_fragment,container,false);
        aboutUmangProgressbar=view.findViewById(R.id.aboutUmangProgressbar);
        aboutUmnagRecyclerView=view.findViewById(R.id.aboutUmangRecyclerView);
        addAboutButton=view.findViewById(R.id.addAboutButton);
        if(!Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            addAboutButton.setVisibility(View.GONE);
        }

        context=getContext();
        addAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ADD ABOUT
                showUploadDialog();
            }
        });


// set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        aboutUmnagRecyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        aboutUmnagRecyclerView.setHasFixedSize(true);

        fetchAboutFromFirebase();

        return view;
    }


    private void fetchAboutFromFirebase() {

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AboutUmang_model,
                AboutUmangViewHolder>(AboutUmang_model.class,R.layout.about_umang_recycler_view_row,
                AboutUmangViewHolder.class,FirebaseDatabase.getInstance().getReference("AboutUmang")) {
            @Override
            protected void populateViewHolder(AboutUmangViewHolder aboutUmangViewHolder, final AboutUmang_model aboutUmang_model, int i) {

                aboutUmangProgressbar.setVisibility(View.GONE);
                Glide.with(context)
                        .load(aboutUmang_model.getImageUrl())
                        .into(aboutUmangViewHolder.aboutUmangImageView);
               // Picasso.get().load(aboutUmang_model.getImageUrl()).into(aboutUmangViewHolder.aboutUmangImageView);
                aboutUmangViewHolder.aboutUmangTextView.setText(aboutUmang_model.getAbout());

                //show the full image on click
                aboutUmangViewHolder.aboutUmangImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFullImage(aboutUmang_model);
                    }
                });

                if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    aboutUmangViewHolder.aboutUmangImageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showDeleteDialog(aboutUmang_model);

                            return true;
                        }
                    });
                }


            }
        };
        aboutUmnagRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDeleteDialog(final AboutUmang_model aboutUmang_model) {

        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Delete Image")
                .setCancelable(false)
                .setMessage("Do you really want to delete this Image?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromFirebase(aboutUmang_model);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    private void deleteFromFirebase(final AboutUmang_model aboutUmang_model) {

        FirebaseDatabase.getInstance().getReference("AboutUmang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (Equals.BothEqual(aboutUmang_model, dataSnapshot.getValue(AboutUmang_model.class))) {
                    dataSnapshot.getRef().removeValue();
                    DeleteFromFirebaseStorage.deleteByDownloadUrl(getContext(), aboutUmang_model.getImageUrl());
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

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

    private void showFullImage(final AboutUmang_model aboutUmangModel) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.full_image, null, false);
        ImageView imageView = view.findViewById(R.id.aboutNoticeImageView);
        Button imageDownloadButton=view.findViewById(R.id.imageDownloadButton);
        imageDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask downloadTask=new DownloadTask(context,aboutUmangModel.getImageUrl(),aboutUmangModel.getAbout(),"aboutUmang",aboutUmangModel.getFileExtension());
                downloadTask.DownloadData();
            }
        });
        Glide.with(context).load(aboutUmangModel.getImageUrl()).into(imageView);
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext())
                .setView(view)
                .show();
    }




    private void showUploadDialog() {

        View view=LayoutInflater.from(getContext()).inflate(R.layout.upload_about_fragment,null,false);
        uploadAboutImageView=view.findViewById(R.id.uploadImageView);
        uploadAboutEditText=view.findViewById(R.id.aboutEditText);
        uploadButton=view.findViewById(R.id.uploadButton);

        builder=new AlertDialog.Builder(getContext())
                .setView(view)
                .show();

        uploadAboutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allset()){
                    showProgressDialog();
                        uploadImage(imageUri);
                }
                else{
                    Toast.makeText(getContext(), "Please fill all fields first!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getImage() {
        Intent chooseAndSetImageIntent=new Intent(Intent.ACTION_GET_CONTENT);
        chooseAndSetImageIntent.setType("image/*");
        startActivityForResult(chooseAndSetImageIntent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==getActivity().RESULT_OK&&data!=null){
            imageUri=data.getData();
            Glide.with(context).load(imageUri).into(uploadAboutImageView);
        }
    }

    private boolean allset() {
        if(!uploadAboutEditText.getText().toString().trim().equals("")&&imageUri!=null)
            return true;
        else
            return false;
    }


    private void showProgressDialog() {
        progressDialog =new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(R.style.Animation_Design_BottomSheetDialog);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();


    }
    private void uploadImage(Uri imageUri) {
        UUID uuid=UUID.randomUUID();
        CompressImages compressImages=new CompressImages(getContext(),imageUri);
        try {
            byte[] data=compressImages.compress();
            FirebaseStorage.getInstance().getReference().child(uuid.toString()).putBytes(data).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                                    Toast.makeText(getContext(), "file uploaded", Toast.LENGTH_SHORT).show();
                                    imageUrl=task.getResult().toString();
                                    uploadintoFirebase();

                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (IOException e) {
            Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void uploadintoFirebase() {
        progressDialog.dismiss();
       AboutUmang_model aboutUmang_model=new AboutUmang_model(uploadAboutEditText.getText().toString(),imageUrl);


       FirebaseDatabase.getInstance().getReference("AboutUmang").push().setValue(aboutUmang_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), " thankyou!", Toast.LENGTH_SHORT).show();
                    builder.dismiss();
                }
            });
        }

    public static AboutUmang newInstance() {
        
        Bundle args = new Bundle();
        
        AboutUmang fragment = new AboutUmang();
        fragment.setArguments(args);
        return fragment;
    }
}
