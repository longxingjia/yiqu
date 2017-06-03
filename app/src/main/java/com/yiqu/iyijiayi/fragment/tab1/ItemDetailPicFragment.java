package com.yiqu.iyijiayi.fragment.tab1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.utils.Variable;
import com.yiqu.iyijiayi.CommentActivity;
import com.yiqu.iyijiayi.ImagePagerActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.ImageShowGridAdapter;
import com.yiqu.iyijiayi.adapter.Tab1CommentsAdapter;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.CommentsInfo;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.TypeDictionary;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.yiqu.iyijiayi.utils.DianZanUtils;
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.view.MultiView.MultiImageView;
import com.yiqu.iyijiayi.view.ScrollViewWithListView;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import me.nereo.multi_image_selector.MultiImageSelector;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ItemDetailPicFragment extends AbsAllFragment implements View.OnClickListener {

    public static String TAG = "ItemDetailPicFragment";
    @BindView(R.id.header)
    public CircleImageView header;
    @BindView(R.id.author)
    public TextView author;
    @BindView(R.id.publish_time)
    public TextView publish_time;
    @BindView(R.id.desc)
    public TextView content;
    @BindView(R.id.listener)
    public TextView listener;
    @BindView(R.id.like)
    public TextView like;
    @BindView(R.id.comment)
    public TextView comment;

    @BindView(R.id.no_comments)
    public TextView no_comments;
    @BindView(R.id.multiImageView)
    public MultiImageView multiImageView;
    @BindView(R.id.listview)
    public ScrollViewWithListView listview;
    private Sound sound;
    private Context mContext;
    private Tab1CommentsAdapter tab1CommentsAdapter;

    @BindView(R.id.worth_name)
    public TextView worth_name;
    @BindView(R.id.worth_type)
    public ImageView worth_type;
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
    @BindView(R.id.worth_listener)
    public TextView worth_listener;

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT | FLAG_BTN ;
    }

    @Override
    protected boolean onPageBack() {

        return false;
    }

    @Override
    protected boolean onPageNext() {
        showShare();
        return false;
    }

    private void showShare() {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        final String url = MyNetApiConfig.ServerAddr + "/site/share-page?sid=" + sound.sid;
        L.e(url);
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(sound.stuname);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(EmojiCharacterUtil.decode(sound.desc)  );
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
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
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
                    paramsToShare.setText("分享 " +url);
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
                    if (sound.stuimage != null) {
                        if (sound.stuimage .contains("http://wx.qlogo.cn")) {
                            paramsToShare.setImagePath(sound.stuimage );
                            L.e(sound.stuimage );
                        } else {
                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr +sound.stuimage);
                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage );
                        }
                    } else {
                        Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        paramsToShare.setImageData(imageData);
                    }

                }
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
                    if (sound.stuimage != null) {
                        if (sound.stuimage .contains("http://wx.qlogo.cn")) {
                            paramsToShare.setImagePath(sound.stuimage );
                            L.e(sound.stuimage );
                        } else {
                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr +sound.stuimage);
                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage );
                        }
                    } else {
                        Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        paramsToShare.setImageData(imageData);
                    }

                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setContentType(Platform.SHARE_MUSIC);
                    if (sound.stuimage != null) {
                        if (sound.stuimage .contains("http://wx.qlogo.cn")) {
                            paramsToShare.setImagePath(sound.stuimage );
                            L.e(sound.stuimage );
                        } else {
                            //  paramsToShare.setImagePath(MyNetApiConfig.ImageServerAddr +sound.stuimage );
                            paramsToShare.setImageUrl(MyNetApiConfig.ImageServerAddr +sound.stuimage);
                            L.e(MyNetApiConfig.ImageServerAddr + sound.stuimage );
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

    @Override
    protected void initTitle() {
        setTitleBtnImg(R.mipmap.share_icon);
        setTitleText(getString(R.string.detail_pic));
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.detail_pic)); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), getString(R.string.detail_pic));

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getComments, MyNetRequestConfig
                        .getComments(getActivity(),String.valueOf( sound.sid)),
                "getComments", ItemDetailPicFragment.this, false, true);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.detail_pic));
        JAnalyticsInterface.onPageEnd(getActivity(), getString(R.string.detail_pic));
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab2;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab1_pic_detail;
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
    protected void initView(View v) {
        mContext = getActivity();
        ButterKnife.bind(this, v);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        sound = (Sound) getActivity().getIntent().getSerializableExtra("Sound");
        if (TextUtils.isEmpty(sound.images)) {
        } else {
            final ArrayList<String> imagesList = new Gson().fromJson(sound.images, new TypeToken<ArrayList<String>>() {
            }.getType());
            multiImageView.setList(imagesList);
            multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    ImagePagerActivity.startImagePagerActivity((mContext), imagesList, position, imageSize);
                }
            });
        }

        author.setText(sound.stuname);
