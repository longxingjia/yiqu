package com.yiqu.iyijiayi.fragment.tab1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Variable;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

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

public class SoundItemDetailFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface {
    String tag = "SoundItemDetailFragmentbak";

    private TextView like;
    private TextView musicname;
    private TextView desc;
    private TextView soundtime;
    private TextView stu_listen;
    private TextView tea_name;
    private TextView tectitle;
    private TextView commenttime;
    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private ImageView tea_header;

    private int time;
    private int teatotalTime;
    private int stutotalTime;
    private int stucurrentTime;
    private int teacurrentTime;
    private boolean teafirst = false;
    private boolean stufirst = false;

    private String stufileName;
    private String teafileName;
    private String stuUrl;
    private String teaUrl;
    private File stuFile;
    private File teaFile;
    private boolean stuplay = false;
    private boolean teaplay = false;

    private Timer mTimer;
    private TimerTask mTimerTask;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度

//                    stucurrentTime = stuMediaPlayer.getCurrentPosition();
//                    if (stucurrentTime<0){
//                        stucurrentTime =0;
//                    }
////                    LogUtils.LOGE(tag,stucurrentTime+"--"+stutotalTime);
//                    int leftTime = ( stutotalTime -  stucurrentTime) / 1000;
//                    soundtime.setText(leftTime + "\"");
                    break;
                default:
                    break;
            }

        }
    };
    private TextView tea_listen;
    private Sound sound;


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
        return R.layout.remen_sound_detail;
    }

    @Override
    protected void initView(View v) {

        String sid = getActivity().getIntent().getExtras().getString("data");

        if (AppShare.getIsLogin(getActivity())) {
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                            .getSoundDetail(getActivity(), sid, AppShare.getUserInfo(getActivity()).uid),
                    "getSoundDetail", SoundItemDetailFragment.this);

        }
        musicname = (TextView) v.findViewById(R.id.musicname);
        like = (TextView) v.findViewById(R.id.like);
        desc = (TextView) v.findViewById(R.id.desc);
        soundtime = (TextView) v.findViewById(R.id.soundtime);
        stu_listen = (TextView) v.findViewById(R.id.stu_listen);
        tea_listen = (TextView) v.findViewById(R.id.tea_listen);
        tea_name = (TextView) v.findViewById(R.id.tea_name);
        tectitle = (TextView) v.findViewById(R.id.tectitle);
        commenttime = (TextView) v.findViewById(R.id.commenttime);
        created = (TextView) v.findViewById(R.id.created);
        views = (TextView) v.findViewById(R.id.views);

        stu_header = (ImageView) v.findViewById(R.id.stu_header);
        tea_header = (ImageView) v.findViewById(R.id.tea_header);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag, netResponse.toString());
                Gson gson = new Gson();
                sound = gson.fromJson(netResponse.data, Sound.class);


                desc.setText(sound.desc);
                soundtime.setText(sound.soundtime + "\"");
                tea_name.setText(sound.tecname);
                musicname.setText(sound.musicname);
                tectitle.setText(sound.tectitle);
                commenttime.setText(sound.commenttime + "\"");
//        created.setText(sound.created);
                views.setText(sound.views + "");
                like.setText(sound.like + "");


                PictureUtils.showPicture(getActivity(), sound.tecimage, tea_header);
                PictureUtils.showPicture(getActivity(), sound.stuimage, stu_header);


                String2TimeUtils string2TimeUtils = new String2TimeUtils();
                long currentTimeMillis = System.currentTimeMillis() / 1000;

                long time = currentTimeMillis - sound.edited;
                created.setText(string2TimeUtils.long2Time(time) + "前");

                stuUrl = MyNetApiConfig.ImageServerAddr + sound.soundpath;

                stufileName = stuUrl.substring(
                        stuUrl.lastIndexOf("/") + 1,
                        stuUrl.length());
                stufileName = sound.musicname + "_" + stufileName;
                stuFile = new File(Variable.StorageMusicCachPath, stufileName);

                teaUrl = MyNetApiConfig.ImageServerAddr + sound.commentpath;
                teafileName = teaUrl.substring(
                        teaUrl.lastIndexOf("/") + 1,
                        teaUrl.length());
                teafileName = sound.musicname + "_" + teafileName;
                teaFile = new File(Variable.StorageMusicCachPath, teafileName);

            } else {
                getActivity().finish();

            }
        }

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        stu_listen.setOnClickListener(this);
        tea_listen.setOnClickListener(this);

