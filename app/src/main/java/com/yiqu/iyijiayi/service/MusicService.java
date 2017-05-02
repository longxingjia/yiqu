package com.yiqu.iyijiayi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.yiqu.iyijiayi.fileutils.utils.Player;
import com.utils.LogUtils;

public class MusicService extends Service {
    boolean flag = true;
    //    private MediaPlayer player;
    Player p;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.v("tag", "on bind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("tag", "on create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("tag", "on destroy");
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        Log.v("tag", "on start");
        final Intent in = new Intent();
        String choice = intent.getStringExtra("choice");
        String url = intent.getStringExtra("url");
        if (p == null) {
            p = new Player(this, null, null, null, null, new Player.onPlayCompletion() {
                @Override
                public void completion() {
//发送广播

                    in.putExtra("data", "stop");
                    in.setAction("com.yiqu.iyijiayi.service.MusicService");
                    sendBroadcast(in);
                }
            });
            LogUtils.LOGE("tag", "new");
        }

        Log.e("choice", choice);
        if (!TextUtils.isEmpty(url) && flag) {
            Log.v("tag", "taylor");
//            player = MediaPlayer.create(this, R.raw.jumpthenfall);
//            if (p.isPlaying()){
//                p.stop();
//            }
//            p.playUrl(url);
//            flag = false;
        }

        if (choice.equals("play")) {
            if (!TextUtils.isEmpty(url)) {
                if (p.isPlaying()) {
                    if (!p.getUrl().equals(url)) {
                        p.pause();
                        p.playUrl(url);
                    }
                } else {
                    p.playUrl(url);
                }
            }


        } else if (choice.equals("pause")) {
            // Log.v("tag", "pause");
            if (p.isPlaying()) {
                p.pause();
                in.putExtra("data", "pause");
                in.setAction("com.yiqu.iyijiayi.service.MusicService");
                sendBroadcast(in);
            } else {
                p.rePlay();
                in.putExtra("data", "rePlay");
                in.setAction("com.yiqu.iyijiayi.service.MusicService");
                sendBroadcast(in);
            }
        } else if (choice.equals("stop")) {
            Log.v("tag", "choice");
            p.stop();
            p = null;
            // p.release();
            flag = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("tag", "on start command");
        return super.onStartCommand(intent, flags, startId);
    }
}
