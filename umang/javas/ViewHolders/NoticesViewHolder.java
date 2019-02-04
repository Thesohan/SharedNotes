package com.noticol.thesohankathait.notices.javas.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noticol.thesohankathait.notices.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticesViewHolder extends RecyclerView.ViewHolder {

    public ImageView allNoticeImageView;
    public TextView allNoticeTitleTextView;
    public TextView allNoticeDescriptionTextView;
    public TextView allNoticeSenderTextview;
    public LinearLayout allNotificationlinearLayout;
//    public ProgressBar imageProgressBar;

    public NoticesViewHolder(@NonNull View itemView) {
        super(itemView);
        allNoticeDescriptionTextView=itemView.findViewById(R.id.allNoticedescriptionTextView);
        allNoticeImageView=itemView.findViewById(R.id.allNoticeImageView);
        allNoticeTitleTextView=itemView.findViewById(R.id.allNoiceTitleTextView);
//        imageProgressBar=itemView.findViewById(R.id.imageProgressBar);
        allNoticeSenderTextview=itemView.findViewById(R.id.senderTextView);

        allNotificationlinearLayout=itemView.findViewById(R.id.allNotificationLinearLayout);
//        itemView.setOnCreateContextMenuListener(this);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        contextMenu.setHeaderTitle("Select For Action");
//
//        contextMenu.add(Menu.NONE,Common.R_ID_EDIT, getAdapterPosition(), "Edit");
//        contextMenu.add(Menu.NONE, Common.R_ID_DELETE,getAdapterPosition(),"Delete");
//
//    }
//@Override
//public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//
//    contextMenu.setHeaderTitle("Select an action!");
//    //first argument of the add method is a group, second argument is an id, third order in which you want item to show , foruth title.
//    contextMenu.add(Menu.NONE,Common.R_ID_EDIT, getAdapterPosition(), "Edit");
//    contextMenu.add(Menu.NONE, Common.R_ID_DELETE,getAdapterPosition(),"Delete");
//
//}

}
