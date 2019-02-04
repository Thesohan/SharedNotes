package com.noticol.thesohankathait.classes.Utill;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;

public class DeleteFromFirebaseStorage {
    public static void deleteByDownloadUrl(final Context context, String url){
        if(url!=null) {

            FirebaseStorage.getInstance().getReferenceFromUrl(url).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("exception",task.getException().toString());
                        Toast.makeText(context, "Error while deleting "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
