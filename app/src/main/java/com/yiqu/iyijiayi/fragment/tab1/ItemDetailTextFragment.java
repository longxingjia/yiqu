package com.yiqu.iyijiayi.fragment.tab1;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.service.Download;
import com.service.DownloadService;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ui.abs.AbsFragment;
import com.ui.views.DialogUtil;
import com.ui.views.MenuDialogImage;
import com.ui.views.ObservableScrollView;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.utils.LogUtils;
import com.utils.Player;
import com.utils.Variable;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.CommentActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
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
import com.yiqu.iyijiayi.utils.LyrcUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.utils.WxUtil;
import com.yiqu.lyric.DefaultLrcBuilder;
import com.yiqu.lyric.ILrcBuilder;
import com.yiqu.lyric.LrcRow;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ItemDetailTextFragment extends AbsAllFragment implements View.OnClickListener,
        VoicePlayerInterface, NetCallBack{
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
    @BindView(R.id.share)
    public ImageView share;
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

    @BindView(R.id.lrcView)
    public com.yiqu.lyric.LrcView mLrcView;

    @BindView(R.id.ll_title)
    public LinearLayout ll_title;
    @BindView(R.id.ll_backgroud)
    public LinearLayout ll_backgroud;
    @BindView(R.id.listview)
    public ListView listview;
    @BindView(R.id.teacher_info)
    public LinearLayout teacher_info;
    @BindView(R.id.scrollView)
    public ScrollView scrollView;


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
    //    private File stuFile;
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
    private int mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
    private boolean isBoundDownload;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private Tab1CommentsAdapter tab1CommentsAdapter;

    private int payforTag = 0;
    private int position;
    //  private Player player;
    private Sound soundWorth;
    private String2TimeUtils string2TimeUtils = new String2TimeUtils();
    private String lyricname;
    private IWXAPI api;

//    @Override
//    protected int getContentView() {
//        return R.layout.tab1_text_detail;
//    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab1_text_detail;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        api = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID);
        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        seekbar.setMax(100);


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
                    mLrcView.seekLrcToTime(progress);
                    now_time.setText(string2TimeUtils.stringForTimeS(progress / 1000));
                }
            });

        }
    }

    @Override
    public void OnCompletion() {
        super.OnCompletion();
        video_play.setImageResource(R.mipmap.video_play);
    }



    @Override
    protected int getTitleBarType() {
        return FLAG_ALL;
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
        setTitleBtnImg(R.mipmap.share_icon);
            setTitleText("问题详情");
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
                        "getSoundDetail", ItemDetailTextFragment.this, true, true);

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.addHistory, MyNetRequestConfig
                                .addHistory(getActivity(), sid, userInfo.uid),
                        "addHistory", ItemDetailTextFragment.this, false, true);
                userInfo.coin_apple--;
                payforTag = 1;
                AppShare.setUserInfo(getActivity(), userInfo);
            }

            ToastManager.getInstance(getActivity()).showText(netResponse.result);

        } else if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                L.e(netResponse.toString());
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
                    comments.setText(String.valueOf(commentsInfos.size()));
                }

            }
        } else if (id.equals("addHistory")) {

            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag, netResponse.toString());
            }
        } else if (id.equals("worthSoundList")) {


            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.views, MyNetRequestConfig
                            .getComments(getActivity(), sid),
                    "views", ItemDetailTextFragment.this, false, true);

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
        getActivity().bindService(new Intent(getActivity(), DownloadService.class), mDownloadServiceConnection, Context.BIND_AUTO_CREATE);
        desc.setText(sound.desc);
        tea_name.setText(sound.tecname);
        stu_name.setText(sound.stuname);
        if (!TextUtils.isEmpty(sound.article_content)) {
            article_content.setText(sound.article_content);
            mLrcView.setVisibility(View.GONE);
        }

        video_play.setImageResource(R.mipmap.video_pause);
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
//        mLrcViewOnFirstPage.setMovementMethod(new ScrollingMovementMethod());
        mLrcView.setOnTouchListener(new View.OnTouchListener() {
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
        title.setText(sound.musicname);
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
            // title.setText("作品详情");
        } else {
            teacher_info.setVisibility(View.VISIBLE);
            // title.setText("问题详情");
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
        listview.setOnItemLongClickListener(tab1CommentsAdapter);

        tab1CommentsAdapter.setOnMoreClickListener(new Tab1CommentsAdapter.setDeleteCom() {
            @Override
            public void onDeleteCom() {
                //     L.e("f");
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getComments, MyNetRequestConfig
                                .getComments(getActivity(), sid),
                        "getComments", ItemDetailTextFragment.this, false, true);

            }
        });

        PictureUtils.showPicture(getActivity(), sound.tecimage, tea_header);
        PictureUtils.showPicture(getActivity(), sound.stuimage, stu_header);

        if (TextUtils.isEmpty(sound.soundpath)) {
            //      ll_backgroud.setVisibility(View.GONE);
            //   musictype.setVisibility(View.GONE);
//            stufileName = stuUrl.substring(
//                    stuUrl.lastIndexOf("/") + 1,
//                    stuUrl.length());
//            stufileName = sound.musicname + "_" + stufileName;
            //   stuFile = new File(Variable.StorageMusicCachPath(getActivity()), stufileName);
        } else {
            stuUrl = MyNetApiConfig.ImageServerAddr + sound.soundpath;
            if (mPlayService != null) {
                mPlayService.play(stuUrl, sound.sid);
                L.e(stuUrl);
            }
        }


        teaUrl = MyNetApiConfig.ImageServerAddr + sound.commentpath;
        teafileName = teaUrl.substring(
                teaUrl.lastIndexOf("/") + 1,
                teaUrl.length());
        teafileName = sound.musicname + "_" + teafileName;

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
                MyNetRequestConfig.getSoundList(getActivity(), arr, 0, 1, "views", "desc", "0"),
                "worthSoundList", ItemDetailTextFragment.this, true, true);

    }

    @Override
    protected void init(Bundle savedInstanceState) {

        sound = (Sound) getActivity().getIntent().getSerializableExtra("Sound");
        position = getActivity().getIntent().getIntExtra("position", -1);
        userInfo = AppShare.getUserInfo(getActivity());

        if (sound == null) {
            sid = getActivity().getIntent().getExtras().getString("data");
            String uid = "0";
            if (AppShare.getIsLogin(getActivity())) {
                uid = userInfo.uid;

            }
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                            .getSoundDetail(getActivity(), sid, uid),
                    "getSoundDetail", ItemDetailTextFragment.this, true, false);

        } else {
            sid = String.valueOf(sound.sid);
            String uid = "0";
            if (AppShare.getIsLogin(getActivity())) {
                uid = userInfo.uid;
            }
            //     initData();
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                            .getSoundDetail(getActivity(), sid, uid),
                    "getSoundDetail", ItemDetailTextFragment.this, true, false);

        }

        super.init(savedInstanceState);
    }

    @OnClick({R.id.tea_listen, R.id.like, R.id.comments, R.id.stu_header, R.id.tea_header,
            R.id.video_play, R.id.back, R.id.add_follow, R.id.ll_worth, R.id.share})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_worth:

                if (soundWorth != null) {
                    Intent i = new Intent(getActivity(), StubActivity.class);
                    if (soundWorth.type == 1) {
                        i.putExtra("fragment", ItemDetailFragment.class.getName());
                    } else if (soundWorth.type == 2){
                        i.putExtra("fragment", ItemDetailFragment.class.getName());
                    }else {
                        i.putExtra("fragment", ItemDetailTextFragment.class.getName());
                    }
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
                    if (mPlayService.getMusicPlayerState() != Player.MusicPlayerState.stop) {
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
                } else {
                    mPlayService.play(stuUrl, sound.sid);
                    video_play.setImageResource(R.mipmap.video_pause);
                }
                break;

            case R.id.tea_listen:
                L.e("palyTeacherVoice");
                long t = System.currentTimeMillis() / 1000 - sound.edited;
                if (t < 2 * 24 * 60 * 60 * 1000 && t > 0) {
                //    video_play.setImageResource(R.mipmap.video_play);
                    if (mPlayService.getPlayingPosition() == sound.sid) {
                        if (mPlayService.isPlaying()) {
                            mPlayService.pause();
//                            L.e("pause");
                        } else {
                            mPlayService.resume();
                   //         L.e("resume");
                        }
                    } else {
                        palyTeacherVoice();
                    //    L.e("palyTeacherVoice");
                    }
               //     palyTeacherVoice();


                } else {
                    if (sound.listen == 1) {
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
                                            "eavesdrop", ItemDetailTextFragment.this);
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
                            "like", ItemDetailTextFragment.this, false, true);
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText(getString(R.string.login_tips));
                }

                break;
            case R.id.comments:


                if (AppShare.getIsLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    intent.putExtra("sid", String.valueOf(sid));
                    intent.putExtra("fromuid", String.valueOf(AppShare.getUserInfo(getActivity()).uid));
                    intent.putExtra("touid", String.valueOf(sound.fromuid));
                    getActivity().startActivity(intent);
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText(getString(R.string.login_tips));
                }
