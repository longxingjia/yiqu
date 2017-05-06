package com.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.utils.LogUtils;
import com.utils.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * liteplayer by loader
 * 音乐播放服务
 *
 * @author qibin
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    //private SensorManager mSensorManager;

    private Player mPlayer;
    private OnMusicEventListener mListener;
    private int sid; // 当前正在播放
    private String tag = PlayService.class.getSimpleName();


    private ExecutorService mProgressUpdatedListener = Executors.newSingleThreadExecutor();

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
//		mSensorManager.registerListener(mSensorEventListener,
//				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//				SensorManager.SENSOR_DELAY_GAME);
        return new PlayBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

//		MusicUtils.initMusicList();
//		mPlayingPosition = (Integer) SpUtils.get(this, Constants.PLAY_POS, 0);

        mPlayer = new Player(this, null, null, null, null, new Player.onPlayCompletion() {
            @Override
            public void completion() {

            }
        });
// 开始更新进度的线程
        mPlayer.setOnPreparedCompletion(new Player.onPreparedCompletion() {
            @Override
            public void onPreparedCompletion() {
                mProgressUpdatedListener.execute(mPublishProgressRunnable);
            }
        });


//		mPlayer.setOnBufferingUpdateListener(mBufferUpdateListener);
    }


    /**
     * 更新进度的线程
     */
    private Runnable mPublishProgressRunnable = new Runnable() {
        @Override
        public void run() {
            for (; ; ) {
                if (mPlayer != null && mPlayer.isPlaying() &&
                        mListener != null) {
                    mListener.onPublish(mPlayer.getCurrentPosition());
                }

                SystemClock.sleep(200);
            }
        }
    };

    /**
     * 设置回调
     *
     * @param l
     */
    public void setOnMusicEventListener(OnMusicEventListener l) {
        mListener = l;
    }


    /**
     * 播放
     *
     * @param path 音乐列表的位置
     * @return 当前播放的位置
     */
    public int play(String path, int sid) {
//		if(position < 0) position = 0;
//		if(position >= MusicUtils.sMusicList.size()) position = MusicUtils.sMusicList.size() - 1;

        if (!TextUtils.isEmpty(path)) {

            if (mPlayer.isPlaying()) {
                if (!mPlayer.getUrl().equals(path)) {
                    mPlayer.pause();
                    mPlayer.playUrl(path);
                }
            } else {

                mPlayer.playUrl(path);
            }
        }

        this.sid = sid;
        //	SpUtils.put(Constants.PLAY_POS, mPlayingPosition);
        return sid;
    }

    /**
     * 继续播放
     *
     * @return 当前播放的位置 默认为0
     */
    public int resume() {
        if (isPlaying()) return -1;
        mPlayer.rePlay();
        return sid;
    }

    /**
     * 暂停播放
     *
     * @return 当前播放的位置
     */
    public int pause() {
        if (!isPlaying()) return -1;
        mPlayer.pause();

        return sid;
    }


    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    /**
     * 获取正在播放的位置
     *
     * @return
     */
    public int getPlayingPosition() {
        return sid;
    }

    /**
     * 获取当前正在播放音乐的总时长
     *
     * @return
     */
    public int getDuration() {
        if (!isPlaying()) return 0;
        return mPlayer.getDuration();
    }

    public void seek(int msec) {
        if (!isPlaying()) return;
        mPlayer.seekTo(msec);
    }

    /**
     * 开始播放
     */
    private void start() {
        mPlayer.rePlay();
    }

    /**
     * 音乐播放完毕 自动下一曲
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
//		next();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.w("play service", "unbind");
        // 置空Listener
        // 如果不置空，会出现的问题
        // 即便是activity解绑了service， service依然可以回调
        // 原因是service已经持有了activity的引用
        // 这样会导致一个问题:
        // activity在销毁后不能被回收， so 会造成内存泄漏
        mListener = null;
//		mSensorManager.unregisterListener(mSensorEventListener);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        if (mListener != null) mListener.onChange(sid);
    }

    @Override
    public void onDestroy() {
        release();
        //mSensorManager.unregisterListener(mSensorEventListener);
        super.onDestroy();
    }

    /**
     * 服务销毁时，释放各种控件
     */
    private void release() {
        if (!mProgressUpdatedListener.isShutdown()) mProgressUpdatedListener.shutdownNow();
        mProgressUpdatedListener = null;

        if (mPlayer != null) mPlayer.stop();
        mPlayer = null;
    }

    /**
     * 音乐播放回调接口
     *
     * @author qibin
     */
    public interface OnMusicEventListener {
        public void onPublish(int percent);

        public void onChange(int position);
    }
}
