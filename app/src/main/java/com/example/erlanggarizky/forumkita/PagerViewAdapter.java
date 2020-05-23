package com.example.erlanggarizky.forumkita;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter {


    private int numOfTabs;

    PagerViewAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new Fragment_Jawab();

            case 1:
                return new Fragment_Akun();

            default:
                return null;
        }

    }

    @Override
    public int getCount()
    {
        return numOfTabs;
    }
}
