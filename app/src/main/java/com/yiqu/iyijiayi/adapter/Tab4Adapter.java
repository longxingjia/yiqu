/**
 * @filename PersonAll.java
 * @author byron(linbochuan@hopsun.cn)
 * @date 2013-9-2
 * @vsersion 1.0
 * Copyright (C) 2013 辉盛科技发展责任有限公司
 */
package com.yiqu.iyijiayi.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Tab4Adapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Discovery> datas = new ArrayList<Discovery>();
    private Context mContext;
    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private String tag = "Tab1SoundAdapter";

    public Tab4Adapter(Context context, ImageLoaderHm<ImageView> m) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mImageLoaderHm = m;
    }


    public void setData(ArrayList<Discovery> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Discovery> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Discovery getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {

        TextView musicname;
        TextView desc;
        TextView tea_name;
        TextView tectitle;
        ImageView tea_header;
        ImageView musictype;
        TextView views;
        TextView time;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab4_discovery, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.tea_name = (TextView) v.findViewById(R.id.name);
                h.tectitle = (TextView) v.findViewById(R.id.title);
                h.time = (TextView) v.findViewById(R.id.time);
                h.tea_header = (ImageView) v.findViewById(R.id.header);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.views = (TextView) v.findViewById(R.id.views);
                v.setTag(h);
            }


            h = (HoldChild) v.getTag();
            Discovery f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.tectitle.setText(f.tectitle);
            h.tea_name.setText(f.tecname);
            h.views.setText(f.views+"");

//            if (f.musictype.equals("1")){
//                h.musictype.setBackgroundResource();
//            }



            if (f.tecimage != null) {
                mImageLoaderHm.DisplayImage(MyNetApiConfig.ImageServerAddr + f.tecimage, h.tea_header);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


        // TODO Auto-generated method stub
//        Discovery f = getItem(arg2 - 1);

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
