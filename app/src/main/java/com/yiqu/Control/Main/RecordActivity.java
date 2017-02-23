package com.yiqu.Control.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Tool.Function.AudioFunction;
import com.Tool.Function.CommonFunction;
import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Constant;
import com.Tool.Global.Variable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.yiqu.Tool.Interface.ComposeAudioInterface;
import com.yiqu.Tool.Interface.DecodeOperateInterface;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogGiveupRecordHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogListerner;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.db.DownloadMusicInfoDBHelper;
import com.yiqu.iyijiayi.db.UploadVoiceInfoDBHelper;
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.UploadVoice;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.FileSizeUtil;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.utils.Tools;

public class RecordActivity extends Activity
        implements VoicePlayerInterface, DecodeOperateInterface, ComposeAudioInterface, VoiceRecorderOperateInterface, View.OnClickListener {
    private boolean recordVoiceBegin;
    private boolean recordComFinish = false;
    private String tag = "RecordActivity";
    //    private int width;
//    private int height;
    private int recordTime;
    private int actualRecordTime;
    private TextView tv_record;
    private TextView musicName;
    private TextView musictime;
    private TextView musicSize;
    private TextView recordVoiceButton;
    private String className;
    private String tempVoicePcmUrl;
    private String musicFileUrl;
    private String decodeFileUrl;
    private String composeVoiceUrl;

    private TextView recordHintTextView;
    private TextView recordDurationView;
//
//    private Button composeVoiceButton;
//    private Button deleteVoiceButton;
//    private Button playComposeVoiceButton;

    private ProgressBar composeProgressBar;

    private static RecordActivity instance;
    private Music music;
    private RelativeLayout rlHint;
    private ImageView title_back;
    private ImageView image_anim;
    private Animation rotate;
    private String fileNameCom;
    private UploadVoice uploadVoice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init(R.layout.record_xizuo_fragment);
    }

    private void init(int layoutId) {
        setContentView(layoutId);

        bindView();
        initView();
        initData();

        className = getClass().getSimpleName();

        instance = this;
    }

    public void bindView() {
        recordHintTextView = (TextView) findViewById(R.id.recordHintTextView);
        recordDurationView = (TextView) findViewById(R.id.musictime);
        rlHint = (RelativeLayout) findViewById(R.id.hint);

        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        musicSize = (TextView) findViewById(R.id.musicSize);
        tv_record = (TextView) findViewById(R.id.tv_record);
        recordVoiceButton = (TextView) findViewById(R.id.recordVoiceButton);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
        composeProgressBar = (ProgressBar) findViewById(R.id.composeProgressBar);

        title_back.setOnClickListener(this);
    }

    public void initView() {
//        composeProgressBar.getLayoutParams().width = (int) (width * 0.72);
    }

    public void initData() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果

        Intent intent = getIntent();
        music = (Music) intent.getSerializableExtra("music");
        LogUtils.LOGE(tag, music.toString());
        musicName.setText(music.musicname + "");
        Tools.DB_PATH = Variable.StorageDirectoryPath;
        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        String fileName = Url.substring(
                Url.lastIndexOf("/") + 1,
                Url.length());
        fileName = music.musicname + "_" + fileName;
        String2TimeUtils string2TimeUtils = new String2TimeUtils();
        musictime.setText(string2TimeUtils.stringForTimeS(music.time));

        if (!TextUtils.isEmpty(Tools.DB_PATH)) {
            File mFile = new File(Tools.DB_PATH, fileName);
            if (mFile.exists()) {
//                Log.e(tag, "file " + mFile.getName() + " already exits!!");
                musicSize.setText(FileSizeUtil.getAutoFileOrFilesSize(mFile.getAbsolutePath()));
                recordTime = 0;
                tempVoicePcmUrl = Tools.DB_PATH + music.musicname + "_tempVoice.pcm";
                LogUtils.LOGE(tag, tempVoicePcmUrl);
                musicFileUrl = mFile.getAbsolutePath();
                decodeFileUrl = Tools.DB_PATH + music.musicname + "_decodeFile.pcm";
                fileNameCom = music.musicname + "_composeVoice.mp3";
                composeVoiceUrl = Tools.DB_PATH + music.musicname + "_composeVoice.mp3";
                recordVoiceButton.setOnClickListener(this);
            }
        }

        recordHintTextView.setText("按下开始录音");
    }


    private void goRecordSuccessState() {
        recordVoiceBegin = false;

//        recordVoiceButton.setEnabled(false);
//        deleteVoiceButton.setEnabled(true);
//        composeVoiceButton.setEnabled(true);
//        playComposeVoiceButton.setEnabled(false);

        recordDurationView.setText(CommonFunction.FormatRecordTime(actualRecordTime));

        recordHintTextView.setText("完成录音");
    }

    private void goRecordFailState() {
        recordVoiceBegin = false;

        recordDurationView.setVisibility(View.INVISIBLE);

        recordVoiceButton.setEnabled(true);

        recordHintTextView.setText("点击开始录音");
    }

    private void compose() {
        composeProgressBar.setProgress(0);
        recordHintTextView.setText("合成开始");
        composeProgressBar.setVisibility(View.VISIBLE);
        AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                actualRecordTime + Constant.MusicCutEndOffset, this);
    }

    @Override
    public void recordVoiceBegin() {
        VoiceFunction.StopVoice();

        if (!recordVoiceBegin) {
            recordVoiceBegin = true;

            recordTime = 0;

            recordDurationView.setText(CommonFunction.FormatRecordTime(recordTime));

            recordDurationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void recordVoiceStateChanged(int volume, long recordDuration) {
        if (recordDuration > 0) {
            recordTime = (int) (recordDuration / Constant.OneSecond);

            recordDurationView.setText(CommonFunction.FormatRecordTime(recordTime));
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
        if (recordVoiceBegin) {
            if (actualRecordTime != 0) {
                goRecordSuccessState();
            } else {
                goRecordFailState();
            }
        }
    }

    @Override
    public void recordVoiceFinish() {
        if (recordVoiceBegin) {
            actualRecordTime = recordTime;
            goRecordSuccessState();
        }
    }

    @Override
    public void playVoiceBegin() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_pause);
    }

    @Override
    public void playVoiceFail() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }

    @Override
    public void playVoiceFinish() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }

    @Override
    public void updateDecodeProgress(int decodeProgress) {
        composeProgressBar.setProgress(
                decodeProgress * Constant.MaxDecodeProgress / Constant.NormalMaxProgress);
    }

    @Override
    public void decodeSuccess() {
        composeProgressBar.setProgress(Constant.MaxDecodeProgress);

        AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
                Constant.VoiceWeight, Constant.VoiceBackgroundWeight,
                -1 * Constant.MusicCutEndOffset / 2 * Constant.RecordDataNumberInOneSecond, this);


    }

    @Override
    public void decodeFail() {
        recordHintTextView.setText("解码失败,请您检查网络后，再次尝试");

        composeProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateComposeProgress(int composeProgress) {
        composeProgressBar.setProgress(
                composeProgress * (Constant.NormalMaxProgress - Constant.MaxDecodeProgress) /
                        Constant.NormalMaxProgress + Constant.MaxDecodeProgress);
    }

    @Override
    public void composeSuccess() {
        recordHintTextView.setText("合成成功，可播放合成语音");
        composeProgressBar.setVisibility(View.GONE);
        VoiceFunction.PlayToggleVoice(composeVoiceUrl, instance);

        CommonFunction.showToast("合成成功", className);
        recordVoiceButton.setText("完成");
        recordComFinish = true;
        clearAnimation();
        uploadVoice = new UploadVoice();
        uploadVoice.fromuid = AppShare.getUserInfo(instance).uid;
        uploadVoice.mid = music.mid;
        uploadVoice.musicname = music.musicname;
        uploadVoice.musictype = music.musictype;
        uploadVoice.chapter = music.chapter;
        uploadVoice.accompaniment = music.accompaniment;
        uploadVoice.soundtime = actualRecordTime;
        uploadVoice.isformulation = music.isformulation;
        uploadVoice.isopen = "1";
        uploadVoice.status = "1";
        uploadVoice.listenprice = "1";
        uploadVoice.questionprice = "0";
        uploadVoice.commenttime = "0";
        uploadVoice.commentpath = "";
        uploadVoice.touid = 0;
        uploadVoice.soundpath = "";
        uploadVoice.voicename = fileNameCom;
        uploadVoice.type = music.type + "";
        uploadVoice.isreply = "0";
        uploadVoice.ispay = "0";

        UploadVoiceInfoDBHelper uploadVoiceInfoDBHelper = new UploadVoiceInfoDBHelper(instance);
        uploadVoiceInfoDBHelper.insert(uploadVoice);
        uploadVoiceInfoDBHelper.close();
    }

    @Override
    public void composeFail() {
        recordHintTextView.setText("合成失败");
        composeProgressBar.setVisibility(View.GONE);
        CommonFunction.showToast("合成失败", className);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recordVoiceButton:
                if (recordComFinish) {
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("uploadVoice", uploadVoice);
                    MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(instance, new MenuDialogSelectTeaHelper.TeaListener() {
                        @Override
                        public void onTea(int tea) {
                            switch (tea) {

                                case 0:
                                    Intent intent = new Intent(instance, StubActivity.class);
                                    intent.putExtra("fragment", AddQuestionFragment.class.getName());

                                    intent.putExtras(bundle);
                                    instance.startActivity(intent);

                                    break;
                                case 1:

                                    Intent i = new Intent(instance, StubActivity.class);
                                    i.putExtra("fragment", UploadXizuoFragment.class.getName());

                                    i.putExtras(bundle);

                                    instance.startActivity(i);


                                    break;
                            }

                        }
                    });
                    menuDialogSelectTeaHelper.show(recordVoiceButton);
                } else {
                    rlHint.setVisibility(View.INVISIBLE);
                    if (recordVoiceBegin) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("完成录制");
                        builder.setMessage("当前伴奏还没有结束，确定要提前完成录制吗？");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                recordHintTextView.setText("已结束录音");
//                                recordVoiceButton.setText(getResources().getString(R.string.start_recording));
                                VoiceFunction.StopVoice();
                                VoiceFunction.StopRecordVoice();
                                compose();

                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    } else {
                        startAnimation();
//
                        VoiceFunction.StartRecordVoice(tempVoicePcmUrl,
                                instance);
                        VoiceFunction.PlayToggleVoice(musicFileUrl, instance);
                        recordVoiceButton.setText("完成录制");
                    }
                }
                break;

            case R.id.title_back:
                exit();


                break;
        }

    }

    @Override
    protected void onDestroy() {
        VoiceFunction.StopVoice();
        VoiceFunction.StopRecordVoice();
        super.onDestroy();
    }

    private void exit() {
        if (recordVoiceBegin) {
            MenuDialogGiveupRecordHelper menuDialogGiveupRecordHelper = new MenuDialogGiveupRecordHelper(instance, new MenuDialogListerner() {
                @Override
                public void onSelected(int selected) {
                    switch (selected) {
                        case 0:
                            actualRecordTime = 0;
                            recordVoiceBegin = false;
                            VoiceFunction.StopVoice();
                            VoiceFunction.StopRecordVoice();
                            clearAnimation();
                            recordVoiceButton.setText(getResources().getString(R.string.start_recording));
                            recordHintTextView.setText("按下开始录音");

                            break;
                        case 1:
                            VoiceFunction.StopRecordVoice();
                            VoiceFunction.StopVoice();
                            finish();
                            break;
                    }
                }
            });
            menuDialogGiveupRecordHelper.show(recordVoiceButton);
        } else {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
//            dialog();
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startAnimation() {
        image_anim.startAnimation(rotate);

    }

    private void clearAnimation() {
        image_anim.clearAnimation();

    }


}
