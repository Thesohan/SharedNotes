package com.umangSRTC.thesohankathait.umang.javas.Adapter;

import android.util.Log;

import com.umangSRTC.thesohankathait.umang.javas.Fragment.Notification;
import com.umangSRTC.thesohankathait.umang.javas.Fragment.Schools;
import com.umangSRTC.thesohankathait.umang.javas.Fragment.Upload;

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
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "Notification";

            case 1:
                return "Upload";

            case 2:
                return "Schools";

        }
        return null;
    }


}
