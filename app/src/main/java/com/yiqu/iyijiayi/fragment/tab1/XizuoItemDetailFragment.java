package com.yiqu.iyijiayi.fragment.tab1;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Tool.Global.Variable;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.utils.Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/20.
 */

public class XizuoItemDetailFragment extends AbsAllFragment implements View.OnClickListener {
    String tag = "SoundItemDetailFragmentbak";
    private ImageLoaderHm mImageLoaderHm;
    private TextView like;
    private TextView musicname;
    private TextView desc;
    private TextView soundtime;
    private TextView stu_listen;
    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private Xizuo xizuo;
    private MediaPlayer mediaPlayer;
    private int time;
    private int totalTime;
    private int currentTime;
    private boolean iffirst = false;
    //处理进度条更新
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
                    currentTime = mediaPlayer.getCurrentPosition();
                    if (currentTime<0){
                        currentTime =0;
                    }
                    int leftTime = (totalTime - currentTime) / 1000;
                    soundtime.setText(leftTime + "\"");
                    break;
                default:
                    break;
            }

        }
    };
    private String fileName;
    private String url;
    private File mFile;
    private boolean ifplay = false;
    private boolean ifDownload = false;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText("音乐详情");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.remen_xizuo_detail;
    }

    @Override
    protected void initView(View v) {


        xizuo = (Xizuo) getActivity().getIntent().getExtras().getSerializable("data");

        musicname = (TextView) v.findViewById(R.id.musicname);
        like = (TextView) v.findViewById(R.id.like);
        desc = (TextView) v.findViewById(R.id.desc);
        soundtime = (TextView) v.findViewById(R.id.soundtime);
        stu_listen = (TextView) v.findViewById(R.id.stu_listen);
        created = (TextView) v.findViewById(R.id.created);
        views = (TextView) v.findViewById(R.id.views);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);
        stu_header = (ImageView) v.findViewById(R.id.stu_header);
        stu_listen.setOnClickListener(this);

    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag, netResponse.toString());
                Gson gson = new Gson();
                Sound sound = gson.fromJson(netResponse.data, Sound.class);


            } else {
                getActivity().finish();

            }
        }


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        desc.setText(xizuo.desc);
        soundtime.setText(xizuo.soundtime + "\"");
        musicname.setText(xizuo.musicname);

//        created.setText(sound.created);
        views.setText(xizuo.views + "");
        like.setText(xizuo.like + "");

        time = xizuo.soundtime;
        if (xizuo.stuimage != null) {
            mImageLoaderHm.DisplayImage(MyNetApiConfig.ImageServerAddr + xizuo.stuimage, stu_header);
        }
        String2TimeUtils string2TimeUtils = new String2TimeUtils();
        long currentTimeMillis = System.currentTimeMillis() / 1000;

        long time = currentTimeMillis - xizuo.edited;
        created.setText(string2TimeUtils.long2Time(time) + "前");
