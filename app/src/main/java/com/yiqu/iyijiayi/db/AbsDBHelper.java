package com.yiqu.iyijiayi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @version 1.0
 * @comments 简单数据库封装。实现数据库创建、表创建、版本维护功能。
 * 所有表的实现逻辑类都继承此类。一个表就是AbsDBHelper的一个子类。
 */
public abstract class AbsDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "yijiayi.db";
    private static final int DB_VERSION = 2;


    public AbsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeBatch(version_1, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            executeBatch(new String[]{"DROP TABLE if exists "
                    + DownloadMusicInfoDBHelper.TABLE_NAME}, db);

            executeBatch(version_1, db);
        }
        Log.w("onUpgrade", "oldVersion:" + oldVersion + ",newVersion:"
                + newVersion);
    }

    /**
     * 版本1时的SQL语句。
     */
    public static final String version_1[] = {
            "Create table " + DownloadMusicInfoDBHelper.TABLE_NAME + "("
                    + DownloadMusicInfoDBHelper.TYPE + " text , "
                    + DownloadMusicInfoDBHelper.MID + " text , "
                    + DownloadMusicInfoDBHelper.TYPENAME + " text , "
                    + DownloadMusicInfoDBHelper.IMAGE + " text , "
                    + DownloadMusicInfoDBHelper.MUSICNAME + " text , "
                    + DownloadMusicInfoDBHelper.MUSICPATH + " text , "
                    + DownloadMusicInfoDBHelper.MUSICTYPE + " text , "
                    + DownloadMusicInfoDBHelper.CHAPTER + " text , "
                    + DownloadMusicInfoDBHelper.ACCOMPANIMENT + " text , "
                    + DownloadMusicInfoDBHelper.TIME + " text , "
                    + DownloadMusicInfoDBHelper.SIZE + " text , "
                    + DownloadMusicInfoDBHelper.ISFORMULATION + " text , "
                    + DownloadMusicInfoDBHelper.CREATED + " text , "
                    + DownloadMusicInfoDBHelper.EDITED + " text "
                    + ");",
            "Create table " + UploadVoiceInfoDBHelper.TABLE_NAME + "("
                    + UploadVoiceInfoDBHelper.FROMUID + " text , "
                    + UploadVoiceInfoDBHelper.MID + " text , "
                    + UploadVoiceInfoDBHelper.TYPE + " text , "
                    + UploadVoiceInfoDBHelper.TOUID + " text , "
                    + UploadVoiceInfoDBHelper.SOUNDTIME + " text , "
                    + UploadVoiceInfoDBHelper.MUSICNAME + " text , "
                    + UploadVoiceInfoDBHelper.SOUNDPATH + " text , "
                    + UploadVoiceInfoDBHelper.MUSICTYPE + " text , "
                    + UploadVoiceInfoDBHelper.CHAPTER + " text , "
                    + UploadVoiceInfoDBHelper.ACCOMPANIMENT + " text , "
                    + UploadVoiceInfoDBHelper.COMMENTPATH + " text , "
                    + UploadVoiceInfoDBHelper.COMMENTTIME + " text , "
                    + UploadVoiceInfoDBHelper.ISFORMULATION + " text , "
                    + UploadVoiceInfoDBHelper.QUESTIONPRICE + " text , "
                    + UploadVoiceInfoDBHelper.LISTENPRICE + " text , "
                    + UploadVoiceInfoDBHelper.STATUS + " text , "
                    + UploadVoiceInfoDBHelper.ISPAY + " text , "
                    + UploadVoiceInfoDBHelper.ISREPLY + " text , "
                    + UploadVoiceInfoDBHelper.VOICENAME + " text , "
                    + UploadVoiceInfoDBHelper.ISOPEN + " text "
                    + ");"

    };


    /**
     * @param sqls
     * @param db
     * @comments SQL语句执行批处理
     * @version 1.0
     */
    public static void executeBatch(String[] sqls, SQLiteDatabase db) {
        if (sqls == null)
            return;

        db.beginTransaction();
        try {
            int len = sqls.length;
            for (int i = 0; i < len; i++) {
                db.execSQL(sqls[i]);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

}
