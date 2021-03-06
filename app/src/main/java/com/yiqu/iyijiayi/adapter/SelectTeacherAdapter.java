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
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SelectTeacherAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Teacher> datas = new ArrayList<Teacher>();
    private Activity mContext;

    private String tag = "SelectTeacherAdapter";

    public SelectTeacherAdapter(Activity context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
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

            PictureUtils.showPicture(mContext, f.userimage, h.icon);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Teacher teacher = getItem(arg2);
        if (teacher.uid.equals(AppShare.getUserInfo(mContext).uid)){
            ToastManager.getInstance(mContext).showText("不能向自己提问噢");
        }else {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("teacher", teacher);
            intent.putExtras(bundle);
            mContext.setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            mContext.finish();//此处一定要调用finish()方法
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
