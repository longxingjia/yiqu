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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.ui.views.RoundProgressBar;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.service.MusicService;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class Tab4HotAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag = "Tab1XizuoAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private int mCurrent = -1;
    private ImageView mPlay;
    private TextView mMusicname;
    private TextView mAuthor;
    private LinearLayout mRoot;
    private Intent intent = new Intent();

    public Tab4HotAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;


    }

    public void setCurrent(int current) {
        mCurrent = current;
        notifyDataSetChanged();
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
        return position;
    }

    private class HoldChild {

        TextView name;
        TextView listener;
        TextView like;
        TextView time;
        ImageView icon;
        RoundProgressBar roundProgressBar;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab4_hot_adapter, null);
                h.name = (TextView) v.findViewById(R.id.musicname);
                h.listener = (TextView) v.findViewById(R.id.listener);
                h.like = (TextView) v.findViewById(R.id.like);
                h.time = (TextView) v.findViewById(R.id.time);
                h.icon = (ImageView) v.findViewById(R.id.header);

                h.roundProgressBar = (RoundProgressBar) v.findViewById(R.id.roundProgressBar);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            Sound f = getItem(position);
            h.name.setText(f.musicname);

            h.listener.setText(String.valueOf(f.views));
            h.like.setText(String.valueOf(f.like));
            h.time.setText(f.soundtime + "\"");


            h.roundProgressBar.setProgress(20);
            if (mCurrent == position) {
                // h.play_status.setImageResource(R.mipmap.xizuo_pause);
                h.name.setTextColor(mContext.getResources().getColor(R.color.redMain));
                mMusicname.setText(f.musicname);
                mAuthor.setText(f.stuname);
                String url = MyNetApiConfig.ImageServerAddr + f.soundpath;
                intent.setClass(mContext, MusicService.class);
          //      mContext.stopService(intent);
                intent.putExtra("choice", "play");
                intent.putExtra("url", url);
                mContext.startService(intent);

            } else {
                //   h.play_status.setImageResource(R.mipmap.xizuo_play);
                h.name.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));

            }

            h.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    h.play_status =
                    mCurrent = position;
                    mRoot.setVisibility(View.VISIBLE);

                    notifyDataSetChanged();


                }
            });

            PictureUtils.showPicture(mContext, f.stuimage, h.icon, 47);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Sound f = getItem(position);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        Intent i = new Intent(mContext, StubActivity.class);
        i.putExtra("fragment", ItemDetailFragment.class.getName());
        Bundle bundle = new Bundle();

        bundle.putSerializable("Sound", f);

        i.putExtras(bundle);
        mContext.startActivity(i);
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
