package com.yiqu.iyijiayi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiqu.iyijiayi.model.Music;

import java.util.ArrayList;

/**
 * @version 1.0
 * @comments 数据库
 */
public class DownloadMusicInfoDBHelper extends AbsDBHelper {

    public static final String TAG = "DownloadMusicInfoDB";
    public static final String TABLE_NAME = "download_music_info";

    public static final String TYPE = "type";
    public static final String MID = "mid";
    public static final String TYPENAME = "typename";
    public static final String IMAGE = "image";
    public static final String MUSICNAME = "musicname";
    public static final String MUSICPATH = "musicpath";
    public static final String MUSICTYPE = "musictype";
    public static final String CHAPTER = "chapter";
    public static final String ACCOMPANIMENT = "accompaniment";
    public static final String TIME = "time";
    public static final String SIZE = "size";
    public static final String ISFORMULATION = "isformulation";
    public static final String CREATED = "created";
    public static final String EDITED = "edited";
    public static final String ISDECODE = "isdecode";
    public static final String DECODETIME = "decodetime";
    public static final String DOWNLOADTIME = "downloadtime";


    public DownloadMusicInfoDBHelper(Context context) {
        super(context);
    }


    /**
     * @return
     * @comments 获取所有已经保存的列表
     * @version 1.0
     */
    public ArrayList<Music> getAll() {
        ArrayList<Music> ds = new ArrayList<Music>();
        Cursor c = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            c = db.query(TABLE_NAME, null, null, null, null, null, DOWNLOADTIME +" desc ");
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Music music = new Music();
                music.type = c.getInt(c.getColumnIndex(TYPE));
                music.mid = c.getInt(c.getColumnIndex(MID));
                music.typename = c.getString(c.getColumnIndex(TYPENAME));
                music.image = c.getString(c.getColumnIndex(IMAGE));
                music.musicname = c.getString(c.getColumnIndex(MUSICNAME));
                music.musicpath = c.getString(c.getColumnIndex(MUSICPATH));
                music.musictype = c.getString(c.getColumnIndex(MUSICTYPE));
                music.chapter = c.getString(c.getColumnIndex(CHAPTER));
                music.accompaniment = c.getString(c.getColumnIndex(ACCOMPANIMENT));
                music.time = c.getInt(c.getColumnIndex(TIME));
                music.size = c.getString(c.getColumnIndex(SIZE));
                music.isformulation = c.getString(c.getColumnIndex(ISFORMULATION));
                music.created = c.getLong(c.getColumnIndex(CREATED));
                music.edited = c.getLong(c.getColumnIndex(EDITED));
                music.isdecode = c.getInt(c.getColumnIndex(ISDECODE));
                music.downloadtime = c.getLong(c.getColumnIndex(DOWNLOADTIME));
                ds.add(music);
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            this.close();
        }
        return ds;
    }

    /**
     * @param id
     * @return
     * @comments 获取某一个的ID(其实是查找是否有这个id)
     * @version 1.0
     */
    public String getId(String id) {
        Cursor c = null;
        String a = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            c = db.query(TABLE_NAME, null, MID + " = " + id + " ", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                String theId = c.getString(c.getColumnIndex(MID));
                a = theId;
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            this.close();
        }
        return a;
    }


    /**
     * @param id
     * @return
     * @comments 获取某一个的ID(其实是查找是否有这个id)
     * @version 1.0
     */
    public Music getDecode(int id) {
        Cursor c = null;
        Music m = new Music();
        try {
            SQLiteDatabase db = getReadableDatabase();
            c = db.query(TABLE_NAME, null, MID + " = " + id + " ", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                m.isdecode = c.getInt(c.getColumnIndex(ISDECODE));
                m.decodetime = c.getLong(c.getColumnIndex(DECODETIME));
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            this.close();
        }
        return m;
    }


    /**
     * @param cs 联系人数据列表
     * @return 成功插入的数量
     * @comments 插入一批列表
     * @version 1.0
     */
    public long insertAll(ArrayList<Music> cs) {
        long count = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            for (Music cc : cs) {
                ContentValues c = new ContentValues();
                c.put(MID, cc.mid);
                c.put(TYPE, cc.type);
                c.put(TYPENAME, cc.typename);
                c.put(IMAGE, cc.image);
                c.put(MUSICNAME, cc.musicname);
                c.put(MUSICPATH, cc.musicpath);
                c.put(MUSICTYPE, cc.musictype);
                c.put(CHAPTER, cc.chapter);
                c.put(ACCOMPANIMENT, cc.accompaniment);
                c.put(TIME, cc.time);
                c.put(SIZE, cc.size);
                c.put(ISFORMULATION, cc.isformulation);
                c.put(CREATED, cc.created);
                c.put(EDITED, cc.edited);
                count = db.insert(TABLE_NAME, null, c);
                db.close();
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
        }
        return count;
    }

    /**
     * @param cc 联系人数据列表
     * @return 成功插入的数量
     * @comments 插入一个
     * @version 1.0
     */
    public long insert(Music cc) {
        long count = 0;
        try {
            ContentValues c = new ContentValues();
            SQLiteDatabase db = getWritableDatabase();
            c.put(MID, cc.mid);
            c.put(TYPE, cc.type);
            c.put(TYPENAME, cc.typename);
            c.put(IMAGE, cc.image);
            c.put(MUSICNAME, cc.musicname);
            c.put(MUSICPATH, cc.musicpath);
            c.put(MUSICTYPE, cc.musictype);
            c.put(CHAPTER, cc.chapter);
            c.put(ACCOMPANIMENT, cc.accompaniment);
            c.put(TIME, cc.time);
            c.put(SIZE, cc.size);
            c.put(ISFORMULATION, cc.isformulation);
            c.put(CREATED, cc.created);
            c.put(EDITED, cc.edited);
            c.put(DOWNLOADTIME, cc.downloadtime);
            c.put(ISDECODE, -1);

            count = db.insert(TABLE_NAME, null, c);
            db.close();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
        }
        return count;
    }


    /**
     * @param mid
     * @param status 1表示已经解码,-1 没有解码，0正在解码
     * @return
     * @comments 更新某一个
     * @version 1.0
     */
    public int updateDecode(int mid, int status, long time) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(ISDECODE, status);
            c.put(DECODETIME, time);
//            c.put(TYPENAME, cc.typename);
//            c.put(IMAGE, cc.image);
//            c.put(MUSICNAME, cc.musicname);
//            c.put(MUSICPATH, cc.musicpath);
//            c.put(MUSICTYPE, cc.musictype);
//            c.put(CHAPTER, cc.chapter);
//            c.put(ACCOMPANIMENT, cc.accompaniment);
//            c.put(TIME, cc.time);
//            c.put(SIZE, cc.size);
//            c.put(ISFORMULATION, cc.isformulation);
//            c.put(CREATED, cc.created);
//            c.put(EDITED, cc.edited);
            int r = db.update(TABLE_NAME, c, MID + " = " + mid + " ", null);
//            db.close();
            return r;
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            this.close();
        }
        return 0;
    }


    /**
     * @param mid
     * @return
     * @comments 更新某一个
     * @version 1.0
     */
    public int update(Music cc, String mid) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(TYPE, cc.type);
            c.put(TYPENAME, cc.typename);
            c.put(IMAGE, cc.image);
            c.put(MUSICNAME, cc.musicname);
            c.put(MUSICPATH, cc.musicpath);
            c.put(MUSICTYPE, cc.musictype);
            c.put(CHAPTER, cc.chapter);
            c.put(ACCOMPANIMENT, cc.accompaniment);
            c.put(TIME, cc.time);
            c.put(SIZE, cc.size);
            c.put(ISFORMULATION, cc.isformulation);
            c.put(CREATED, cc.created);
            c.put(EDITED, cc.edited);
            int r = db.update(TABLE_NAME, c, MID + " = " + mid + " ", null);
//            db.close();
            return r;
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            this.close();
        }
        return 0;
    }


    /**
     * @param mid
     * @return
     * @comments 删除某一个
     * @version 1.0
     */
    public int delete(String mid) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db = getWritableDatabase();

            return db.delete(TABLE_NAME, MID + " = " + mid + " ", null);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            this.close();
        }
        return 0;
    }

    /**
     * @param
     * @return
     * @comments 删除全部
     * @version 1.0
     */
    public int deleteAll() {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db = getWritableDatabase();
            return db.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            this.close();
        }
        return 0;
    }
}

