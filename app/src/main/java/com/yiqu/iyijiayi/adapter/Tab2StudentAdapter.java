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
import com.yiqu.iyijiayi.model.Student;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import java.util.ArrayList;

public class Tab2StudentAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater inflater;
    private ArrayList<Student> datas = new ArrayList<Student>();
    private Context mContext;
    private ImageLoaderHm<ImageView> mImageLoaderHm;

    public Tab2StudentAdapter(Context context, ImageLoaderHm<ImageView> m) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mImageLoaderHm = m;
    }


    public void setData(ArrayList<Student> list) {

        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Student> allDatas) {
        // TODO Auto-generated method stub
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Student getItem(int position) {
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
                v = inflater.inflate(R.layout.zhaoren_student, parent,false);
//                h.name = (TextView) v.findViewById(R.id.name);
//                h.content = (TextView) v.findViewById(R.id.desc);
//                h.icon = (ImageView) v.findViewById(R.id.header);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
//            Student f = getItem(position);
//            h.name.setText(f.username);
//            h.content.setText(f.title);
//            LogUtils.LOGE(MyNetApiConfig.ImageServerAddr+f.userimage);
         //   mImageLoaderHm.DisplayImage( MyNetApiConfig.ImageServerAddr+f.userimage, h.icon);

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
