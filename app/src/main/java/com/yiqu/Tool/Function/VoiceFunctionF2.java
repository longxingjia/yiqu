package com.yiqu.Tool.Function;

import com.yiqu.Tool.Global.Variable;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.Tool.Player.VoicePlayerEngine;
import com.yiqu.Tool.Recorder.Mp3RecorderEngine;
import com.yiqu.Tool.Recorder.RecorderEngine;


/**
 * Created by zhengtongyu on 16/5/29.
 */
public class VoiceFunctionF2 {
    private static String filePath;

    public static boolean IsRecordingVoice() {
        return RecorderEngine.getInstance().IsRecording();
    }

    public synchronized static String StartRecordVoice(boolean is2mp3, VoiceRecorderOperateInterface voiceRecorderOperateInterface) {
        String tmp = Variable.StorageMusicPath + System.currentTimeMillis();

        if (is2mp3) {
            filePath = tmp + ".pcm";
            Mp3RecorderEngine.getInstance()
                    .startRecordVoice(pcmFilePath, voiceRecorderOperateInterface);
        } else {
            RecorderEngine.getInstance()
                    .startRecordVoice(pcmFilePath, voiceRecorderOperateInterface);
        }
        return tmp;

    }

    /**
     * 获取录音路径
     *
     * @return
     */
    // public static String getRecorderMp3Path() {
//        return mp3FilePath;
//    }
    public static String getRecorderPcmPath() {
        return pcmFilePath;
    }

    public static void StopRecordVoice(boolean is2mp3) {
        if (is2mp3) {
            Mp3RecorderEngine.getInstance().stopRecordVoice();
        } else {
            RecorderEngine.getInstance().stopRecordVoice();
        }
    }

    public static boolean isPauseRecordVoice(boolean is2mp3) {
        if (is2mp3) {
            return Mp3RecorderEngine.getInstance().isPause();
        } else {

            return RecorderEngine.getInstance().isPause();
        }

    }

    public static void pauseRecordVoice(boolean is2mp3) {


        if (is2mp3) {
            Mp3RecorderEngine.getInstance().pauseRecording();
        } else {

            RecorderEngine.getInstance().pauseRecording();
        }
    }

    public static void restartRecording(boolean is2mp3) {
        if (is2mp3) {
            Mp3RecorderEngine.getInstance().restartRecording();
        } else {

            RecorderEngine.getInstance().restartRecording();
        }

    }

    public synchronized static void PrepareGiveUpRecordVoice(boolean fromHand) {
        RecorderEngine.getInstance().prepareGiveUpRecordVoice(fromHand);
    }

    public synchronized static void RecoverRecordVoice(boolean fromHand) {
        RecorderEngine.getInstance().recoverRecordVoice(fromHand);
    }

    public synchronized static void GiveUpRecordVoice(boolean fromHand) {

        if (fromHand) {
            Mp3RecorderEngine.getInstance().giveUpRecordVoice();
        } else {

            RecorderEngine.getInstance().giveUpRecordVoice();
        }
    }

    public synchronized static String getPlayingUrl() {
        return VoicePlayerEngine.getInstance().getPlayingUrl();
    }

    public synchronized static boolean IsPlaying() {
        return VoicePlayerEngine.getInstance().isPlaying();
    }

    private synchronized static boolean IsPlayVoice(String fileUrl) {
        if (CommonFunction.isEmpty(fileUrl)) {
            return false;
        }

        return getPlayingUrl().equals(fileUrl);
    }

    /**
     * @param fileUrl 是否在播
     * @return
     */
    public synchronized static boolean IsPlayingVoice(String fileUrl) {
        if (IsPlayVoice(fileUrl)) {
            return VoicePlayerEngine.getInstance().isPlaying();
        } else {
            return false;
        }
    }

    /**
     * @param fileUrl 是否在播
     * @return
     */
//    public synchronized static int getVoiceDuration(String fileUrl) {
////        if (IsPlayVoice(fileUrl)) {
//            return VoicePlayerEngine.getInstance().getDuration(fileUrl);
////        } else {
////            return false;
////        }
//    }
    public synchronized static void PlayToggleVoice(String fileUrl,
                                                    VoicePlayerInterface voicePlayerInterface) {
        if (IsPlayVoice(fileUrl)) {
            VoicePlayerEngine.getInstance().stopVoice();
        } else {
            VoicePlayerEngine.getInstance()
                    .playVoice(fileUrl, voicePlayerInterface);
        }
    }

    public synchronized static void PlayToggleVoice(String fileUrl,
                                                    VoicePlayerInterface voicePlayerInterface, int time) {
        if (IsPlayVoice(fileUrl)) {
            VoicePlayerEngine.getInstance().stopVoice();
        } else {
            VoicePlayerEngine.getInstance()
                    .playVoice(fileUrl, voicePlayerInterface, time);
        }
    }

    public synchronized static void StopVoice() {
        VoicePlayerEngine.getInstance().stopVoice();
    }

    public synchronized static void StopVoice(String fileUrl) {
        if (getPlayingUrl().equals(fileUrl)) {
            VoicePlayerEngine.getInstance().stopVoice();
        }
    }

    public synchronized static int pauseVoice(String fileUrl) {
        if (getPlayingUrl().equals(fileUrl)) {
            return VoicePlayerEngine.getInstance().pauseVoice();
        }
        return 0;
    }

    public synchronized static void pauseVoice() {
        VoicePlayerEngine.getInstance().pauseVoice();
    }


    public synchronized static int startPlayVoice() {

        return VoicePlayerEngine.getInstance().restartVoice();

    }
//    public synchronized static void seekTo(int time) {
//        VoicePlayerEngine.getInstance().seekTo(time);
//    }
}