//                Intent intent = new Intent(getActivity(), CommentActivity.class);
//                    intent.putExtra("sid", String.valueOf(sid));
//                    intent.putExtra("fromuid",String.valueOf(AppShare.getUserInfo(getActivity()).uid));
//                    intent.putExtra("touid", String.valueOf(sound.fromuid ));
//                    getActivity().startActivity(intent);

                break;

            case R.id.stu_header:
                initHomepage(getActivity(), String.valueOf(sound.fromuid));
                break;
            case R.id.tea_header:
                initHomepage(getActivity(), String.valueOf(sound.touid));
                break;
            case R.id.share:
             //   showShare();

                //            showShareOri();
                break;

        }
    }

    private void showShare() {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        final String url = MyNetApiConfig.ServerAddr + "/site/share-page?sid=" + sid;
        L.e(url);
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(sound.musicname);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(sound.stuname);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //   oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        oks.setImageUrl(url);
//        if (sound.stuimage != null) {
//            if (sound.stuimage .contains("http://wx.qlogo.cn")) {
//                oks.setImagePath(sound.stuimage );
//                L.e(sound.stuimage );
//            } else {
//                //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
//                oks.setImageUrl(MyNetApiConfig.ImageServerAddr +sound.stuimage);
//                L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage );
//            }
//        } else {
//            Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//            oks.setImageData(imageData);
//            oks.setim
//        }
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                }
                if ("QQ".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    paramsToShare.setImageData(imageData);
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
//                    if (sound.stuimage != null) {
//                        if (sound.stuimage .contains("http://wx.qlogo.cn")) {
//                            paramsToShare.setImagePath(sound.stuimage );
//                            L.e(sound.stuimage );
//                        } else {
//                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
//                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr +sound.stuimage);
//                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage );
//                        }
//                    } else {
//                        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                        paramsToShare.setImageData(image);
//                    }
                }

                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setUrl(url);
                    paramsToShare.setText("分享 " + url);
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
                    if (sound.stuimage != null) {
                        if (sound.stuimage.contains("http://wx.qlogo.cn")) {
                            paramsToShare.setImagePath(sound.stuimage);
                            L.e(sound.stuimage);
                        } else {
                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr + sound.stuimage);
                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage);
                        }
                    } else {
                        Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        paramsToShare.setImageData(imageData);
                    }

                }
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
                    if (sound.stuimage != null) {
                        if (sound.stuimage.contains("http://wx.qlogo.cn")) {
                            paramsToShare.setImagePath(sound.stuimage);
                            L.e(sound.stuimage);
                        } else {
                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr + sound.stuimage);
                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage);
                        }
                    } else {
                        Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        paramsToShare.setImageData(imageData);
                    }

                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
                    if (sound.stuimage != null) {
                        if (sound.stuimage.contains("http://wx.qlogo.cn")) {
                            paramsToShare.setImagePath(sound.stuimage);
                            L.e(sound.stuimage);
                        } else {
                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr + sound.stuimage);
                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage);
                        }
                    } else {
                        Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        paramsToShare.setImageData(imageData);
                    }
                }

            }
        });

