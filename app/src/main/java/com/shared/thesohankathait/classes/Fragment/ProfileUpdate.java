package com.shared.thesohankathait.classes.Fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.shared.thesohankathait.classes.Utill.CompressImages;
import com.shared.thesohankathait.classes.Utill.Initialisation;
import com.shared.thesohankathait.classes.model.AdminProfile;
import com.shared.thesohankathait.notices.R;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ProfileUpdate extends DialogFragment {

    private Context context;
    private EditText nameEditText,descriptionEditText;
    private Button uploadButton;
    private ImageView imageView;
    private Uri imageUri=null;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        View view = inflater.inflate(R.layout.profile_upload_fragment, container, false);
        context=getContext();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        nameEditText=view.findViewById(R.id.nameEditText);
        descriptionEditText=view.findViewById(R.id.descriptionEditText);
        imageView=view.findViewById(R.id.uploadImageView);
        uploadButton=view.findViewById(R.id.uploadButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET IMAGE;
                getImage();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllFieldsFilled()){
                 //upload the content
                 uploadImage(imageUri);
                }
            }
        });

        return view;
    }

    private void getImage() {
            Intent chooseAndSetImageIntent=new Intent(Intent.ACTION_GET_CONTENT);
            chooseAndSetImageIntent.setType("image/*");
            startActivityForResult(chooseAndSetImageIntent,2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==getActivity().RESULT_OK&&data!=null){
            imageUri=data.getData();
            Glide.with(context).load(imageUri).into(imageView);
        }
    }



    private boolean checkAllFieldsFilled() {

        if(imageUri==null){
            Toast.makeText(context, "Select an image first!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(nameEditText.getText().toString().trim().equals("")){
            nameEditText.setError("Please enter your name ");
            return false;
        }
        if(descriptionEditText.getText().toString().trim().equals("")){
            descriptionEditText.setError("Please enter description");
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public static ProfileUpdate newInstance() {
        
        Bundle args = new Bundle();
        ProfileUpdate fragment = new ProfileUpdate();
        fragment.setArguments(args);
        return fragment;
    }

    private void uploadImage(Uri imageUri) {
        progressDialog.show();
        UUID uuid=UUID.randomUUID();
        CompressImages compressImages=new CompressImages(context,imageUri);
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
                                    Toast.makeText(context, "file uploaded", Toast.LENGTH_SHORT).show();
                                    uploadintoFirebase(task.getResult().toString());
                                    progressDialog.dismiss();

                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(context, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void uploadintoFirebase(String imageurl) {
        AdminProfile adminProfile=new AdminProfile(nameEditText.getText().toString(),descriptionEditText.getText().toString(),imageurl);
        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege).child("AdminProfile").setValue(adminProfile);
    }
}