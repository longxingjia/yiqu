package com.yiqu.iyijiayi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiqu.iyijiayi.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Tab1ViewPagerAdapter extends PagerAdapter {

    ArrayList<ImageView> mImageViews;
    public Tab1ViewPagerAdapter( ArrayList<ImageView> mImageViews) {
              this.mImageViews = mImageViews;
    }

    @Override
    public int getCount() {

        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)                       //销毁Item
    {
        //	((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)                                //实例化Item
    {
         container.addView(mImageViews.get(position % mImageViews.size()), 0);

        LogUtils.LOGE(mImageViews.get(position % mImageViews.size()) + "");
        return mImageViews.get(position % mImageViews.size());
    }
}