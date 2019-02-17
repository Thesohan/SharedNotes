package com.shared.thesohankathait.notices.javas.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shared.thesohankathait.notices.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QueryViewHolder extends RecyclerView.ViewHolder{

    public TextView questionTextView;
    public TextView answerTextView;
    public Button replyButton;
    public LinearLayout linearLayout;
//    public ProgressBar imageProgressBar;

    public QueryViewHolder(@NonNull View itemView) {
        super(itemView);
        questionTextView=itemView.findViewById(R.id.questionTextView);
        answerTextView=itemView.findViewById(R.id.ansTextView);
        replyButton=itemView.findViewById(R.id.replyButton);
        linearLayout=itemView.findViewById(R.id.linearLayoutQuery);
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
