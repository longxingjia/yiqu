package com.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Created by Administrator on 2016/5/20.
 */
public class  LogUtils {
    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

//    public static void LOGE( String message) {
//        Log.e("he", message);
//    }

    /**
     * 返回切割后的MP3文件的路径 返回null则切割失败 开始时间和结束时间的整数部分都是秒，以秒为单位
     *
     *
     * @param list
     * @param startTime
     * @param stopTime
     * @return
     * @throws IOException
     */
    public static String CutingMp3(String path, String name,
                                   List<Integer> list, double startTime, double stopTime)
            throws IOException {
        File file = new File(path);
        String luJing="/storage/emulated/0/"+"HH音乐播放器/切割/";
        File f=new File(luJing);
        f.mkdirs();
        int start = (int) (startTime / 0.026);
        int stop = (int) (stopTime / 0.026);
        if ((start > stop) || (start < 0) || (stop < 0) || (stop > list.size())) {
            return null;
        } else {
            long seekStart = 0;// 开始剪切的字节的位置
            for (int i = 0; i < start; i++) {
                seekStart += list.get(i);
            }
            long seekStop = 0;// 结束剪切的的字节的位置
            for (int i = 0; i < stop; i++) {
                seekStop += list.get(i);
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(seekStart);
            File file1 = new File(luJing + name + "(HH切割).mp3");
            FileOutputStream out = new FileOutputStream(file1);
            byte[] bs=new byte[(int)(seekStop-seekStart)];
            raf.read(bs);
            out.write(bs);
            raf.close();
            out.close();
            File filed=new File(path);
            if(filed.exists())
                filed.delete();
            return file1.getAbsolutePath();
        }

    }

}
