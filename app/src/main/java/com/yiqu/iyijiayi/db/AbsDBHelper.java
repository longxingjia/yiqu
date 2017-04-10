package com.yiqu.iyijiayi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yiqu.iyijiayi.utils.LogUtils;

import java.sql.SQLException;


/**
 * @version 1.0
 * @comments 简单数据库封装。实现数据库创建、表创建、版本维护功能。
 * 所有表的实现逻辑类都继承此类。一个表就是AbsDBHelper的一个子类。
 */
public abstract class AbsDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "yijiayi.db";
    private static final int DB_VERSION = 14;


    public AbsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeBatch(version_1, db);
        db.execSQL("create table " + CachFileDBHelper.TABLE_NAME + "(FileName STRING PRIMARY KEY,FileSize INTEGER)");
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            executeBatch(new String[]{"DROP TABLE if exists "
                    + DownloadMusicInfoDBHelper.TABLE_NAME, "DROP TABLE if exists "
                    + ComposeVoiceInfoDBHelper.TABLE_NAME}, db);
            db.execSQL("create table " + CachFileDBHelper.TABLE_NAME + "(FileName STRING PRIMARY KEY,FileSize INTEGER)");
            executeBatch(version_1, db);
        } else {
            String columns = DownloadMusicInfoDBHelper.TYPE + "," +
                    DownloadMusicInfoDBHelper.MID + "," +
                    DownloadMusicInfoDBHelper.TYPENAME + "," +
                    DownloadMusicInfoDBHelper.IMAGE + "," +
                    DownloadMusicInfoDBHelper.MUSICNAME + "," +
                    DownloadMusicInfoDBHelper.MUSICPATH + "," +
                    DownloadMusicInfoDBHelper.MUSICTYPE + "," +
                    DownloadMusicInfoDBHelper.CHAPTER + "," +
                    DownloadMusicInfoDBHelper.ACCOMPANIMENT + "," +
                    DownloadMusicInfoDBHelper.TIME + "," +
                    DownloadMusicInfoDBHelper.SIZE + "," +
                    DownloadMusicInfoDBHelper.ISFORMULATION + "," +
                    DownloadMusicInfoDBHelper.CREATED + "," +
                    DownloadMusicInfoDBHelper.DECODETIME + "," +
                    DownloadMusicInfoDBHelper.ISDECODE + "," +
                    DownloadMusicInfoDBHelper.EDITED;
            upgradeTables(db, DownloadMusicInfoDBHelper.TABLE_NAME, version_14, columns);

        }
        Log.w("onUpgrade", "oldVersion:" + oldVersion + ",newVersion:"
                + newVersion);
    }


    /**
     * Upgrade tables. In this method, the sequence is:
     * <b>
     * <p>[1] Rename the specified table as a temporary table.
     * <p>[2] Create a new table which name is the specified name.
     * <p>[3] Insert data into the new created table, data from the temporary table.
     * <p>[4] Drop the temporary table.
     * </b>
     *
     * @param db        The database.
     * @param tableName The table name.
     * @param columns   The columns range, format is "ColA, ColB, ColC, ... ColN";
     */
    protected void upgradeTables(SQLiteDatabase db, String tableName, String dbVersion, String columns) {
        try {

            // 1, Rename table.
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            executeBatch(sql, db);
//            db.execSQL("");


            // 2, Create table.
            executeBatch(dbVersion, db);

            // 3, Load data
            sql = "INSERT INTO " + tableName +
                    " (" + columns + ") " +
                    " SELECT " + columns + " FROM " + tempTableName;

            executeBatch(sql, db);
            // LogUtils.LOGE("ssss",sql);

            // 4, Drop the temporary table.
            executeBatch("DROP TABLE IF EXISTS " + tempTableName, db);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //   db.endTransaction();
        }
    }

    private static final String version_14 =
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
                    + DownloadMusicInfoDBHelper.ISDECODE + " text , "
                    + DownloadMusicInfoDBHelper.DECODETIME + " text , "
                    + DownloadMusicInfoDBHelper.DOWNLOADTIME + " text , "
                    + DownloadMusicInfoDBHelper.CREATED + " text , "
                    + DownloadMusicInfoDBHelper.EDITED + " text "
                    + ");";


    private static final String version_7 =
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
                    + DownloadMusicInfoDBHelper.ISDECODE + " text , "
                    + DownloadMusicInfoDBHelper.DECODETIME + " text , "
                    + DownloadMusicInfoDBHelper.CREATED + " text , "
                    + DownloadMusicInfoDBHelper.EDITED + " text "
                    + ");";


    /**
     * @param sqls
     * @param db
     * @comments SQL语句执行批处理
     * @version 1.0
     */
    public static void executeBatch(String sqls, SQLiteDatabase db) {
        if (sqls == null)
            return;

        db.beginTransaction();
        try {
            db.execSQL(sqls);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


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

    /**
     * 版本1时的SQL语句。
     */
    private static final String version_1[] = {
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
                    + DownloadMusicInfoDBHelper.ISDECODE + " text , "
                    + DownloadMusicInfoDBHelper.DECODETIME + " text , "
                    + DownloadMusicInfoDBHelper.DOWNLOADTIME + " text , "
                    + DownloadMusicInfoDBHelper.CREATED + " text , "
                    + DownloadMusicInfoDBHelper.EDITED + " text "
                    + ");",
            "Create table " + ComposeVoiceInfoDBHelper.TABLE_NAME + "("
                    + ComposeVoiceInfoDBHelper.FROMUID + " text , "
                    + ComposeVoiceInfoDBHelper.MID + " text , "
                    + ComposeVoiceInfoDBHelper.TYPE + " text , "
                    + ComposeVoiceInfoDBHelper.TOUID + " text , "
                    + ComposeVoiceInfoDBHelper.SOUNDTIME + " text , "
                    + ComposeVoiceInfoDBHelper.MUSICNAME + " text , "
                    + ComposeVoiceInfoDBHelper.SOUNDPATH + " text , "
                    + ComposeVoiceInfoDBHelper.MUSICTYPE + " text , "
                    + ComposeVoiceInfoDBHelper.CHAPTER + " text , "
                    + ComposeVoiceInfoDBHelper.ACCOMPANIMENT + " text , "
                    + ComposeVoiceInfoDBHelper.COMMENTPATH + " text , "
                    + ComposeVoiceInfoDBHelper.COMMENTTIME + " text , "
                    + ComposeVoiceInfoDBHelper.ISFORMULATION + " text , "
                    + ComposeVoiceInfoDBHelper.QUESTIONPRICE + " text , "
                    + ComposeVoiceInfoDBHelper.LISTENPRICE + " text , "
                    + ComposeVoiceInfoDBHelper.STATUS + " text , "
                    + ComposeVoiceInfoDBHelper.ISPAY + " text , "
                    + ComposeVoiceInfoDBHelper.ISREPLY + " text , "
                    + ComposeVoiceInfoDBHelper.VOICENAME + " text , "
                    + ComposeVoiceInfoDBHelper.CREATETIME + " text , "
                    + ComposeVoiceInfoDBHelper.ISCOMPOSE + " text , "
                    + ComposeVoiceInfoDBHelper.DESC + " text , "
                    + ComposeVoiceInfoDBHelper.ISOPEN + " text "
                    + ");"

    };


}
