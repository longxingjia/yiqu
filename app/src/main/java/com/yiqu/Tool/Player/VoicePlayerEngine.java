package com.yiqu.Tool.Player;

import android.media.MediaPlayer;
import android.media.audiofx.PresetReverb;
import android.os.Handler;

import com.utils.L;
import com.utils.LogUtils;
import com.yiqu.Tool.Function.CommonFunction;
import com.yiqu.Tool.Function.LogFunction;
import com.yiqu.Tool.Function.UpdateFunction;

import com.yiqu.Tool.Data.MusicData;
import com.yiqu.Tool.Interface.VoicePlayerInterface;

import java.io.File;

/**
 * Created by zhengtongyu on 16/5/29.
 */
public class VoicePlayerEngine {
    private int musicPlayerState;

    private String playingUrl;
    private int currentTime;
    private final int sampleDuration = 500;// 间隔取样时间
    private VoicePlayerInterface voicePlayerInterface;
    //    private Timer mTimer = new Timer();
    private MediaPlayer voicePlayer;
    private Handler handler;
    private static VoicePlayerEngine instance;

    private VoicePlayerEngine() {
        musicPlayerState = MusicData.MusicPlayerState.reset;

        voicePlayer = new MediaPlayer();
        handler = new Handler();
        voicePlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                start();
                if (currentTime > 0) {
                    seekTo(currentTime);
                }
                PresetReverb mReverb = new PresetReverb(0, voicePlayer.getAudioSessionId());
                PresetReverb.Settings settings = mReverb.getProperties();
                String str = settings.toString();
                settings = new PresetReverb.Settings(str);
                short preset =  PresetReverb.PRESET_LARGEHALL;;
                settings.preset = preset;
                mReverb.setProperties(settings);
            }
        });

        voicePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (voicePlayerInterface != null) {
                    voicePlayerInterface.playVoiceFinish();
                }
                musicPlayerState = MusicData.MusicPlayerState.pausing;
                playingUrl = null;
            }
        });

        voicePlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(final MediaPlayer mediaPlayer, int what, int extra) {
                playFail();

                return true;
            }
        });
    }

    public static VoicePlayerEngine getInstance() {
        if (instance == null) {
            synchronized (VoicePlayerEngine.class) {
                if (instance == null) {
                    instance = new VoicePlayerEngine();
                }
            }
        }

        return instance;
    }

    public synchronized static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }

        instance = null;
    }

    private void destroy() {
        voicePlayer.release();

        voicePlayer = null;
    }

    public void playVoice(String voiceUrl, VoicePlayerInterface voicePlayerInterface) {
        if (CommonFunction.isEmpty(voiceUrl)) {
            UpdateFunction.ShowToastFromThread("不存在语音文件");
            return;
        }

        stopVoice();

        this.voicePlayerInterface = voicePlayerInterface;

        prepareMusic(voiceUrl);
    }

    public void playVoice(String voiceUrl, VoicePlayerInterface voicePlayerInterface, int time) {
        if (CommonFunction.isEmpty(voiceUrl)) {
            UpdateFunction.ShowToastFromThread("不存在语音文件");
            return;
        }
        stopVoice();
        this.voicePlayerInterface = voicePlayerInterface;
        prepareMusic(voiceUrl, time);
    }


    private synchronized void prepareMusic(String voiceUrl, int time) {
        playingUrl = voiceUrl;
        currentTime = time;
        musicPlayerState = MusicData.MusicPlayerState.preparing;

        try {
            voicePlayer.reset();
            voicePlayer.setDataSource(voiceUrl);
            voicePlayer.prepareAsync();

        } catch (Exception e) {

            File file = new File(playingUrl);
            file.delete();
            playFail();
            UpdateFunction.ShowToastFromThread("播放语音文件失败,点击重新下载");
            LogFunction.error("播放语音异常", e);
        }
    }

    private synchronized void prepareMusic(String voiceUrl) {
        playingUrl = voiceUrl;

        musicPlayerState = MusicData.MusicPlayerState.preparing;

        try {
            voicePlayer.reset();
            voicePlayer.setDataSource(voiceUrl);
            voicePlayer.prepareAsync();
        } catch (Exception e) {
            playFail();
            UpdateFunction.ShowToastFromThread("播放语音文件失败");
            LogFunction.error("播放语音异常", e);
        }
    }


    private void playFail() {
        if (voicePlayerInterface != null) {
            voicePlayerInterface.playVoiceFail();
        }

        playingUrl = null;
    }

    public boolean isPlaying() {
        return voicePlayer.isPlaying();
    }

    private void start() {
        voicePlayer.start();

        musicPlayerState = MusicData.MusicPlayerState.playing;
//        mTimer.schedule(mTimerTask, 0, 1000);
        updateMicStatus();
        if (voicePlayerInterface != null) {
            int position = voicePlayer.getDuration();
            voicePlayerInterface.playVoiceBegin(position);
        }
    }

    private void pause() {
        if (!voicePlayer.isPlaying()) {
            return;
        }

        //   playingUrl = null;
        voicePlayer.pause();
        musicPlayerState = MusicData.MusicPlayerState.pausing;

        if (voicePlayerInterface != null) {
            voicePlayerInterface.playVoicePause();
        }
    }

    private void reset() {
        voicePlayer.reset();
        musicPlayerState = MusicData.MusicPlayerState.reset;

        playingUrl = null;
    }

    public void stopVoice() {
        switch (musicPlayerState) {
            case MusicData.MusicPlayerState.playing:
                pause();
                break;
            case MusicData.MusicPlayerState.preparing:
                reset();
                break;
        }
    }

    public void seekTo(int time) {

        if (musicPlayerState == MusicData.MusicPlayerState.playing) {
            if (!voicePlayer.isPlaying()) {
                return;
            }
            voicePlayer.seekTo(time);
        }
    }

    public int pauseVoice() {
        if (musicPlayerState == MusicData.MusicPlayerState.playing) {
            if (!voicePlayer.isPlaying()) {
                return 0;
            }
            //   playingUrl = null;
            voicePlayer.pause();
            musicPlayerState = MusicData.MusicPlayerState.pausing;

            if (voicePlayerInterface != null) {
                voicePlayerInterface.playVoicePause();
            }
            return voicePlayer.getCurrentPosition();
        } else if (musicPlayerState == MusicData.MusicPlayerState.preparing) {
            reset();
            return 0;
        }
        return 0;
    }

    public int restartVoice() {
        if (musicPlayerState == MusicData.MusicPlayerState.pausing) {
//            if (!voicePlayer.isPlaying()) {
//                return 0;
//            }
            //   playingUrl = null;




            voicePlayer.start();
//            mReverb
//            voicePlayer.set
            musicPlayerState = MusicData.MusicPlayerState.playing;
            int pos = voicePlayer.getCurrentPosition();
            updateMicStatus();
            if (voicePlayerInterface != null) {
//                LogUtils.LOGE("tag",pos+"");
//                voicePlayerInterface.playVoiceStateChanged(pos);
            }
            return pos;
        } else if (musicPlayerState == MusicData.MusicPlayerState.preparing) {
            reset();
            return 0;
        }
        return 0;
    }

    public String getPlayingUrl() {
        return playingUrl == null ? "" : playingUrl;
    }

    private void updateMicStatus() {
//        int volume = recorder.getVolume();
//        recordVoiceStateChanged(volume);

        handler.postDelayed(updateMicStatusThread, sampleDuration);
    }

    private Runnable updateMicStatusThread = new Runnable() {
        public void run() {
            if (musicPlayerState == MusicData.MusicPlayerState.playing) {
                // 判断是否超时
                int position = voicePlayer.getCurrentPosition();
                if (voicePlayerInterface != null) {
                    voicePlayerInterface.playVoiceStateChanged(position);
                }
                updateMicStatus();
            }
        }
    };
}