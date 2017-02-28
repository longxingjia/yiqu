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

//        if (url!=null&&url.contains("http://wx.qlogo.cn")){
////                mImageLoaderHm.DisplayImage(f.userimage, h.icon);
//            Picasso.with(context).load(url).placeholder(R.mipmap.menu_head).into(icon);
//        }else {
//            Picasso.with(context).load(MyNetApiConfig.ImageServerAddr +url).placeholder(R.mipmap.menu_head).into(icon);
//        }
        if (url != null) {
            if (url.contains("http://wx.qlogo.cn")) {
//                mImageLoaderHm.DisplayImage(f.userimage, h.icon);
                Picasso.with(context).load(url)
                        .placeholder(R.mipmap.menu_head).into(icon);
            } else {
                Picasso.with(context).load(MyNetApiConfig.ImageServerAddr + url)
                        .placeholder(R.mipmap.menu_head).into(icon);
            }
        }
    }
}
