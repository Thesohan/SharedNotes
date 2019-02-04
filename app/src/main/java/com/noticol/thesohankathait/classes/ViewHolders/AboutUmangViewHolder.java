package com.noticol.thesohankathait.classes.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.noticol.thesohankathait.notices.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AboutUmangViewHolder extends RecyclerView.ViewHolder{

    public TextView aboutUmangTextView;
    public ImageView aboutUmangImageView;

    public AboutUmangViewHolder(@NonNull View itemView) {
        super(itemView);
        aboutUmangTextView=itemView.findViewById(R.id.aboutUmangTextView);
        aboutUmangImageView=itemView.findViewById(R.id.aboutUmangImageView);
    }

}
