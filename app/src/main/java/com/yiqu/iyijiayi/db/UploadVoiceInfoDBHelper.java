package com.yiqu.iyijiayi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.UploadVoice;

import java.util.ArrayList;

/**
 * @version 1.0
 * @comments 数据库
 */
public class UploadVoiceInfoDBHelper extends AbsDBHelper {

    public static final String TAG = "UploadVoiceInfoDBHelper";
    public static final String TABLE_NAME = "upload_voice_info";
    public static final String FROMUID = "fromuid";
    public static final String TOUID = "touid";
    public static final String MID = "mid";
    public static final String MUSICNAME = "musicname";
    public static final String MUSICTYPE = "musictype";
    public static final String CHAPTER = "chapter";
    public static final String ACCOMPANIMENT = "accompaniment";
    public static final String SOUNDTIME = "soundtime";
    public static final String SOUNDPATH = "soundpath";
    public static final String ISFORMULATION = "isformulation";
    public static final String COMMENTPATH = "commentpath";
    public static final String COMMENTTIME = "commenttime";
    public static final String QUESTIONPRICE = "questionprice";
    public static final String LISTENPRICE = "listenprice";
    public static final String ISOPEN = "isopen";
    public static final String STATUS = "status";
    public static final String VOICENAME = "voicename";
    public static final String TYPE = "type";
    public static final String ISPAY = "ispay";
    public static final String ISREPLY = "isreply";


    public UploadVoiceInfoDBHelper(Context context) {
        super(context);
    }


    /**
     * @return
     * @comments 获取所有已经保存的列表
     * @version 1.0
     */
    public ArrayList<UploadVoice> getAll() {
        ArrayList<UploadVoice> ds = new ArrayList<UploadVoice>();
        Cursor c = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            c = db.query(TABLE_NAME, null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                UploadVoice music = new UploadVoice();
                music.fromuid = c.getString(c.getColumnIndex(FROMUID));
                music.mid = c.getInt(c.getColumnIndex(MID));
                music.touid = c.getInt(c.getColumnIndex(TOUID));
                music.soundtime = c.getInt(c.getColumnIndex(SOUNDTIME));
                music.musicname = c.getString(c.getColumnIndex(MUSICNAME));
                music.soundpath = c.getString(c.getColumnIndex(SOUNDPATH));
                music.musictype = c.getString(c.getColumnIndex(MUSICTYPE));
                music.chapter = c.getString(c.getColumnIndex(CHAPTER));
                music.accompaniment = c.getString(c.getColumnIndex(ACCOMPANIMENT));
                music.commentpath = c.getString(c.getColumnIndex(COMMENTPATH));
                music.commenttime = c.getString(c.getColumnIndex(COMMENTTIME));
                music.isformulation = c.getString(c.getColumnIndex(ISFORMULATION));
                music.questionprice = c.getString(c.getColumnIndex(QUESTIONPRICE));
                music.listenprice = c.getString(c.getColumnIndex(LISTENPRICE));
                music.status = c.getString(c.getColumnIndex(STATUS));
                music.isopen = c.getString(c.getColumnIndex(ISOPEN));
                music.voicename = c.getString(c.getColumnIndex(VOICENAME));
                music.type = c.getString(c.getColumnIndex(TYPE));
                music.ispay = c.getString(c.getColumnIndex(ISPAY));
                music.isreply = c.getString(c.getColumnIndex(ISREPLY));
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
     * @param cs 联系人数据列表
     * @return 成功插入的数量
     * @comments 插入一批列表
     * @version 1.0
     */
//    public long insertAll(ArrayList<Music> cs) {
//        long count = 0;
//        try {
//            SQLiteDatabase db = getWritableDatabase();
//            for (Music cc : cs) {
//                ContentValues c = new ContentValues();
//                c.put(MID, cc.mid);
//                c.put(TYPE, cc.type);
//                c.put(TYPENAME, cc.typename);
//                c.put(IMAGE, cc.image);
//                c.put(MUSICNAME, cc.musicname);
//                c.put(MUSICPATH, cc.musicpath);
//                c.put(MUSICTYPE, cc.musictype);
//                c.put(CHAPTER, cc.chapter);
//                c.put(ACCOMPANIMENT, cc.accompaniment);
//                c.put(TIME, cc.time);
//                c.put(SIZE, cc.size);
//                c.put(ISFORMULATION, cc.isformulation);
//                c.put(CREATED, cc.created);
//                c.put(EDITED, cc.edited);
//                count = db.insert(TABLE_NAME, null, c);
//            }
//        } catch (Exception e) {
//            Log.w(TAG, e.toString());
//        } finally {
//        }
//        return count;
//    }

    /**
     * @param cc 联系人数据列表
     * @return 成功插入的数量
     * @comments 插入一个
     * @version 1.0
     */
    public long insert(UploadVoice cc) {
        long count = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(MID, cc.mid);
            c.put(FROMUID, cc.fromuid);
            c.put(TOUID, cc.touid);
            c.put(SOUNDTIME, cc.soundtime);
            c.put(MUSICNAME, cc.musicname);
            c.put(SOUNDPATH, cc.soundpath);
            c.put(MUSICTYPE, cc.musictype);
            c.put(CHAPTER, cc.chapter);
            c.put(ACCOMPANIMENT, cc.accompaniment);
            c.put(COMMENTPATH, cc.commentpath);
            c.put(COMMENTTIME, cc.commenttime);
            c.put(ISFORMULATION, cc.isformulation);
            c.put(QUESTIONPRICE, cc.questionprice);
            c.put(LISTENPRICE, cc.listenprice);
            c.put(STATUS, cc.status);
            c.put(ISOPEN, cc.isopen);
            c.put(VOICENAME, cc.voicename);
            c.put(TYPE, cc.type);
            c.put(ISPAY, cc.ispay);
            c.put(ISREPLY, cc.isreply);

            count = db.insert(TABLE_NAME, null, c);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
        }
        return count;
    }


    /**
     * @param mid
     * @return
     * @comments 更新某一个
     * @version 1.0
     */
    public int update(UploadVoice cc, String mid) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(MID, cc.mid);
            c.put(FROMUID, cc.fromuid);
            c.put(TOUID, cc.touid);
            c.put(SOUNDTIME, cc.soundtime);
            c.put(MUSICNAME, cc.musicname);
            c.put(SOUNDPATH, cc.soundpath);
            c.put(MUSICTYPE, cc.musictype);
            c.put(CHAPTER, cc.chapter);
            c.put(ACCOMPANIMENT, cc.accompaniment);
            c.put(COMMENTPATH, cc.commentpath);
            c.put(COMMENTTIME, cc.commenttime);
            c.put(ISFORMULATION, cc.isformulation);
            c.put(QUESTIONPRICE, cc.questionprice);
            c.put(LISTENPRICE, cc.listenprice);
            c.put(STATUS, cc.status);
            c.put(ISOPEN, cc.isopen);

            return db.update(TABLE_NAME, c, MID + " = " + mid + " ", null);
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

