package com.yiqu.Control.Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.ui.views.CircleImageView;
import com.utils.L;
import com.utils.Variable;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.model.ComposeVoice;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.LyrcUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.lyric.DefaultLrcBuilder;
import com.yiqu.lyric.ILrcBuilder;
import com.yiqu.lyric.LrcRow;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class PlayActivity extends BaseActivity
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

    @BindView(R.id.background)
    public ImageView background;
    @BindView(R.id.pre_bg)
    public CircleImageView pre_bg;
    @BindView(R.id.play_bg)
    public CircleImageView play_bg;
    @BindView(R.id.next_bg)
    public CircleImageView next_bg;
    @BindView(R.id.seekbar)
    public SeekBar seekbar;
    @BindView(R.id.lrcView)
    public com.yiqu.lyric.LrcView mLrcView;

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

    @Override
    protected void onStart() {
        super.onStart();
        allowBindService();
    }

    @Override
    public void onStop() {
        super.onStop();
        allowUnbindService();
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

    private void initBackground(File file) {
        try{
            PictureUtils.showPictureFile(instance, file, image_anim, 270);
            Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());

            //  background.setImageBitmap(bt);
            Bitmap b = BitmapUtil.blur(bt, 25f, this);
            Bitmap bb = BitmapUtil.blur(b, 25f, this);
            background.setImageBitmap(bb);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //  background.setImageAlpha(120); //0完全透明，255

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

        musicName.setText(voice.musicname);
        play.setImageResource(R.mipmap.icon_pause);

        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);

        Bitmap bitmap = BitmapUtil.createColorBitmap(this, Color.parseColor(color[i]));

        pre_bg.setImageBitmap(bitmap);
        play_bg.setImageBitmap(bitmap);
        next_bg.setImageBitmap(bitmap);

//        String fileName = voice.musicname + "_" + voice.mid;
        playUrl( voice.voicename);
        if (mPlayService!=null){
            mPlayService.pause();
        }
        UserInfo userInfo = AppShare.getUserInfo(this);
        L.e(voice.lrcpath);
        if (TextUtils.isEmpty(voice.lrcpath)){
            String url = userInfo.userimage;
            if (url != null) {
                if (!url.contains("http://wx.qlogo.cn")) {
                    url = MyNetApiConfig.ImageServerAddr + url;
                }
                String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                File file = new File(Variable.StorageImagePath, fileName);
                if (file.exists()) {
                    initBackground(file);
                } else {
                    DownLoaderTask downLoaderTask = new DownLoaderTask(url, fileName, Variable.StorageImagePath, image_anim, background);
                    downLoaderTask.execute();
                }
            }
            mLrcView.setVisibility(View.GONE);
        }else {
            L.e(voice.lrcpath);

            String  lyricUrl = MyNetApiConfig.ImageServerAddr + voice.lrcpath;
            String   lyricname = lyricUrl.substring(lyricUrl.lastIndexOf("/")+1, lyricUrl.length());
            File file = new File(Variable.StorageLyricCachPath, lyricname);
            if (file.exists()) {
                String result = LyrcUtil.readLRCFile(file);
                //解析歌词构造器
                ILrcBuilder builder = new DefaultLrcBuilder();
                //解析歌词返回LrcRow集合
                List<LrcRow> rows = builder.getLrcRows(result);
                mLrcView.setLrc(rows);

            }
        }
    }

    private Handler handler = new Handler();


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
            if (voicePlayer==null)
                return;

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
                mLrcView.seekLrcToTime(position);
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
                String title = "找个导师点评一下吗？";
                String[] items = new String[]{"免费上传作品","找导师请教"};
                MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(instance,title,items, new MenuDialogSelectTeaHelper.TeaListener() {
                    @Override
                    public void onTea(int tea) {
                        switch (tea) {

                            case 1:
                                Intent intent = new Intent(instance, StubActivity.class);
                                intent.putExtra("fragment", AddQuestionFragment.class.getName());
                                intent.putExtras(bundle);
                                instance.startActivity(intent);


                                break;
                            case 0:

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
        if (mTimerTask!=null){
            mTimerTask.cancel();
            mTimer.cancel();
        }

        //  player.stop();
        if (voicePlayer != null) {
            voicePlayer.stop();
            voicePlayer.release();
            voicePlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onChange(int position) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("播放录音或者声乐页面");
        MobclickAgent.onResume(this);
        JAnalyticsInterface.onPageStart(this,"播放录音或者声乐页面");

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("播放录音或者声乐页面");
        MobclickAgent.onPause(this);
        JAnalyticsInterface.onPageEnd(this,"播放录音或者声乐页面");
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
            if (mFile.exists())
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
}
