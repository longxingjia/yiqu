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
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import java.util.ArrayList;

public class SelectTeacherAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Teacher> datas = new ArrayList<Teacher>();
    private Context mContext;

    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private  String tag="SelectTeacherAdapter";

    public SelectTeacherAdapter(Context context, ImageLoaderHm<ImageView> m) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mImageLoaderHm = m;

    }


    public void setData(ArrayList<Teacher> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Teacher> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Teacher getItem(int position) {
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
                v = mLayoutInflater.inflate(R.layout.select_techer_list, null);
                h.name = (TextView) v.findViewById(R.id.name);
                h.content = (TextView) v.findViewById(R.id.desc);
                h.icon = (ImageView) v.findViewById(R.id.header);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
           final Teacher f = getItem(position);
            h.name.setText(f.username);
            h.content.setText(f.title);

            if (f.userimage!=null) {
                mImageLoaderHm.DisplayImage(MyNetApiConfig.ImageServerAddr + f.userimage, h.icon);
            }
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
