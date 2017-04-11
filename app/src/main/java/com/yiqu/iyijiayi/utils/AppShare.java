package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yiqu.iyijiayi.model.Banner;
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.TeacherApply;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.WechatUserInfo;
import com.yiqu.iyijiayi.model.ZhaoRen;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/6.
 */
public class AppShare {
    /**
     * xml文件名
     */
    private static final String FILE_NAME = "config";
    private static final String GLOBAL_BFILE_NAME = "global_config";
    private static final String WECHAT_ACCOUNT_INFO = "wechat_account_info";
    private static final String USERINFO_INFO = "userinfo_info";
    private static final String ZHAOREN = "zhaoren";
    private static final String LIKE = "like";
    private static final String BANNER = "banner";
    private static final String REMEN = "remen";
    private static final String IS_LOGIN = "is_login";
    private static final String TEACHERAPPLY_INFO = "teacherapply_info";
    private static final String LAST_LOGIN_PHONE = "last_login_phone";
    private static final String LAST_COMMENT = "last_comment";

    /**
     * 获取是否登录
     *
     * @param c
     * @return
     */
    public static boolean getIsLogin(Context c) {
        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        return p.getBoolean(IS_LOGIN, false);
    }


    /**
     * 保存是否登录
     *
     * @param c
     */
    public static void setIsLogin(Context c, boolean login) {
        SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(IS_LOGIN, login);
        e.commit();
    }

    /**
     * 获取上次登录手机号码
     *
     * @param c
     * @return
     */
    public static String getLastLoginPhone(Context c) {
        SharedPreferences p = c.getSharedPreferences(GLOBAL_BFILE_NAME, Context.MODE_APPEND);
        return p.getString(LAST_LOGIN_PHONE, "");
    }


    /**
     * 保存上次登录手机号码
     *
     * @param c
     */
    public static void setLastLoginPhone(Context c, String loginPhone) {
        SharedPreferences p = c.getSharedPreferences(GLOBAL_BFILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor e = p.edit();
        e.putString(LAST_LOGIN_PHONE, loginPhone);
        e.commit();
    }

    /**
     * 获取上次登录手机号码
     *
     * @param c
     * @return
     */
    public static String getLastComment(Context c) {
        SharedPreferences p = c.getSharedPreferences(GLOBAL_BFILE_NAME, Context.MODE_APPEND);
        return p.getString(LAST_COMMENT, "");
    }


    /**
     * 保存上次登录手机号码
     *
     * @param c
     */
    public static void setLastComment(Context c, String loginPhone) {
        SharedPreferences p = c.getSharedPreferences(GLOBAL_BFILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor e = p.edit();
        e.putString(LAST_COMMENT, loginPhone);
        e.commit();
    }


    /**
     * 获取微信的信息
     *
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
     *
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
     *
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
       // LogUtils.LOGE("..sh..",tmp);
        return result;
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param userInfo
     */
    public static void setUserInfo(Context context, UserInfo userInfo) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(USERINFO_INFO, new Gson().toJson(userInfo));
        editor.commit();
    }

    public static void setTeacherApplyInfo(Context context, TeacherApply teacherApply) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(TEACHERAPPLY_INFO, new Gson().toJson(teacherApply));
        editor.commit();
    }

    public static TeacherApply getTeacherApplyInfo(Context context) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(TEACHERAPPLY_INFO, "");
        TeacherApply result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, TeacherApply.class);
        }
        return result;
    }


    /**
     * 清除用户信息
     *
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
     *
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
        return result;
    }

    /**
     * 保存
     *
     * @param context
     * @param zhaoRen
     */
    public static void setZhaoRenList(Context context, ZhaoRen zhaoRen) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(ZHAOREN, new Gson().toJson(zhaoRen));
        editor.commit();
    }

    public static ArrayList<Like> getLikeList(Context context) {

        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(LIKE, "");
        ArrayList<Like> result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, new TypeToken<ArrayList<Like>>() {
            }.getType());
        }
        return result;
    }

    /**
     * 保存
     *
     * @param context
     * @param likes
     */
    public static void setLikeList(Context context, ArrayList<Like> likes) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(LIKE, new Gson().toJson(likes));
        editor.commit();
    }


    public static ArrayList<Banner> getBannerList(Context context) {

        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        String tmp = p.getString(BANNER, "");
        ArrayList<Banner> result = null;
        if (tmp != null && tmp.length() > 0) {
            result = new Gson().fromJson(tmp, new TypeToken<ArrayList<Banner>>() {
            }.getType());
        }
        return result;
    }

    /**
     * 保存
     *
     * @param context
     * @param likes
     */
    public static void setBannerList(Context context, ArrayList<Banner> likes) {
        SharedPreferences p = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = p.edit();
        editor.putString(BANNER, new Gson().toJson(likes));
        editor.commit();
    }


    /**
     * 获取
     *
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
     *
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
