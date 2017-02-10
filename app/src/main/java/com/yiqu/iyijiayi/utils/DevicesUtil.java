package com.yiqu.iyijiayi.utils;

import android.app.Activity;
import android.content.Context;
import android.text.GetChars;
import android.util.DisplayMetrics;



/**
 * Created by Dragon on 2016/8/1.
 */
public class DevicesUtil {

    private DevicesUtil(){

    }

    public static DevicesUtil getInstance(){
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder{
        private static final DevicesUtil sInstance = new DevicesUtil();
    }

    public  boolean GetDevicesIs_4to3(Activity activity){
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        double W = (double) mDisplayMetrics.widthPixels;
        double H = (double) mDisplayMetrics.heightPixels;
        double i = Math.abs(W / H - 4.0 / 3);
        double j = Math.abs(W / H - 16.0 / 9);
        if (i > j) {
//            setContentView(R.layout.activity_web_view_16_9);
            return false;
        } else {
//            setContentView(R.layout.activity_web_view);
            return true;
        }
    }
}