//        Tools.DB_PATH = Tools.getCacheDirectory(getActivity(), Environment.DIRECTORY_MUSIC).toString();

        url = MyNetApiConfig.ImageServerAddr + xizuo.soundpath;
        fileName = url.substring(
                url.lastIndexOf("/") + 1,
                url.length());
        fileName = xizuo.musicname + "_" + fileName;
        mFile = new File(Variable.StorageMusicCachPath, fileName);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ifplay = false;
            }
        });
        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_listen:


                    if (mFile.exists()) {


                        if (ifplay){  //正在播放，点击暂停
                            if (mediaPlayer != null) {
                                pause();
                            }
                            ifplay = false;
                        }else {     //暂停，点击播放
                            if (!iffirst) {
//                                init(mFile.toString());
                                mediaPlayer.reset();
                                try {
                                    mediaPlayer.setDataSource(mFile.getAbsolutePath());
                                    mediaPlayer.prepare();// 准备

                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //----------定时器记录播放进度---------//
                                if (mTimer == null) {
                                    mTimer = new Timer();
                                }
                                if (mTimerTask == null) {
                                    mTimerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            mHandler.sendEmptyMessage(0);

                                        }
                                    };
                                    mTimer.schedule(mTimerTask, 1000, 1000);
                                    iffirst=true;
                                }

                            }

                            ifplay = true;
                            totalTime = mediaPlayer.getDuration();
                            mediaPlayer.start();

                        }


                    }else {
                        DownLoaderTask task = new DownLoaderTask(url, Variable.StorageMusicCachPath, fileName, getActivity());
                        task.execute();
                    }


                break;
        }
    }

    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private int mProgress = 0;
        private ProgressReportingOutputStream mOutputStream;
        private Activity mContext = null;
        private int contentLength = 1;

        public DownLoaderTask(String downloadPath, String out, String fileName, Activity context) {
            super();
            mContext = context;
            try {
                mUrl = new URL(downloadPath);

                mFile = new File(out, fileName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setProgress(0);
            stu_listen.setText("");

//
        }

        @Override
        protected Long doInBackground(Void... params) {
            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

//            if (mDialog == null)
//                return;
            if (values.length > 1) {

                contentLength = values[1];
                if (contentLength == -1) {
//                    progressBar.setIndeterminate(true);
                } else {
//                    progressBar.setMax(contentLength);
                }
            } else {
//                progressBar.setProgress(values[0].intValue());
                if (contentLength == -1) {

                } else {
                    stu_listen.setText(values[0].intValue() * 100 / contentLength + "%");
                }
            }

            if (isCancelled()) {
                mFile.delete();
                return;
            }

        }

        @Override
        protected void onPostExecute(Long result) {
            // super.onPostExecute(result);

            //	Log.e(TAG, "下载完");
//            init(mFile.toString());
//            play();

            if (!iffirst) {
//                                init(mFile.toString());
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(mFile.getAbsolutePath());
                    mediaPlayer.prepare();// 准备

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //----------定时器记录播放进度---------//
                if (mTimer == null) {
                    mTimer = new Timer();
                }
                if (mTimerTask == null) {
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(0);
                        }
                    };
                    mTimer.schedule(mTimerTask, 1000, 1000);
                    iffirst=true;
                }
            }

            ifplay = true;
            totalTime = mediaPlayer.getDuration();
            mediaPlayer.start();

            if (isCancelled())
                mFile.delete();
            return;
        }

        private long download() {
            URLConnection connection = null;
            int bytesCopied = 0;
            try {
                connection = mUrl.openConnection();
                int length = connection.getContentLength();
                if (mFile.exists()/* && length == mFile.length() */) {
                    Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                    mFile.delete();
                }
                File directory = new File(Variable.StorageMusicCachPath);
                if (null != directory && !directory.exists()) {
                    directory.mkdir();
                }

                mOutputStream = new ProgressReportingOutputStream(mFile);
                publishProgress(0, length);
                bytesCopied = copy(connection.getInputStream(), mOutputStream);
                if (bytesCopied != length && length != -1) {
                    Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied
                            + ", length" + length);
                }
                mOutputStream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return bytesCopied;
        }

        private int copy(InputStream input, OutputStream output) {
            byte[] buffer = new byte[1024 * 8];
            BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
            BufferedOutputStream out = new BufferedOutputStream(output,
                    1024 * 8);
            int count = 0, n = 0;
            try {
                while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                    out.write(buffer, 0, n);
                    count += n;
                }
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return count;
        }

        private final class ProgressReportingOutputStream extends
                FileOutputStream {

            public ProgressReportingOutputStream(File file)
                    throws FileNotFoundException {
                super(file);
            }

            @Override
            public void write(byte[] buffer, int byteOffset, int byteCount)
                    throws IOException {
                super.write(buffer, byteOffset, byteCount);
                mProgress += byteCount;
                publishProgress(mProgress);
            }

        }

    }


    //初始化音乐播放
    void init(String path) {
//进入Idle

        try {
//初始化
            mediaPlayer.setDataSource(path);

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // prepare 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    //测试播放音乐
    void play() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                ifplay = true;
                totalTime = mediaPlayer.getDuration();
            }
        });
    }

    //暂停音乐
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onDestroy() {

        mImageLoaderHm.stop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }
        super.onDestroy();
    }
}
