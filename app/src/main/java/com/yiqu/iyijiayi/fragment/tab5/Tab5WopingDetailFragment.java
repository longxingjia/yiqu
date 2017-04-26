package com.yiqu.iyijiayi.fragment.tab5;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yiqu.Tool.Function.VoiceFunction;
import com.yiqu.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.UploaderTask;
import com.yiqu.iyijiayi.fileutils.utils.Player;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.RecorderAndPlayUtil;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Administrator on 2017/2/20.
 */

public class Tab5WopingDetailFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface {
    String tag = "Tab5WopingDetailFragment";
    private TextView like;
    private TextView musicname;
    private TextView desc;

    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private int totalTime;
    private int currentTime;
    //处理进度条更新
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
                  //  soundtime.setText(--totalTime + "\"");
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
    private Timer mTimer;
    private TimerTask mTimerTask;
    private ImageView musictype;
    private Sound sound;
    private ImageView send;
    private ImageView record;
    private ImageView new_record;
    private ImageView play;
    private boolean mIsRecording = false;
    private Animation rotate;
    private RecorderAndPlayUtil mRecorderUtil;
    private int recordTime;
    private boolean mIsLittleTime;
    private int mSecond;
    private TextView msecond;
    private TimerTask timerTask;
    private Timer timer;
    private String recordPath;
    private ImageView recording;
    private TextView stop_text;
    private ImageView rejuse;

    @BindView(R.id.question_type)
    public LinearLayout question_type;
    @BindView(R.id.tips)
    public LinearLayout tips;
    @BindView(R.id.close_tips)
    public ImageView close_tips;
    @BindView(R.id.video_play)
    public ImageView video_play;
    @BindView(R.id.seekbar)
    public SeekBar seekbar;
    @BindView(R.id.now_time)
    public TextView now_time;
    @BindView(R.id.soundtime)
    public TextView soundtime;
    private Player player;


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
        setTitleText("待评详情");
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("待评详情"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("待评详情");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_dianping_detail;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        sound = (Sound) getActivity().getIntent().getExtras().getSerializable("data");

        musicname = (TextView) v.findViewById(R.id.musicname);

        desc = (TextView) v.findViewById(R.id.desc);

        msecond = (TextView) v.findViewById(R.id.msecond);
        created = (TextView) v.findViewById(R.id.created);

        stu_header = (ImageView) v.findViewById(R.id.stu_header);
        musictype = (ImageView) v.findViewById(R.id.musictype);
        recording = (ImageView) v.findViewById(R.id.recording);
        rejuse = (ImageView) v.findViewById(R.id.rejuse);

        recording.setOnClickListener(this);
        rejuse.setOnClickListener(this);
        stop_text = (TextView) v.findViewById(R.id.stop_text);
        send = (ImageView) v.findViewById(R.id.send);
        record = (ImageView) v.findViewById(R.id.record);
        new_record = (ImageView) v.findViewById(R.id.new_record);
        play = (ImageView) v.findViewById(R.id.play);
        send.setOnClickListener(this);
        record.setOnClickListener(this);
        new_record.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = (int) (progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        desc.setText(sound.desc);
        musicname.setText(sound.musicname);


        if (sound.type == 1) {
            musictype.setImageResource(R.mipmap.shengyue);
        } else if ((sound.type ==2)){
            musictype.setImageResource(R.mipmap.boyin);
        }else {
            question_type.setVisibility(View.GONE);
        }

        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new Player(getActivity(), seekbar, video_play, now_time, soundtime, new Player.onPlayCompletion() {
            @Override
            public void completion() {

            }
        });

        PictureUtils.showPicture(getActivity(), sound.stuimage, stu_header);

        String2TimeUtils string2TimeUtils = new String2TimeUtils();
        long currentTimeMillis = System.currentTimeMillis() / 1000;

        long time = currentTimeMillis - sound.edited;
        created.setText(string2TimeUtils.long2Time(time));

        url = MyNetApiConfig.ImageServerAddr + sound.soundpath;
        fileName = url.substring(
                url.lastIndexOf("/") + 1,
                url.length());
        fileName = sound.musicname + "_" + fileName;
        mFile = new File(Variable.StorageMusicCachPath, fileName);
        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
        mRecorderUtil = new RecorderAndPlayUtil(getActivity());
        recordTime = 0;
        super.init(savedInstanceState);
    }

    private void startAnimation() {
        recording.startAnimation(rotate);

    }

    private void stopAnimation() {
        recording.clearAnimation();

    }

    @OnClick({R.id.close_tips,R.id.video_play})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_tips:

                tips.setVisibility(View.GONE);

                break;
            case R.id.video_play:
                if (player.isPlaying()) {
                    video_play.setImageResource(R.mipmap.video_play);
                    player.pause();
                } else {
                    video_play.setImageResource(R.mipmap.video_pause);
                    if (url.equals(player.getUrl())){
                        player.rePlay();
                    }else {
                        player.playUrl(url);
                    }
                }

                break;
            case R.id.send:
                final Map<String, String> params = new HashMap<String, String>();

                params.put("type", String.valueOf(1));
                if (!TextUtils.isEmpty(mRecorderUtil.getRecorderPath())) {
                    File file = new File(mRecorderUtil.getRecorderPath());
                    if (file.exists()) {
                        upLoaderTask u = new upLoaderTask(getActivity(), MyNetApiConfig.uploadSounds.getPath(), params, file);
                        u.execute();
                    }
                }


                break;
            case R.id.record:
                PermissionGen.needPermission(this, 100, Manifest.permission.RECORD_AUDIO);

                break;
            case R.id.new_record:
                if (mIsRecording) {
                    StopRecording();
                }
                msecond.setText("0");
                record.setVisibility(View.VISIBLE);
                recording.setVisibility(View.GONE);
                stop_text.setVisibility(View.GONE);

                break;
            case R.id.play:
                if (!TextUtils.isEmpty(recordPath))
                    VoiceFunction.PlayToggleVoice(recordPath, this);
                break;
            case R.id.recording:
                if (mIsRecording) {
                    StopRecording();
                }

                break;
            case R.id.rejuse:
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.refuseReply, MyNetRequestConfig.refuseReply(
                                getActivity(), sound.sid + "", AppShare.getUserInfo(getActivity()).uid), "refuseReply", Tab5WopingDetailFragment.this);

