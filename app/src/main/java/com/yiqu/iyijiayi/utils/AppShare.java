package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.WechatUserInfo;
import com.yiqu.iyijiayi.model.ZhaoRen;

/**
 * Created by Administrator on 2016/5/6.
 */
public class AppShare {
    /**
     * xml文件名
     */
    private static final String FILE_NAME = "config";
    private static final String WECHAT_ACCOUNT_INFO = "wechat_account_info";
    private static final String USERINFO_INFO = "userinfo_info";
    private static final String ZHAOREN = "zhaoren";
    private static final String REMEN = "remen";
    private static final String IS_LOGIN = "is_login";
    /**
     * 获取是否登录
     * @param c
     * @return
     */
    public static boolean getIsLogin(Context c){
        SharedPreferences p = c.getSharedPreferences(FILE_NAME,Context.MODE_APPEND);
        return p.getBoolean(IS_LOGIN, false);
    }


    /**
     * 保存是否登录
     * @param c

     */
    public static void setIsLogin(Context c, boolean login){
        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(IS_LOGIN, login);
        e.commit();
    }

    /**
     * 获取微信的信息
     * @param context
     * @return
     */
    public static WechatUserInfo getWechatUserInfo(Context context) {

        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(WECHAT_ACCOUNT_INFO, "");

        WechatUserInfo result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, WechatUserInfo.class);
        }
        return result;
    }

    /**
     * 保存微信信息
     * @param context
     * @param accountVo
     */
    public static void setWechatUserInfo(Context context, WechatUserInfo accountVo) {
        //     SharedPreferences settings =PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(WECHAT_ACCOUNT_INFO, new Gson().toJson(accountVo));
        editor.commit();
    }

    /**
     * 获取用户信息
     * @param context
     * @return
     */
    public static UserInfo getUserInfo(Context context) {

        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(USERINFO_INFO, "");

        UserInfo result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, UserInfo.class);
        }
//        LogUtils.LOGE("..sh.."+tmp);
        return result;
    }

    /**
     * 保存用户信息
     * @param context
     * @param userInfo
     */
    public static void setUserInfo(Context context, UserInfo userInfo) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(USERINFO_INFO, new Gson().toJson(userInfo));
        editor.commit();
    }


    /**
     * 清除用户信息
     * @param context
     */
    public static void clearShare(Context context) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }


    /**
     * 获取
     * @param context
     * @return
     */
    public static ZhaoRen getZhaoRenList(Context context) {

        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(ZHAOREN, "");
        ZhaoRen result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, ZhaoRen.class);
        }
//        LogUtils.LOGE("..sh.."+tmp);
        return result;
    }

    /**
     * 保存
     * @param context
     * @param zhaoRen
     */
    public static void setZhaoRenList(Context context, ZhaoRen zhaoRen) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(ZHAOREN, new Gson().toJson(zhaoRen));
        editor.commit();
    }



    /**
     * 获取
     * @param context
     * @return
     */
    public static Remen getRemenList(Context context) {

        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(REMEN, "");
        Remen result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, Remen.class);
        }
//        LogUtils.LOGE("..sh.."+tmp);
        return result;
    }

    /**
     * 保存
     * @param context
     * @param remen
     */
    public static void setRemenList(Context context, Remen remen) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(REMEN, new Gson().toJson(remen));
        editor.commit();
    }



}
