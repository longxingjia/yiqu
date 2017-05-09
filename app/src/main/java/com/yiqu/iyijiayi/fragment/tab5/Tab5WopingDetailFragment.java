package com.yiqu.iyijiayi.fragment.tab5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import com.utils.LogUtils;
import com.utils.Player;
import com.utils.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Function.VoiceFunctionF2;
import com.yiqu.Tool.Global.RecordConstant;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.UploaderTask;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Administrator on 2017/2/20.
 */

public class Tab5WopingDetailFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface, VoiceRecorderOperateInterface {
    String tag = "Tab5WopingDetailFragment";
    private TextView like;
    private TextView musicname;
    private TextView desc;

    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private int totalTime = 10 * 60;

    private String fileName;
    private String url;
    private File mFile;

    private ImageView musictype;
    private Sound sound;
    private ImageView send;
    private ImageView record;
    private ImageView new_record;
    private ImageView play;
    private boolean mIsRecording = false;
    private Animation rotate;
    //  private RecorderAndPlayUtil mRecorderUtil;
    private int recordTime;
    private TextView msecond;
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
//    private Player player;

    private boolean is2mp3 = true;
    private boolean recordComFinish = false;


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

    @Override
    public void recordVoiceBegin() {

    }

    @Override
    public void recordVoiceStateChanged(int volume, long recordDuration) {

        if (recordDuration > 0) {
            recordTime = (int) (recordDuration / RecordConstant.OneSecond);
            int leftTime = totalTime - recordTime;
            msecond.setText(String.valueOf(recordTime));

            if (leftTime <= 0) {
                VoiceFunctionF2.StopRecordVoice(is2mp3);
            }
        }

    }

    @Override
    public void prepareGiveUpRecordVoice() {

    }

    @Override
    public void recoverRecordVoice() {

    }

    @Override
    public void giveUpRecordVoice() {

    }

    @Override
    public void recordVoiceFail() {

    }

