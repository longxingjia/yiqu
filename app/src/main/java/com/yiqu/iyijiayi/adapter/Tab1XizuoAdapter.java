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
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.XizuoItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;


import java.util.ArrayList;

public class Tab1XizuoAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag ="Tab1XizuoAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Xizuo> datas = new ArrayList<Xizuo>();
    private Context mContext;

    public Tab1XizuoAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

    }


    public void setData(ArrayList<Xizuo> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Xizuo> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Xizuo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class HoldChild {

        TextView name;
        TextView content;
        ImageView icon;
        ImageView musictype;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.remen_xizuo, null);
                h.name = (TextView) v.findViewById(R.id.musicname);
                h.content = (TextView) v.findViewById(R.id.desc);
                h.icon = (ImageView) v.findViewById(R.id.header);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            Xizuo f = getItem(position);
            h.name.setText(f.musicname);
            h.content.setText(f.desc);
            if (f.type==1){
                h.musictype.setImageResource(R.mipmap.shengyue);
            }else {
                h.musictype.setImageResource(R.mipmap.boyin);
            }

            PictureUtils.showPicture(mContext,f.stuimage,h.icon,47);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Xizuo f = getItem(position);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (AppShare.getIsLogin(mContext)){
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", XizuoItemDetailFragment.class.getName());
            i.putExtra("data",f.sid+"");
            LogUtils.LOGE(tag,f.sid+"");

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
