package com.example.anu.smartbudgettracker;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Anu on 4/5/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> fragments= new ArrayList<>();
    ArrayList<String> tabtitles= new ArrayList<>();
    public void addFragments( Fragment fragments , String tabtitles){

        this.fragments.add(fragments);
        this.tabtitles.add(tabtitles);
    }


    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);

    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
