package com.yiqu.iyijiayi.fileutils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerForLocal implements VoicePlayerInterface {

    private SeekBar skbProgress;
    private TextView now_time;
    private TextView all_time;
    private ImageView video_play;
    private boolean isfirst = true;

    private Timer mTimer = new Timer();
    private int duration;
    Context context;
    private boolean USE_PROXY = true;
    private onPlayCompletion onPlayCompletion;

    public PlayerForLocal(Context context, SeekBar skbProgress, ImageView video_play ,
                          TextView now_time, TextView all_time,onPlayCompletion onPlayCompletion) {
        this.skbProgress = skbProgress;
        this.now_time = now_time;
        this.all_time = all_time;
        this.context = context;
        this.video_play = video_play;
        this.onPlayCompletion = onPlayCompletion;


    }


    @Override
    public void playVoiceBegin() {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {

    }

    public interface onPlayCompletion {
        public void completion();
    }



    public String changeNum(int num) {
        if (num == 0) {
            return "00";
        }
        if (num == 1) {
            return "01";
        }
        if (num == 2) {
            return "02";
        }
        if (num == 3) {
            return "03";
        }
        if (num == 4) {
            return "04";
        }
        if (num == 5) {
            return "05";
        }
        if (num == 6) {
            return "06";
        }
        if (num == 7) {
            return "07";
        }
        if (num == 8) {
            return "08";
        }
        if (num == 9) {
            return "09";
        }

        return "" + num;
    }
}
