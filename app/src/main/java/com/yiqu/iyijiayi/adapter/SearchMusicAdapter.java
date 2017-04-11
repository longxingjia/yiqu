/**
 * @filename PersonAll.java
 * @author byron(linbochuan@hopsun.cn)
 * @date 2013-9-2
 * @vsersion 1.0
 * Copyright (C) 2013 辉盛科技发展责任有限公司
 */
package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.yiqu.iyijiayi.fragment.tab3.DownloadXizuoFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.utils.AppShare;

import java.util.ArrayList;

public class SearchMusicAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag ="Tab1XizuoAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Music> datas = new ArrayList<Music>();
    private Context mContext;

    public SearchMusicAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

    }


    public void setData(ArrayList<Music> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Music> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Music getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class HoldChild {

        TextView name;
        TextView musictype;
        TextView accompaniment;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.search_music, null);
                h.name = (TextView) v.findViewById(R.id.musicname);
                h.musictype = (TextView) v.findViewById(R.id.musictype);
                h.accompaniment = (TextView) v.findViewById(R.id.accompaniment);

                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            Music f = getItem(position);
            h.name.setText(f.musicname);
            h.musictype.setText(f.musictype);
            h.accompaniment.setText(f.accompaniment);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position-1<0){
            return;
        }

        Music f = getItem(position-1);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (AppShare.getIsLogin(mContext)){
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", DownloadXizuoFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putSerializable("music",f);
            i.putExtras(bundle);
            mContext.startActivity(i);
        }else {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SelectLoginFragment.class.getName());
            ToastManager.getInstance(mContext).showText("请登录后再试");
            mContext.startActivity(i);

        }
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
