package com.shared.thesohankathait.classes.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shared.thesohankathait.classes.Fragment.FullScreenDialogFragment;
import com.shared.thesohankathait.classes.Utill.DownloadTask;
import com.shared.thesohankathait.classes.model.Notices;
import com.shared.thesohankathait.notices.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class SearchListAdapter extends ArrayAdapter<Notices> {

    private List<Notices> searchedNotices;
    private List<Notices> origData;
    private Context context;
    private static final int PDF_NOTICE = 1, IMAGE_NOTICE = 2;

    public SearchListAdapter(Context context, List<Notices> searchedNotices) {
        super(context, R.layout.all_notification_row, searchedNotices);
        this.context = context;
        this.searchedNotices = searchedNotices;
        this.origData = searchedNotices;
    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        //according to me it should be 2 but while using 2 in is giving array index outofbond error so i have used 3
        return 3;
    }

    @Override
    public int getCount() {
        return searchedNotices.size();
    }

    @Override
    public int getItemViewType(int position) {
        try {

            Notices item = getItem(position);
            Log.d("item", item.getTitle());
            assert item != null;
            if (item.getFileExtension() != null) {
                if (item.getFileExtension().equals("JPEG")) return IMAGE_NOTICE;
                else return PDF_NOTICE;
            } else {
                return PDF_NOTICE;
            }
        } catch (Exception e) {
            return IMAGE_NOTICE;
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == PDF_NOTICE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pdf_notification_row, parent, false);

            ImageView pdfNoticeImageView;
            TextView pdfNoticeTitleTextView;
            TextView pdfNoticeDescriptionTextView;
            TextView pdfNoticeSenderTextview;
            TextView pdfSchoolNameTextView;
            Button pdfDeleteButton;
            LinearLayout pdfNotificationLinearLayout;

            pdfNotificationLinearLayout=convertView.findViewById(R.id.pdfimageandTitleLinearLayout);
            pdfSchoolNameTextView = convertView.findViewById(R.id.pdfschoolNameTextView);
            pdfNoticeDescriptionTextView = convertView.findViewById(R.id.pdfNoticedescriptionTextView);
            pdfNoticeImageView = convertView.findViewById(R.id.pdfNoticeImageView);
            pdfNoticeTitleTextView = convertView.findViewById(R.id.pdfNoiceTitleTextView);
            pdfNoticeSenderTextview = convertView.findViewById(R.id.pdfsenderTextView);
            pdfDeleteButton = convertView.findViewById(R.id.deletepdfButton);

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

            pdfNotificationLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DownloadTask downloadTask =new DownloadTask(context,searchedNotices.get(position).imageUrl,searchedNotices.
                            get(position).getTitle(),"searched",searchedNotices.get(position).fileExtension);
                    downloadTask.DownloadData();

                }
            });

        } else if (viewType == IMAGE_NOTICE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_notification_row, parent, false);

            ImageView allNoticeImageView;
            final TextView allNoticeTitleTextView;
            TextView allNoticeSenderTextview;
            LinearLayout allNotificationlinearLayout;
            allNoticeImageView = convertView.findViewById(R.id.allNoticeImageView);
            allNoticeTitleTextView = convertView.findViewById(R.id.allNoiceTitleTextView);
            allNoticeSenderTextview = convertView.findViewById(R.id.senderTextView);
            allNotificationlinearLayout = convertView.findViewById(R.id.allNotificationLinearLayout);

            Glide.with(context).load(searchedNotices.get(position).getImageUrl()).into(allNoticeImageView);
            allNoticeSenderTextview.setText(searchedNotices.get(position).getSender());
            allNoticeTitleTextView.setText(searchedNotices.get(position).getTitle());
            Log.d("ttle", searchedNotices.get(position).getTitle());
//            allNotificationlinearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //GO TO THE FULL SCREEN FRAGMENT
//                    Toast.makeText(context, allNoticeTitleTextView.getText().toString(), Toast.LENGTH_SHORT).show();
//                    ((AppCompatActivity)context).getSupportFragmentManager()
//                }
//            });
//
            allNoticeTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullNotices("searched",searchedNotices.get(position));
                }
            });


        }

        return convertView;
    }
    private void showFullNotices(String schoolName, Notices notices) {


        //show full dialog fragment

        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        FullScreenDialogFragment fullScreenDialogFragment = FullScreenDialogFragment.newInstance(schoolName,notices);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, fullScreenDialogFragment).addToBackStack("faf").commit();

    }

    /*
    Filter has two major components i.e performFiltering(..) method and publishResults(..) method.
            performFiltering() method creates a new Arraylist with filtered data.
    publishResults() method is for notify data set changed.*/

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    List<Notices> filterList = new ArrayList<Notices>();
                    for (int i = 0; i < origData.size(); i++) {
                        if ((origData.get(i).getTitle().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            Notices notices = new Notices(origData.get(i)
                                    .getDescription(), origData.get(i)
                                    .getTitle(), origData.get(i).getSender(), origData.get(i).getImageUrl());
                            filterList.add(notices);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = origData.size();
                    results.values = origData;
                }
                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                searchedNotices = (ArrayList<Notices>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
