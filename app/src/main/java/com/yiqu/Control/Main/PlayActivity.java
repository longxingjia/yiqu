package com.yiqu.Control.Main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.umeng.analytics.MobclickAgent;
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
import com.yiqu.iyijiayi.model.Voice;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;

import java.util.Timer;
import java.util.TimerTask;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class PlayActivity extends Activity
        implements VoicePlayerInterface, View.OnClickListener {

    private String tag = "RecordActivityForRecordFrag";
    //    private int width;
//    private int height;


    private TextView musicName;
    private TextView musictime;
    private TextView recordVoiceButton;

    private static PlayActivity instance;
    //    private Music music;
    private RelativeLayout rlHint;
    private ImageView title_back;
    private ImageView image_anim;
    private Animation rotate;
    private TimerTask mTimerTask = null;
    private Timer mTimer = null;

    private static final int MSG_TIME_SHORT = 0x123;
    private EditText desc;
    private TextView content;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_SHORT:

                    break;

            }
        }
    };
    private ComposeVoice voice;
    private int mtime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init(R.layout.record_voice_fragment);
    }

    private void init(int layoutId) {
        setContentView(layoutId);
        bindView();
        instance = this;
        initData();
    }

    public void bindView() {

        rlHint = (RelativeLayout) findViewById(R.id.hint);
        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        content = (TextView) findViewById(R.id.content);
        recordVoiceButton = (TextView) findViewById(R.id.recordVoiceButton);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);

        title_back.setOnClickListener(this);
    }


    public void initData() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
        recordVoiceButton.setOnClickListener(this);

        voice = (ComposeVoice) getIntent().getSerializableExtra("data");
        musicName.setText(voice.musicname);
        recordVoiceButton.setText("去提问");
        LogUtils.LOGE(tag, voice.toString());
        startAnimation();
        VoiceFunction.PlayToggleVoice(Variable.StorageMusicPath + voice.voicename, instance);

        mtime = voice.soundtime;
        musictime.setText(mtime + "\"");
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mtime--;
                if (mtime < 0) {
                    mtime = 0;
                    mTimer.cancel();
                    mTimerTask.cancel();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        musictime.setText(mtime + "\"");

                    }
                });

            }
        };
        mTimer = new Timer(true);
        mTimer.schedule(mTimerTask, 1000, 1000);

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
        stopAnimation();
        mTimer.cancel();
        mTimerTask.cancel();
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recordVoiceButton:

                final Bundle bundle = new Bundle();
                bundle.putSerializable("composeVoice", voice);
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

                break;

            case R.id.title_back:
                VoiceFunction.StopVoice();
                finish();


                break;
        }

    }

    @Override
    protected void onDestroy() {
        VoiceFunction.StopVoice();
        super.onDestroy();
    }


    private void startAnimation() {
        image_anim.startAnimation(rotate);

    }

    private void stopAnimation() {
        image_anim.clearAnimation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("播放录音或者声乐页面");
        MobclickAgent.onResume(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("播放录音或者声乐页面");
        MobclickAgent.onPause(this);
    }
}