    @Override
    public void recordVoiceFinish() {

    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int pro;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            if (mPlayService != null) {
                pro = (progress * mPlayService.getDuration()
                        / seekBar.getMax());
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            mPlayService.seek(pro);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        desc.setText(sound.desc);
        musicname.setText(sound.musicname);
        soundtime.setText(string2TimeUtils.stringForTimeS(sound.soundtime));

        if (sound.type == 1) {
            musictype.setImageResource(R.mipmap.shengyue);
        } else if ((sound.type == 2)) {
            musictype.setImageResource(R.mipmap.boyin);
        } else {
            question_type.setVisibility(View.GONE);
        }

        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

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
        // mRecorderUtil = new RecorderAndPlayUtil(getActivity());
        recordTime = 0;
        super.init(savedInstanceState);
    }

    private void startAnimation() {
        recording.startAnimation(rotate);

    }

    private void stopAnimation() {
        recording.clearAnimation();

    }

    @Override
    public void onStart() {
        super.onStart();

        allowBindService(getActivity());

    }

    /**
     * stop时， 回调通知activity解除绑定服务
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.e("fragment", "onDestroyView");
        allowUnbindService(getActivity());
    }

    @OnClick({R.id.close_tips, R.id.video_play})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_tips:

                tips.setVisibility(View.GONE);

                break;
            case R.id.video_play:

                if (mPlayService.getPlayingPosition() > 0) {
                    if (mPlayService.isPlaying()) {
                        video_play.setImageResource(R.mipmap.video_play);
                        mPlayService.pause();
                    } else {
                        video_play.setImageResource(R.mipmap.video_pause);
                        mPlayService.resume();
                    }
                } else {
                    mPlayService.play(url, sound.sid);
                    video_play.setImageResource(R.mipmap.video_pause);
                }


                break;
            case R.id.send:
                if (recordComFinish) {
                    final Map<String, String> params = new HashMap<String, String>();

                    params.put("type", String.valueOf(1));
                    if (!TextUtils.isEmpty(recordPath)) {
                        File file = new File(recordPath);
                        if (file.exists()) {
                            upLoaderTask u = new upLoaderTask(getActivity(), MyNetApiConfig.uploadSounds.getPath(), params, file);
                            u.execute();
                        }
                    }
                }

                break;
            case R.id.record:
                if (mPlayService != null) {
                    if (mPlayService.isPlaying()) {
                        video_play.setImageResource(R.mipmap.video_play);
                        mPlayService.pause();
                    }
                }
                VoiceFunctionF2.StartRecordVoice(is2mp3, this);
                startAnimation();
                recordPath = VoiceFunctionF2.getRecorderPath();
                record.setVisibility(View.GONE);
                recording.setVisibility(View.VISIBLE);
                stop_text.setVisibility(View.VISIBLE);
                mIsRecording = true;


                break;
            case R.id.new_record:


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("重新录制");
                builder.setMessage("确定删除已录制作品，重新录制？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  recordVoiceButton.setText(getResources().getString(R.string.start_recording));

                        recordComFinish = false;
                        VoiceFunctionF2.StopRecordVoice(is2mp3);
                        VoiceFunctionF2.StopVoice();

                        msecond.setText("0");
                        record.setVisibility(View.VISIBLE);
                        recording.setVisibility(View.GONE);
                        stop_text.setVisibility(View.GONE);
                    }
                });
                builder.show();


                break;
            case R.id.play:

                if (recordComFinish && !TextUtils.isEmpty(recordPath)) {

                    VoiceFunctionF2.StopVoice();
                    VoiceFunctionF2.PlayToggleVoice(recordPath, this);
                }
                break;
            case R.id.recording:
                if (mIsRecording) {
                    VoiceFunctionF2.StopRecordVoice(is2mp3);
                    stopAnimation();
                    mIsRecording = false;
                    recording.setVisibility(View.GONE);
                    record.setVisibility(View.VISIBLE);
                    stop_text.setVisibility(View.GONE);
                    recordComFinish = true;
                }


                break;
            case R.id.rejuse:
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.refuseReply, MyNetRequestConfig.refuseReply(
                                getActivity(), sound.sid + "", AppShare.getUserInfo(getActivity()).uid), "refuseReply", Tab5WopingDetailFragment.this);

                break;
        }
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

    }


    public static int sub(int totalTime, int currentTime) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(totalTime));
        BigDecimal b2 = new BigDecimal(Double.valueOf(currentTime) / 1000);
        return b1.subtract(b2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    Handler mHandler = new Handler();
    String2TimeUtils string2TimeUtils = new String2TimeUtils();

    @Override
    public void onPublish(final int progress) {
        super.onPublish(progress);

        if (mPlayService.getPlayingPosition() > 0) {
            seekbar.setProgress(progress * 100 / mPlayService.getDuration());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    now_time.setText(string2TimeUtils.stringForTimeS(progress / 1000));
                }
            });
        }
    }

    @Override
    public void onDestroy() {

        VoiceFunctionF2.StopVoice();
        if (mPlayService != null) {
            if (mPlayService.isPlaying()) {

                mPlayService.pause();

            }
        }

        if (mIsRecording) {
            VoiceFunctionF2.StopRecordVoice(is2mp3);
        }

        super.onDestroy();
    }

    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        if (mIsRecording) {
            //  StopRecording();

        } else {
            //   initRecord();
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
                        MyNetRequestConfig.soundReply(getActivity(), String.valueOf(sound.sid ), AppShare.getUserInfo(getActivity()).uid,
                                s,String.valueOf(recordTime ) , "1"),
                        "soundReply", Tab5WopingDetailFragment.this);

            } else {
                ToastManager.getInstance(getActivity()).showText(getString(R.string.net_error));

            }

            super.onPostExecute(s);
        }
    }
}
