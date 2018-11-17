package com.umangSRTC.thesohankathait.umang.javas.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umangSRTC.thesohankathait.umang.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PdfNoticesViewHolder extends RecyclerView.ViewHolder {

    public ImageView pdfNoticeImageView;
    public TextView pdfNoticeTitleTextView;
    public TextView pdfNoticeDescriptionTextView;
    public TextView pdfNoticeSenderTextview;
    public TextView pdfSchoolNameTextView;
    public LinearLayout pdfNotificationlinearLayout;
//    public ProgressBar imageProgressBar;

    public PdfNoticesViewHolder(@NonNull View itemView) {
        super(itemView);
        pdfSchoolNameTextView=itemView.findViewById(R.id.pdfschoolNameTextView);
        pdfNoticeDescriptionTextView=itemView.findViewById(R.id.pdfNoticedescriptionTextView);
        pdfNoticeImageView=itemView.findViewById(R.id.pdfNoticeImageView);
        pdfNoticeTitleTextView=itemView.findViewById(R.id.pdfNoiceTitleTextView);
//        imageProgressBar=itemView.findViewById(R.id.imageProgressBar);
        pdfNoticeSenderTextview=itemView.findViewById(R.id.pdfsenderTextView);

        pdfNotificationlinearLayout=itemView.findViewById(R.id.pdfimageandTitleLinearLayout);
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