// 启动分享GUI
        oks.show(getActivity());
    }

    private void showShareOri() {

        final MenuDialogImage menuDialogImage = new MenuDialogImage(getActivity(), new MenuDialogImage.OnMenuListener() {
            @Override
            public void onMenuClick(MenuDialogImage dialog, int which) {
                switch (which) {
                    case MenuDialogImage.WXCHAT:
                        mTargetScene = SendMessageToWX.Req.WXSceneSession;
                        break;
                    case MenuDialogImage.WXCHATMOMENTS:
                        mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                        break;
                }

                final String url = MyNetApiConfig.ServerAddr + "/site/share-page?sid=" + sid;

                WXMusicObject musicObject = new WXMusicObject();
                musicObject.musicUrl = url;

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = musicObject;
                msg.title = sound.musicname;
                msg.description = sound.desc;

//                if (sound.stuimage != null) {
//                    if (sound.stuimage.contains("http://wx.qlogo.cn")) {
//                        msg.setImagePath(sound.stuimage);
//                        L.e(sound.stuimage);
//                    } else {
//                        //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
//                        paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr + sound.stuimage);
//                        L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage);
//                    }
//                } else {
//                    Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                    paramsToShare.setImageData(imageData);
//                }


                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                msg.thumbData = WxUtil.bmpToByteArray(thumb, true);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("music");
                req.message = msg;
                req.scene = mTargetScene;
                api.sendReq(req);

            }

            @Override
            public void onMenuCanle(MenuDialogImage dialog) {

            }
        });
        menuDialogImage.show(share);


    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        /** 注册下载完成接收广播 **/
//        getActivity().registerReceiver(downloadCompleteReceiver,
//                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        MobclickAgent.onPageStart("声乐详情");
        JAnalyticsInterface.onPageStart(getActivity(), "声乐详情");
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getComments, MyNetRequestConfig
                        .getComments(getActivity(), sid),
                "getComments", ItemDetailTextFragment.this, false, true);


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
//        getActivity().unregisterReceiver(downloadCompleteReceiver);
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            future.cancel(true);
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;

        }
        MobclickAgent.onPageEnd("声乐详情");
        super.onPause();
        JAnalyticsInterface.onPageEnd(getActivity(), "声乐详情");
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
        allowUnbindService(getActivity());
