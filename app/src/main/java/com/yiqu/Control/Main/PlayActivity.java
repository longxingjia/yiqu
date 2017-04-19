package com.yiqu.Control.Main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.ui.views.CircleImageView;
import com.yiqu.Tool.Function.VoiceFunction;
import com.yiqu.Tool.Global.Variable;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.fileutils.utils.Player;
import com.yiqu.iyijiayi.fileutils.utils.PlayerForLocal;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PlayUtils;
import com.yiqu.iyijiayi.view.LyricLoader;
import com.yiqu.iyijiayi.view.LyricView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends Activity
        implements VoicePlayerInterface ,NetCallBack{

    private String tag = "PlayActivity";
    //    private int width;
//    private int height;


    private TextView musicName;
    private TextView musictime;
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

    @BindView(R.id.pre)
    public ImageView pre;
    @BindView(R.id.now_time)
    public TextView now_time;
    @BindView(R.id.next)
    public ImageView next;
    @BindView(R.id.play)
    public ImageView play;
    @BindView(R.id.mode)
    public ImageView mode;
    @BindView(R.id.upload)
    public ImageView upload;

    @BindView(R.id.lyricview)
    public LyricView lyricView;

    @BindView(R.id.pre_bg)
    public CircleImageView pre_bg;
    @BindView(R.id.play_bg)
    public CircleImageView play_bg;
    @BindView(R.id.next_bg)
    public CircleImageView next_bg;
    @BindView(R.id.seekbar)
    public SeekBar seekbar;

    private final static int Mode_LIST = 2000;
    private final static int Mode_ONE = 2001;
    private final static int Mode_RANDOM = 2002;
    private int currentMode = Mode_LIST;


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
    private PlayerForLocal player;
    private ArrayList<ComposeVoice> composeVoices;
    private int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init(R.layout.play_activity);
    }

    private void init(int layoutId) {
        setContentView(layoutId);
        ButterKnife.bind(this);
        bindView();
        composeVoices = (ArrayList<ComposeVoice>) getIntent().getSerializableExtra("data");
        position = getIntent().getIntExtra("position", 0);
        voice = composeVoices.get(position);
        initData();
    }

    public void bindView() {

        rlHint = (RelativeLayout) findViewById(R.id.hint);
        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        content = (TextView) findViewById(R.id.content);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new PlayerForLocal(this, seekbar, null, now_time, musictime, onPlayCompletion);

    }


    public void initData() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
        //   voice = (ComposeVoice) getIntent().getSerializableExtra("ff");

        //    LogUtils.LOGE(tag, composeVoices.toString());


        musicName.setText(voice.musicname);

        startAnimation();
        //  VoiceFunction.PlayToggleVoice(Variable.StorageMusicPath + voice.voicename, instance);

        play.setImageResource(R.mipmap.icon_pause);
       player.stop();

        player.playUrl(Variable.StorageMusicPath + voice.voicename);
        handler.post(runnable);
        // player.onCompletion

        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);

        Bitmap bitmap = BitmapUtil.createColorBitmap(this, Color.parseColor(color[i]));

        pre_bg.setImageBitmap(bitmap);
        play_bg.setImageBitmap(bitmap);
        next_bg.setImageBitmap(bitmap);

       String   fileName = voice.musicname + "_" + voice.mid;
        File    lrc = new File(Variable.StorageMusicCachPath, fileName + ".lrc");
        if (!lrc.exists()) {

            MyNetApiConfig myNetApiConfig = new MyNetApiConfig(voice.musicname);

            RestNetCallHelper.callNet(this,
                    myNetApiConfig.getlyric, MyNetRequestConfig
                            .getlyric(this),
                    "getlyric", instance);
        } else {
            LogUtils.LOGE(tag,fileName);
            lyricView.initLyricFile(LyricLoader.loadLyricFile(fileName));
            lyricView.invalidate();
        }




    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (player.isPlaying()) {
                int time = player.getCurrentPosition();

                LogUtils.LOGE(tag, player.getDuration()+"");
                lyricView.updateLyrics( time, player.getDuration());
                handler.postDelayed(this, 100);

            }

        }
    };

    PlayerForLocal.onPlayCompletion onPlayCompletion = new PlayerForLocal.onPlayCompletion() {
        @Override
        public void completion() {
            switch (currentMode) {
                case Mode_LIST:
                    position++;
                    if (position>=composeVoices.size()){
                        position =0;
                    }
                    voice = composeVoices.get(position);
                    initData();

                    break;
                case Mode_ONE:
                    player.playUrl(Variable.StorageMusicPath + voice.voicename);
                    break;
                case Mode_RANDOM:
                    Random r = new Random();
                    position = r.nextInt(composeVoices.size());
                    voice = composeVoices.get(position);
                    initData();
                    break;
            }


        }
    };

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

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
        musictime.setText(0 + "\"");
        mTimer.cancel();
        mTimerTask.cancel();
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }


    @OnClick({R.id.play_bg, R.id.next_bg, R.id.pre_bg, R.id.upload, R.id.mode, R.id.title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_bg:

                switch (currentMode) {
                    case Mode_LIST:
                    case Mode_ONE:
                        position--;
                        if (position < 0) {
                            position = composeVoices.size() - 1;
                        }
                        voice = composeVoices.get(position);
                        initData();

                        //   player.playUrl(Variable.StorageMusicPath + voice.voicename);
                        break;
                    case Mode_RANDOM:
                        Random r = new Random();
                        position = r.nextInt(composeVoices.size());
                        voice = composeVoices.get(position);
                        initData();
                        break;
                }


                break;

            case R.id.next_bg:

                switch (currentMode) {
                    case Mode_LIST:
                    case Mode_ONE:
                        position++;
                        if (position>=composeVoices.size()) {
                            position = 0;
                        }
                        voice = composeVoices.get(position);
                        initData();

                        //   player.playUrl(Variable.StorageMusicPath + voice.voicename);
                        break;
                    case Mode_RANDOM:
                        Random r = new Random();
                        position = r.nextInt(composeVoices.size());
                        voice = composeVoices.get(position);
                        initData();
                        break;
                }


                break;

            case R.id.play_bg:

                if (player.isPlaying()) {
                    play.setImageResource(R.mipmap.icon_play);
                    player.pause();
                //    handler.removeCallbacks(runnable);
                } else {
                    play.setImageResource(R.mipmap.icon_pause);
                    player.playUrl(Variable.StorageMusicPath + voice.voicename);
                    handler.post(runnable);
                }
                break;

            case R.id.title_back:
                VoiceFunction.StopVoice();
                finish();


                break;

            case R.id.mode:
                LogUtils.LOGE(tag, currentMode + "");
                if (currentMode == Mode_LIST) {
                    currentMode = Mode_ONE;
                    mode.setImageResource(R.mipmap.mode_on);
                } else if (currentMode == Mode_ONE) {
                    currentMode = Mode_RANDOM;
                    mode.setImageResource(R.mipmap.mode_random);
                } else if (currentMode == Mode_RANDOM) {
                    currentMode = Mode_LIST;
                    mode.setImageResource(R.mipmap.mode_list);
                }


                break;

            case R.id.upload:

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
                menuDialogSelectTeaHelper.show(upload);
                break;
        }

    }

    @Override
    protected void onDestroy() {

        player.stop();
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
