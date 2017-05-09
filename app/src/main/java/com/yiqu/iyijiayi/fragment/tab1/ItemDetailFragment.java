package com.yiqu.iyijiayi.fragment.tab1;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.abs.AbsFragment;
import com.ui.views.DialogUtil;
import com.ui.views.ObservableScrollView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Function.VoiceFunction;
import com.utils.Variable;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.CommentActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab1CommentsAdapter;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.PayforYBFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.CommentsInfo;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.MathUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/2/20.
 */

public class ItemDetailFragment extends AbsFragment implements View.OnClickListener, VoicePlayerInterface, NetCallBack, ObservableScrollView.ScrollViewListener {
    String tag = "ItemDetailFragment";
    @BindView(R.id.back)
    public ImageView back;
    @BindView(R.id.video_play)
    public ImageView video_play;
    @BindView(R.id.seekbar)
    public SeekBar seekbar;
    @BindView(R.id.like)
    public TextView like;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.musicname)
    public TextView musicname;
    @BindView(R.id.desc)
    public TextView desc;
    @BindView(R.id.soundtime)
    public TextView soundtime;
    @BindView(R.id.tea_name)
    public TextView tea_name;
    @BindView(R.id.publish_time)
    public TextView publish_time;
    @BindView(R.id.worth_name)
    public TextView worth_name;
    @BindView(R.id.stu_name)
    public TextView stu_name;
    @BindView(R.id.tectitle)
    public TextView tectitle;
    @BindView(R.id.commenttime)
    public TextView commenttime;
    @BindView(R.id.comments)
    public TextView comments;
    @BindView(R.id.views)
    public TextView views;
    @BindView(R.id.musictype)
    public ImageView musictype;
    @BindView(R.id.worth_type)
    public ImageView worth_type;
    @BindView(R.id.add_follow)
    public ImageView add_follow;
    @BindView(R.id.tea_listen)
    public TextView tea_listen;
    @BindView(R.id.worth_desc)
    public TextView worth_desc;
    @BindView(R.id.worth_header)
    public ImageView worth_header;
    @BindView(R.id.worth_teacher_name)
    public TextView worth_teacher_name;
    @BindView(R.id.worth_teacher_desc)
    public TextView worth_teacher_desc;
    @BindView(R.id.worth_comment)
    public TextView worth_comment;
    @BindView(R.id.worth_like)
    public TextView worth_like;
    @BindView(R.id.now_time)
    public TextView now_time;
    @BindView(R.id.worth_listener)
    public TextView worth_listener;
    @BindView(R.id.stu_header)
    public ImageView stu_header;
    @BindView(R.id.tea_header)
    public ImageView tea_header;
    //    @BindView(R.id.ll_backgroud)
//    public LinearLayout ll_backgroud;
    @BindView(R.id.ll_title)
    public LinearLayout ll_title;
    @BindView(R.id.listview)
    public ListView listview;
    @BindView(R.id.teacher_info)
    public LinearLayout teacher_info;
    @BindView(R.id.scrollView)
    public ObservableScrollView scrollView;

    @BindView(R.id.no_comments)
    public TextView no_comments;
    @BindView(R.id.comment)
    public TextView comment;
    @BindView(R.id.article_content)
    public TextView article_content;

    private int teatotalTime;
    private int stutotalTime;
    private int stucurrentTime;
    private int teacurrentTime;
    private String stufileName;
    private String teafileName;
    private String stuUrl;
    private String teaUrl;
    private File stuFile;
    //    private File teaFile;
    private Timer mTimer;
    private TimerTask mTimerTask;

    private Sound sound;
    private AlertDialog dialog;
    private UserInfo userInfo;
    private String sid;
    private DownloadManager downloadManager;

    ScheduledExecutorService scheduledExecutorService;
    Future<?> future;
    private long downloadTeaId = -1;
    private long downloadStuId = -1;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
