package com.yiqu.Control.Main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import android.util.Log;

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

import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Global.Variable;

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

import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.yiqu.iyijiayi.utils.DensityUtil;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.PlayUtils;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.RecorderAndPlayUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
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

    private String className;
    private TextView recordHintTextView;

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
    private TextView content;
    private String mRecorderPath;
    @BindView(R.id.background)
    public ImageView background;
    @BindView(R.id.icon_record)
    public ImageView icon_record;
    @BindView(R.id.icon_finish)
    public ImageView icon_finish;
    @BindView(R.id.back)
    public CircleImageView back;
    @BindView(R.id.record)
    public CircleImageView recordVoiceButton;
    @BindView(R.id.finish)
    public CircleImageView finish;


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

    private PlayUtils playUtils;
    private String desc;
    private String2TimeUtils string2TimeUtils;
    private String eid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PermissionGen.needPermission(this, 100, Manifest.permission.RECORD_AUDIO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void init(int layoutId) {
        setContentView(layoutId);

        ButterKnife.bind(this);
        bindView();
        className = getClass().getSimpleName();
        instance = this;
        initData();
    }

    public void bindView() {
        recordHintTextView = (TextView) findViewById(R.id.recordHintTextView);

        rlHint = (RelativeLayout) findViewById(R.id.hint);

        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
//        musicSize = (TextView) findViewById(R.id.musicSize);
        tv_record = (TextView) findViewById(R.id.tv_record);
        content = (TextView) findViewById(R.id.content);

        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
//        composeProgressBar = (ProgressBar) findViewById(R.id.composeProgressBar);

        title_back.setOnClickListener(this);

        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);
        Bitmap bitmap = createColorBitmap(Color.parseColor(color[i]));
        back.setImageBitmap(bitmap);
        recordVoiceButton.setImageBitmap(bitmap);
        finish.setImageBitmap(bitmap);
    }

    private Bitmap createColorBitmap(int color) {
        Bitmap bmp = Bitmap.createBitmap(DensityUtil.dip2px(this, 60), DensityUtil.dip2px(this, 60), Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;
    }

    public void initData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("musicname");
        desc = intent.getStringExtra("musicdesc");
        eid = intent.getStringExtra("eid");
        if (TextUtils.isEmpty(eid)){
            eid ="0";
        }
        playUtils = new PlayUtils(this);

        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果

        mRecorderUtil = new RecorderAndPlayUtil(this);


        mHandler.sendEmptyMessageDelayed(POPUPWINDOW, 200);
        recordHintTextView.setText("按下开始录音");
        //   File mFile = new File(Variable.StorageMusicCachPath, "红豆词_1474598402.mp3");
        musicName.setText(name);

        UserInfo userInfo = AppShare.getUserInfo(this);
        String url = userInfo.userimage;
        if (url != null) {
            if (!url.contains("http://wx.qlogo.cn")) {
                url = MyNetApiConfig.ImageServerAddr + url;
            }
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            File file = new File(Variable.StorageImagePath, fileName);
            //   background.setBackgroundResource(R.color.wechat_green);
            if (file.exists()) {

                initBackground(file);

            } else {

                DownLoaderTask downLoaderTask = new DownLoaderTask(url, fileName, Variable.StorageImagePath, image_anim, background);
                downLoaderTask.execute();
            }
        }

    }

    private void initBackground(File file) {
        PictureUtils.showPictureFile(instance, file, image_anim, 270);
        Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());

        //  background.setImageBitmap(bt);
        Bitmap b = BitmapUtil.blur(bt, 25f, this);
        Bitmap bb = BitmapUtil.blur(b, 25f, this);
        background.setImageBitmap(bb);
        //  background.setImageAlpha(120); //0完全透明，255

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("录制录音页面");
        MobclickAgent.onResume(this);


    }

    @Override
    protected void onPause() {

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



    @OnClick({R.id.record, R.id.back, R.id.finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
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

                                    break;
                                case 1:

                                    Intent i = new Intent(instance, StubActivity.class);
                                    i.putExtra("fragment", UploadXizuoFragment.class.getName());
                                    i.putExtras(bundle);
                                    i.putExtra("eid",eid);
                                    instance.startActivity(i);

                                    break;
                            }

                        }
                    });
                    menuDialogSelectTeaHelper.show(recordVoiceButton);

                } else if (mIsRecording) {

                    if (mSecond > 9) {
                        stopRecording();
                        icon_finish.setImageResource(R.mipmap.upload);
                        playUtils.playUrl(mRecorderPath);
                        icon_record.setImageResource(R.mipmap.icon_pause);
                        recordComFinish = true;
                    } else {
                        ToastManager.getInstance(instance).showText("录音时间要大于10秒钟");
                    }


                }


                break;
            case R.id.record:

                if (recordComFinish) {
                    if (mIsRecording) {

                    } else {
                        if (playUtils.isPlaying()) {
                            playUtils.pause();
                            icon_record.setImageResource(R.mipmap.icon_play);
                        } else {
                            playUtils.playUrl(mRecorderPath);
                            icon_record.setImageResource(R.mipmap.icon_pause);
                        }
                    }
                } else if (mIsRecording) {

                } else {
                    startRecord();
                }

                break;

            case R.id.title_back:
                exit();
                break;

            case R.id.back:
                mSecond = 0;
                mIsRecording = false;
                recordComFinish = false;
                playUtils.pause();
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimerTask.cancel();
                }
                mRecorderUtil.stopRecording();
                icon_finish.setImageResource(R.mipmap.finish);
                icon_record.setImageResource(R.mipmap.icon_record);

                break;

        }

    }



    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private ImageView cicle;
        private ImageView background;
        private DownLoaderTask.ProgressReportingOutputStream mOutputStream;

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

                mOutputStream = new DownLoaderTask.ProgressReportingOutputStream(mFile);
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


    private void stopRecording() {
        composeVoice = new ComposeVoice();
        composeVoice.fromuid = AppShare.getUserInfo(instance).uid;
        composeVoice.mid = 0;
        composeVoice.type = "2";
        composeVoice.musicname = musicName.getText().toString();

        composeVoice.musictype = "";
        composeVoice.chapter = "";
        composeVoice.desc = desc;

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

            stopRecording();
        }
        playUtils.stop();

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

                                stopRecording();
                            }
//                            VoiceFunction.StopRecordVoice();
                            stopAnimation();


                            recordHintTextView.setText("按下开始录音");

                            break;
                        case 1:

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


    private void startRecord() {
        if (mTimer != null) mTimer.cancel();
        if (mTimerTask != null) mTimerTask.cancel();
        string2TimeUtils = new String2TimeUtils();
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

                      //  musictime.setText(mSecond + "\"");
                        musictime.setText(string2TimeUtils.stringForTimeS(mSecond));

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
