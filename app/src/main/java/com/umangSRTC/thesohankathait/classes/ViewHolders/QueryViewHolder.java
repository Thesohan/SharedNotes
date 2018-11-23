package com.umangSRTC.thesohankathait.classes.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umangSRTC.thesohankathait.umang.R;

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


}
