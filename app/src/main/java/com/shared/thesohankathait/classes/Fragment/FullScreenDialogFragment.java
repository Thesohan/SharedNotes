package com.shared.thesohankathait.classes.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.shared.thesohankathait.classes.Utill.DownloadTask;
import com.shared.thesohankathait.classes.database.DbHelper;
import com.shared.thesohankathait.classes.model.NoticeRequest;
import com.shared.thesohankathait.classes.model.Notices;
import com.shared.thesohankathait.notices.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class FullScreenDialogFragment extends DialogFragment {

    private Button saveButton,downloadButton;
    private TextView descriptionTextView,senderNameTextView,titleTextView,schoolNameTextView,linkTextView;
    private ImageView noticeImageView;
    private ImageButton closeButton;
    private ProgressBar progressBar;
    private Context context;

    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_screen_notification, container, false);
        context=getContext();

//        initialising
        MobileAds.initialize(getContext(),"ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));//modify the ad id from string resources
        //loading  an ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        progressBar=view.findViewById(R.id.fullScreenProgressbar);
        saveButton=view.findViewById(R.id.save_button);
        closeButton=view.findViewById(R.id.button_close);
        downloadButton=view.findViewById(R.id.imageDownloadButton);
        descriptionTextView=view.findViewById(R.id.fullDescription);
        schoolNameTextView=view.findViewById(R.id.schoolNamesTextView);
        saveButton=view.findViewById(R.id.save_button);
        senderNameTextView=view.findViewById(R.id.senderNameTextView);
        linkTextView=view.findViewById(R.id.linkTextView);



        titleTextView=view.findViewById(R.id.dialogTitleTextView);
        titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleTextView.setSingleLine(true);
        titleTextView.setSelected(true);



        noticeImageView=view.findViewById(R.id.fullScreenNoticeImageView);

        //getting bundle passed from the notification fragments;
        Bundle args=getArguments();
        final Notices notices= (Notices) args.getSerializable("NOTICE");
        final String schoolName=args.getString("SCHOOL_NAME");

        setDialogWithContent(schoolName,notices);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the notice into local database;

                DbHelper dbHelper=new DbHelper(context);
                NoticeRequest noticeRequest=new NoticeRequest(schoolName,notices);
                dbHelper.saveNotice(noticeRequest);
                dbHelper.close();

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //dismiss this dialog
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DOWNLOAD THIS IMAGE

                DownloadTask downloadTask=new DownloadTask(context,notices.getImageUrl(),notices.getTitle(),schoolName,notices.getFileExtension());
                downloadTask.DownloadData();
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
                else{
//                    Log.d("sdfa","asdfa");
                }

            }
        });

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //INTENT TO CHROME OR ANY BROWSER


                Uri uri = Uri.parse(linkTextView.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "This isn't a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    private void setDialogWithContent(String schoolName, Notices notices) {

        titleTextView.setText(notices.getTitle());

        if(notices.getImageUrl()==null){
            progressBar.setVisibility(View.GONE);
        }

        Glide.with(context).load(notices.getImageUrl()).into(noticeImageView);

        descriptionTextView.setText(notices.getDescription());

        schoolNameTextView.setText(schoolName);

        String sender="- "+notices.getSender();
        senderNameTextView.setText(sender);

        if(notices.getLink()!=null) {
            linkTextView.setText(notices.getLink());
        }
        else{
            linkTextView.setVisibility(View.GONE);
        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public static FullScreenDialogFragment newInstance(String schoolName, Notices notices) {
        
        Bundle args = new Bundle();
        args.putString("SCHOOL_NAME",schoolName);
        args.putSerializable("NOTICE",notices);
        FullScreenDialogFragment fragment = new FullScreenDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}