//        content.setText(sound.desc);
        content.setText(EmojiCharacterUtil.decode(sound.desc));
        comment.setText(sound.comments);
        like.setText(String.valueOf(sound.like));
        listener.setText(String.valueOf(sound.views));
        try {
            publish_time.setText(String2TimeUtils.longToString(sound.created * 1000, "yyyy/MM/dd HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PictureUtils.showPicture(mContext, sound.stuimage, header, 47);
        if (sound.islike == 0) {
            DianZanUtils.initDianZan(mContext, like, false);
        } else {
            DianZanUtils.initDianZan(mContext, like, true);
        }
        like.setOnClickListener(this);
        tab1CommentsAdapter = new Tab1CommentsAdapter(getActivity(), String.valueOf(sound.sid), sound.fromuid + "");
        listview.setAdapter(tab1CommentsAdapter);
        listview.setOnItemClickListener(tab1CommentsAdapter);
        listview.setOnItemLongClickListener(tab1CommentsAdapter);

        tab1CommentsAdapter.setOnMoreClickListener(new Tab1CommentsAdapter.setDeleteCom() {
            @Override
            public void onDeleteCom() {
                //     L.e("f");

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getComments, MyNetRequestConfig
                                .getComments(getActivity(),String.valueOf( sound.sid)),
                        "getComments", ItemDetailPicFragment.this, false, true);
            }
        });
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.views, MyNetRequestConfig
                        .getComments(getActivity(),String.valueOf( sound.sid)),
                "views", ItemDetailPicFragment.this, false, true);

        TypeDictionary nsDictionary = new TypeDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.isreply = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "2";

        Gson gson = new Gson();
        String arr = gson.toJson(nsDictionary);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, 0, 1, "created", "asc", "0"),
                "worthSoundList", ItemDetailPicFragment.this, true, true);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

        if (id.equals("like")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                if (sound.islike == 0) {
                    like.setText(String.valueOf(sound.like + 1));
                    DianZanUtils.initDianZan(mContext, like, true);
                }

            }
        }else if (id.equals("getComments")) {

            if (type == NetCallBack.TYPE_SUCCESS) {
                ArrayList<CommentsInfo> commentsInfos
                        = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<CommentsInfo>>() {
                }.getType());

                if (commentsInfos == null || commentsInfos.size() == 0) {
                    comment.setText("0");
                } else {
                    tab1CommentsAdapter.setData(commentsInfos);
                    no_comments.setVisibility(View.GONE);
                    comment.setText(String.valueOf(commentsInfos.size()));
                }


            }
        }else if (id.equals("view")){
//            L.e(netResponse.toString());
        }else if (id.equals("worthSoundList")) {

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
                    PictureUtils.showPicture(getActivity(), soundWorth.stuimage, worth_header, 40);
                    worth_teacher_name.setText(soundWorth.stuname);
                    try {
                        worth_teacher_desc.setText(String2TimeUtils.longToString(soundWorth.created *1000,"yyyy-MM-dd HH:mm:ss"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    worth_like.setText(String.valueOf(soundWorth.like));
                    worth_comment.setText(String.valueOf(soundWorth.comments));
                    worth_listener.setText(String.valueOf(soundWorth.views));



                }

            }


        }


    }
    private Sound soundWorth;
    @OnClick({R.id.comment,R.id.header,R.id.ll_worth,
            R.id.worth_like,R.id.worth_header})
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
            case R.id.worth_like:
                if (AppShare.getIsLogin(getActivity())) {
                    RestNetCallHelper.callNet(getActivity(),
                            MyNetApiConfig.like, MyNetRequestConfig
                                    .like(getActivity(), AppShare.getUserInfo(getActivity()).uid,String.valueOf(soundWorth.sid) ),
                            "worth_like", ItemDetailPicFragment.this, false, true);
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText(getString(R.string.login_tips));
                }

                break;
            case R.id.worth_header:
                initHomepage(getActivity(), String.valueOf(soundWorth.fromuid));
                break;
            case R.id.like:
                if (AppShare.getIsLogin(getActivity())) {
                    RestNetCallHelper.callNet(getActivity(),
                            MyNetApiConfig.like, MyNetRequestConfig
                                    .like(getActivity(), AppShare.getUserInfo(getActivity()).uid, String.valueOf(sound.sid)),
                            "like", ItemDetailPicFragment.this, false, true);
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText(getString(R.string.login_tips));
                }
                break;
            case R.id.comment:

                if (AppShare.getIsLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    intent.putExtra("sid", String.valueOf(sound.sid));
                    intent.putExtra("fromuid", String.valueOf(AppShare.getUserInfo(getActivity()).uid));
                    intent.putExtra("touid", String.valueOf(sound.fromuid));
                    getActivity().startActivity(intent);
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText(getString(R.string.login_tips));
                }
                break;
            case R.id.header:
                initHomepage(getActivity(), String.valueOf(sound.fromuid));
                break;

        }
    }


}
