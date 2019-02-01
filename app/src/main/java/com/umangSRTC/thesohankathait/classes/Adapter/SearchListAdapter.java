package com.umangSRTC.thesohankathait.classes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umangSRTC.thesohankathait.classes.model.ChatMessage;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.List;

import androidx.annotation.NonNull;


public class SearchListAdapter extends ArrayAdapter<Notices> {

    private List<Notices> searchedNotices;
    private  Context context;
    private static final int PDF_NOTICE=1,IMAGE_NOTICE=2;
    public SearchListAdapter(Context context, List<Notices> searchedNotices) {
        super(context, R.layout.all_notification_row, searchedNotices);
        this.context=context;
        this.searchedNotices=searchedNotices;
    }
    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Notices item = getItem(position);
        if(item.getFileExtension()!=null) {
            if (item.getFileExtension().equals("JPEG")) return IMAGE_NOTICE;
            else return PDF_NOTICE;
        }
        else{
            return IMAGE_NOTICE;
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType ==PDF_NOTICE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pdf_notification_row, parent, false);


         ImageView pdfNoticeImageView;
         TextView pdfNoticeTitleTextView;
         TextView pdfNoticeDescriptionTextView;
         TextView pdfNoticeSenderTextview;
         TextView pdfSchoolNameTextView;
         Button pdfDeleteButton;

         pdfSchoolNameTextView=convertView.findViewById(R.id.pdfschoolNameTextView);
         pdfNoticeDescriptionTextView=convertView.findViewById(R.id.pdfNoticedescriptionTextView);
         pdfNoticeImageView=convertView.findViewById(R.id.pdfNoticeImageView);
         pdfNoticeTitleTextView=convertView.findViewById(R.id.pdfNoiceTitleTextView);
         pdfNoticeSenderTextview=convertView.findViewById(R.id.pdfsenderTextView);
         pdfDeleteButton=convertView.findViewById(R.id.deletepdfButton);

         pdfNoticeImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //DOWNLOAD THE NOTICE
             }
         });

         pdfNoticeTitleTextView.setText(searchedNotices.get(position).getTitle());
         pdfNoticeDescriptionTextView.setText(searchedNotices.get(position).getDescription());
         pdfNoticeSenderTextview.setText(searchedNotices.get(position).getSender());
         pdfSchoolNameTextView.setVisibility(View.GONE);
         pdfDeleteButton.setVisibility(View.GONE);

        } else if (viewType == IMAGE_NOTICE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_notification_row, parent, false);

            ImageView allNoticeImageView;
            TextView allNoticeTitleTextView;
            TextView allNoticeSenderTextview;
            LinearLayout allNotificationlinearLayout;
                allNoticeImageView=convertView.findViewById(R.id.allNoticeImageView);
                allNoticeTitleTextView=convertView.findViewById(R.id.allNoiceTitleTextView);
                allNoticeSenderTextview=convertView.findViewById(R.id.senderTextView);
                allNotificationlinearLayout=convertView.findViewById(R.id.allNotificationLinearLayout);

            Glide.with(context).load(searchedNotices.get(position).getImageUrl()).into(allNoticeImageView);
            allNoticeSenderTextview.setText(searchedNotices.get(position).getSender());
            allNoticeTitleTextView.setText(searchedNotices.get(position).getTitle());
            allNotificationlinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GO TO THE FULL SCREEN FRAGMENT
                }
            });
//
        }

        return convertView;
    }

}
