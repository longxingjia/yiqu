package com.yiqu.iyijiayi.fragment.tab3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/17.
 */

public class MyTab3FragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;

    public MyTab3FragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }


}