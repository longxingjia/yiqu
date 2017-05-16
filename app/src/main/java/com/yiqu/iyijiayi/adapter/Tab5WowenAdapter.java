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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.util.ArrayList;

public class Tab5WowenAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private String tag = "tab5WowenAdapter";

    public Tab5WowenAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
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
        TextView name;
        TextView status;
        ImageView musictype;
        TextView desc;
        TextView created;
        LinearLayout text_q;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab5_wowen, null);
                h.name = (TextView) v.findViewById(R.id.name);
                h.status = (TextView) v.findViewById(R.id.status);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.created = (TextView) v.findViewById(R.id.created);
                h.text_q = (LinearLayout) v.findViewById(R.id.text_q);

                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            Sound f = getItem(position);
            h.desc.setText(f.desc);
            h.name.setText(f.musicname);

            h.created.setText(String2TimeUtils.longToString(f.created*1000, "yyyy-MM-dd HH:mm"));


            if (f.type == 1) {
                h.text_q.setVisibility(View.VISIBLE);
                h.musictype.setBackgroundResource(R.mipmap.shengyue);
            } else if (f.type==3){
                h.text_q.setVisibility(View.GONE);
            }else {
                h.text_q.setVisibility(View.VISIBLE);
                h.musictype.setBackgroundResource(R.mipmap.boyin);
            }

            if (f.isreply == 0) {//待点评
                h.status.setText("待点评");
                h.status.setTextColor(mContext.getResources().getColor(R.color.stu_voice));

            } else if (f.isreply == 1) {//已点评
                h.status.setTextColor(mContext.getResources().getColor(R.color.redMain));
                 h.status.setText("已点评");

            } else if (f.isreply == -1) {//被拒绝

                h.status.setText("被拒绝");
                h.status.setTextColor(mContext.getResources().getColor(R.color.dd_gray));
            }


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
        if (f.isreply == 1) {//问题
            Intent intent = new Intent(mContext, StubActivity.class);
            intent.putExtra("fragment", ItemDetailFragment.class.getName());
            intent.putExtra("data",f.sid+"");

            mContext.startActivity(intent);
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
