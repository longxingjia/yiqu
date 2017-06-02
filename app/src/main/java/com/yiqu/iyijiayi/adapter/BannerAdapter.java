/*
 * AUTHOR：Yolanda
 * 
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.WebVFragment;
import com.yiqu.iyijiayi.model.Banner;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.List;

/**
 * Created by Yolanda on 2016/5/5.
 *
 * @author Yolanda; QQ: 757699476
 */
public class BannerAdapter extends PagerAdapter {

    private Context mContext;

    private List<Banner> banners;

    public BannerAdapter(Context context) {
        this.mContext = context;
    }

    public void update(List<Banner> banners) {
        if (this.banners != null)
            this.banners.clear();
        if (banners != null)
            this.banners = banners;
    }

    @Override
    public int getCount() {
        return banners == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(banners.get());
        final Banner banner  =  banners.get(position % banners.size());
        PictureUtils.showBannersPicture(mContext,banner.image, imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(banner.url)){
                    Intent in = new Intent(mContext, StubActivity.class);
                    in.putExtra("fragment", WebVFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data",banner);
                    in.putExtras(bundle);
                    mContext.startActivity(in);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }
}