//        stuMediaPlayer = new MediaPlayer();
//        teaMediaPlayer = new MediaPlayer();
//        teaMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                stuplay = false;
//            }
//        });
//        stuMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                stuplay = false;
//            }
//        });

        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_listen:

                if (stuFile.exists()) {
                    if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {  //正在播放，点击暂停
                        stucurrentTime = VoiceFunction.pauseVoice(stuFile.getAbsolutePath());
                        if (mTimer == null) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                        if (mTimerTask == null) {
                            mTimerTask.cancel();
                            mTimerTask = null;
                        }

                    } else {     //暂停，点击播放
//                            if (!stufirst) {
//                                init(mFile.toString());
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
                            stufirst = true;
                        }

//                            }

//                            stuplay = true;
                        //    stutotalTime = stuMediaPlayer.getDuration();
                        // stuMediaPlayer.start();

                    }


                } else {
                    String path = Variable.StorageMusicCachPath;
                    DownLoaderTask task = new DownLoaderTask(stuUrl, path, stufileName, getActivity());
                    task.execute();
                }


                break;
            case R.id.tea_listen:

//                    if (teaFile.exists()) {
//                       // Log.e(tag, "file " + teaFile.getName() + " already exits!!");
//
//                        if (teaplay) {  //正在播放，点击暂停
//                            if (teaMediaPlayer != null) {
//                                stuPause();
//                            }
//                            teaplay = false;
//                        } else {     //暂停，点击播放
//                            if (!teafirst) {
////                                init(mFile.toString());
//                                teaMediaPlayer.reset();
//                                try {
//                                    teaMediaPlayer.setDataSource(teaFile.getAbsolutePath());
//                                    teaMediaPlayer.prepare();// 准备
//
//                                } catch (IllegalArgumentException e) {
//                                    e.printStackTrace();
//                                } catch (IllegalStateException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                //----------定时器记录播放进度---------//
//                                if (mTimer == null) {
//                                    mTimer = new Timer();
//                                }
//                                if (mTimerTask == null) {
//                                    mTimerTask = new TimerTask() {
//                                        @Override
//                                        public void run() {
//                                            mHandler.sendEmptyMessage(1);
//
//                                        }
//                                    };
//                                    mTimer.schedule(mTimerTask, 1000, 1000);
//                                    teafirst = true;
//                                }
//
//                            }
//
//                            teaplay = true;
//                            teatotalTime = teaMediaPlayer.getDuration();
//                            teaMediaPlayer.start();
//
//                        }
//
//
//                    } else {
//
//                        DownLoaderTeaTask t = new DownLoaderTeaTask(teaUrl, Variable.StorageMusicCachPath, teafileName, getActivity());
//                        t.execute();
//                    }


                break;
        }
    }

    @Override
    public void playVoiceBegin() {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoiceFinish() {

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

            if (!stufirst) {
//                                init(mFile.toString());
//                stuMediaPlayer.reset();
//                try {
//                    stuMediaPlayer.setDataSource(mFile.getAbsolutePath());
//                    stuMediaPlayer.prepare();// 准备

//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

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
                    stufirst = true;
                }
            }

            stuplay = true;
//            stutotalTime = stuMediaPlayer.getDuration();
//            stuMediaPlayer.start();

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

    public class DownLoaderTeaTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private int mProgress = 0;
        private ProgressReportingOutputStream mOutputStream;
        private Activity mContext = null;
        private int contentLength = 1;

        public DownLoaderTeaTask(String downloadPath, String out, String fileName, Activity context) {
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
                    tea_listen.setText(values[0].intValue() * 100 / contentLength + "%");
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

            if (!teafirst) {
//                                init(mFile.toString());
//                teaMediaPlayer.reset();
                try {
//                    teaMediaPlayer.setDataSource(mFile.getAbsolutePath());
//                    teaMediaPlayer.prepare();// 准备

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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
                            mHandler.sendEmptyMessage(1);
                        }
                    };
                    mTimer.schedule(mTimerTask, 1000, 1000);
                    teafirst = true;
                }
            }

            teaplay = true;
//            teatotalTime = teaMediaPlayer.getDuration();
//            teaMediaPlayer.start();

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


    @Override
    public void onDestroy() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }
        super.onDestroy();
    }
}
