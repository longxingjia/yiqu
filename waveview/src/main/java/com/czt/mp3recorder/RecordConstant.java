package com.czt.mp3recorder;


import android.media.AudioFormat;
import android.media.MediaRecorder;

import com.shuyu.waveview.BuildConfig;

public class RecordConstant {
    public static final boolean Debug = BuildConfig.DEBUG;

    public static final int NoExistIndex = -1;

    public static final int OneSecond = 1000;

//    public static final int RecordSampleRate = 44100; // 采样率
    public static final int RecordByteNumber = 2;

    public static final int NormalMaxProgress = 100;

    //=======================AudioRecord Default Settings=======================
    public static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /**
     * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
     */
    public static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率
    public static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    /**
     * 下面是对此的封装
     * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
     */
    public static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;

    //======================Lame Default Settings===================== 0=很好很慢 9=很差很快
    public static final int DEFAULT_LAME_MP3_QUALITY = 2;
    /**
     * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
     */
    public static final int DEFAULT_LAME_IN_CHANNEL = 1;
    /**q
     * Encoded bit rate. MP3 file will be encoded with bit rate 32kbps
     */
//    public static final int DEFAULT_LAME_MP3_BIT_RATE = 192;
    public static final int DEFAULT_LAME_MP3_BIT_RATE = 192;

    //==================================================================

    /**
     * 自定义 每160帧作为一个周期，通知一下需要进行编码
     */
    public static final int FRAME_COUNT = 160;


//    public static final int RecordChannelNumber = 1;  // 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1

//    public static final int RecordDataNumberInOneSecond =
//            RecordSampleRate * RecordByteNumber * RecordChannelNumber;

//    public static final int BehaviorSampleRate = 44100; // 采样率

//    public static final int LameMp3Quality = 2; // Lame Default Settings，
//    public static final int LameBehaviorChannelNumber = RecordChannelNumber;
//
//    public static final int lameRecordBitRate = 64;
//    // Encoded bit rate. MP3 file will be encoded with bit rate 64kbps
//    public static final int LameBehaviorBitRate = 192;
////    public static final int LameBehaviorBitRate = 256;
//
//    public static final int MusicCutEndOffset = 1;
////    public static final int MusicCutEndOffset =2;
//
//    public static final int MaxDecodeProgress = 50;


    public static final int RecordVolumeMaxRank = 9;
    public static final int ThreadPoolCount = 5;

    public static final float VoiceEarWeight = 1.8f;
    public static final float VoiceEarBackgroundWeight = 0.2f;
    public static final float VoiceWeight = 1.5f;
    public static final float VoiceBackgroundWeight = 0.1f;

    public static final String IGeneImageSuffix = ".ipg";
    public static final String JPGSuffix = ".jpg";
    public static final String PngSuffix = ".png";
    public static final String MusicSuffix = ".mp3";
    public static final String LyricSuffix = ".lrc";
    public static final String RecordSuffix = ".mp3";
    public static final String PcmSuffix = ".pcm";
}