package com.umangSRTC.thesohankathait.classes.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umangSRTC.thesohankathait.umang.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
