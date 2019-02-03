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
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.model.College;
import com.umangSRTC.thesohankathait.umang.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class RequestCollegeName extends DialogFragment {

    private Context context;

    private College college=null;
    private Button sendRequestButton;
    private EditText emailEditText,phoneEditText,collgeNameEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        View view = inflater.inflate(R.layout.request_to_add_college, container, false);
        context=getContext();
        emailEditText=view.findViewById(R.id.email_editText);
        phoneEditText=view.findViewById(R.id.phoneEditText);
        collgeNameEditText=view.findViewById(R.id.CollegeNameEdittext);
        sendRequestButton=view.findViewById(R.id.sendRequestButton);

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "sdf", Toast.LENGTH_SHORT).show();
                addCollegeRequest();
            }
        });

        return view;
    }

    private void addCollegeRequest() {

        if (checkAllFieldsFilled()) {
            college = new College(collgeNameEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), phoneEditText.getText().toString().trim());
            FirebaseDatabase.getInstance().getReference("CollegeRequests").push().setValue(college);
            showDialog();
        }
    }

    private void showDialog() {

        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setTitle("Your request is sent, you will be notified when your request get accepted\n ThankYou!")
                .show();

    }

    private boolean checkAllFieldsFilled() {

        if(collgeNameEditText.getText().toString().trim().equals("")&& Admin.isCorrect(collgeNameEditText.getText().toString())){
            collgeNameEditText.setError("Please enter College name ");
            return false;
        }
        if(phoneEditText.getText().toString().trim().equals("")){
            phoneEditText.setError("Please enter phone no.");
            return false;
        }
        if(emailEditText.getText().toString().trim().equals("")){
            emailEditText.setError("Please enter email");
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


    public static RequestCollegeName newInstance() {
        
        Bundle args = new Bundle();
        RequestCollegeName fragment = new RequestCollegeName();
        fragment.setArguments(args);
        return fragment;
    }

}