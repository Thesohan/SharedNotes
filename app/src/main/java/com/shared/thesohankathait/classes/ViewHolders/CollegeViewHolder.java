package com.shared.thesohankathait.classes.ViewHolders;

import android.view.View;
import android.widget.CheckBox;

import com.shared.thesohankathait.notices.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CollegeViewHolder extends RecyclerView.ViewHolder {


    public CheckBox collegeNameCheckBox;
    public CollegeViewHolder(@NonNull View itemView) {
        super(itemView);

        collegeNameCheckBox=itemView.findViewById(R.id.selectCollegeCheckBox);
    }
}
