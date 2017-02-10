package com.yiqu.iyijiayi.utils;

import java.io.File;
import java.io.StringReader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class StorageUtil {

    //录音输出文件
    private static String TAG = StorageUtil.class.getName();
    private static String path = null;

    /**
     * SD卡是否正常
     *
     * @return
     */
    public static boolean isStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /*
    获取SD卡路径
     */
    public static String getSDPath(Context context) {
        if (isStorageAvailable()) {
          //  LogUtils.LOGE("he", "tre");
            try {
                //  File file= context.getFilesDir();
                File file = context.getExternalCacheDir();

                if (!file.exists()) {
                    file.mkdir();
                 //   LogUtils.LOGE("StorageUtil", file.getPath() + "111");
                    path = file.getAbsolutePath();
                } else {
                  //  LogUtils.LOGE("StorageUtil", file.getPath() + "111");
                    path = file.getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           // LogUtils.LOGE("StorageUtil", path + "11");
            return path;
        } else {
            ToastHelper.display(context, "SD卡异常");
            return null;
        }

    }

    /**
     * @param context
     * @return App music 路径
     */
    public static String getMusicPath(Context context) {
        String path = context.getFilesDir() + "/music";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;

    }

    /**
     * @param context
     * @return App music 路径
     */
    public static String getVideoPath(Context context) {
        if (getSDPath(context) != null && !getSDPath(context).isEmpty()) {
            String path = getSDPath(context) + "/video";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }

            return path;
        } else {
            return "";
        }

    }

    /**
     * @param context
     * @return App 录音 路径
     */
    public static String getRecordingPath(Context context) {

        if (getSDPath(context) != null && !getSDPath(context).isEmpty()) {
            String path = getSDPath(context) + "/audio";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }

            return path;
        } else {
            return "";
        }
    }

    /**
     * @param context
     * @return App 相册路径
     */
    public static String getImagePath(Context context) {

        if (getSDPath(context) != null && !getSDPath(context).isEmpty()) {
            String path = getSDPath(context) + "/image";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            return path;
        } else {
            return "";
        }
    }

    /**
     * @param context
     * @return App 画板路径
     */
    public static String getCanvasPath(Context context) {

        if (getSDPath(context) != null && !getSDPath(context).isEmpty()) {
            String path = getSDPath(context) + "/canvas";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            return path;
        } else {
            return "";
        }
    }
}