                break;
        }
    }

    private void initRecord() {
        if (timerTask != null) timerTask.cancel();
        if (timer != null) timer.cancel();

        mSecond = 0;
        mIsRecording = true;
        mIsLittleTime = true;
        // 录音结束
        timerTask = new TimerTask() {
            int i = 120;

            @Override
            public void run() {
                mIsLittleTime = false;
                mSecond += 1;
                i--;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        msecond.setText(mSecond+ "");

                    }
                });
                if (i == 0) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 录音结束
                            timer.cancel();
                            timerTask.cancel();
                            mIsRecording = false;
                            mRecorderUtil.stopRecording();
                            StopRecording();
                        }
                    });
                }
                if (i < 0) {
                    timer.cancel();
                    timerTask.cancel();
                }
            }
        };
        startAnimation();
        record.setVisibility(View.GONE);
        recording.setVisibility(View.VISIBLE);
        stop_text.setVisibility(View.VISIBLE);
        mRecorderUtil.startRecording();
        timer = new Timer(true);
        timer.schedule(timerTask, 1000, 1000);

    }

    @Override
    public void playVoiceBegin(long duration) {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoiceStateChanged(long currentDuration) {

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
    }



//
//    private void palyVoice() {
//
//        totalTime = sound.soundtime;
//
//        if (VoiceFunction.IsPlayingVoice(mFile.getAbsolutePath())) {  //正在播放，点击暂停
//            currentTime = VoiceFunction.pauseVoice(mFile.getAbsolutePath());
//            if (mTimer != null) {
//                mTimer.cancel();
//                mTimer = null;
//            }
//            if (mTimerTask != null) {
//                mTimerTask.cancel();
//                mTimerTask = null;
//            }
//
//        } else {     //暂停，点击播放
//
//            if (mTimer == null) {
//                mTimer = new Timer();
//            }
//            if (mTimerTask == null) {
//                mTimerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        mHandler.sendEmptyMessage(0);
//                    }
//                };
//                mTimer.schedule(mTimerTask, 1000, 1000);
//
//            }
//
//            if (currentTime > 0) {
//
//                totalTime = sub(sound.soundtime, currentTime);
//                VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), this, currentTime);
//            } else {
//                VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), this, 0);
//            }
//
//        }
//    }

    public static int sub(int totalTime, int currentTime) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(totalTime));
        BigDecimal b2 = new BigDecimal(Double.valueOf(currentTime) / 1000);
        return b1.subtract(b2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }


    @Override
    public void onDestroy() {
        player.stop();
        VoiceFunction.StopVoice();
        currentTime = 0;
        if (mIsRecording) {
            StopRecording();
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }
        super.onDestroy();
    }

    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        if (mIsRecording) {
            StopRecording();

        } else {
            initRecord();
        }
    }

    private void StopRecording() {
        stopAnimation();

        mRecorderUtil.stopRecording();
        mIsRecording = false;
        recordPath = mRecorderUtil.getRecorderPath();
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
        }
    }

    @PermissionFail(requestCode = 100)
    public void failContact() {
        ToastManager.getInstance(getActivity()).showText(getString(R.string.permission_record_hint));
        getActivity().finish();
        PermissionUtils.openSettingActivity(getActivity());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("refuseReply")) {
            if (type == TYPE_SUCCESS) {
                rejuse.setVisibility(View.INVISIBLE);
                ToastManager.getInstance(getActivity()).showText("拒绝成功");
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);

            }

        } else if (id.equals("soundReply")) {
            if (type == TYPE_SUCCESS) {
                ToastManager.getInstance(getActivity()).showText("回复成功");
            } else {
                ToastManager.getInstance(getActivity()).showText("回复失败");
            }
        }

    }

    private class upLoaderTask extends UploaderTask {
        private DialogHelper dialogHelper;

        public upLoaderTask(Context context, String RequestURL, Map<String, String> params, File file) {
            super(context, RequestURL, params, file);
        }

        @Override
        protected void onPreExecute() {
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(getActivity(), this, 100);
                dialogHelper.showProgressDialog();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialogHelper.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();

            }
            if (!TextUtils.isEmpty(s)) {
                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.soundReply,
                        MyNetRequestConfig.soundReply(getActivity(), sound.sid + "", AppShare.getUserInfo(getActivity()).uid, s, mSecond + "", "1"),
                        "soundReply", Tab5WopingDetailFragment.this);

            } else {
                ToastManager.getInstance(getActivity()).showText(getString(R.string.net_error));

            }

            super.onPostExecute(s);
        }
    }
}
