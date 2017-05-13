package com.yiqu.Tool.Function;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sinaapp.bashell.VoAACEncoder;
import com.utils.Variable;
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
public class AudioFunction051211 {
    private static String tag = "AudioFunction";
    private static FileOutputStream fileOutputStream;


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
//                ComposeAudio(firstAudioPath, secondAudioPath, composeFilePath, deleteSource,
//                        firstAudioWeight, secondAudioWeight, audioOffset, composeAudioInterface);
                File file = new File(firstAudioPath);
                File file2 = new File(secondAudioPath);
                File[] files = new File[]{file2, file};

                mixAudios(files, composeFilePath, composeAudioInterface);
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


    public static void mixAudios(File[] rawAudioFiles, String filepath, final ComposeAudioInterface composeAudioInterface) {

        final int fileSize = rawAudioFiles.length;

        FileInputStream[] audioFileStreams = new FileInputStream[fileSize];

        File audioFile = null;

        FileInputStream inputStream;
        byte[][] allAudioBytes = new byte[fileSize][];
        boolean[] streamDoneArray = new boolean[fileSize];
        byte[] buffer = new byte[512];
        int offset = 0;
        boolean fistFinish = false;
        int totalSize = 0;
        int readed = 0;//表示已经读取的文件
        Handler handler = new Handler(Looper.getMainLooper());
        VoAACEncoder vo = new VoAACEncoder();
        vo.Init(44100, 32000, (short) 2, (short) 1);// 采样率:44100,bitRate:32k,声道数:1，编码:0.raw 1.ADTS
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            FileOutputStream fospcm = new FileOutputStream(Variable.StorageMusicPath + "he.pcm");
            for (int fileIndex = 0; fileIndex < fileSize; ++fileIndex) {
                audioFile = rawAudioFiles[fileIndex];
                audioFileStreams[fileIndex] = new FileInputStream(audioFile);
            }
            totalSize = audioFileStreams[0].available();
            while (!fistFinish) {

                for (int streamIndex = 0; streamIndex < fileSize; ++streamIndex) {

                    inputStream = audioFileStreams[streamIndex];
                    if (!streamDoneArray[streamIndex] && (offset = inputStream.read(buffer)) != -1) {
                        allAudioBytes[streamIndex] = Arrays.copyOf(buffer, buffer.length);

                    } else {
                        streamDoneArray[streamIndex] = true;
                        allAudioBytes[streamIndex] = new byte[512];
                    }
                    if (offset < 0) {
                        fistFinish = true;

                    } else {

                        readed += offset;
                        final int process = readed * 100 / totalSize;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (composeAudioInterface != null) {
                                    composeAudioInterface.updateComposeProgress(process);
                                }
                            }
                        });
                    }
                }
//                LogUtils.LOGE(tag+"0",allAudioBytes[0][0]+"");
//                LogUtils.LOGE(tag,allAudioBytes[1][0]+"");

                byte[] mixBytes = averageMix(allAudioBytes);
//                LogUtils.LOGE("mixBytes",mixBytes.length+"");
                fospcm.write(mixBytes);

                byte[] ret = vo.Enc(mixBytes);

                fos.write(ret);

//                boolean done = true;
//                for(boolean streamEnd : streamDoneArray){
//                    if(!streamEnd){
//                        done = false;
//                    }
//                }
//
//                if(done){
//                    break;
//                }
            }

            fos.close();
            fospcm.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (composeAudioInterface != null) {
                        composeAudioInterface.composeSuccess();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (composeAudioInterface != null) {
                        composeAudioInterface.composeFail();
                    }
                }
            });
