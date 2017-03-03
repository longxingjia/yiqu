package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab1SoundAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.adapter.Tab5DianpingAdapter;
import com.yiqu.iyijiayi.adapter.Tab5TiwenAdapter;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PageCursorView;
import com.yiqu.iyijiayi.utils.PictureUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private int rows = 100;
    private LoadMoreView mLoadMoreView;
    private int TYPE = 1;
    private String uid;
    private String myUid;
    private Tab5DianpingAdapter tab5DianpingAdapter;
    private Tab1XizuoAdapter tab5XizuoAdapter;
    //    private Tab1SoundAdapter tab1SoundAdapter;
    private Context mContext;
    private TextView price;
    private LinearLayout tea;
    private LinearLayout stu;
    private TextView questioncount;
    private TextView totalincome;
    private TextView questionincome;
    private TextView tincome;
    public ArrayList<Sound> sounds;
    private TextView content;
    private ImageView background;
    private HomePage homePage;

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

        if (homePage==null){
            setTitleText("我");
        }else {

            setTitleText(userInfo.username+"的主页");
        }


    }

    @Override
    protected void initView(View v) {
        homePage = (HomePage) getActivity().getIntent().getSerializableExtra("data");
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


        myUid = AppShare.getUserInfo(mContext).uid;

        tab5DianpingAdapter = new Tab5DianpingAdapter(getActivity(), uid);
//        tab1SoundAdapter = new Tab1SoundAdapter(getActivity());
        tab5XizuoAdapter = new Tab1XizuoAdapter(getActivity());

        listView.setAdapter(tab5DianpingAdapter);
        listView.setOnItemClickListener(tab5DianpingAdapter);
        listView.setRefreshListListener(this);
        if (homePage == null) {
            userInfo = AppShare.getUserInfo(mContext);
            if (userInfo.type.equals("2")) {
                TYPE = 2;

            } else {
                TYPE = 1;
            }

            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getUserPageSoundList,
                    MyNetRequestConfig.getUserPageSoundList(getActivity()
                            , TYPE, userInfo.uid, count, rows, myUid),
                    "getUserPageSoundList",
                    HomePageFragment.this);

        } else {
            userInfo = homePage.user;
            sounds = homePage.sound;
            tab5DianpingAdapter.setData(sounds);
            LogUtils.LOGE(tag, homePage.toString());
        }


        initUI();



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
        tea = (LinearLayout) v.findViewById(R.id.tea);
        stu = (LinearLayout) v.findViewById(R.id.stu);
        xizuo_tab = (RelativeLayout) v.findViewById(R.id.xizuo_tab);


    }


    private class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            TYPE = index;
            LogUtils.LOGE(tag, index + "---");
            int i = index;
            switch (TYPE) {
                case 1:
                    if (userInfo.type.equals("2")) {

                    } else {
                        i = index - 1;
                    }
                    listView.setAdapter(tab5DianpingAdapter);
                    break;
                case 2:
                    if (userInfo.type.equals("2")) {
                        i = index - 2;
                    }
                    listView.setAdapter(tab5DianpingAdapter);
                    break;

                case 3:
                    if (userInfo.type.equals("2")) {
                        i = index - 1;
                    } else {
                        i = index - 2;
                    }
                    listView.setAdapter(tab5XizuoAdapter);

                    break;

            }
            count = 0;
            cursor.setPosition(i);
            LogUtils.LOGE(tag, i + "");

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
            InitCursor(3);
            dianping_tab.setVisibility(View.VISIBLE);
            descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.title, userInfo.followcount, userInfo.myfollowcount);
            TYPE = 2;

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
            TYPE = 1;
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
        PictureUtils.showPicture(getActivity(), userInfo.backgroundimage, background);
    }


    /*
     * 初始化图片的位移像素
     */
    private void InitCursor(int count) {
        cursor.setCount(count);
    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if (id.equals("getUserPageSoundList")) {
            LogUtils.LOGE(tag, netResponse.toString());
            if (type == NetCallBack.TYPE_SUCCESS) {
                switch (TYPE) {
                    case 2:
//                        break;
                    case 1:
                        try {
                            sounds = JsonUtils.parseSoundList(netResponse.data);
                            tab5DianpingAdapter.setData(sounds);
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
                            ArrayList<Xizuo> xizuos = JsonUtils.parseXizuoList(netResponse.data);
                            tab5XizuoAdapter.setData(xizuos);
                            if (xizuos.size() == rows) {
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
            }
        } else if ("getUserPageSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {
                LogUtils.LOGE(tag, netResponse.toString());
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
