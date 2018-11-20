package com.umangSRTC.thesohankathait.classes.Adapter;

import android.util.Log;

import com.umangSRTC.thesohankathait.classes.Fragment.AboutUmang;
import com.umangSRTC.thesohankathait.classes.Fragment.Notification;
import com.umangSRTC.thesohankathait.classes.Fragment.Schools;
import com.umangSRTC.thesohankathait.classes.Fragment.Upload;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        Log.d("position",""+position);
        switch(position) {
            case 0:
                return Notification.newInstance();

            case 1:
                return Upload.newInstance();

            case 2:
                return Schools.newInstance();
            case 3:
                return AboutUmang.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "Notices";

            case 1:
                return "Upload";

            case 2:
                return "Schools";
            case 3:
                return "About";

        }
        return null;
    }


}
