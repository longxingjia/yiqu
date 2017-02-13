/**
 * @filename PersonAll.java
 * @author byron(linbochuan@hopsun.cn)
 * @date 2013-9-2
 * @vsersion 1.0
 * Copyright (C) 2013 辉盛科技发展责任有限公司
 */
package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;

import java.util.ArrayList;

public class Tab1SoundAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private ImageLoaderHm<ImageView> mImageLoaderHm;

    public Tab1SoundAdapter(Context context, ImageLoaderHm<ImageView> m) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mImageLoaderHm = m;
    }


    public void setData(ArrayList<Sound> list) {
        // TODO Auto-generated method stub
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Sound> allDatas) {
        // TODO Auto-generated method stub
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Sound getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {

        TextView name;
        TextView content;
        ImageView icon;

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
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            Sound f = getItem(position);
            h.name.setText(f.musicname);
            h.content.setText(f.desc);
            LogUtils.LOGE(MyNetApiConfig.ImageServerAddr+f.stuimage);
            mImageLoaderHm.DisplayImage( MyNetApiConfig.ImageServerAddr+f.stuimage, h.icon);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
//        Xizuo f = getItem(arg2 - 1);
//
//        if (!isNetworkConnected(mContext)) {
//            ToastManager.getInstance(mContext).showText(
//                    R.string.fm_net_call_no_network);
//            return;
//        }.

//        if ("1".equals(f.type)) {
//            //1表示资讯类信息
//            Intent i = new Intent(mContext, StubActivity.class);
//            i.putExtra("fragment", BeautifulTextFragment.class.getName());
//            i.putExtra("data", f);
//            mContext.startActivity(i);
//        } else {
//            //2.美丽园区
//            Intent i = new Intent(mContext, StubActivity.class);
//            i.putExtra("fragment", BeautifulWebFragment.class.getName());
//            i.putExtra("data", f);
//            mContext.startActivity(i);
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
