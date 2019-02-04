package com.noticol.thesohankathait.classes.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.noticol.thesohankathait.classes.Fragment.SelectCollegeFragment;
import com.noticol.thesohankathait.notices.R;

public class SelectCollege extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_college);
        addDifferentFragment(SelectCollegeFragment.newInstance());

    }
    public void addDifferentFragment(Fragment replacableFragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.selectCollgeFrameLayout,replacableFragment).commit();

    }

}
