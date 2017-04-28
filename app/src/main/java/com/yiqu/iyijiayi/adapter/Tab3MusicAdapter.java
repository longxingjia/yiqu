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
import com.ui.views.RoundProgressBar;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.fileutils.utils.Player;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.service.MusicService;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Tab3MusicAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Music> datas = new ArrayList<Music>();
    private Activity mContext;
    private String tag = "Tab3MusicAdapter";
    private Player player;
    private int mCurrent = -1;
    //  private ArrayList<Like> likes;

    public Tab3MusicAdapter(Activity context, Player player) {
        mContext = context;
        this.player = player;
        mLayoutInflater = LayoutInflater.from(mContext);


    }

    public void setData(ArrayList<Music> list) {

        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Music> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Music getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {

        TextView musicname;
        TextView chapter;
        ImageView music_play;
        RoundProgressBar round_pb;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab3_music_adapter, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.chapter = (TextView) v.findViewById(R.id.chapter);
                h.music_play = (ImageView) v.findViewById(R.id.music_play);
                h.round_pb = (RoundProgressBar) v.findViewById(R.id.round_pb);

                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final Music f = getItem(position);
            h.musicname.setText(f.musicname);
            h.chapter.setText(f.chapter);
            if (mCurrent == position) {
                // h.play_status.setImageResource(R.mipmap.xizuo_pause);
                h.music_play.setImageResource(R.mipmap.music_pause);

            } else {
                //   h.play_status.setImageResource(R.mipmap.xizuo_play);
                h.music_play.setImageResource(R.mipmap.music_play);
            }

            h.music_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.playUrl(MyNetApiConfig.ImageServerAddr + f.musicpath);
                    mCurrent = position;
                    notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        if (arg2 < 2) {
//            return;
//        }
        Music f = getItem(arg2);//加了头部
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", f);
        intent.putExtras(bundle);
        mContext.setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        mContext.finish();//此处一定要调用finish()方法

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