//                    if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {
//                        soundtime.setText(--stutotalTime + "\"");
//                        if (stutotalTime == 0) {
//                            stutotalTime = 1;
//                        }
//                    } else if (VoiceFunction.IsPlayingVoice(teaFile.getAbsolutePath())) {
//                        commenttime.setText(--teatotalTime + "\"");
//                        if (teatotalTime == 0) {
//                            teatotalTime = 1;
//                        }
//                    }
                    break;
                case Constant.QUERY_STUDENT:
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadStuId);//筛选下载任务，传入任务ID，可变参数
                    Cursor cursor = downloadManager.query(query);

                    if (cursor != null && cursor.moveToFirst()) {
                        //此处直接查询文件大小
                        int bytesDLIdx =
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int bytesDL = cursor.getInt(bytesDLIdx);

                        //获取文件下载总大小
                        long fileTotalSize = cursor.getLong(cursor.getColumnIndex(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        cursor.close();

//                        Log.w(tag, "downloaded size: " + bytesDL);
//                        Log.w(tag, "total size: " + fileTotalSize);

                        if (fileTotalSize != 0) {
                            int percentage = (int) (bytesDL * 100 / fileTotalSize);

//                            statusBar.setProgress(percentage);
//                            statusText.setText(percentage + "%");
                        }

                        //终止轮询task
//                        if (fileTotalSize == bytesDL)
//                            future.cancel(true);
                    }
                    break;
                case Constant.QUERY_TEACHER:

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadTeaId);//筛选下载任务，传入任务ID，可变参数
                    Cursor c = downloadManager.query(q);

                    if (c != null && c.moveToFirst()) {
                        //此处直接查询文件大小
                        int bytesDLIdx =
                                c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int bytesDL = c.getInt(bytesDLIdx);

                        //获取文件下载总大小
                        long fileTotalSize = c.getLong(c.getColumnIndex(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        c.close();

//                        Log.w(tag, "downloaded size: " + bytesDL);
//                        Log.w(tag, "total size: " + fileTotalSize);

                        if (fileTotalSize != 0) {
                            int percentage = (int) (bytesDL * 100 / fileTotalSize);
                            tea_listen.setText(percentage + "%");
//                            statusBar.setProgress(percentage);
//                            statusText.setText(percentage + "%");
                        }

                        //终止轮询task
//                        if (fileTotalSize == bytesDL)
//                            future.cancel(true);
                    }
                    break;

                default:

                    break;
            }

        }
    };

    private Tab1CommentsAdapter tab1CommentsAdapter;

    private int payforTag = 0;
    private int position;
    //  private Player player;
    private Sound soundWorth;
    private String2TimeUtils string2TimeUtils = new String2TimeUtils();
    ;

    @Override
    protected int getContentView() {
        return R.layout.tab1_sound_detail;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);

        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        seekbar.setMax(100);
        initListeners();


    }

    private int imageHeight;

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

    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = ll_title.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            @Override
            public void onGlobalLayout() {
                ll_title.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = ll_title.getHeight();

                scrollView.setScrollViewListener(ItemDetailFragment.this);
            }
        });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            title.setTextColor(getResources().getColor(R.color.white));
            back.setImageResource(R.mipmap.back_write);
            ll_title.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            ll_title.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {
            ll_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            title.setTextColor(getResources().getColor(R.color.normal_text_color));
            back.setImageResource(R.mipmap.back);
        }
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

    private void initDianZan() {
        Drawable leftDrawable = getResources().getDrawable(R.mipmap.dianzan_pressed_new);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        like.setCompoundDrawables(leftDrawable, null, null, null); //(Drawable left, Drawable top, Drawable right, Drawable bottom)
    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("eavesdrop")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), sid, userInfo.uid),
                        "getSoundDetail", ItemDetailFragment.this, true, true);

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.addHistory, MyNetRequestConfig
                                .addHistory(getActivity(), sid, userInfo.uid),
                        "addHistory", ItemDetailFragment.this, false, true);
                userInfo.coin_apple--;
                payforTag = 1;
                AppShare.setUserInfo(getActivity(), userInfo);
            }

            ToastManager.getInstance(getActivity()).showText(netResponse.result);

        } else if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                Gson gson = new Gson();
                sound = gson.fromJson(netResponse.data, Sound.class);

                initData();

            } else {
                getActivity().finish();

            }
        } else if (id.equals("like")) {

            if (type == NetCallBack.TYPE_SUCCESS) {

                if (sound.islike == 0) {
                    like.setText(String.valueOf(sound.like + 1));
                    initDianZan();
                }

            } else {

            }
        } else if (id.equals("getComments")) {

            if (type == NetCallBack.TYPE_SUCCESS) {
                ArrayList<CommentsInfo> commentsInfos
                        = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<CommentsInfo>>() {
                }.getType());

                if (commentsInfos == null || commentsInfos.size() == 0) {

                } else {
                    tab1CommentsAdapter.setData(commentsInfos);
                    no_comments.setVisibility(View.GONE);
                }

            }
        } else if (id.equals("addHistory")) {

            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag, netResponse.toString());
            }
        } else if (id.equals("worthSoundList")) {

            if (mPlayService != null) {
                mPlayService.play(stuUrl, sound.sid);
            }

            video_play.setImageResource(R.mipmap.video_pause);
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.views, MyNetRequestConfig
                            .getComments(getActivity(), sid),
                    "views", ItemDetailFragment.this, false, true);

            if (type == NetCallBack.TYPE_SUCCESS) {
                ArrayList<Sound> sounds = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());
                soundWorth = sounds.get(0);
                if (sound != null) {
                    //   LogUtils.LOGE(tag, netResponse.toString());
                    worth_name.setText(soundWorth.musicname);
                    if (!TextUtils.isEmpty(soundWorth.desc))
                        worth_desc.setText(soundWorth.desc);
                    if (soundWorth.type == 1) {
                        worth_type.setImageResource(R.mipmap.shengyue);
                    } else {
                        worth_type.setImageResource(R.mipmap.boyin);
                    }
                    PictureUtils.showPicture(getActivity(), soundWorth.tecimage, worth_header, 40);
                    worth_teacher_name.setText(soundWorth.tecname);
                    worth_teacher_desc.setText(sound.tectitle);
                    worth_like.setText(String.valueOf(soundWorth.like));
                    worth_comment.setText(String.valueOf(soundWorth.comments));
                    worth_listener.setText(String.valueOf(soundWorth.views));

                }

            }


        } else if (id.equals("views")) {

            if (type == TYPE_SUCCESS) {

            }
        }


    }

    private void initData() {
        desc.setText(sound.desc);
        tea_name.setText(sound.tecname);
        stu_name.setText(sound.stuname);
        article_content.setText(sound.article_content);
        article_content.setMovementMethod(new ScrollingMovementMethod());


        article_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
        try {
            publish_time.setText(String2TimeUtils.longToString(sound.created * 1000, "yyyy/MM/dd HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        musicname.setText(sound.musicname);
        tectitle.setText(sound.tectitle);
        commenttime.setText(sound.commenttime + "\"");
        soundtime.setText(string2TimeUtils.stringForTimeS(sound.soundtime));
        views.setText(sound.views + "");
        like.setText(sound.like + "");
        comments.setText(String.valueOf(sound.comments));
        if (sound.type == 1) {
            musictype.setImageResource(R.mipmap.shengyue);
            String text = "";
            if (!TextUtils.isEmpty(sound.chapter)) {
                text = sound.chapter + " | ";
            }
            if (!TextUtils.isEmpty(sound.musictype)) {
                text = text + sound.musictype + " | ";
            }


        } else {
            musictype.setImageResource(R.mipmap.boyin);
        }


        if (sound.touid == 0) {
            teacher_info.setVisibility(View.GONE);
            title.setText("作品详情");
        } else {
            teacher_info.setVisibility(View.VISIBLE);
            title.setText("问题详情");
        }

        long t = System.currentTimeMillis() / 1000 - sound.edited;
        if (t < 2 * 24 * 60 * 60 * 1000 && t > 0) {
            tea_listen.setText("限时免费听");
        } else {
            if (sound.listen == 1) {
                tea_listen.setText("已付费");
            } else {
                tea_listen.setText("1元偷偷听");
            }
        }

        tab1CommentsAdapter = new Tab1CommentsAdapter(getActivity(), sid, sound.fromuid + "");
        listview.setAdapter(tab1CommentsAdapter);
        listview.setOnItemClickListener(tab1CommentsAdapter);

        PictureUtils.showPicture(getActivity(), sound.tecimage, tea_header);
        PictureUtils.showPicture(getActivity(), sound.stuimage, stu_header);

        stuUrl = MyNetApiConfig.ImageServerAddr + sound.soundpath;
        stufileName = stuUrl.substring(
                stuUrl.lastIndexOf("/") + 1,
                stuUrl.length());
        stufileName = sound.musicname + "_" + stufileName;
        stuFile = new File(Variable.StorageMusicCachPath, stufileName);

        teaUrl = MyNetApiConfig.ImageServerAddr + sound.commentpath;
        teafileName = teaUrl.substring(
                teaUrl.lastIndexOf("/") + 1,
                teaUrl.length());
        teafileName = sound.musicname + "_" + teafileName;
        //   teaFile = new File(Variable.StorageMusicCachPath, teafileName);

        if (sound.islike != 0) {
            initDianZan();
        }


        NSDictionary nsDictionary = new NSDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.isreply = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "1";

        Gson gson = new Gson();
        String arr = gson.toJson(nsDictionary);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, 0, 1, "edited", "desc", "0"),
                "worthSoundList", ItemDetailFragment.this, true, true);

    }

    @Override
    protected void init(Bundle savedInstanceState) {

        sound = (Sound) getActivity().getIntent().getSerializableExtra("Sound");
        position = getActivity().getIntent().getIntExtra("position", -1);
        userInfo = AppShare.getUserInfo(getActivity());

        if (sound == null) {
            sid = getActivity().getIntent().getExtras().getString("data");

            if (AppShare.getIsLogin(getActivity())) {

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), sid, userInfo.uid),
                        "getSoundDetail", ItemDetailFragment.this, true, false);

            }
        } else {
            sid = String.valueOf(sound.sid);
            initData();
        }
        super.init(savedInstanceState);
    }

    @OnClick({R.id.tea_listen, R.id.like, R.id.comment, R.id.stu_header, R.id.tea_header,
            R.id.video_play, R.id.back, R.id.add_follow, R.id.ll_worth})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_worth:

                if (soundWorth != null) {
                    Intent i = new Intent(getActivity(), StubActivity.class);
                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Sound", soundWorth);
                    i.putExtras(bundle);
                    getActivity().startActivity(i);
                    getActivity().finish();

                }
                break;
            case R.id.add_follow:


                break;
            case R.id.back:
                getActivity().finish();
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
                    mPlayService.play(stuUrl, sound.sid);
                    video_play.setImageResource(R.mipmap.video_pause);
                }

                break;
            case R.id.stu_listen:

                if (stuFile.exists()) {
                    palyStudentVoice();

                } else {

                    downloadStuId = dowoload(stuUrl, stufileName, 0);
                }


                break;
            case R.id.tea_listen:
                long t = System.currentTimeMillis() / 1000 - sound.edited;

                if (t < 2 * 24 * 60 * 60 * 1000 && t > 0) {

                    video_play.setImageResource(R.mipmap.video_play);
                    if (mPlayService.getPlayingPosition() == 0) {
                        if (mPlayService.isPlaying()) {
                            mPlayService.pause();
                        } else {
                            mPlayService.resume();
                        }
                    } else {
                        palyTeacherVoice();
                    }

                } else {
                    if (sound.listen == 1) {
//                        if (teaFile.exists()) {
//                            palyTeacherVoice();
//
//                        } else {
//                            downloadTeaId = dowoload(teaUrl, teafileName, 1);
//                        }
                        palyTeacherVoice();
                    } else {
                        String desc = "";
                        if (userInfo.coin_apple > 0) {
                            desc = "支付";
                        } else {
                            desc = "去充值";
                        }
                        String coin = "0";
                        if (userInfo.coin_apple > 0) {
                            coin = userInfo.coin_apple + "00";
                        }

                        dialog = DialogUtil.showMyDialog(getActivity(), "使用艺币支付", "偷听需支付100艺币，当前余额"
                                + coin + "艺币", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        }, desc, new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (userInfo.coin_apple >= 1) {

                                    RestNetCallHelper.callNet(getActivity(),
                                            MyNetApiConfig.eavesdrop, MyNetRequestConfig
                                                    .eavesdrop(getActivity(), userInfo.uid, sound.sid),
                                            "eavesdrop", ItemDetailFragment.this);
                                } else {
                                    Model.startNextAct(getActivity(),
                                            PayforYBFragment.class.getName());
                                }
                                dialog.dismiss();

                            }
                        });
                    }
                }

                break;

            case R.id.like:
                if (AppShare.getIsLogin(getActivity())) {
                    RestNetCallHelper.callNet(getActivity(),
                            MyNetApiConfig.like, MyNetRequestConfig
                                    .like(getActivity(), AppShare.getUserInfo(getActivity()).uid, sid),
                            "like", ItemDetailFragment.this, false, true);
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText("请您登录后在操作");
                }

                break;
            case R.id.comment:
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("sid", sid + "");
                intent.putExtra("fromuid", AppShare.getUserInfo(getActivity()).uid + "");
                LogUtils.LOGE(tag, sound.toString());
                intent.putExtra("touid", sound.fromuid + "");
                getActivity().startActivity(intent);

                break;

            case R.id.stu_header:
                initHomepage(getActivity(), String.valueOf(sound.fromuid));
                break;
            case R.id.tea_header:
                initHomepage(getActivity(), String.valueOf(sound.touid));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /** 注册下载完成接收广播 **/
        getActivity().registerReceiver(downloadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        MobclickAgent.onPageStart("声乐详情");


        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getComments, MyNetRequestConfig
                        .getComments(getActivity(), sid),
                "getComments", ItemDetailFragment.this, false, true);


    }


    private void initHomepage(final Context mContext, String uid) {
        String mUid = "0";
        if (AppShare.getIsLogin(mContext)) {
            mUid = AppShare.getUserInfo(mContext).uid;
        }
        RestNetCallHelper.callNet(mContext,
                MyNetApiConfig.getUserPage,
                MyNetRequestConfig.getUserPage(mContext
                        , uid, mUid),
                "getUserPage",
                new NetCallBack() {
                    @Override
                    public void onNetNoStart(String id) {

                    }

                    @Override
                    public void onNetStart(String id) {

                    }

                    @Override
                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                        if (TYPE_SUCCESS == type) {
                            Gson gson = new Gson();
                            HomePage homePage = gson.fromJson(netResponse.data, HomePage.class);
                            Intent i = new Intent(mContext, StubActivity.class);
                            i.putExtra("fragment", HomePageFragment.class.getName());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", homePage);
                            i.putExtras(bundle);
                            mContext.startActivity(i);
                        }

                    }
                });
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(downloadCompleteReceiver);
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            future.cancel(true);
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;

        }
        MobclickAgent.onPageEnd("声乐详情");
        super.onPause();

        //player.pause();


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

    private long dowoload(String downloadUrl, String fileName, final int tag) {
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        //downloadUrl为下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        //设置文件下载目录和文件名
        //   request.setDestinationInExternalPublicDir("Android/data/com.yiqu.iyijiayi/cache/musiccach", fileName);
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName);

        //设置下载中通知栏提示的标题
        request.setTitle(fileName);

        //设置下载中通知栏提示的介绍
        request.setDescription(fileName);
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                if (tag == 0) {
                    msg.what = Constant.QUERY_STUDENT;
                } else {
                    msg.what = Constant.QUERY_TEACHER;
                }

                mHandler.sendMessage(msg);
            }
        }, 500, 300, TimeUnit.MILLISECONDS);

        return downloadManager.enqueue(request);

    }

    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (completeDownloadId == downloadTeaId) {
                tea_listen.setText("100%");
                downloadTeaId = -1;
                palyTeacherVoice();
            }
            if (completeDownloadId == downloadStuId) {
                downloadStuId = -1;
                palyStudentVoice();

            }
            if (downloadTeaId == -1 && downloadTeaId == -1) {
                if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
                    future.cancel(true);
                    scheduledExecutorService.shutdown();
                    scheduledExecutorService = null;
                }
            }
        }
    };


    private void palyStudentVoice() {

        teacurrentTime = 0;
        stutotalTime = sound.soundtime;
        teatotalTime = sound.commenttime;
        commenttime.setText(teatotalTime + "\"");

        if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {  //正在播放，点击暂停
            stucurrentTime = VoiceFunction.pauseVoice(stuFile.getAbsolutePath());
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }

        } else {     //暂停，点击播放

            if (mTimer == null) {
                mTimer = new Timer();
            }
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {

                        mHandler.sendEmptyMessage(0);

                    }
                };
                mTimer.schedule(mTimerTask, 1000, 1000);

            }

            if (stucurrentTime > 0) {

                stutotalTime = MathUtils.sub(sound.soundtime, stucurrentTime);
                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, stucurrentTime);
            } else {
                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, 0);
            }

        }
    }

    private void palyTeacherVoice() {
//        stucurrentTime = 0;
//        teatotalTime = sound.commenttime;
//        stutotalTime = sound.soundtime;
//
//        if (VoiceFunction.IsPlayingVoice(teaFile.getAbsolutePath())) {  //正在播放，点击暂停
//            teacurrentTime = VoiceFunction.pauseVoice(teaFile.getAbsolutePath());
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
//
//            if (mTimer == null) {
//                mTimer = new Timer();
//            }
//            if (mTimerTask == null) {
//                mTimerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        mHandler.sendEmptyMessage(0);
//
//                    }
//                };
//                mTimer.schedule(mTimerTask, 1000, 1000);
//
//            }
//            if (teacurrentTime > 0) {
//                teatotalTime = MathUtils.sub(sound.commenttime, teacurrentTime);
//                VoiceFunction.PlayToggleVoice(teaFile.getAbsolutePath(), this, teacurrentTime);
//            } else {
//                VoiceFunction.PlayToggleVoice(teaFile.getAbsolutePath(), this, 0);
//            }
//        }
        if (mPlayService != null) {
            mPlayService.play(teaUrl, 0);
        }
    }

    @Override
    public void playVoiceBegin(long duration) {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoiceStateChanged(long recordDuration) {

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

        stucurrentTime = 0;
        teacurrentTime = 0;
        commenttime.setText(sound.commenttime + "\"");
        soundtime.setText(sound.soundtime + "\"");

    }


    @Override
    public void onDestroy() {
        if (mPlayService != null) {
            if (mPlayService.isPlaying()) {
                if (mPlayService.getPlayingPosition() <= 0) {
                    mPlayService.pause();
                }
            }
        }


        stucurrentTime = 0;
        teacurrentTime = 0;

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }

        if (downloadManager != null) {
            if (downloadTeaId != -1) {
                downloadManager.remove(downloadTeaId);
            }
            if (downloadStuId != -1) {
                downloadManager.remove(downloadStuId);
            }

        }

        super.onDestroy();
    }
}
