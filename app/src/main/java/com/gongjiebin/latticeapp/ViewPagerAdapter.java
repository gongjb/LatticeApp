package com.gongjiebin.latticeapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int PAGE_COUNT = 2;

    private Context context;
    private List<FragmentText1> baseFragments;

    public void setPAGE_COUNT(int PAGE_COUNT) {
        this.PAGE_COUNT = PAGE_COUNT;
    }

    public void setBaseFragments(List<FragmentText1> baseFragments) {
        this.baseFragments = baseFragments;
    }

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
//        return PageFragment.newInstance(position + 1);
        return baseFragments.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];
        return "";
    }
}