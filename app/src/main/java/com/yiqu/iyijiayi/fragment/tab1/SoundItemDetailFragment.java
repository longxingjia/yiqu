package com.yiqu.iyijiayi.fragment.tab1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.DialogUtil;
import com.ui.views.DialogView;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.fragment.Tab5Fragment;
import com.yiqu.iyijiayi.fragment.tab5.PayforYBFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.MathUtils;
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
import java.math.BigDecimal;
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
    private int teatotalTime;
    private int stutotalTime;
    private int stucurrentTime;
    private int teacurrentTime;
    private String stufileName;
    private String teafileName;
    private String stuUrl;
    private String teaUrl;
    private File stuFile;
    private File teaFile;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView tea_listen;
    private Sound sound;
    private DownLoaderTask taskS;
    private DownLoaderTeaTask taskT;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
                    if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {
                        soundtime.setText(--stutotalTime + "\"");
                        if (stutotalTime == 0) {
                            stutotalTime = 1;
                        }
                    } else if (VoiceFunction.IsPlayingVoice(teaFile.getAbsolutePath())) {
                        commenttime.setText(--teatotalTime + "\"");
                        if (teatotalTime == 0) {
                            teatotalTime = 1;
                        }
                    }
                    break;
                default:

                    break;
            }

        }
    };
    private ImageView musictype;
    private AlertDialog dialog;
    private UserInfo userInfo;
    private String sid;


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

        sid = getActivity().getIntent().getExtras().getString("data");

        if (AppShare.getIsLogin(getActivity())) {
            userInfo = AppShare.getUserInfo(getActivity());
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                            .getSoundDetail(getActivity(), sid, userInfo.uid),
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
        musictype = (ImageView) v.findViewById(R.id.musictype);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("eavesdrop")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), sid, userInfo.uid),
                        "getSoundDetail", SoundItemDetailFragment.this);
                userInfo.coin_apple --;
                AppShare.setUserInfo(getActivity(),userInfo);
            }

            ToastManager.getInstance(getActivity()).showText(netResponse.result);

        } else if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                LogUtils.LOGE(tag,netResponse.toString());
                Gson gson = new Gson();
                sound = gson.fromJson(netResponse.data, Sound.class);

                desc.setText(sound.desc);
                soundtime.setText(sound.soundtime + "\"");

                tea_name.setText(sound.tecname);
                musicname.setText(sound.musicname);
                tectitle.setText(sound.tectitle);
                commenttime.setText(sound.commenttime + "\"");
                soundtime.setText(sound.soundtime + "\"");
                views.setText(sound.views + "");
                like.setText(sound.like + "");
                if (sound.type == 1) {
                    musictype.setImageResource(R.mipmap.shengyue);
                } else {
                    musictype.setImageResource(R.mipmap.boyin);
                }

                long t = System.currentTimeMillis() / 1000 - sound.created;
                if (t < 2 * 24 * 60 * 60 && t > 0) {
                    tea_listen.setText("限时免费听");
                } else {
                    if (sound.listen == 1) {
                        tea_listen.setText("已付费");
                    } else {
                        tea_listen.setText("1元偷偷听");
                    }
                }

                PictureUtils.showPicture(getActivity(), sound.tecimage, tea_header);
                PictureUtils.showPicture(getActivity(), sound.stuimage, stu_header);

                String2TimeUtils string2TimeUtils = new String2TimeUtils();
                long currentTimeMillis = System.currentTimeMillis() / 1000;

                long time = currentTimeMillis - sound.edited;
                created.setText(string2TimeUtils.long2Time(time));

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

        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_listen:

                if (stuFile.exists()) {
                    palyStudentVoice();

                } else {
                    String path = Variable.StorageMusicCachPath;
                    taskS = new DownLoaderTask(stuUrl, path, stufileName, getActivity());
                    taskS.execute();
                }


                break;
            case R.id.tea_listen:
                long t = System.currentTimeMillis() / 1000 - sound.created;


                if (t < 2 * 24 * 60 * 60 && t > 0) {
                    if (teaFile.exists()) {
                        palyTeacherVoice();

                    } else {
                        String path = Variable.StorageMusicCachPath;
                        taskT = new DownLoaderTeaTask(teaUrl, path, teafileName, getActivity());
                        taskT.execute();
                    }
                } else {
                    if (sound.listen == 1) {
                        if (teaFile.exists()) {
                            palyTeacherVoice();

                        } else {
                            String path = Variable.StorageMusicCachPath;
                            taskT = new DownLoaderTeaTask(teaUrl, path, teafileName, getActivity());
                            taskT.execute();
                        }
                    } else {
                        String desc = "";
                        if (userInfo.coin_apple > 1) {
                            desc = "支付";
                        } else {
                            desc = "去充值";
                        }

                        dialog = DialogUtil.showMyDialog(getActivity(), "使用艺币支付", "偷听需支付100艺币，当前余额"
                                + userInfo.coin_apple + "00艺币", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        }, desc, new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (userInfo.coin_apple >= 1) {

                                    RestNetCallHelper.callNet(getActivity(),
                                            MyNetApiConfig.eavesdrop, MyNetRequestConfig
                                                    .eavesdrop(getActivity(), userInfo.uid, sound.sid),
                                            "eavesdrop", SoundItemDetailFragment.this);
                                } else {
                                    Model.startNextAct(getActivity(),
                                            PayforYBFragment.class.getName());
                                }
                                dialog.dismiss();

                            }
                        });
                    }
                }

                break;
        }
    }



    private void palyStudentVoice() {

        teacurrentTime = 0;

        stutotalTime = sound.soundtime;
        teatotalTime = sound.commenttime;
        commenttime.setText(teatotalTime + "\"");

        if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {  //正在播放，点击暂停
            stucurrentTime = VoiceFunction.pauseVoice(stuFile.getAbsolutePath());
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

                    }
                };
                mTimer.schedule(mTimerTask, 1000, 1000);

            }

            if (stucurrentTime > 0) {

                stutotalTime = MathUtils.sub(sound.soundtime, stucurrentTime);
                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, stucurrentTime);
            } else {
                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, 0);
            }

        }
    }

    private void palyTeacherVoice() {
        stucurrentTime = 0;
        teatotalTime = sound.commenttime;
        stutotalTime = sound.soundtime;
        soundtime.setText(stutotalTime + "\"");


        if (VoiceFunction.IsPlayingVoice(teaFile.getAbsolutePath())) {  //正在播放，点击暂停
            teacurrentTime = VoiceFunction.pauseVoice(teaFile.getAbsolutePath());
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

                    }
                };
                mTimer.schedule(mTimerTask, 1000, 1000);

            }
            if (teacurrentTime > 0) {
                teatotalTime = MathUtils.sub(sound.commenttime, teacurrentTime);
                VoiceFunction.PlayToggleVoice(teaFile.getAbsolutePath(), this, teacurrentTime);
            } else {
                VoiceFunction.PlayToggleVoice(teaFile.getAbsolutePath(), this, 0);
            }
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
//        LogUtils.LOGE(tag, "----");

