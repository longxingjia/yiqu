package com.yiqu.Control.Main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.utils.ToastManager;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Function.VoiceFunction;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogGiveupRecordHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogListerner;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.db.ComposeVoiceInfoDBHelper;
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.RecorderAndPlayUtil;

import java.util.Timer;
import java.util.TimerTask;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class RecordOnlyActivity extends Activity
        implements VoicePlayerInterface, View.OnClickListener {
    private boolean recordComFinish = false;
    private String tag = "RecordOnlyActivity";
    private TextView tv_record;
    private TextView musicName;
    private TextView musictime;
    private TextView recordVoiceButton;
    private String className;
    private TextView recordHintTextView;
    private TextView recordDurationView;
    private boolean mIsRecording = false;
    private boolean mIsLittleTime = false;
    private boolean mIsSendVoice = false;
    private static RecordOnlyActivity instance;
    private RelativeLayout rlHint;
    private ImageView title_back;
    private ImageView image_anim;
    private Animation rotate;
    private ComposeVoice composeVoice;
    private RecorderAndPlayUtil mRecorderUtil;
    private TimerTask mTimerTask = null;
    private Timer mTimer = null;
    private int mSecond = 0;
    private static final int MSG_TIME_SHORT = 0x123;
    private static final int POPUPWINDOW = 0x124;
    private EditText desc;
    private TextView content;
    private String mRecorderPath;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_SHORT:

                    break;
                case POPUPWINDOW:
                    // 这里是位置显示方式,在屏幕的左侧
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PermissionGen.needPermission(this, 100, Manifest.permission.RECORD_AUDIO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void init(int layoutId) {
        setContentView(layoutId);

        bindView();
        className = getClass().getSimpleName();
        instance = this;
        initData();
    }

    public void bindView() {
        recordHintTextView = (TextView) findViewById(R.id.recordHintTextView);
        recordDurationView = (TextView) findViewById(R.id.musictime);
        rlHint = (RelativeLayout) findViewById(R.id.hint);

        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
//        musicSize = (TextView) findViewById(R.id.musicSize);
        tv_record = (TextView) findViewById(R.id.tv_record);
        content = (TextView) findViewById(R.id.content);
        recordVoiceButton = (TextView) findViewById(R.id.recordVoiceButton);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
//        composeProgressBar = (ProgressBar) findViewById(R.id.composeProgressBar);

        title_back.setOnClickListener(this);
    }


    public void initData() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果

        mRecorderUtil = new RecorderAndPlayUtil(this);

        recordVoiceButton.setOnClickListener(this);
        mHandler.sendEmptyMessageDelayed(POPUPWINDOW, 200);
        recordHintTextView.setText("按下开始录音");
     //   File mFile = new File(Variable.StorageMusicCachPath, "红豆词_1474598402.mp3");
     //   VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), instance);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("录制录音页面");
        MobclickAgent.onResume(this);


    }

    @Override
    protected void onPause() {
//        VoiceFunction.StopVoice();
        super.onPause();
        MobclickAgent.onPageEnd("录制录音页面");
        MobclickAgent.onPause(this);
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
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recordVoiceButton:
                if (recordComFinish) {
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("composeVoice", composeVoice);
                    MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(instance, new MenuDialogSelectTeaHelper.TeaListener() {
                        @Override
                        public void onTea(int tea) {
                            switch (tea) {

                                case 0:
                                    Intent intent = new Intent(instance, StubActivity.class);
                                    intent.putExtra("fragment", AddQuestionFragment.class.getName());
                                    intent.putExtras(bundle);
                                    instance.startActivity(intent);
                                    VoiceFunction.StopVoice();

                                    break;
                                case 1:

                                    Intent i = new Intent(instance, StubActivity.class);
                                    i.putExtra("fragment", UploadXizuoFragment.class.getName());
                                    i.putExtras(bundle);
                                    instance.startActivity(i);
                                    VoiceFunction.StopVoice();

                                    break;
                            }

                        }
                    });
                    menuDialogSelectTeaHelper.show(recordVoiceButton);
                } else {
                    rlHint.setVisibility(View.INVISIBLE);
                    if (mIsRecording) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("完成录制");
                        builder.setMessage("确定要完成录制吗？");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                recordHintTextView.setText("已结束录音");
                                if (mIsRecording) {
                                    initRecording();
                                    recordComFinish = true;
                                }
                                VoiceFunction.PlayToggleVoice(mRecorderPath, instance);

                                stopAnimation();
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    } else {
                        startAnimation();
                        down();

                        recordVoiceButton.setText("完成录制");
                    }
                }
                break;

            case R.id.title_back:
                exit();
                break;
        }

    }






    private void initRecording() {
        composeVoice = new ComposeVoice();
        composeVoice.fromuid = AppShare.getUserInfo(instance).uid;
        composeVoice.mid = 0;
        composeVoice.type = "2";
        composeVoice.musicname = musicName.getText().toString();
        composeVoice.musictype ="";
        composeVoice.chapter = "";
        composeVoice.desc = desc.getText().toString();
        composeVoice.accompaniment = "";
        composeVoice.soundtime = mSecond;
        composeVoice.isformulation = "0";
        composeVoice.isopen = "1";
        composeVoice.status = "1";
        composeVoice.listenprice = "1";
        composeVoice.questionprice = "0";
        composeVoice.commenttime = "0";
        composeVoice.commentpath = "";
        composeVoice.touid = 0;
        composeVoice.soundpath = "";

        LogUtils.LOGE(tag,mRecorderPath);

        composeVoice.voicename = mRecorderPath.substring(
                mRecorderPath.lastIndexOf("/") + 1,
                mRecorderPath.length());

        composeVoice.isreply = "0";
        composeVoice.ispay = "0";
        composeVoice.createtime = System.currentTimeMillis();

        ComposeVoiceInfoDBHelper composeVoiceInfoDBHelper = new ComposeVoiceInfoDBHelper(instance);
        composeVoiceInfoDBHelper.insert(composeVoice, ComposeVoiceInfoDBHelper.UNCOMPOSE);
        composeVoiceInfoDBHelper.close();

        if (mTimer != null) {
            mTimer.cancel();
            mTimerTask.cancel();
        }
        mRecorderUtil.stopRecording();

        mIsRecording = false;
        mSecond = 0;
//        mRecorderUtil.getRecorderPath();
    }

    @Override
    protected void onDestroy() {
        if (mIsRecording) {
            initRecording();
        }
        VoiceFunction.StopVoice();
        super.onDestroy();
    }

    private void exit() {
        if (mIsRecording) {
            MenuDialogGiveupRecordHelper menuDialogGiveupRecordHelper = new MenuDialogGiveupRecordHelper(instance, new MenuDialogListerner() {
                @Override
                public void onSelected(int selected) {
                    switch (selected) {
                        case 0:

                            // mIsRecording = false;
                            if (mIsRecording) {
                                initRecording();
                            }
//                            VoiceFunction.StopRecordVoice();
                            stopAnimation();
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

    private void stopAnimation() {
        image_anim.clearAnimation();

    }


    private void down() {
        if (mTimer != null) mTimer.cancel();
        if (mTimerTask != null) mTimerTask.cancel();

        mSecond = 0;
        mIsRecording = true;
        mIsLittleTime = true;
        mTimerTask = new TimerTask() {
            int i = 300;

            @Override
            public void run() {
                mIsLittleTime = false;
                mSecond += 1;
                i--;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        musictime.setText(mSecond + "\"");

                    }
                });
                if (i == 0) {
                    mIsSendVoice = true;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 录音结束
                            mTimer.cancel();
                            mTimerTask.cancel();
                            mIsRecording = false;
                            mRecorderUtil.stopRecording();

                        }
                    });
                }
                if (i < 0) {
                    mTimer.cancel();
                    mTimerTask.cancel();
                }
            }
        };
        mRecorderUtil.startRecording();
        mRecorderPath = mRecorderUtil.getRecorderPath();

        mTimer = new Timer(true);
        mTimer.schedule(mTimerTask, 1000, 1000);

    }

    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        init(R.layout.record_voice_fragment);
    }

    @PermissionFail(requestCode = 100)
    public void failContact() {

        Toast.makeText(this, getString(R.string.permission_record_hint), Toast.LENGTH_SHORT).show();
        finish();
        PermissionUtils.openSettingActivity(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

}