package com.yiqu.Tool.Function;

import android.os.Handler;
import android.os.Looper;

import com.yiqu.Tool.Decode.DecodeEngine;
import com.yiqu.Tool.Interface.ComposeAudioInterface;
import com.yiqu.Tool.Interface.DecodeOperateInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhengtongyu on 16/5/29.
 */
public class AudioFunctionbakb {
    private static String tag = "AudioFunction";

    public static void DecodeMusicFile(final String musicFileUrl, final String decodeFileUrl, final int startSecond,
                                       final int endSecond,
                                       final DecodeOperateInterface decodeOperateInterface) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                DecodeEngine.getInstance().beginDecodeMusicFile(musicFileUrl, decodeFileUrl, startSecond, endSecond,
                        decodeOperateInterface);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogFunction.error("异常观察", e.toString());
                    }

                    @Override
                    public void onNext(String string) {
                    }
                });
    }

    public static void BeginComposeAudio(final String firstAudioPath, final String secondAudioPath,
                                         final String composeFilePath, final boolean deleteSource,
                                         final float firstAudioWeight,
                                         final float secondAudioWeight, final int audioOffset,
                                         final ComposeAudioInterface composeAudioInterface) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                ComposeAudio(firstAudioPath, secondAudioPath, composeFilePath, deleteSource,
                        firstAudioWeight, secondAudioWeight, audioOffset, composeAudioInterface);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String string) {
                    }
                });
    }

    public static void ComposeAudio(String firstAudioFilePath, String secondAudioFilePath,
                                    String composeAudioFilePath, boolean deleteSource,
                                    float firstAudioWeight, float secondAudioWeight,
                                    int audioOffset,
                                    final ComposeAudioInterface composeAudioInterface) {
        boolean firstAudioFinish = false;
        boolean secondAudioFinish = false;

        byte[] firstAudioByteBuffer;
        byte[] secondAudioByteBuffer;
        byte[] mp3Buffer;
        short resultShort;
        short[] outputShortArray;
        int index;
        int firstAudioReadNumber;
        int secondAudioReadNumber;
        int outputShortArrayLength;
        final int byteBufferSize = 512;

        firstAudioByteBuffer = new byte[byteBufferSize];
        secondAudioByteBuffer = new byte[byteBufferSize];


        Handler handler = new Handler(Looper.getMainLooper());

        FileInputStream firstAudioInputStream = FileFunction.GetFileInputStreamFromFile
                (firstAudioFilePath);
        File file = new File(firstAudioFilePath);

        FileInputStream secondAudioInputStream = FileFunction.GetFileInputStreamFromFile
                (secondAudioFilePath);
        FileOutputStream composeAudioOutputStream = FileFunction.GetFileOutputStreamFromFile
                (composeAudioFilePath);
        byte[][] allAudioBytes = new byte[2][];
        try {
            while (!firstAudioFinish) {


                while (firstAudioInputStream.read(firstAudioByteBuffer)!=-1){
                    allAudioBytes[0] =  Arrays.copyOf(firstAudioByteBuffer,firstAudioByteBuffer.length);
                }



            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (deleteSource) {
            FileFunction.DeleteFile(firstAudioFilePath);
            FileFunction.DeleteFile(secondAudioFilePath);
        }

        try {
            firstAudioInputStream.close();
            secondAudioInputStream.close();
        } catch (IOException e) {
            LogFunction.error("关闭合成输入音频流异常", e);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (composeAudioInterface != null) {
                    composeAudioInterface.composeSuccess();
                }
            }
        });
    }


    //噪音消除算法
    static void calc(short[] lin, int off, int len) {
        int i, j;
        for (i = 0; i < len; i++) {
            j = lin[i + off];
            lin[i + off] = (short) (j >> 2);
        }
    }
}