//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
//        if (mTimerTask != null) {
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }
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

        stucurrentTime = 0;
        teacurrentTime = 0;
        LogUtils.LOGE(tag, stucurrentTime + "----" + teacurrentTime);
        commenttime.setText(sound.commenttime + "\"");
        soundtime.setText(sound.soundtime + "\"");

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
            super.onPostExecute(result);
            if (isCancelled()) {
                mFile.delete();
                return;
            }
            palyStudentVoice();


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
            tea_listen.setText("");
//
        }

        @Override
        protected Long doInBackground(Void... params) {
            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if (values.length > 1) {
                contentLength = values[1];
                if (contentLength == -1) {
                } else {
                }
            } else {
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

            if (isCancelled()) {
                mFile.delete();
                return;
            }
            palyTeacherVoice();

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

        VoiceFunction.StopVoice();
        stucurrentTime = 0;
        teacurrentTime = 0;

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }

        if (taskT != null && taskT.getStatus() == AsyncTask.Status.RUNNING) {
            taskT.cancel(true); // 如果Task还在运行，则先取消它
        }
        if (taskS != null && taskS.getStatus() == AsyncTask.Status.RUNNING) {
            taskS.cancel(true); // 如果Task还在运行，则先取消它
        }
        super.onDestroy();
    }
}
