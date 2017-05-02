/**
 * @filename PersonAll.java
 * @author byron(linbochuan@hopsun.cn)
 * @date 2013-9-2
 * @vsersion 1.0
 * Copyright (C) 2013 辉盛科技发展责任有限公司
 */
package com.yiqu.iyijiayi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab3.ShowArticalFragment;
import com.yiqu.iyijiayi.model.SelectArticle;

import java.util.ArrayList;

public class Tab3ArticleAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<SelectArticle> datas = new ArrayList<SelectArticle>();
    private Activity mContext;
    private Fragment fragment;

    private String tag = "Tab3MusicAdapter";
    //  private ArrayList<Like> likes;

    public Tab3ArticleAdapter(Fragment fragment) {
        this.fragment = fragment;
        mContext = fragment.getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);

    }


    public void setData(ArrayList<SelectArticle> list) {

        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<SelectArticle> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public SelectArticle getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {

        TextView title;
        TextView class_name;
        TextView author;
        TextView reads;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab3_article_adapter, null);
                h.title = (TextView) v.findViewById(R.id.title);
                h.author = (TextView) v.findViewById(R.id.author);
                h.class_name = (TextView) v.findViewById(R.id.class_name);
                h.reads = (TextView) v.findViewById(R.id.reads);

                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final SelectArticle f = getItem(position);
            h.title.setText(f.title);
            if (TextUtils.isEmpty(f.author)) {
                h.author.setVisibility(View.GONE);
            } else {
                h.author.setVisibility(View.VISIBLE);
                h.author.setText(f.author);
            }
            h.class_name.setText(f.class_name);
            h.reads.setText(String.valueOf(f.reads));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 < 1) {
            return;
        }
        SelectArticle f = getItem(arg2 - 1);//加了头部
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }

        Intent intent = new Intent(mContext, StubActivity.class);
        intent.putExtra("fragment", ShowArticalFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", f);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, 0);

    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


}
