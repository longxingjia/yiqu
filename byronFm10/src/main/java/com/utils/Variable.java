package com.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Variable {
    public static boolean isBigEnding = false;

    /**
     * 缓存跟目录
     * @param context
     * @return
     */
    public static String StorageDirectoryPath(Context context) {
        if (!IsExitsSdcard()) {
            return context.getCacheDir().getAbsolutePath();
        } else {
            return context.getExternalCacheDir().getAbsolutePath();
        }
    }

//    public static String StorageInImage(Context context){
//        return "/data/data/"
//                + context.getPackageName() + "/image_manager_disk_cache";
//    }


    /**
     * 背景音乐下载路径
     * @param context
     * @return
     */
    public static String StorageMusicCachPath(Context context) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        CreateDirectory(path);
        return path;
    }


    public static String ErrorFilePath(Context context) {
        return StorageDirectoryPath(context) + "error.txt";
    }

    /**
     *
     * @param context
     * @return
     */
    public static String StorageMusicPath(Context context) {
        String path = Variable.StorageDirectoryPath(context) + "/music/";
        CreateDirectory(path);
        return path;
    }

    /**
     * 播放路径
     * @param context
     * @return
     */
    public static String StorageQandAPath(Context context) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        CreateDirectory(path);
        return path;
    }


    /**
     * 歌词
     * @param context
     * @return
     */
    public static String StorageLyricCachPath(Context context) {
        String path = Variable.StorageDirectoryPath(context) + "/lyric/";
        CreateDirectory(path);
        return path;
    }

    /**
     * 图片
     * @param context
     * @return
     */
    public static String StorageImagePath(Context context){
        String path = Variable.StorageDirectoryPath(context) + "/image/";
        CreateDirectory(path);
        return path;
    };


    public static String StorageSaveImagePath() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        CreateDirectory(path);
        return path;
    }

    ;
//
//    public static void InitStorage(Application application) {
//
//        //   Variable.ErrorFilePath = StorageDirectoryPath + "error.txt";
//        Variable.StorageImagePath = Variable.StorageDirectoryPath + "/image/";
//        Variable.StorageMusicPath = Variable.StorageDirectoryPath + "/music/";
//      //  Variable.StorageLyricCachPath = Variable.StorageDirectoryPath + "/lyric/";
//
////        Variable.StorageQandAPath = application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//
//        CreateDirectory(Variable.StorageDirectoryPath);
//        CreateDirectory(Variable.StorageMusicPath);
//        CreateDirectory(Variable.StorageImagePath);
////        CreateDirectory(Variable.StorageQandAPath);
//        CreateDirectory(Variable.StorageLyricCachPath);
//
//    }

    private static void CreateDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static boolean IsExitsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


}
