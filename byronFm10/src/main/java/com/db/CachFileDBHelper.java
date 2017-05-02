package com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * @version 1.0
 * @comments 数据库
 */
public class CachFileDBHelper extends AbsDBHelper {

    public static final String TAG = "CachFileDBHelper";
    public static final String TABLE_NAME = "cache_file_info";
    public static final String FILENAME = "FileName";
    public static final String FILESIZE = "FileSize";


    public CachFileDBHelper(Context context) {
        super(context);
    }

//    public CachFileDBHelper() {
//      //  CachFileDBHelper(App.mContext);
//        super(App.mContext);
//    }
    /**
     * @param
     * @return
     * @comments 获取某一个的ID(其实是查找是否有这个id)
     * @version 1.0
     */
    public int getFileSize(String fileName) {
        Cursor c = null;
        int theId = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            c = db.query(TABLE_NAME, null, FILENAME + "=?", new String[]{fileName}, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                 theId = c.getInt(c.getColumnIndex(FILESIZE));

            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            this.close();
        }
        return theId;
    }

    public void insertOrUpdate(String fileName, int fileSize) {
        CacheFileInfo cacheFileInfo = new CacheFileInfo(fileName, fileSize);
        if (getFileSize(cacheFileInfo.getFileName()) == -1) {
            insert(cacheFileInfo);
        } else {
            update(cacheFileInfo);
        }
    }

    private void insert(CacheFileInfo cacheFileInfo) {
        SQLiteDatabase db = getWritableDatabase();
        try {

            db.beginTransaction();
            ContentValues c = new ContentValues();
            c.put(FILENAME, cacheFileInfo.fileName);
            c.put(FILESIZE, cacheFileInfo.fileSize);
            db.insert(TABLE_NAME, null, c);
            db.setTransactionSuccessful();
        }catch (Exception e) {
           e.printStackTrace();
        } finally {
            db.endTransaction();
            this.close();
        }
    }
    private void update(CacheFileInfo cacheFileSize) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = packData(cacheFileSize);
            sqLiteDatabase.update(TABLE_NAME, cv, "FileName=?", new String[] { cacheFileSize.getFileName() });
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            this.close();
            sqLiteDatabase.endTransaction();
        }
    }

    public ContentValues packData(CacheFileInfo cacheFileSize) {
        ContentValues cv = new ContentValues();
        cv.put("FileName", cacheFileSize.getFileName());
        cv.put("FileSize", cacheFileSize.getFileSize());
        return cv;
    }

    class CacheFileInfo {
        private String fileName;
        private int fileSize;

        public CacheFileInfo(String fileName, int fileSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
        }

        public CacheFileInfo() {
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }
    }

    public void delete(String fileName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete(TABLE_NAME, "FileName=?", new String[] { fileName });
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
    }
}

