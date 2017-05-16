package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab3ArticleAdapter;
import com.model.Music;
import com.yiqu.iyijiayi.model.Article_class_name;
import com.yiqu.iyijiayi.model.SelectArticle;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.view.tagview.Tag;
import com.yiqu.iyijiayi.view.tagview.TagListView;
import com.yiqu.iyijiayi.view.tagview.TagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SelectArticalFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {
    private static final String TAG = "SelectArticalFragment";


    @BindView(R.id.search)
    public LinearLayout search;
    @BindView(R.id.listView)
    public RefreshList listView;

    private int count = 0;
    private int rows = 20;
    private String class_id = "0";
    private String event_id = "0";
    private LoadMoreView mLoadMoreView;

    private Tab3ArticleAdapter tab3ArticleAdapter;
    private ArrayList<SelectArticle> selectArticles;
    private TagListView mTagListView;

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit:

                break;
            default:
                break;
        }
    }


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.select_artical_fragment;
    }

    private final List<Tag> mTags = new ArrayList<Tag>();

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        View headview =  LayoutInflater.from(getActivity()).inflate(R.layout.tab4_article_headview, null);
        tab3ArticleAdapter = new Tab3ArticleAdapter(this);
        listView.setAdapter(tab3ArticleAdapter);
        listView.setOnItemClickListener(tab3ArticleAdapter);
        listView.setRefreshListListener(this);
        listView.addHeaderView(headview);
//
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);

        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        mTagListView = (TagListView) headview.findViewById(R.id.tagview);
        //   setUpData();

        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
             //   LogUtils.LOGE("tag", tag.toString());

                Intent intent = new Intent(getActivity(), StubActivity.class);
                intent.putExtra("fragment", SelectArticalClassFragment.class.getName());
                intent.putExtra("title",tag.getTitle());
                intent.putExtra("class_id",String.valueOf(tag.getId()));
                startActivityForResult(intent,1);


            }
        });

    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getSoundArticleList,
                MyNetRequestConfig.getSoundArticleList(getActivity()
                        , class_id, event_id, String.valueOf(rows), String.valueOf(count)),
                "getSoundArticleList",
                SelectArticalFragment.this);

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getSoundArticleClass,
                MyNetRequestConfig.getSoundArticleClass(getActivity()),
                "getSoundArticleClass",
                SelectArticalFragment.this);
    }


    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundArticleList,
                        MyNetRequestConfig.getSoundArticleList(getActivity()
                                , class_id, event_id, String.valueOf(rows), String.valueOf(count)),
                        "getSoundArticleList_more",
                        SelectArticalFragment.this);

            }
        }

        return false;
    }

    @Override
    public void onRefresh() {
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        count = 0;
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getSoundArticleList,
                MyNetRequestConfig.getSoundArticleList(getActivity()
                        , class_id, event_id, String.valueOf(rows), String.valueOf(count)),
                "getSoundArticleList",
                SelectArticalFragment.this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle b = data.getExtras(); //data为B中回传的Intent
            Intent intent = new Intent();
            intent.putExtras(b);
            getActivity().setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            getActivity().finish();//此处一定要调用finish()方法

        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

        if (id.equals("getSoundArticleList")) {
            if (type == TYPE_SUCCESS) {
                selectArticles = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<SelectArticle>>() {
                }.getType());
                //  LogUtils.LOGE(TAG, backgroudMusics.toString());
                tab3ArticleAdapter.setData(selectArticles);
                if (selectArticles.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                count += rows;
                resfreshOk();
            } else {
                //  tab3MusicAdapter.setData(null);
                resfreshFail();
            }
        } else if (id.equals("getSoundArticleList_more")) {
            if (TYPE_SUCCESS == type) {

                selectArticles = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<SelectArticle>>() {
                }.getType());
                tab3ArticleAdapter.addData(selectArticles);
                if (selectArticles.size() < rows) {
                    mLoadMoreView.setMoreAble(false);
                }
                count += rows;
                mLoadMoreView.end();

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        } else if (id.equals("getSoundArticleClass")) {
            if (type == TYPE_SUCCESS) {
             //   LogUtils.LOGE("tag", netResponse.toString());
               ArrayList<Article_class_name> classNames = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Article_class_name>>() {
                }.getType());
                setUpData(classNames);
                mTagListView.setTags(mTags);
            }
        }
    }

    private void setUpData(ArrayList<Article_class_name> classNames) {
        for (int i = 0; i < classNames.size(); i++) {
            Tag tag = new Tag();
            Article_class_name article_class_name = classNames.get(i);
            tag.setId(article_class_name.id);
            tag.setChecked(true);
            tag.setTitle(article_class_name.class_name);
            mTags.add(tag);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("范文列表");
        JAnalyticsInterface.onPageStart(getActivity(),"范文列表");
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("范文列表");
        JAnalyticsInterface.onPageEnd(getActivity(),"范文列表");

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
        setTitleText("范文");

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
