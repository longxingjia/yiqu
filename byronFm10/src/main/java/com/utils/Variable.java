package com.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Variable {
    public static boolean isBigEnding = false;

    public static String StorageDirectoryPath(Context context) {
        if (!IsExitsSdcard()) {
            return context.getCacheDir().getAbsolutePath();
        } else {
            return context.getExternalCacheDir().getAbsolutePath();
        }
    }

    public static String StorageMusicCachPath(Context context) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        CreateDirectory(path);
        return path;
    }

    public static String StorageDirectoryPath;
    public static String ErrorFilePath;
    public static String StorageMusicPath;
    public static String StorageQandAPath;
    public static String StorageLyricCachPath;
    public static String StorageImagePath;

    public static void InitStorage(Application application) {

        Variable.ErrorFilePath = StorageDirectoryPath + "error.txt";
        Variable.StorageImagePath = Variable.StorageDirectoryPath + "/image/";
        Variable.StorageMusicPath = Variable.StorageDirectoryPath + "/music/";
        Variable.StorageLyricCachPath = Variable.StorageDirectoryPath + "/lyric/";

        Variable.StorageQandAPath = application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        CreateDirectory(Variable.StorageDirectoryPath);
        CreateDirectory(Variable.StorageMusicPath);
        CreateDirectory(Variable.StorageImagePath);
        CreateDirectory(Variable.StorageQandAPath);
        CreateDirectory(Variable.StorageLyricCachPath);

    }

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
