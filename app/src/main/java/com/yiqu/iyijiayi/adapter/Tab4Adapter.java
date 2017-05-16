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
import com.yiqu.iyijiayi.fragment.tab4.EventFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Events;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class Tab4Adapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Events> datas = new ArrayList<Events>();
    private Context mContext;

    private String tag = "Tab1SoundAdapter";

    public Tab4Adapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

    }


    public void setData(ArrayList<Events> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Events> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Events getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {


        TextView title;
        ImageView icon;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab4_event_adapt, null);
                h.title = (TextView) v.findViewById(R.id.title);
                h.icon = (ImageView) v.findViewById(R.id.icon);

                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            Events f = getItem(position);
            h.title.setText(f.title);
            PictureUtils.showPicture(mContext,f.image,h.icon,25);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Events f = getItem(arg2);

        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }

        Intent i = new Intent(mContext, StubActivity.class);
        i.putExtra("fragment", EventFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",f);
        i.putExtras(bundle);
        mContext.startActivity(i);

//        if (AppShare.getIsLogin(mContext)){
//            Intent i = new Intent(mContext, StubActivity.class);
//            i.putExtra("fragment", EventFragment.class.getName());
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("data",f);
//            i.putExtras(bundle);
//            mContext.startActivity(i);
//
//        }else {
//            Intent i = new Intent(mContext, StubActivity.class);
//            i.putExtra("fragment", SelectLoginFragment.class.getName());
//            ToastManager.getInstance(mContext).showText("请登录后再试");
//            mContext.startActivity(i);
//
//        }

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
