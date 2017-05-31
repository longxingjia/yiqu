package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.ImagePagerActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.adapter.Tab1SoundAdapter;
import com.yiqu.iyijiayi.adapter.Tab5DianpingAdapter;

import com.yiqu.iyijiayi.adapter.Tab5PicAdapter;
import com.yiqu.iyijiayi.adapter.Tab5XizuoAdapter;

import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab3.RecordFragment;
import com.yiqu.iyijiayi.fragment.tab3.Tab3Activity;
import com.yiqu.iyijiayi.fragment.tab3.TextQuestionFragment;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PageCursorView;
import com.yiqu.iyijiayi.utils.PictureUtils;

import org.json.JSONException;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class HomePageFragment extends AbsAllFragment implements RefreshList.IRefreshListViewListener, LoadMoreView.OnMoreListener {


    private PageCursorView cursor;
    private UserInfo userInfo;
    private String tag = "HomePageFragment";
    private LinearLayout dianping_tab;
    private LinearLayout tiwen_tab;
    private LinearLayout pic_tab;
    private RelativeLayout xizuo_tab;
    private TextView name;
    private ImageView sex;
    private TextView desc;
    private ImageView head;
    private RefreshList listView;
    private int count = 0;
    private int rows = 100;
    private LoadMoreView mLoadMoreView;
    private int TYPE = 1;
    private String uid;
    private String myUid = "0";
    private Tab5DianpingAdapter tab5DianpingAdapter;
    private Tab5XizuoAdapter tab5XizuoAdapter;
    private Tab1SoundAdapter tab1SoundAdapter;
    private Tab5PicAdapter tab5PicAdapter;
    private Context mContext;
    private TextView price;

    private TextView questioncount;
    private TextView totalincome;
    private TextView questionincome;
    private TextView tincome;
    private ArrayList<Sound> sounds;
    private TextView content;
    private ImageView background;
    private HomePage homePage;
    private ArrayList<Sound> xizuos;
    private LinearLayout ll_tw;
    private TextView price1;
    private TextView ok;
    /**
     * 是否是自己的个人主页，true 是
     */
    private boolean flag = false;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5_edit_info;
    }

    @Override
    protected int getBodyView() {
        return R.layout.home_page_fragment;
    }

    @Override
    protected int getTitleBarType() {
        if (flag) {
            return FLAG_ALL;
        } else {
            return FLAG_BACK | FLAG_TXT;
        }

    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {

        Model.startNextAct(getActivity(),
                InfoFragment.class.getName());
        return false;
    }

    @Override
    protected void initTitle() {
        if (flag) {
            setTitleText("我");
        } else {
            setTitleText(userInfo.username + "的主页");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人主页"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), "个人主页");


    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人主页");
        JAnalyticsInterface.onPageEnd(getActivity(), "个人主页");

    }


    @Override
    protected void initView(View v) {
        homePage = (HomePage) getActivity().getIntent().getSerializableExtra("data");
        mContext = getActivity();
        listView = (RefreshList) v.findViewById(R.id.listView);
        ll_tw = (LinearLayout) v.findViewById(R.id.ll_tw);
        price1 = (TextView) v.findViewById(R.id.price);
        ok = (TextView) v.findViewById(R.id.ok);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View headview = inflater.inflate(R.layout.home_page_headerview, null);
        listView.addHeaderView(headview);
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        InitHeadView(headview);
        if (AppShare.getIsLogin(mContext))
            myUid = AppShare.getUserInfo(mContext).uid;


        listView.setRefreshListListener(this);
        if (homePage == null) {
            userInfo = AppShare.getUserInfo(mContext);
            flag = true;
//            if (userInfo.type.equals("2")) {
//
//                listView.setAdapter(tab5DianpingAdapter);
//                listView.setOnItemClickListener(tab5DianpingAdapter);
////                TYPE = 2;
//
//            } else {
//                listView.setAdapter(tab1SoundAdapter);
//                listView.setOnItemClickListener(tab1SoundAdapter);
//
//            }
//            listView.setAdapter(tab5PicAdapter);
//            listView.setOnItemClickListener(tab5PicAdapter);
//            TYPE = 4;
//            RestNetCallHelper.callNet(getActivity(),
//                    MyNetApiConfig.getUserPageSoundList,
//                    MyNetRequestConfig.getUserPageSoundList(getActivity()
//                            , TYPE, userInfo.uid, count, rows, myUid),
//                    "getUserPageSoundList",
//                    HomePageFragment.this);

        } else {
            flag = false;
            userInfo = homePage.user;
            sounds = homePage.sound;
//            if (userInfo.type.equals("2")) {
//                listView.setAdapter(tab5DianpingAdapter);
//                listView.setOnItemClickListener(tab5DianpingAdapter);
//                tab5DianpingAdapter.setData(sounds);
//                price1.setText(userInfo.price + "");

//            } else {
              //  listView.setAdapter(tab1SoundAdapter);
             //   tab1SoundAdapter.setData(sounds);
             //   listView.setOnItemClickListener(tab1SoundAdapter);
//            }
            if (userInfo.type.equals("2")){
                ll_tw.setVisibility(View.VISIBLE);
            }

        }
        if (AppShare.getUserInfo(getActivity())!=null &&AppShare.getUserInfo(getActivity()).uid.equals(userInfo.uid) ){
            ll_tw.setVisibility(View.GONE);
        }


        tab5DianpingAdapter = new Tab5DianpingAdapter(getActivity(), uid);
        tab1SoundAdapter = new Tab1SoundAdapter(this);
        tab5PicAdapter = new Tab5PicAdapter(getActivity(),flag);
        tab5XizuoAdapter = new Tab5XizuoAdapter(getActivity(),flag);
        listView.setAdapter(tab5PicAdapter);
        listView.setOnItemClickListener(tab5PicAdapter);


        TYPE = 4;
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getUserPageSoundList,
                MyNetRequestConfig.getUserPageSoundList(getActivity()
                        , TYPE, userInfo.uid, count, rows, myUid),
                "getUserPageSoundList",
                HomePageFragment.this);
        initUI();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Model.startNextAct(getActivity(),
//                        Tab3Activity.class.getName());
//                Intent intent = new Intent(getActivity(), Tab3Activity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", userInfo);
//                intent.putExtras(bundle);
//                startActivity(intent);
                if (!AppShare.getIsLogin(getActivity())) {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText(getString(R.string.login_tips));
                    return;

                }

                String title = "提示";
                String[] items = new String[]{"文字提问","作品提问"};

                MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(mContext, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
                    @Override
                    public void onTea(int tea) {
                        switch (tea) {
                            case 0:
                                Model.startNextAct(getActivity(),
                                        TextQuestionFragment.class.getName());
                                break;
                            case 1:
                                Model.startNextAct(getActivity(),
                                        RecordFragment.class.getName());
//                                Intent i = new Intent(getActivity(), StubActivity.class);
//                                i.putExtra("fragment", RecordFragment.class.getName());
//                                getActivity().startActivity(i);
                                break;
                        }
                    }
                });
                menuDialogSelectTeaHelper.show(v);






            }
        });

    }

    /*
 * 初始化标签名
 */
    public void InitHeadView(View v) {
        cursor = (PageCursorView) v.findViewById(R.id.cursor);
        name = (TextView) v.findViewById(R.id.name);
        desc = (TextView) v.findViewById(R.id.desc);
        content = (TextView) v.findViewById(R.id.content);
        price = (TextView) v.findViewById(R.id.price);
        questioncount = (TextView) v.findViewById(R.id.questioncount);
        totalincome = (TextView) v.findViewById(R.id.totalincome);
        tincome = (TextView) v.findViewById(R.id.tincome);
        questionincome = (TextView) v.findViewById(R.id.questionincome);
        sex = (ImageView) v.findViewById(R.id.sex);
        head = (ImageView) v.findViewById(R.id.head);
        background = (ImageView) v.findViewById(R.id.background);
        dianping_tab = (LinearLayout) v.findViewById(R.id.dianping_tab);
        tiwen_tab = (LinearLayout) v.findViewById(R.id.tiwen_tab);
        pic_tab = (LinearLayout) v.findViewById(R.id.pic_tab);

        xizuo_tab = (RelativeLayout) v.findViewById(R.id.xizuo_tab);

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> imagesList = new ArrayList<String>();
                String url = userInfo.userimage;
                imagesList.add(url);
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(v.getMeasuredWidth(), v.getMeasuredHeight());
                ImagePagerActivity.startImagePagerActivity((getActivity()), imagesList, 0, imageSize);
            }
        });


    }


    private class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TYPE = index;
            int i = index;

            switch (index) {
                case 1://tiwen

                    i = index - 1;
                    TYPE = 4;
                    listView.setAdapter(tab5PicAdapter);


                    break;
                case 2://dianping

                    i = index - 1;
                    //  listView.setAdapter(tab5DianpingAdapter);
                    TYPE = 3;
                    listView.setAdapter(tab5XizuoAdapter);

                    break;

                case 3://xizuo
                    if (userInfo.type.equals("2")) {
                        i = index - 1;
                    } else {
                        i = index - 2;
                    }
                    //    listView.setAdapter(tab5XizuoAdapter);
                    TYPE = 2;
                    listView.setAdapter(tab5DianpingAdapter);

                    break;

                case 4:
                    if (userInfo.type.equals("2")) {
                        i = index - 1;
                    } else {
                        i = index - 2;
                    }
                    //    listView.setAdapter(tab5XizuoAdapter);
                    TYPE = 1;
                    listView.setAdapter(tab1SoundAdapter);

                    break;

            }
            count = 0;
            cursor.setPosition(i);

            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getUserPageSoundList,
                    MyNetRequestConfig.getUserPageSoundList(getActivity()
                            , TYPE, userInfo.uid, count, rows, myUid),
                    "getUserPageSoundList",
                    HomePageFragment.this);

        }
    }

    private void initUI() {
        String descStr = "";
        if (userInfo.type.equals("2")) {
            InitCursor(4);
            dianping_tab.setVisibility(View.VISIBLE);
            String title =  userInfo.title;
            if (title.length()>14){
                title = title.substring(0,14);
            }
            if (TextUtils.isEmpty(userInfo.title)) {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", "未填写", userInfo.followcount, userInfo.myfollowcount);
            } else {
                descStr = String.format("%s | 粉丝:%s | 收听:%s",title , userInfo.followcount, userInfo.myfollowcount);
            }
            TYPE = 4;

//            tea.setVisibility(View.VISIBLE);
//            stu.setVisibility(View.GONE);
            price.setText(userInfo.price);
            questioncount.setText(userInfo.questioncount);
            questionincome.setText(userInfo.questionincome);
            totalincome.setText(userInfo.totalincome);
        } else {
//            tea.setVisibility(View.GONE);
//            stu.setVisibility(View.VISIBLE);
            InitCursor(3);
            dianping_tab.setVisibility(View.GONE);
            if (TextUtils.isEmpty(userInfo.school)) {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", "未填写", userInfo.followcount, userInfo.myfollowcount);
            } else {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.school, userInfo.followcount, userInfo.myfollowcount);
            }
            TYPE = 4;
            tincome.setText(userInfo.totalincome);
        }
        uid = userInfo.uid;
        name.setText(userInfo.username);
        content.setText(descStr);
        if (!TextUtils.isEmpty(userInfo.desc)) {
            desc.setText(userInfo.desc);
        }

        if (userInfo.sex.equals("0")) {
            sex.setBackgroundResource(R.mipmap.sex_female);
        } else {
            sex.setBackgroundResource(R.mipmap.sex_male);
        }
        dianping_tab.setOnClickListener(new txListener(3));
        tiwen_tab.setOnClickListener(new txListener(4));
        xizuo_tab.setOnClickListener(new txListener(2));
        pic_tab.setOnClickListener(new txListener(1));

        PictureUtils.showPicture(getActivity(), userInfo.userimage, head);
        PictureUtils.showBackgroudPicture(getActivity(), userInfo.backgroundimage, background);

    }

    /*
     * 初始化图片的位移像素
     */
    private void InitCursor(int count) {
        cursor.setCount(count);
        cursor.setPosition(0);
        if (count == 2) {
            cursor.setOffset(120);
        } else if (count == 3) {
            cursor.setOffset(80);
        } else if (count == 4) {
            cursor.setOffset(50);
        }
    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getUserPageSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                switch (TYPE) {
                    case 2:
                        try {
                            sounds = JsonUtils.parseSoundList(netResponse.data);
                            tab5DianpingAdapter.setData(sounds);
                            listView.setOnItemClickListener(tab5DianpingAdapter);


                            if (sounds.size() == rows) {
                                mLoadMoreView.setMoreAble(true);
                            }
                            count += rows;
                            resfreshOk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            sounds = JsonUtils.parseSoundList(netResponse.data);
                            tab1SoundAdapter.setData(sounds);
                            listView.setOnItemClickListener(tab1SoundAdapter);

                            if (sounds.size() == rows) {
                                mLoadMoreView.setMoreAble(true);
                            }
                            count += rows;
                            resfreshOk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        break;
                    case 3:
                        try {
                            xizuos = JsonUtils.parseXizuoList(netResponse.data);
                            tab5XizuoAdapter.setData(xizuos);
                            if (xizuos.size() == rows) {
                                mLoadMoreView.setMoreAble(true);
                            }
                            count += rows;
                            resfreshOk();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (position < 2) {
                                    return;
                                }

                                Sound xizuo = xizuos.get(position - 2);

                                if (AppShare.getIsLogin(mContext)) {
                                    Intent i = new Intent(mContext, StubActivity.class);
                                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                                    i.putExtra("data", xizuo.sid + "");
                                    mContext.startActivity(i);
                                } else {
                                    Intent i = new Intent(mContext, StubActivity.class);
                                    i.putExtra("fragment", SelectLoginFragment.class.getName());
                                    ToastManager.getInstance(mContext).showText("请登录后再试");
                                    mContext.startActivity(i);

                                }
                            }
                        });
                        break;
                    case 4:
                        try {
                            sounds = JsonUtils.parseSoundList(netResponse.data);
                            tab5PicAdapter.setData(sounds);
                            listView.setOnItemClickListener(tab5PicAdapter);

                            if (sounds.size() == rows) {
                                mLoadMoreView.setMoreAble(true);
                            }
                            count += rows;
                            resfreshOk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } else {
                resfreshFail();
//                switch (TYPE) {
//                    case 2:
//                        try {
//                            sounds = null;
//                            tab5DianpingAdapter.setData(sounds);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case 1:
//                        try {
//                            sounds = null;
//
//                            tab1SoundAdapter.setData(sounds);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                        break;
//                    case 3:
//                        try {
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                }
            }
        } else if ("getUserPageSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {
                LogUtils.LOGE(tag, netResponse.toString());


            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getUserPageSoundList,
                MyNetRequestConfig.getUserPageSoundList(getActivity()
                        , TYPE, uid, count, rows, userInfo.uid),
                "getUserPageSoundList",
                this);

    }

    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getUserPageSoundList,
                        MyNetRequestConfig.getUserPageSoundList(getActivity()
                                , TYPE, uid, count, rows, userInfo.uid),
                        "getUserPageSoundList_more",
                        this);

            }
        }


        return false;
    }

    public void resfreshOk() {
        listView.refreshOk();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listView.stopRefresh();
            }


        }.execute();

    }

    public void resfreshFail() {
        listView.refreshFail();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listView.stopRefresh();
            }


        }.execute();
    }


}
