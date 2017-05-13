package com.m3b.rbaudiomixlibrary;

/**
 * Created by m3b on 17/3/31.
 */

public class AudioRecorderNative {
    static {
        System.loadLibrary("rbaudiorecorder");
    }

    public native int NativeInit();
    public native void NativeDeInit();
    public native byte[] MusicMixEncode(int iSampleRate, int iChannelNumber,
                                           byte[] pData, int iLen);
    public native byte[] VoiceMixEncode(int iSampleRate, int iChannelNumber,
                                         byte[] pData, int iLen);
    public native byte[] MusicEncode(int iSampleRate, int iChannelNumber,
                                        byte[] pData, int iLen);
    public native byte[] VoiceEncode(int iSampleRate, int iChannelNumber,
                                      byte[] pData, int iLen);
    public native byte[] PcmMixFlush();
    public native void SetMusicVol(float fMusicGain);
    public native void SetVoiceVol(float fMicGain);

    public native int AudioFileCut(String InputFilePathname, String OutputFilePathname,
                                   float fStartTime, float fEndTime, float fBitRate);

}