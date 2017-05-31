package com.yiqu.Control.Main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.service.Download;
import com.service.DownloadService;
import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Function.AudioFunction;
import com.yiqu.Tool.Function.CommonFunction;
import com.yiqu.Tool.Function.FileFunction;
import com.yiqu.Tool.Function.LogFunction;
import com.yiqu.Tool.Function.UpdateFunction;
import com.yiqu.Tool.Function.VoiceFunctionF2;
import com.czt.mp3recorder.RecordConstant;
import com.utils.Variable;
import com.yiqu.Tool.Interface.ComposeAudioInterface;
import com.yiqu.Tool.Interface.DecodeOperateInterface;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogGiveupRecordHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogListerner;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.db.ComposeVoiceInfoDBHelper;
import com.db.DownloadMusicInfoDBHelper;
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.model.ComposeVoice;
import com.yiqu.iyijiayi.model.LyricDownInfo;
import com.model.Music;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.AppInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.yiqu.iyijiayi.utils.DensityUtil;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.LyrcUtil;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.lyric.DefaultLrcBuilder;
import com.yiqu.lyric.ILrcBuilder;
import com.yiqu.lyric.LrcRow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class RecordComActivity extends Activity
        implements VoicePlayerInterface, DecodeOperateInterface, ComposeAudioInterface,
        VoiceRecorderOperateInterface, View.OnClickListener, NetCallBack {
    private static final int REQUESTUPLOAD = 0x111;
    private boolean recordVoiceBegin;
    private boolean recordComFinish = false;
    private String tag = "RecordComActivity";

    private int recordTime;
    private int actualRecordTime;
    private TextView tv_record;
    private TextView musicName;
    private TextView musictime;
    private boolean is2mp3 = false;
    private String className;

    private String tempVoicePcmUrl;
    private String musicFileUrl;
    private String decodeFileUrl;
    private String composeVoiceUrl;
    private String fileNameCom;

    private ProgressBar composeProgressBar;
    private static RecordComActivity instance;
    private Music music;
    private RelativeLayout rlHint;
    private ImageView title_back;
    private ImageView image_anim;
    private Animation rotate;

    private ComposeVoice composeVoice;
    private String2TimeUtils string2TimeUtils;
    private int totalTime;
    private AudioManager audoManager;

    @BindView(R.id.background)
    public ImageView background;


    @BindView(R.id.icon_record)
    public ImageView icon_record;
    @BindView(R.id.icon_finish)
    public ImageView icon_finish;
    @BindView(R.id.reset)
    public CircleImageView reset;
    @BindView(R.id.record)
    public CircleImageView recordVoiceButton;
    @BindView(R.id.finish)
    public CircleImageView finish;
    @BindView(R.id.lrcView)
    public com.yiqu.lyric.LrcView mLrcView;

    private File lrc;
    private String fileName;
    private TextView totaltime;
    private ProgressBar pb_record;
    protected DownloadService mDownloadService;
    private String lyricUrl;
    private String lyricname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance = this;
        init(R.layout.record_and_com_fragment);
    }

    private void init(int layoutId) {
        setContentView(layoutId);
        ButterKnife.bind(this);
        PermissionGen.needPermission(this, 100, Manifest.permission.RECORD_AUDIO);
        className = getClass().getSimpleName();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void bindView() {

        rlHint = (RelativeLayout) findViewById(R.id.hint);
        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        totaltime = (TextView) findViewById(R.id.totaltime);
        tv_record = (TextView) findViewById(R.id.tv_record);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
        composeProgressBar = (ProgressBar) findViewById(R.id.composeProgressBar);
        pb_record = (ProgressBar) findViewById(R.id.pb_record);
        title_back.setOnClickListener(this);

    }

    public void initView() {

    }

    private Bitmap createColorBitmap(int color) {
        Bitmap bmp = Bitmap.createBitmap(DensityUtil.dip2px(this, 60), DensityUtil.dip2px(this, 60), Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;
    }

    public void initData() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
        audoManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        music = (Music) intent.getSerializableExtra("music");
        string2TimeUtils = new String2TimeUtils();
        musicName.setText(music.musicname);
        totalTime = music.time;
        pb_record.setMax(music.time);

        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);
        Bitmap bitmap = createColorBitmap(Color.parseColor(color[i]));
        reset.setImageBitmap(bitmap);
        recordVoiceButton.setImageBitmap(bitmap);
        finish.setImageBitmap(bitmap);



        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        URI uri = URI.create(Url);
        fileName = FileFunction.getValidFileName(uri);
        File mFile = new File(Variable.StorageMusicCachPath(this), fileName);

        if (mFile.exists())
        {
            musicFileUrl = mFile.getAbsolutePath();
            getDuration(musicFileUrl);//设置音乐总时间
            String tempfileName = fileName.substring(0, fileName.lastIndexOf("."));
            decodeFileUrl = Variable.StorageMusicPath(instance) + tempfileName + ".pcm";
            recordTime = 0;
            long t = System.currentTimeMillis() / 1000;

            fileNameCom = music.musicname + t + "cv.mp3";
            composeVoiceUrl = Variable.StorageMusicPath(instance) + fileNameCom;
            recordVoiceButton.setOnClickListener(this);

        }

    }


    private void initBackground(File file) {
        PictureUtils.showPictureFile(instance, file, image_anim, 270);
        Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap b = BitmapUtil.blur(bt, 25f, this);
        Bitmap bb = BitmapUtil.blur(b, 25f, this);
        background.setImageBitmap(bb);
        //  background.setImageAlpha(120); //0完全透明，255
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("录制声乐页面");
        MobclickAgent.onResume(this);
        JAnalyticsInterface.onPageStart(this, "录制声乐页面");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("录制声乐页面");
        MobclickAgent.onPause(this);
        JAnalyticsInterface.onPageEnd(this, "录制声乐页面");
    }


    private void goRecordSuccessState() {
        recordVoiceBegin = false;
        musictime.setText(string2TimeUtils.stringForTimeS(actualRecordTime));
        pb_record.setProgress(actualRecordTime);

        icon_finish.setImageResource(R.mipmap.upload);
        recordComFinish = true;
        compose();

    }

    private void goRecordFailState() {
        recordVoiceBegin = false;
        // musictime.setVisibility(View.INVISIBLE);

    }

    private void getDuration(String url) {
        final MediaPlayer vp = new MediaPlayer();
        vp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                totalTime = vp.getDuration() / 1000;
                totaltime.setText(string2TimeUtils.stringForTimeS(totalTime));
                pb_record.setMax(totalTime);
                vp.release();

                DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(instance);
                Music m = downloadMusicInfoDBHelper.getDecode(music.mid);
                downloadMusicInfoDBHelper.close();
                if (m.isdecode == -1) {
                    AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                            totalTime, instance);
                    downloadMusicInfoDBHelper.updateDecode(music.mid, 0, System.currentTimeMillis());
                } else if (m.isdecode == 0 && System.currentTimeMillis() - m.decodetime > 2 * 60 * 1000) { //解码超过两分钟,重新解码。
                    File file = new File(decodeFileUrl);
                    file.delete();
                    AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                            totalTime, instance);
                    downloadMusicInfoDBHelper.updateDecode(music.mid, 0, System.currentTimeMillis());
                }
                downloadMusicInfoDBHelper.close();
            }
        });

        try {
            vp.reset();
            vp.setDataSource(url);
            vp.prepareAsync();
        } catch (Exception e) {

            UpdateFunction.ShowToastFromThread("播放语音文件失败");
            LogFunction.error("播放语音异常", e);
        }

    }

    @Override
    public void recordVoiceBegin() {
        if (!recordVoiceBegin) {
            recordVoiceBegin = true;

            recordTime = 0;
            musictime.setText(string2TimeUtils.stringForTimeS(recordTime));
            pb_record.setProgress(actualRecordTime);
            musictime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void recordVoiceStateChanged(int volume, long recordDuration) {
        if (recordDuration > 0) {
            recordTime = (int) (recordDuration / RecordConstant.OneSecond);
            int leftTime = totalTime - recordTime;
            musictime.setText(string2TimeUtils.stringForTimeS(recordTime));
            pb_record.setProgress(recordTime);

            if (leftTime <= 0) {
                VoiceFunctionF2.StopRecordVoice(is2mp3);
             //   compose();
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
        if (recordVoiceBegin) {
            if (actualRecordTime != 0) {
                // goRecordSuccessState();
                recordVoiceBegin = false;
                musictime.setText(string2TimeUtils.stringForTimeS(actualRecordTime));
                pb_record.setProgress(actualRecordTime);
            } else {
                goRecordFailState();
            }
        }
    }

    @Override
    public void recordVoiceFinish() {
        if (recordVoiceBegin && recordComFinish == false) {
            actualRecordTime = recordTime;
            goRecordSuccessState();
        }
    }


    @Override
    public void playVoiceBegin(long duration) {

    }

    @Override
    public void playVoiceFail() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }

    @Override
    public void playVoiceStateChanged(long currentDuration) {
     //   LogUtils.LOGE(tag,currentDuration+"");
        int playtime = (int) (currentDuration / RecordConstant.OneSecond);
        if (recordComFinish) {
            pb_record.setMax(recordTime);
            totaltime.setText(string2TimeUtils.stringForTimeS(recordTime));
            if (currentDuration > 0) {
                musictime.setText(string2TimeUtils.stringForTimeS(playtime));
                pb_record.setProgress(playtime);
            }
        }

        mLrcView.seekLrcToTime(currentDuration);

    }

    @Override
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {
        if (recordComFinish) {
            icon_record.setImageResource(R.mipmap.icon_play);
            upload();
        }
    }

    @Override
    public void updateDecodeProgress(int decodeProgress) {
//        composeProgressBar.setProgress(
//                decodeProgress * RecordConstant.MaxDecodeProgress / RecordConstant.NormalMaxProgress);
    }

    private DialogHelper dialogHelper;

    private void compose() {
        composeProgressBar.setProgress(0);
        composeProgressBar.setVisibility(View.VISIBLE);
        recordVoiceButton.setEnabled(false);
        if (audoManager.isWiredHeadsetOn()) { //true 带了耳机
            AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
                    RecordConstant.VoiceEarWeight, RecordConstant.VoiceEarBackgroundWeight,
                    0, this);
        } else {
            AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
                    RecordConstant.VoiceWeight, RecordConstant.VoiceBackgroundWeight,
                    0, this);
        }

        if (dialogHelper == null) {
            dialogHelper = new DialogHelper(instance, 100);
            dialogHelper.showProgressDialog();
        }
    }

    @Override
    public void decodeSuccess() {
        DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(this);
        downloadMusicInfoDBHelper.updateDecode(music.mid, 1, System.currentTimeMillis());
        downloadMusicInfoDBHelper.close();
    }

    @Override
    public void decodeFail() {
        ToastManager.getInstance(instance).showText("对不起，音频解码失败，请在设置意见反馈中提交您的机型。");
        composeProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateComposeProgress(int composeProgress) {
        if (composeProgress == 10 || composeProgress == 20 ||
                composeProgress == 30 || composeProgress == 40 ||
                composeProgress == 50 || composeProgress == 60 ||
                composeProgress == 70 || composeProgress == 80 ||
                composeProgress == 90 || composeProgress == 100) {
            composeProgressBar.setProgress(composeProgress);
            dialogHelper.setProgress(composeProgress);
        }
    }

    @Override
    public void composeSuccess() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);
        composeVoice = new ComposeVoice();
        composeVoice.fromuid = AppShare.getUserInfo(instance).uid;
        composeVoice.mid = music.mid;
        composeVoice.musicname = music.musicname;
        composeVoice.musictype = music.musictype;
        composeVoice.chapter = music.chapter;
        composeVoice.lrcpath = music.lrcpath;
        composeVoice.accompaniment = music.accompaniment;
        composeVoice.soundtime = actualRecordTime;
        composeVoice.isformulation = music.isformulation;
        composeVoice.isopen = "1";
        composeVoice.status = "1";
        composeVoice.listenprice = "1";
        composeVoice.questionprice = "0";
        composeVoice.commenttime = "0";
        composeVoice.commentpath = "";
        composeVoice.touid = 0;
        composeVoice.soundpath = "";
        composeVoice.voicename = composeVoiceUrl;
        composeVoice.type = music.type + "";
        composeVoice.isreply = "0";
        composeVoice.ispay = "0";
        composeVoice.createtime = System.currentTimeMillis();

        if (AppInfo.isForeground(instance, "RecordComActivity")) {
            icon_record.setImageResource(R.mipmap.icon_pause);
            VoiceFunctionF2.PlayToggleVoice(composeVoiceUrl, instance);
            CommonFunction.showToast("合成成功", className);
        }

        ComposeVoiceInfoDBHelper composeVoiceInfoDBHelper = new ComposeVoiceInfoDBHelper(instance);
        composeVoiceInfoDBHelper.insert(composeVoice, ComposeVoiceInfoDBHelper.COMPOSE);
        composeVoiceInfoDBHelper.close();

        if (dialogHelper != null) {
            dialogHelper.dismissProgressDialog();
            dialogHelper = null;
        }


    }

    @Override
    public void composeFail() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);
        CommonFunction.showToast("合成失败", className);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTUPLOAD:
                    finish();
                    break;
            }
        }
    }

    @OnClick({R.id.record, R.id.reset, R.id.finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record:
                rlHint.setVisibility(View.INVISIBLE);
                if (recordComFinish) {
                    if (recordVoiceBegin) {
                    } else {
                        if (VoiceFunctionF2.IsPlaying()) {
                            VoiceFunctionF2.pauseVoice();
                            icon_record.setImageResource(R.mipmap.icon_play);
                        } else {
                            VoiceFunctionF2.startPlayVoice();
                            icon_record.setImageResource(R.mipmap.icon_pause);
                        }
                    }
                } else if (recordVoiceBegin) {
                    if (VoiceFunctionF2.isPauseRecordVoice(is2mp3)) {
                        VoiceFunctionF2.restartRecording(is2mp3);
                        icon_record.setImageResource(R.mipmap.icon_pause);
                        VoiceFunctionF2.startPlayVoice();
                    } else {
                        VoiceFunctionF2.pauseRecordVoice(is2mp3);
                        icon_record.setImageResource(R.mipmap.icon_record);
                        VoiceFunctionF2.pauseVoice();
                    }
                } else {
                    VoiceFunctionF2.PlayToggleVoice(musicFileUrl, this);
                    VoiceFunctionF2.StartRecordVoice(is2mp3,instance, instance);
                    tempVoicePcmUrl = VoiceFunctionF2.getRecorderPath();
                    icon_record.setImageResource(R.mipmap.icon_pause);
                }
                break;
            case R.id.finish:
                if (recordComFinish) {
                    upload();
                } else if (recordVoiceBegin) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("完成录制");
                    builder.setMessage("确定要完成录制吗？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (recordTime > 9) {
                                VoiceFunctionF2.StopRecordVoice(is2mp3);
                                VoiceFunctionF2.StopVoice();

                            } else {
                                ToastManager.getInstance(instance).showText("录音时间要大于10秒钟");
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;
            case R.id.reset:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("重新录制");
                builder.setMessage("确定删除已录制作品，重新录制？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                });
                builder.show();

                break;
            case R.id.title_back:
                exit();
                break;

        }

    }

    private void reset() {
        icon_finish.setImageResource(R.mipmap.finish);
        icon_record.setImageResource(R.mipmap.icon_record);
        pb_record.setProgress(0);
        musictime.setText("00:00");
        actualRecordTime = 0;
        recordVoiceBegin = false;
        recordComFinish = false;
        totalTime = music.time;
        pb_record.setMax(music.time);
        totaltime.setText(string2TimeUtils.stringForTimeS(totalTime));
        pb_record.setMax(totalTime);

        VoiceFunctionF2.StopRecordVoice(is2mp3);
        VoiceFunctionF2.StopVoice();
    }

    private void upload() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable("composeVoice", composeVoice);
        VoiceFunctionF2.pauseVoice();
        icon_record.setImageResource(R.mipmap.icon_play);
        String title = "找个导师点评一下吗？";
        String[] items = new String[]{"免费上传作品", "找导师请教"};
        MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(instance, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
            @Override
            public void onTea(int tea) {
                switch (tea) {
                    case 1:
                        Intent intent = new Intent(instance, StubActivity.class);
                        intent.putExtra("fragment", AddQuestionFragment.class.getName());
                        intent.putExtras(bundle);
                        instance.startActivityForResult(intent, REQUESTUPLOAD);

                        break;
                    case 0:

                        Intent i = new Intent(instance, StubActivity.class);
                        i.putExtra("fragment", UploadXizuoFragment.class.getName());
                        i.putExtras(bundle);
                        instance.startActivityForResult(i, REQUESTUPLOAD);
                        //  mediaPlayer.stop();
                        //   playUtils.stop();
                        break;
                }

            }
        });
        menuDialogSelectTeaHelper.show(recordVoiceButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, DownloadService.class), mDownloadServiceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mDownloadServiceConnection);
    }

    @Override
    protected void onDestroy() {
        VoiceFunctionF2.StopRecordVoice(is2mp3);
        VoiceFunctionF2.StopVoice();

        super.onDestroy();
    }

    private Download.OnDownloadListener downloadListener =
            new Download.OnDownloadListener() {

                @Override
                public void onStart(int downloadId, long fileSize) {
                }

                @Override
                public void onPublish(int downloadId, long size) {

                }

                @Override
                public void onSuccess(int downloadId) {
                    File f1 = new File(Variable.StorageLyricCachPath(instance), lyricname);
                    String result = LyrcUtil.readLRCFile(f1);
                    //解析歌词构造器
                    ILrcBuilder builder = new DefaultLrcBuilder();
                    //解析歌词返回LrcRow集合
                    List<LrcRow> rows = builder.getLrcRows(result);
                    mLrcView.setLrc(rows);

                }

                @Override
                public void onPause(int downloadId) {
                    LogUtils.LOGE(tag, "onPause");
                }

                @Override
                public void onError(int downloadId) {
                    LogUtils.LOGE(tag, "onError");
                }

                @Override
                public void onCancel(int downloadId) {
                    LogUtils.LOGE(tag, "onCancel");
                }

                @Override
                public void onGoon(int downloadId, long localSize) {
                    LogUtils.LOGE(tag, "onGoon");
                }
            };

    private ServiceConnection mDownloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.e("mDownloadService","onServiceConnected");

            mDownloadService = ((DownloadService.DownloadBinder) service).getService();
            mDownloadService.setOnDownloadEventListener(downloadListener);

            UserInfo userInfo = AppShare.getUserInfo(RecordComActivity.this);
            if (TextUtils.isEmpty(music.lrcpath)){
                String url = userInfo.userimage;
                if (url != null) {
                    if (!url.contains("http://wx.qlogo.cn")) {
                        url = MyNetApiConfig.ImageServerAddr + url;
                    }
                    String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                    File file = new File(Variable.StorageImagePath(instance), fileName);
                    if (file.exists()) {
                        initBackground(file);
                    } else {
                        DownLoaderTask downLoaderTask = new DownLoaderTask(url, fileName, Variable.StorageImagePath(instance), image_anim, background);
                        downLoaderTask.execute();
                    }
                }
            }else {
                lyricUrl = MyNetApiConfig.ImageServerAddr + music.lrcpath;
                lyricname = lyricUrl.substring(lyricUrl.lastIndexOf("/")+1, lyricUrl.length());

                File file = new File(Variable.StorageLyricCachPath(instance), lyricname);
                if (file.exists()) {
                    String result = LyrcUtil.readLRCFile(file);
                    //解析歌词构造器
                    ILrcBuilder builder = new DefaultLrcBuilder();
                    //解析歌词返回LrcRow集合
                    List<LrcRow> rows = builder.getLrcRows(result);
                    mLrcView.setLrc(rows);

                } else {
                    if (mDownloadService != null) {
                        mDownloadService.download(music.mid, lyricUrl, Variable.StorageLyricCachPath(instance),
                                lyricname);
                    }
                }
            }
        }
    };


    private void exit() {
        if (recordVoiceBegin) {
            MenuDialogGiveupRecordHelper menuDialogGiveupRecordHelper = new MenuDialogGiveupRecordHelper(instance, new MenuDialogListerner() {
                @Override
                public void onSelected(int selected) {
                    switch (selected) {
                        case 0:
                            reset();

                            break;
                        case 1:
                            finish();

                            VoiceFunctionF2.GiveUpRecordVoice(is2mp3);
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
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        bindView();
        initView();
        initData();
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

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

    }


    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private ImageView cicle;
        private ImageView background;
        private ProgressReportingOutputStream mOutputStream;

        private int mProgress = 0;

        public DownLoaderTask(String downloadPath, String fileName, String out, ImageView cicle, ImageView background) {
            super();
            this.cicle = cicle;
            this.background = background;

            try {
                mUrl = new URL(downloadPath);

                mFile = new File(out, fileName);
            } catch (MalformedURLException e) {

                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Long doInBackground(Void... params) {

            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            // super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);
            Log.e(TAG, "下载完");
            initBackground(mFile);
            if (isCancelled())
                return;
        }

        private long download() {
            URLConnection connection = null;
            int bytesCopied = 0;
            try {
                connection = mUrl.openConnection();
                int length = connection.getContentLength();
                if (mFile.exists()/* && length == mFile.length()*/) {
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bytesCopied;
        }

        private int copy(InputStream input, OutputStream output) {
            byte[] buffer = new byte[1024 * 8];
            BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
            BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
            int count = 0, n = 0;
            try {
                while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                    out.write(buffer, 0, n);
                    count += n;
                }
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return count;
        }

        private final class ProgressReportingOutputStream extends FileOutputStream {

            public ProgressReportingOutputStream(File file)
                    throws FileNotFoundException {
                super(file);
                // TODO Auto-generated constructor stub
            }

            @Override
            public void write(byte[] buffer, int byteOffset, int byteCount)
                    throws IOException {
                // TODO Auto-generated method stub
                super.write(buffer, byteOffset, byteCount);
                mProgress += byteCount;
                publishProgress(mProgress);
            }

        }

    }

}
