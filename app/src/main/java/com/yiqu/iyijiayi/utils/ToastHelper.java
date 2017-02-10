package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class ToastHelper {
    public static void display(Context context, String string) {
        Toast tst = Toast.makeText(context, string, Toast.LENGTH_LONG);
        tst.setGravity(Gravity.BOTTOM, 0, tst.getYOffset() / 2);
        tst.show();
    }

    public static void display(Context context, int resId) {
        Toast tst = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        tst.setGravity(Gravity.BOTTOM, 0, tst.getYOffset() / 2);
        tst.show();
    }
}
