package com.yiqu.Control.Main;

import android.annotation.SuppressLint;
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

import cn.zhaiyifan.lyric.LyricUtils;
import cn.zhaiyifan.lyric.widget.LyricView ;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends Activity
        implements NetCallBack {

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
    //   private TimerTask mTimerTask = null;
    private Timer mTimer = new Timer();

    private static final int MSG_TIME_SHORT = 0x123;
//    private EditText desc;
//    private TextView content;

    @BindView(R.id.pre)
    public ImageView pre;
    @BindView(R.id.now_time)
    public TextView now_time;
    @BindView(R.id.totaltime)
    public TextView totaltime;
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
    private MediaPlayer voicePlayer;

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
        voicePlayer = new MediaPlayer();
        mTimer.schedule(mTimerTask, 0, 1000);
        voicePlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                voicePlayer.start();
            }
        });

        voicePlayer.setOnCompletionListener(onCompletion);

        initData();
    }

    public void bindView() {

        rlHint = (RelativeLayout) findViewById(R.id.hint);
        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

    }


    public void initData() {
//        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
//        LinearInterpolator lin = new LinearInterpolator();
//        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果

        musicName.setText(voice.musicname);


        play.setImageResource(R.mipmap.icon_pause);

        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);

        Bitmap bitmap = BitmapUtil.createColorBitmap(this, Color.parseColor(color[i]));

        pre_bg.setImageBitmap(bitmap);
        play_bg.setImageBitmap(bitmap);
        next_bg.setImageBitmap(bitmap);

        String fileName = voice.musicname + "_" + voice.mid;
        playUrl(Variable.StorageMusicPath + voice.voicename);
        File lrc = new File(Variable.StorageMusicCachPath, fileName + ".lrc");
        if (!lrc.exists()) {

            MyNetApiConfig myNetApiConfig = new MyNetApiConfig(voice.musicname);

            RestNetCallHelper.callNet(this,
                    myNetApiConfig.getlyric, MyNetRequestConfig
                            .getlyric(this),
                    "getlyric", instance);
        } else {
            //  LogUtils.LOGE(tag,fileName);
//            lyricView.initLyricFile(LyricLoader.loadLyricFile(fileName));
//            lyricView.invalidate();

            lyricView.setLyric(  LyricUtils.parseLyric(lrc,"utf-8"));
            lyricView.play();
        }


    }

    private Handler handler = new Handler();

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if (player.isPlaying()) {
//                int time = player.getCurrentPosition();
//
//                LogUtils.LOGE(tag, player.getDuration()+"");
//                lyricView.updateLyrics( time, player.getDuration());
//                handler.postDelayed(this, 100);
//
//            }
//
//        }
//    };


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
            this.progress = (int) (progress * voicePlayer.getDuration()
                    / seekBar.getMax());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            voicePlayer.seekTo(progress);
        }
    }

    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {

            if (voicePlayer.isPlaying() && seekbar.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handleProgress = new Handler() {


        public void handleMessage(Message msg) {
            int position = voicePlayer.getCurrentPosition();
            int duration = voicePlayer.getDuration();

            if (duration > 0) {
                long pos = seekbar.getMax() * position / duration;
                seekbar.setProgress((int) pos);
                now_time.setText("" + changeNum(position / (60 * 1000)) + ":" + changeNum(position % (60 * 1000) / 1000));
                totaltime.setText("" + changeNum(duration / (60 * 1000)) + ":" + changeNum(duration % (60 * 1000) / 1000));
            }


        }

        ;
    };

    public String changeNum(int num) {
        if (num == 0) {
            return "00";
        }
        if (num == 1) {
            return "01";
        }
        if (num == 2) {
            return "02";
        }
        if (num == 3) {
            return "03";
        }
        if (num == 4) {
            return "04";
        }
        if (num == 5) {
            return "05";
        }
        if (num == 6) {
            return "06";
        }
        if (num == 7) {
            return "07";
        }
        if (num == 8) {
            return "08";
        }
        if (num == 9) {
            return "09";
        }

        return "" + num;
    }


    MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            switch (currentMode) {
                case Mode_LIST:
                    position++;
                    if (position >= composeVoices.size()) {
                        position = 0;
                    }
                    voice = composeVoices.get(position);
                    initData();
                    break;
                case Mode_ONE:
                    initData();
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
                        if (position >= composeVoices.size()) {
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

                if (voicePlayer.isPlaying()) {
                    play.setImageResource(R.mipmap.icon_play);
                    voicePlayer.pause();
                    //    handler.removeCallbacks(runnable);
                } else {
                    play.setImageResource(R.mipmap.icon_pause);
                    voicePlayer.start();

                    //    handler.post(runnable);
                }
                break;

            case R.id.title_back:
                //  VoiceFunction.StopVoice();
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
                                //   VoiceFunction.StopVoice();

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
                menuDialogSelectTeaHelper.show(upload);
                break;
        }

    }

    private void playUrl(String url) {
        try {
            voicePlayer.reset();
            voicePlayer.setDataSource(url);
            voicePlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {

        //  player.stop();
        if (voicePlayer != null) {
            voicePlayer.stop();
            voicePlayer.release();
            voicePlayer = null;
        }

        super.onDestroy();
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
