package com.noticol.thesohankathait.notices.javas.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.noticol.thesohankathait.notices.R;
import com.noticol.thesohankathait.notices.javas.Activity.Functionality;
import com.noticol.thesohankathait.notices.javas.Utill.Initialisation;
import com.noticol.thesohankathait.notices.javas.Utill.Admin;
import com.noticol.thesohankathait.notices.javas.model.Notices;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Upload extends Fragment {

    private ImageView imageView;
    private EditText titleEditText,descriptionEditText;
    private Button uploadButton;
    private Spinner spinner;
    private Context context;
    private Uri imageUri=null;
    private ProgressDialog progressDialog;
    private String imageURl;
    private String selectedSchool=null;
    private ArrayAdapter<String>  spinnerArrayAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.upload_fragment,container,false);

        context=getContext();
        imageView=view.findViewById(R.id.uploadImageView);
        titleEditText=view.findViewById(R.id.titleEditText);
        descriptionEditText=view.findViewById(R.id.descriptionEditText);
        uploadButton=view.findViewById(R.id.uploadButton);
        spinner=view.findViewById(R.id.spinner);


       // Collections.sort(Initialisation.schools.subList(1,Initialisation.schools.size()));
        spinnerArrayAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item,
                        Initialisation.schools); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allSet()){
                    selectedSchool=spinner.getSelectedItem().toString();
                    showProgressDialog();
                    if(imageUri!=null)
                        uploadImage(imageUri);
                    else{
                        uploadintoFirebase();
                    }
                }
                else{
                    Toast.makeText(context, "Please fill all fields first!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
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
                Picasso.get().load(imageUri).into(imageView);
            }
        }

    private boolean allSet() {
        if(!spinner.getSelectedItem().toString().equals("Select Schools") &&
                !titleEditText.getText().toString().trim().equals("") &&
                !descriptionEditText.getText().toString().trim().equals(""))
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
        FirebaseStorage.getInstance().getReference().child(uuid.toString()).putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                                imageURl =task.getResult().toString();
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
    }

    private void uploadintoFirebase() {
        progressDialog.dismiss();
        Notices notices=new Notices(descriptionEditText.getText().toString(),titleEditText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),imageURl);

        if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            FirebaseDatabase.getInstance().getReference("Category").child(selectedSchool).push().setValue(notices).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "notice send", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            FirebaseDatabase.getInstance().getReference("Requests").child(selectedSchool).push().setValue(notices).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Your NoticeRequest is send to admin, thankyou!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        goToFirstPage();
    }

    private void goToFirstPage() {
    Intent intent=new Intent(context,Functionality.class);
    startActivity(intent);
    }


    public static Upload newInstance() {
        
        Bundle args = new Bundle();
        
        Upload fragment = new Upload();
        fragment.setArguments(args);
        return fragment;
    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed()) {
//
//            spinnerArrayAdapter.clear();
//
//           SpinnerAdapter spinnerArrayAdapter = new ArrayAdapter<String>
//                    (context, android.R.layout.simple_spinner_item,
//                            Initialisation.schools); //selected item will look like a spinner set from XML
//            ((ArrayAdapter) spinnerArrayAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(spinnerArrayAdapter);
//            //do your Stuffs
//        }
//    }
}
