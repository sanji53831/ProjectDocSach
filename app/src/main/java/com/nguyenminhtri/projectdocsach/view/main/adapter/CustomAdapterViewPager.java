package com.nguyenminhtri.projectdocsach.view.main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class CustomAdapterViewPager extends FragmentStatePagerAdapter {

    ArrayList<Fragment> listHinh;
    ArrayList<String> listTitle;

    public CustomAdapterViewPager(FragmentManager fm) {
        super(fm);
        listHinh = new ArrayList<>();
        listTitle = new ArrayList<>();
    }

    public void addFragment(Fragment fragment,String title){
        listHinh.add(fragment);
        listTitle.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return listHinh.get(i);
    }

    @Override
    public int getCount() {
        return listHinh.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
