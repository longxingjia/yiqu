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
import com.yiqu.iyijiayi.fragment.tab1.SoundItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5WopingDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5WopingListFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.util.ArrayList;

public class Tab5WopingAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;

    private String tag = "Tab5WopingAdapter";
    private final String2TimeUtils string2TimeUtils;

    public Tab5WopingAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        string2TimeUtils = new String2TimeUtils();
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
        ImageView header;
        TextView name;
        TextView title;
        TextView status;
        TextView musicname;
        ImageView musictype;
        TextView desc;
        TextView created;
        TextView views;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab5_woping, null);
                h.header = (ImageView) v.findViewById(R.id.header);
                h.name = (TextView) v.findViewById(R.id.name);
                h.title = (TextView) v.findViewById(R.id.title);
                h.status = (TextView) v.findViewById(R.id.status);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.created = (TextView) v.findViewById(R.id.created);
                h.views = (TextView) v.findViewById(R.id.views);

                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.name.setText(f.stuname);
            h.views.setText(f.views + "");

            long currentTimeMillis = System.currentTimeMillis() / 1000;
            long time = currentTimeMillis - f.edited;
            h.created.setText(string2TimeUtils.long2Time(time));


            if (f.type == 1) {
                h.musictype.setBackgroundResource(R.mipmap.shengyue);
            } else {
                h.musictype.setBackgroundResource(R.mipmap.boyin);
            }

            if (f.isreply == 0) {//问题
                h.status.setText("问题");
                h.status.setTextColor(mContext.getResources().getColor(R.color.redMain));

            } else if (f.isreply == 1) {//追问
                h.status.setTextColor(mContext.getResources().getColor(R.color.dd_gray));
                if (f.isnewreply == 1) {
                    h.status.setText("追问");
                } else {
                    h.status.setText("已点评");
                }

            } else if (f.isreply == -1) {//已拒绝

                h.status.setText("已拒绝");
                h.status.setTextColor(mContext.getResources().getColor(R.color.dd_gray));
            }

            PictureUtils.showPicture(mContext, f.stuimage, h.header);

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
        Sound f = getItem(arg2 - 1);//加了头部
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (f.isreply == 0) {//问题
            Intent intent = new Intent(mContext, StubActivity.class);
            intent.putExtra("fragment", Tab5WopingDetailFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", f);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } else if (f.isreply == 1) {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SoundItemDetailFragment.class.getName());
            i.putExtra("data",f.sid+"");
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
