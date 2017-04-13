package com.yiqu.iyijiayi.utils;

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

import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.fileutils.utils.MediaPlayerProxy;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayUtils implements  OnCompletionListener, MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private boolean isfirst = true;

    Context context;
    private boolean USE_PROXY = true;

    public PlayUtils(Context context ) {


        this.context = context;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }


    public void play() {
        mediaPlayer.start();
    }

    public boolean isPlaying() {

        return mediaPlayer.isPlaying();
    }

    public void playUrl(String url) {


        if (isfirst) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);

                mediaPlayer.prepare();

                mediaPlayer.start();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isfirst = false;
        }else {
            play();
        }
    }

    public void pause() {
        mediaPlayer.pause();
    }
    public int getCurrentPosition() {
      return   mediaPlayer.getCurrentPosition();
    }
    public int getDuration() {
      return   mediaPlayer.getDuration();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onPrepared");
        arg0.start();

    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
        isfirst = true;

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
