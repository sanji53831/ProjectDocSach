package com.nguyenminhtri.projectdocsach.view.tusach.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class CustomAdapter_ViewPager_TuSach extends FragmentStatePagerAdapter {

    ArrayList<Fragment> listFragment;
    ArrayList<String> listTitle;

    public CustomAdapter_ViewPager_TuSach(FragmentManager fm) {
        super(fm);
        listFragment = new ArrayList<>();
        listTitle = new ArrayList<>();
    }

    public void addFragment(Fragment fragment,String title){
        listFragment.add(fragment);
        listTitle.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return listFragment.get(i);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
