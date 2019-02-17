package com.shared.thesohankathait.classes.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.shared.thesohankathait.classes.Fragment.AboutUs;
import com.shared.thesohankathait.classes.Fragment.Request;
import com.shared.thesohankathait.classes.Fragment.SelectCollegeFragment;
import com.shared.thesohankathait.notices.R;
import com.shared.thesohankathait.classes.Fragment.PdfNotice;
import com.shared.thesohankathait.classes.Fragment.Policy;
import com.shared.thesohankathait.classes.Fragment.Query;

public class Features extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);


        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("FRAGMENT_NAME");
        switch (fragmentName) {
            case "Request":
                addDifferentFragment(Request.newInstance());
                break;
            case "Query":
                addDifferentFragment(Query.newInstance());
                break;
            case "Policy":
                addDifferentFragment(Policy.newInstance());
                break;
            case "PdfNotice":
                String schoolName = getIntent().getStringExtra("SCHOOL_NAME");
                addDifferentFragment(PdfNotice.newInstance(schoolName));
                break;
            case "AboutUs":
                addDifferentFragment(AboutUs.newInstance());
                break;
            case "SelectCollege":
                addDifferentFragment(SelectCollegeFragment.newInstance());
                break;

        }

    }

    private void addDifferentFragment(Fragment replacableFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.featureFrameLayout, replacableFragment).commit();

    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {

            finish();
        }
    }
}
