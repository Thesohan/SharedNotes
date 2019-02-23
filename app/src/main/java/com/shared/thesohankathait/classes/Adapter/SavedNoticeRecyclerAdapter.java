package com.shared.thesohankathait.classes.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.shared.thesohankathait.classes.ViewHolders.SavedNoticeViewHolder;
import com.shared.thesohankathait.classes.database.DbHelper;
import com.shared.thesohankathait.classes.model.NoticeRequest;
import com.shared.thesohankathait.classes.model.Notices;
import com.shared.thesohankathait.notices.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedNoticeRecyclerAdapter extends RecyclerView.Adapter<SavedNoticeViewHolder> {
    ArrayList<NoticeRequest> noticeRequestsArraList;
    Context context;

    public SavedNoticeRecyclerAdapter() {
    }

    public SavedNoticeRecyclerAdapter(ArrayList<NoticeRequest> noticeRequestsArraList, Context context) {
        this.noticeRequestsArraList = noticeRequestsArraList;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedNoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.saved_notice_row,parent,false);

        return new SavedNoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedNoticeViewHolder holder, final int position) {

        final Notices notices=noticeRequestsArraList.get(position).getNotices();
        holder.schoolNameTextView.setText(noticeRequestsArraList.get(position).getSchoolName());
        holder.allNoticeDescriptionTextView.setText(notices.getDescription());
        if(notices.getFileExtension().equals("JPEG")) {
            Glide.with(context).load(notices.getImageUrl()).into(holder.allNoticeImageView);
        }
        else {
            Glide.with(context).load(R.drawable.shared_notices).into(holder.allNoticeImageView);
        }
        if(notices.getLink()!=null) {
            holder.linkTextView.setText(notices.getLink());
        }
        else{
            holder.linkTextView.setVisibility(View.GONE);
        }

        holder.linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO TO ADDRESS
                Uri uri = Uri.parse(holder.linkTextView.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null)
                {
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "This isn't a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String sender="- "+notices.getSender();
        holder.allNoticeSenderTextview.setText(sender);
        holder.allNoticeTitleTextView.setText(notices.getTitle());

//        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;//to generate random colors
//        holder.allNoticeTitleTextView.setTextColor(colorGenerator.getRandomColor());

        holder.allNoticeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImage(noticeRequestsArraList.get(position).getSchoolName(),notices);
            }
        });

        holder.allNotificationlinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteWarning(noticeRequestsArraList.get(position));
                return true;
            }
        });
    }

    private void showDeleteWarning(final NoticeRequest noticeRequest) {

        new AlertDialog.Builder(context).setTitle("Delete")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage("Please make sure that you want to delete this notice.")
                .setCancelable(false)
                .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DbHelper dbHelper=new DbHelper(context);
                        dbHelper.deleteNotice(noticeRequest);
                        dbHelper.close();
                        noticeRequestsArraList.remove(noticeRequest);
                        notifyDataSetChanged();
                    }
                })

                .setNegativeButton("CANCEL",null).show();
    }

    @Override
    public int getItemCount() {
        return noticeRequestsArraList.size();
    }

    private void showFullImage(final String schoolName, final Notices notices) {

        View view = LayoutInflater.from(context).inflate(R.layout.full_image, null, false);
        ImageView imageView = view.findViewById(R.id.aboutNoticeImageView);
        Button imageDownloadButton=view.findViewById(R.id.imageDownloadButton);
        imageDownloadButton.setVisibility(View.GONE);
        Glide.with(context).load(notices.getImageUrl()).into(imageView);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();

    }
}
