package com.yiqu.iyijiayi.utils;

import android.content.Context;

/**
 * Created by Administrator on 2016/7/20.
 * 静态内部类单例模式
 */
public class Singleton {
    private Singleton(){


    }
    Context context;

    public static Singleton getInstance(Context context){
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder{
        private static final Singleton sInstance = new Singleton();
    }
}
