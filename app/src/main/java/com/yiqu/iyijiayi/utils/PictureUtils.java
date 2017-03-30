package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.net.MyNetApiConfig;

/**
 * Created by Administrator on 2017/2/27.
 */

public class PictureUtils {


    public static void showPicture(Context context, String url, ImageView icon) {

        if (url != null) {
            if (url.contains("http://wx.qlogo.cn")) {
                Picasso.with(context).load(url)
                        .resize(DensityUtil.dip2px(context,50), DensityUtil.dip2px(context,50))
                        .centerCrop()
                        .placeholder(R.mipmap.menu_head).into(icon);
            } else {
                Picasso.with(context).load(MyNetApiConfig.ImageServerAddr + url)
                        .resize(DensityUtil.dip2px(context,50), DensityUtil.dip2px(context,50))
                        .centerCrop()
                        .placeholder(R.mipmap.menu_head).into(icon);
            }
        }else {
            Picasso.with(context).load(R.mipmap.menu_head).into(icon);
        }
    }


    public static void showPicture(Context context, String url, ImageView icon,int sizedp) {

        if (url != null) {
            if (url.contains("http://wx.qlogo.cn")) {
                Picasso.with(context).load(url)
                        .resize(DensityUtil.dip2px(context,sizedp), DensityUtil.dip2px(context,sizedp))
                        .centerCrop()
                        .placeholder(R.mipmap.menu_head).into(icon);
            } else {
                Picasso.with(context).load(MyNetApiConfig.ImageServerAddr + url)
                        .resize(DensityUtil.dip2px(context,sizedp), DensityUtil.dip2px(context,sizedp))
                        .centerCrop()
                        .placeholder(R.mipmap.menu_head).into(icon);
            }
        }else {
            Picasso.with(context).load(R.mipmap.menu_head).into(icon);
        }
    }

    public static void showBackgroudPicture(Context context, String url, ImageView icon) {

        if (url != null) {
            if (url.contains("http://wx.qlogo.cn")) {
                Picasso.with(context).load(url)
                        .resize(DensityUtil.dip2px(context,150), DensityUtil.dip2px(context,50))
                        .centerCrop()
                        .placeholder(R.mipmap.home_bg).into(icon);
            } else {
                Picasso.with(context).load(MyNetApiConfig.ImageServerAddr + url)

                        .placeholder(R.mipmap.home_bg).into(icon);
            }
        }else {
            Picasso.with(context).load(R.mipmap.home_bg).into(icon);
        }
    }

    public static void showBannersPicture(Context context, String url, ImageView icon) {

        if (url != null) {
            if (url.contains("http://wx.qlogo.cn")) {
                Picasso.with(context).load(url)
                        .resize(DensityUtil.dip2px(context,150), DensityUtil.dip2px(context,50))
                        .centerCrop()
                        .placeholder(R.mipmap.banner_1).into(icon);
            } else {
                Picasso.with(context).load(MyNetApiConfig.ImageServerAddr + "/"+url)

                        .placeholder(R.mipmap.banner_1).into(icon);

            }
        }else {
            Picasso.with(context).load(R.mipmap.banner_1).into(icon);
        }
    }
}
