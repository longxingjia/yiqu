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

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.squareup.picasso.Picasso;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class Tab5DianpingAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private String uid;

    private String tag = "Tab2ListFragmetAdapter";

    public Tab5DianpingAdapter(Context context, String uid) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.uid = uid;
    }

    public void setData(ArrayList<Sound> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Sound> allDatas) {
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

        TextView musicname;
        TextView desc;
        TextView soundtime;
        TextView tea_name;
        TextView tectitle;
        ImageView stu_header;
        ImageView tea_header;
        TextView stu_listen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.remen_sound, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.soundtime = (TextView) v.findViewById(R.id.soundtime);
                h.tea_name = (TextView) v.findViewById(R.id.tea_name);
                h.tectitle = (TextView) v.findViewById(R.id.tectitle);
                h.stu_header = (ImageView) v.findViewById(R.id.stu_header);
                h.tea_header = (ImageView) v.findViewById(R.id.tea_header);
                h.stu_listen = (TextView) v.findViewById(R.id.stu_listen);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.soundtime.setText(f.soundtime + "\"");
            h.tea_name.setText(f.tecname);
            h.tectitle.setText(f.tectitle);
            //  LogUtils.LOGE(tag,f.soundpath);


            PictureUtils.showPicture(mContext, f.tecimage, h.tea_header);
            PictureUtils.showPicture(mContext, f.stuimage, h.stu_header);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

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
