package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yiqu.iyijiayi.service.MusicService;

/**
 * Created by Administrator on 2017/4/13.
 */

public class ServiceUtils {

    public static void stopService(Context context) {
        Intent intent = new Intent();
        intent.putExtra("choice", "stop");
        intent.setClass(context, MusicService.class);
        context.startService(intent);
    }
}
