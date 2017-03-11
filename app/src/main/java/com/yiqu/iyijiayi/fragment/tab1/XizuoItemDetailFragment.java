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

import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Variable;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
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
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/20.
 */

public class XizuoItemDetailFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface {
    String tag = "SoundItemDetailFragmentbak";
    private TextView like;
    private TextView musicname;
    private TextView desc;
    private TextView soundtime;
    private TextView stu_listen;
    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private Xizuo xizuo;

    private int totalTime;
    private int currentTime;
    //处理进度条更新
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
                    soundtime.setText(--totalTime + "\"");
                    if (totalTime == 0) {
                        totalTime = 1;
                    }
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
    private DownLoaderTask task;
    private ImageView musictype;

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

        String sid = getActivity().getIntent().getExtras().getString("data");

        if (AppShare.getIsLogin(getActivity())) {
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                            .getSoundDetail(getActivity(), sid, AppShare.getUserInfo(getActivity()).uid),
                    "getSoundDetail", XizuoItemDetailFragment.this);

        }

        musicname = (TextView) v.findViewById(R.id.musicname);
        like = (TextView) v.findViewById(R.id.like);
        desc = (TextView) v.findViewById(R.id.desc);
        soundtime = (TextView) v.findViewById(R.id.soundtime);
        stu_listen = (TextView) v.findViewById(R.id.stu_listen);
        created = (TextView) v.findViewById(R.id.created);
        views = (TextView) v.findViewById(R.id.views);
        stu_header = (ImageView) v.findViewById(R.id.stu_header);
        musictype = (ImageView) v.findViewById(R.id.musictype);
        stu_listen.setOnClickListener(this);

    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                Gson gson = new Gson();
                 xizuo = gson.fromJson(netResponse.data, Xizuo.class);
                desc.setText(xizuo.desc);
                soundtime.setText(xizuo.soundtime + "\"");
                musicname.setText(xizuo.musicname);

                views.setText(xizuo.views + "");
                like.setText(xizuo.like + "");
                if (xizuo.type==1){
                    musictype.setImageResource(R.mipmap.shengyue);
                }else {
                    musictype.setImageResource(R.mipmap.boyin);
                }


                PictureUtils.showPicture(getActivity(), xizuo.stuimage, stu_header);

                String2TimeUtils string2TimeUtils = new String2TimeUtils();
                long currentTimeMillis = System.currentTimeMillis() / 1000;

                long time = currentTimeMillis - xizuo.edited;
                created.setText(string2TimeUtils.long2Time(time));

                url = MyNetApiConfig.ImageServerAddr + xizuo.soundpath;
                fileName = url.substring(
                        url.lastIndexOf("/") + 1,
                        url.length());
                fileName = xizuo.musicname + "_" + fileName;
                mFile = new File(Variable.StorageMusicCachPath, fileName);

            } else {
                getActivity().finish();
            }
        }


    }

    @Override
    protected void init(Bundle savedInstanceState) {


        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_listen:

                if (mFile.exists()) {
                    palyVoice();
                } else {
                    task = new DownLoaderTask(url, Variable.StorageMusicCachPath, fileName, getActivity());
                    task.execute();
                }
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
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        currentTime = 0;

        soundtime.setText(xizuo.soundtime + "\"");

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
            if (isCancelled()) {
                return;
            }
            palyVoice();

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


    private void palyVoice() {

        totalTime = xizuo.soundtime;

        if (VoiceFunction.IsPlayingVoice(mFile.getAbsolutePath())) {  //正在播放，点击暂停
            currentTime = VoiceFunction.pauseVoice(mFile.getAbsolutePath());
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }

        } else {     //暂停，点击播放

            if (mTimer == null) {
                mTimer = new Timer();
            }
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {

                        mHandler.sendEmptyMessage(0);
                        LogUtils.LOGE(tag, "000");

                    }
                };
                mTimer.schedule(mTimerTask, 1000, 1000);

            }

            if (currentTime > 0) {


                totalTime = sub(xizuo.soundtime, currentTime);
                VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), this, currentTime);
            } else {
                VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), this, 0);
            }

        }
    }

    public static int sub(int totalTime, int currentTime) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(totalTime));
        BigDecimal b2 = new BigDecimal(Double.valueOf(currentTime) / 1000);
        return b1.subtract(b2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }


    @Override
    public void onDestroy() {

        VoiceFunction.StopVoice();
        currentTime = 0;

        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true); // 如果Task还在运行，则先取消它
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
