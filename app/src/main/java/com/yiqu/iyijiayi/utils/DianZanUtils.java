package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.yiqu.iyijiayi.R;

/**
 * Created by Administrator on 2017/5/5.
 */

public class DianZanUtils {

    public static void  initDianZan(Context mContext, TextView textView, boolean t) {
        Drawable leftDrawable;
        if (t) {
            leftDrawable = mContext.getResources().getDrawable(R.mipmap.dianzan_pressed_new);

        } else {
            leftDrawable = mContext.getResources().getDrawable(R.mipmap.dianzan__new);

        }
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        textView.setCompoundDrawables(leftDrawable, null, null, null); //(Drawable left, Drawable top, Drawable right, Drawable bottom)
    }
}
