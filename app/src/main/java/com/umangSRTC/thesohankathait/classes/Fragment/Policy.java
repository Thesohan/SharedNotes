package com.umangSRTC.thesohankathait.classes.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umangSRTC.thesohankathait.umang.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Policy extends Fragment {

    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.policy_fragment,container,false);
       progressBar=view.findViewById(R.id.progressbar);
       final WebView webView = view.findViewById(R.id.webview);

        FirebaseDatabase.getInstance().getReference("Link").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    webView.setVisibility(View.VISIBLE);
                    String url = dataSnapshot.getValue().toString();
//                    webView.loadUrl("https://www.journaldev.com");
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    webView.loadUrl(url);
                    progressBar.setVisibility(View.GONE);
                }
                catch (Exception e){
                    Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    return view;
    }

    public static Policy newInstance() {
        
        Bundle args = new Bundle();
        
        Policy fragment = new Policy();
        fragment.setArguments(args);
        return fragment;
    }
}
