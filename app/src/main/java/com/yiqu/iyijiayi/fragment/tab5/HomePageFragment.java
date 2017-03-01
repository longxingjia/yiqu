package com.yiqu.iyijiayi.fragment.tab5;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.MyFragmentPagerAdapter;
import com.yiqu.iyijiayi.adapter.Tab5DianpingAdapter;
import com.yiqu.iyijiayi.adapter.Tab5TiwenAdapter;
import com.yiqu.iyijiayi.adapter.Tab5XizuoAdapter;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.NoScollViewPager;
import com.yiqu.iyijiayi.utils.PageCursorView;
import com.yiqu.iyijiayi.utils.PictureUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class HomePageFragment extends AbsAllFragment implements RefreshList.IRefreshListViewListener, LoadMoreView.OnMoreListener {


    private PageCursorView cursor;
    private UserInfo userInfo;
    private String tag = "HomePageFragment";
    private LinearLayout dianping_tab;
    private LinearLayout tiwen_tab;
    private RelativeLayout xizuo_tab;
    private TextView name;
    private ImageView sex;
    private TextView desc;
    private ImageView head;
    private RefreshList listView;
    private int count = 0;
    private int rows = 10;
    private LoadMoreView mLoadMoreView;
    private String type = "1";
    private String uid;
    private String myUid;
    private Tab5DianpingAdapter tab5DianpingAdapter;
    private Tab5XizuoAdapter tab5XizuoAdapter;
    private Tab5TiwenAdapter tab5TiwenAdapter;
    private Context mContext;
    private TextView price;
    private LinearLayout tea;

    private LinearLayout stu;
    private TextView questioncount;
    private TextView totalincome;
    private TextView questionincome;
    private TextView tincome;
    public ArrayList<Sound> sound;
    private TextView content;

    @Override
    protected int getTitleView() {

        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.home_page_fragment;
    }

    @Override
    protected int getTitleBarType() {

        return FLAG_BACK | FLAG_TXT;
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

        setTitleText("我");
    }

    @Override
    protected void initView(View v) {

        mContext = getActivity();
        listView = (RefreshList) v.findViewById(R.id.listView);
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


        HomePage homePage = (HomePage) getActivity().getIntent().getSerializableExtra("data");
        myUid = AppShare.getUserInfo(mContext).uid;
        userInfo = homePage.user;
        sound = homePage.sound;

        LogUtils.LOGE(tag, homePage.toString());
        initUI();

        tab5DianpingAdapter = new Tab5DianpingAdapter(getActivity(), uid);
        tab5TiwenAdapter = new Tab5TiwenAdapter(getActivity(), uid);
        tab5XizuoAdapter = new Tab5XizuoAdapter(getActivity(), uid);

        listView.setAdapter(tab5DianpingAdapter);
        listView.setOnItemClickListener(tab5DianpingAdapter);
        listView.setRefreshListListener(this);
        tab5DianpingAdapter.setData(sound);
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
        dianping_tab = (LinearLayout) v.findViewById(R.id.dianping_tab);
        tiwen_tab = (LinearLayout) v.findViewById(R.id.tiwen_tab);
        tea = (LinearLayout) v.findViewById(R.id.tea);
        stu = (LinearLayout) v.findViewById(R.id.stu);
        xizuo_tab = (RelativeLayout) v.findViewById(R.id.xizuo_tab);


    }


    private class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
//            if (!userInfo.type.equals("2")) {
//                index = i - 1;
//            } else {
//                index = i;
//            }
            index = i;
            type = index + "";
        }

        @Override
        public void onClick(View v) {

            LogUtils.LOGE(tag, index + "");

            cursor.setPosition(index);
            switch (index) {
                case 2:
                    if (userInfo.type.equals("2")) {
                        cursor.setPosition(index - 2);
                    }

                    break;
                case 1:
                    if (userInfo.type.equals("2")) {

                    } else {
                        cursor.setPosition(index - 1);
                    }
                    break;
                case 3:
                    if (userInfo.type.equals("2")) {
                        cursor.setPosition(index - 1);
                    } else {
                        cursor.setPosition(index - 2);
                    }
                    //    listView.setAdapter(tab5XizuoAdapter);

                    break;

            }

            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getUserPageSoundList,
                    MyNetRequestConfig.getUserPageSoundList(getActivity()
                            , type, userInfo.uid, count, rows, myUid),
                    "getUserPageSoundList",
                    HomePageFragment.this);

        }
    }

    private void initUI() {
        String descStr = "";
        if (userInfo.type.equals("2")) {
            InitCursor(3);
            dianping_tab.setVisibility(View.VISIBLE);
            descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.title, userInfo.followcount, userInfo.myfollowcount);
            type = "2";

            tea.setVisibility(View.VISIBLE);
            stu.setVisibility(View.GONE);
            price.setText(userInfo.price);
            questioncount.setText(userInfo.questioncount);
            questionincome.setText(userInfo.questionincome);
            totalincome.setText(userInfo.totalincome);
        } else {
            tea.setVisibility(View.GONE);
            stu.setVisibility(View.VISIBLE);
            InitCursor(2);
            dianping_tab.setVisibility(View.GONE);
            descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.school, userInfo.followcount, userInfo.myfollowcount);
            type = "1";
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
        dianping_tab.setOnClickListener(new txListener(2));
        tiwen_tab.setOnClickListener(new txListener(1));
        xizuo_tab.setOnClickListener(new txListener(3));

        PictureUtils.showPicture(getActivity(), userInfo.userimage, head);
    }


    /*
     * 初始化图片的位移像素
     */
    private void InitCursor(int count) {
        cursor.setCount(count);
    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        LogUtils.LOGE(tag, netResponse.toString());
        if (id.equals("getUserPageSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {


//                try {
//                    datas = parseList(netResponse.data.toString());
//                    tab2ListFragmetAdapter.setData(datas);
//                    if (datas.size() == rows) {
//                        mLoadMoreView.setMoreAble(true);
//                    }
//                    count += rows;
//                    resfreshOk();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            } else {
                resfreshFail();
            }
        } else if ("getUserPageSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

//                try {
//                    datas = parseList(netResponse.data.toString());
//                    tab2ListFragmetAdapter.addData(datas);
//                    if (datas.size() < rows) {
//                        mLoadMoreView.setMoreAble(false);
//                    }
//                    count += rows;
//                    mLoadMoreView.end();

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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
                        , uid, type, count, rows, userInfo.uid),
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
                        MyNetApiConfig.get_follow_recommend_list,
                        MyNetRequestConfig.get_follow_recommend_list(getActivity()
                                , uid, type, count, rows),
                        "get_follow_recommend_list_more",
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