//            if(mOnAudioMixListener != null)
//                mOnAudioMixListener.onMixError(1);
        } finally {
            try {
                for (FileInputStream in : audioFileStreams) {
                    if (in != null)
                        in.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 每一行是一个音频的数据
     */
    static byte[] averageMix(byte[][] bMulRoadAudioes) {

        if (bMulRoadAudioes == null || bMulRoadAudioes.length == 0)
            return null;

        byte[] realMixAudio = bMulRoadAudioes[0];

        if (bMulRoadAudioes.length == 1)
            return realMixAudio;

        for (int rw = 0; rw < bMulRoadAudioes.length; ++rw) {
            if (bMulRoadAudioes[rw].length != realMixAudio.length) {
                Log.e("app", "column of the road of audio + " + rw + " is diffrent.");
                return null;
            }
        }

        int row = bMulRoadAudioes.length;
        int coloum = realMixAudio.length / 2;
        short[][] sMulRoadAudioes = new short[row][coloum];

        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < coloum; ++c) {
                sMulRoadAudioes[r][c] = (short) ((bMulRoadAudioes[r][c * 2] & 0xff) | (bMulRoadAudioes[r][c * 2 + 1] & 0xff) << 8);
            }
        }

        short[] sMixAudio = new short[coloum];
        int mixVal;
        int sr = 0;
        for (int sc = 0; sc < coloum; ++sc) {
            mixVal = 0;
            sr = 0;
            for (; sr < row; ++sr) {
                mixVal += sMulRoadAudioes[sr][sc];
            }
            sMixAudio[sc] = (short) (mixVal / row);
        }

        for (sr = 0; sr < coloum; ++sr) {
            realMixAudio[sr * 2] = (byte) (sMixAudio[sr] & 0x00FF);
            realMixAudio[sr * 2 + 1] = (byte) ((sMixAudio[sr] & 0xFF00) >> 8);
        }

        return realMixAudio;
    }

//    public static void ComposeAudio(String firstAudioFilePath, String secondAudioFilePath,
//                                    String composeAudioFilePath, boolean deleteSource,
//                                    float firstAudioWeight, float secondAudioWeight,
//                                    int audioOffset,
//                                    final ComposeAudioInterface composeAudioInterface) {
//        try {
//            fileOutputStream = new FileOutputStream("/sdcard/out.pcm");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        boolean firstAudioFinish = false;
//        boolean secondAudioFinish = false;
//
//        byte[] firstAudioByteBuffer;
//        byte[] secondAudioByteBuffer;
//        byte[] mp3Buffer;
//        short resultShort;
//        byte resultByte;
//        short[] outputShortArray;
//        byte[] outputByteArray;
//        int index;
//        int firstAudioReadNumber;
//        int secondAudioReadNumber;
//        int outputShortArrayLength;
//        final int byteBufferSize = 1024;
//
//        firstAudioByteBuffer = new byte[byteBufferSize];
//        secondAudioByteBuffer = new byte[byteBufferSize];
//        mp3Buffer = new byte[(int) (7200 + (byteBufferSize * 1.25))];
//
//        outputByteArray = new byte[byteBufferSize];
//        outputShortArray = new short[byteBufferSize / 2];
//
//        Handler handler = new Handler(Looper.getMainLooper());
//
//        FileInputStream firstAudioInputStream = FileFunction.GetFileInputStreamFromFile
//                (firstAudioFilePath);
//        File file = new File(firstAudioFilePath);
//
//        FileInputStream secondAudioInputStream = FileFunction.GetFileInputStreamFromFile
//                (secondAudioFilePath);
//        FileOutputStream composeAudioOutputStream = FileFunction.GetFileOutputStreamFromFile
//                (composeAudioFilePath);
//        int totalSize = 0;
//        int readed = 0;//表示已经读取的文件
//        try {
//            totalSize = firstAudioInputStream.available();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        LameUtil.init(RecordConstant.DEFAULT_SAMPLING_RATE, RecordConstant.DEFAULT_LAME_IN_CHANNEL,
//                RecordConstant.DEFAULT_SAMPLING_RATE, RecordConstant.DEFAULT_LAME_MP3_BIT_RATE, RecordConstant.DEFAULT_LAME_MP3_QUALITY);
//
//        try {
//            while (!firstAudioFinish) {
////            while (!firstAudioFinish && !secondAudioFinish) {
//                index = 0;
//
//                if (audioOffset < 0) {
//                    secondAudioReadNumber = secondAudioInputStream.read(secondAudioByteBuffer);
//
//                    outputShortArrayLength = secondAudioReadNumber / 2;
//
//                    for (; index < outputShortArrayLength; index++) {
//                        resultShort = CommonFunction.GetShort(secondAudioByteBuffer[index * 2],
//                                secondAudioByteBuffer[index * 2 + 1], Variable.isBigEnding);
//
//                        outputShortArray[index] = (short) (resultShort * secondAudioWeight);
//                    }
//
//                    audioOffset += secondAudioReadNumber;
//
//                    if (secondAudioReadNumber < 0) {
//                        secondAudioFinish = true;
//                        break;
//                    }
//
//                    if (audioOffset >= 0) {
//                        break;
//                    }
//                } else {
//
//
//                    firstAudioReadNumber = firstAudioInputStream.read(firstAudioByteBuffer);
//                    outputShortArrayLength = firstAudioReadNumber / 2;
//
//                    for (; index < outputShortArrayLength; index++) {
//                        resultShort = CommonFunction.GetShort(firstAudioByteBuffer[index * 2],
//                                firstAudioByteBuffer[index * 2 + 1], Variable.isBigEnding);
//
//                        outputShortArray[index] = (short) (resultShort * firstAudioWeight);
//                    }
//                    audioOffset -= firstAudioReadNumber;
//                    if (firstAudioReadNumber < 0) {
//                        firstAudioFinish = true;
//                        break;
//                    }
//                    if (audioOffset <= 0) {
//                        break;
//                    }
//                }
//                if (outputShortArrayLength > 0) {
//                    int encodedSize = LameUtil.encode(outputShortArray, outputShortArray,
//                            outputShortArrayLength, mp3Buffer);
//                    if (encodedSize > 0) {
//                        composeAudioOutputStream.write(mp3Buffer, 0, encodedSize);
//
//                    }
//                   // byte[] t = Array.ConvertAll<short, byte>(arr, Convert.ToByte);
//
//
//                }
//            }
//
//            while (!firstAudioFinish) {
////            while (!firstAudioFinish || !secondAudioFinish) {
//                index = 0;
//                firstAudioReadNumber = firstAudioInputStream.read(firstAudioByteBuffer);
//                readed += firstAudioReadNumber;
//                final int process = readed * 100 / totalSize;
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (composeAudioInterface != null) {
//                            composeAudioInterface.updateComposeProgress(process);
//                        }
//                    }
//                });
//
//                secondAudioReadNumber = secondAudioInputStream.read(secondAudioByteBuffer);
//                int minAudioReadNumber = Math.min(firstAudioReadNumber, secondAudioReadNumber);
//                int maxAudioReadNumber = Math.max(firstAudioReadNumber, secondAudioReadNumber);
//
//                if (firstAudioReadNumber < 0) {
//                    firstAudioFinish = true;
//                }
//
//                if (secondAudioReadNumber < 0) {
//                    secondAudioFinish = true;
//                }
//                for (int i=0;i<firstAudioReadNumber;i++){
//                    resultByte = (byte) (firstAudioByteBuffer[index] +secondAudioByteBuffer[index]);
//                    outputByteArray[index] = resultByte;
//                }
//
//                int halfMinAudioReadNumber = minAudioReadNumber / 2;
//
//                outputShortArrayLength = maxAudioReadNumber / 2;
//
//                for (; index < halfMinAudioReadNumber; index++) {
//                    resultShort = CommonFunction.WeightShort(firstAudioByteBuffer[index * 2],
//                            firstAudioByteBuffer[index * 2 + 1], secondAudioByteBuffer[index * 2],
//                            secondAudioByteBuffer[index * 2 + 1], firstAudioWeight,
//                            secondAudioWeight, Variable.isBigEnding);
//
//                    outputShortArray[index] = resultShort;
//                }
//
//                if (firstAudioReadNumber != secondAudioReadNumber) {
//                    if (firstAudioReadNumber > secondAudioReadNumber) {
//                        for (; index < outputShortArrayLength; index++) {
//                            resultShort = CommonFunction.GetShort(firstAudioByteBuffer[index * 2],
//                                    firstAudioByteBuffer[index * 2 + 1], Variable.isBigEnding);
//
//                            outputShortArray[index] = (short) (resultShort * firstAudioWeight);
//                        }
//                    } else {
//                        for (; index < outputShortArrayLength; index++) {
//                            resultShort = CommonFunction.GetShort(secondAudioByteBuffer[index * 2],
//                                    secondAudioByteBuffer[index * 2 + 1], Variable.isBigEnding);
//
//                            outputShortArray[index] = (short) (resultShort * secondAudioWeight);
//                        }
//                    }
//                }
//
//                if (outputShortArrayLength > 0) { //outputShortArrayLength=512
//
//                    int encodedSize = LameUtil.encode(outputShortArray, outputShortArray,
//                            outputShortArrayLength, mp3Buffer);
//
//                    if (encodedSize > 0) {
//                        composeAudioOutputStream.write(mp3Buffer, 0, encodedSize);
//                    }
////                    LogUtils.LOGE("outputShortArray",outputShortArray[0]+"");
////
////                    byte[] bytes =  toByteArray (outputShortArray);
////                    LogUtils.LOGE("bytes",bytes[0]+"");
//                    fileOutputStream.write(outputByteArray);
//                }
//            }
//        } catch (Exception e) {
//            LogFunction.error("ComposeAudio异常", e);
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (composeAudioInterface != null) {
//                        composeAudioInterface.composeFail();
//                    }
//                }
//            });
//
//            return;
//        }
//
//        try {
//            final int flushResult = LameUtil.flush(mp3Buffer);
//
//            if (flushResult > 0) {
//                composeAudioOutputStream.write(mp3Buffer, 0, flushResult);
//            }
//        } catch (Exception e) {
//            LogFunction.error("释放ComposeAudio LameUtil异常", e);
//        } finally {
//            try {
//                composeAudioOutputStream.close();
//            } catch (Exception e) {
//                LogFunction.error("关闭合成输出音频流异常", e);
//            }
//
//            LameUtil.close();
//        }
//
//        if (deleteSource) {
//            FileFunction.DeleteFile(firstAudioFilePath);
//            FileFunction.DeleteFile(secondAudioFilePath);
//        }
//
//        try {
//            firstAudioInputStream.close();
//            secondAudioInputStream.close();
//        } catch (IOException e) {
//            LogFunction.error("关闭合成输入音频流异常", e);
//        }
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (composeAudioInterface != null) {
//                    composeAudioInterface.composeSuccess();
//                }
//            }
//        });
//    }

    public static byte[] toByteArray(short[] src) {

        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i] >> 8);
            dest[i * 2 + 1] = (byte) (src[i] >> 0);
        }

        return dest;
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