//        L.e(String.valueOf(isBoundDownload));
        if (isBoundDownload) {
            getActivity().unbindService(mDownloadServiceConnection);
            isBoundDownload = false;
        }


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

//    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//
//            if (completeDownloadId == downloadTeaId) {
//                tea_listen.setText("100%");
//                downloadTeaId = -1;
//                palyTeacherVoice();
//            }
//            if (completeDownloadId == downloadStuId) {
//                downloadStuId = -1;
//                palyStudentVoice();
//
//            }
//            if (downloadTeaId == -1 && downloadTeaId == -1) {
//                if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
//                    future.cancel(true);
//                    scheduledExecutorService.shutdown();
//                    scheduledExecutorService = null;
//                }
//            }
//        }
//    };


//    private void palyStudentVoice() {
//
//        teacurrentTime = 0;
//        stutotalTime = sound.soundtime;
//        teatotalTime = sound.commenttime;
//        commenttime.setText(teatotalTime + "\"");
//
//        if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {  //正在播放，点击暂停
//            stucurrentTime = VoiceFunction.pauseVoice(stuFile.getAbsolutePath());
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
//            if (mTimer == null) {
//                mTimer = new Timer();
//            }
//            if (mTimerTask == null) {
//                mTimerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//
//                        mHandler.sendEmptyMessage(0);
//
//                    }
//                };
//                mTimer.schedule(mTimerTask, 1000, 1000);
//
//            }
//
//            if (stucurrentTime > 0) {
//
//                stutotalTime = MathUtils.sub(sound.soundtime, stucurrentTime);
//                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, stucurrentTime);
//            } else {
//                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, 0);
//            }
//
//        }
//    }

    private void palyTeacherVoice() {

        if (mPlayService != null) {
            mPlayService.play(teaUrl, sound.sid);
        }
    }

    private ServiceConnection mDownloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //   Log.e("mDownloadService","onServiceConnected");

            mDownloadService = ((DownloadService.DownloadBinder) service).getService();
            mDownloadService.setOnDownloadEventListener(downloadListener);
            isBoundDownload = true;

            if (TextUtils.isEmpty(sound.lrc_url)) {
                mLrcView.setVisibility(View.GONE);
            } else {
                L.e(sound.lrc_url);
                String lyricUrl = MyNetApiConfig.ImageServerAddr + sound.lrc_url;
                lyricname = lyricUrl.substring(lyricUrl.lastIndexOf("/") + 1, lyricUrl.length());
                File file = new File(Variable.StorageLyricCachPath(getActivity()), lyricname);
                if (file.exists()) {
                    String result = LyrcUtil.readLRCFile(file);
                    //解析歌词构造器
                    ILrcBuilder builder = new DefaultLrcBuilder();
                    //解析歌词返回LrcRow集合
                    List<LrcRow> rows = builder.getLrcRows(result);
                    mLrcView.setLrc(rows);

                } else {
                    L.e("fd");
                    if (mDownloadService != null) {
                        L.e("f");
                        mDownloadService.download(sound.mid, lyricUrl, Variable.StorageLyricCachPath(getActivity()),
                                lyricname);
                    }
                }
            }


        }
    };

    private Download.OnDownloadListener downloadListener =
            new Download.OnDownloadListener() {
                //  private DialogHelper dialogHelper;

                @Override
                public void onStart(int downloadId, long fileSize) {
//                 tv_lyric.setText("歌词下载中");
                }

                @Override
                public void onPublish(int downloadId, long size) {

                }

                @Override
                public void onSuccess(int downloadId) {
                    File f1 = new File(Variable.StorageLyricCachPath(getActivity()), lyricname);
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
