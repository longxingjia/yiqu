package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.WechatUserInfo;

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
//        LogUtils.LOGE("..sh.."+tmp);
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



//    /**
//     * 获取画布高度
//     *
//     * @param c
//     * @return
//     */
//    public static Integer getCanvasHeight(Context c) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        return p.getInt(Keys.CANVAS_HEIGHT, 0);
//    }
//
//    /**
//     * 保存画布高度
//     *
//     * @param c
//     * @param height
//     */
//    public static void setCanvasHeight(Context c, int height) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        SharedPreferences.Editor e = p.edit();
//        e.putInt(Keys.CANVAS_HEIGHT, height);
//        e.commit();
//    }
//
//    /**
//     * 获取画布高度
//     *
//     * @param c
//     * @return
//     */
//    public static Integer getCanvasWidth(Context c) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        return p.getInt(Keys.CANVAS_WIDTH, 0);
//    }
//
//    /**
//     * 保存画布高度
//     *
//     * @param c
//     * @param width
//     */
//    public static void setCanvasWidth(Context c, int width) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        SharedPreferences.Editor e = p.edit();
//        e.putInt(Keys.CANVAS_WIDTH, width);
//        e.commit();
//    }
//
//    /**
//     * 获取数据库版本
//     *
//     * @param c
//     * @return
//     */
//    public static Integer getDbVersion(Context c) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        return p.getInt(Keys.DBVERSION, 0);
//    }
//
//    /**
//     * 保存数据库版本
//     *
//     * @param c
//     * @param dbVersion
//     */
//    public static void setDbVersion(Context c, int dbVersion) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        SharedPreferences.Editor e = p.edit();
//        e.putInt(Keys.DBVERSION, dbVersion);
//        e.commit();
//    }
//
//    /**
//     * 保存用户名
//     *
//     * @param c
//     * @param username
//     */
//    public static void setLoginName(Context c, String username) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        SharedPreferences.Editor e = p.edit();
//        e.putString(Keys.USERNAME, username);
//        e.commit();
//    }
//
//
//    /**
//     * 获取用户名
//     *
//     * @param c
//     */
//    public static String getLoginName(Context c) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        return p.getString(Keys.USERNAME, "");
//
//    }
//
//    /**
//     * 保存密码
//     *
//     * @param c
//     * @param passwrod
//     */
//    public static void setPassWord(Context c, String passwrod) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        SharedPreferences.Editor e = p.edit();
//        e.putString(Keys.PASSWORD, passwrod);
//        e.commit();
//    }
//
//    /**
//     * 获取密码
//     *
//     * @param c
//     */
//    public static String getPassWord(Context c) {
//        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        return p.getString(Keys.PASSWORD, "");
//
//    }
//
//

//
//

//
//    public static void setAccount(final Context context, String accessToken) {
//        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
//        //  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        p.edit().putString(Keys.PREF_ACCOUNT, accessToken).commit();
//    }
//
//    public static void clear(final Context context) {
//        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
//        editor.clear();
//        editor.commit();
//    }
//
//    public static void putBookList(Context context, String subjectId, BookVoResponse bookVoResponse){
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(Keys.PREF_BOOK_LIST+subjectId, new Gson().toJson(bookVoResponse));
//        editor.commit();
//    }
//
//    public static BookVoResponse getBookList(Context context, String subjectId){
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        String tmp = settings.getString(Keys.PREF_BOOK_LIST + subjectId, "");
//        BookVoResponse result = null;
//        if(tmp!=null && tmp.length()>0){
//            result = new Gson().fromJson(tmp,BookVoResponse.class);
//        }
//        return result;
//    }


}
