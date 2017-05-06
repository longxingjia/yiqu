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
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.DianZanUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.util.ArrayList;

public class Tab4DiscoveryAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;

    private String tag = "Tab1SoundAdapter";

    public Tab4DiscoveryAdapter(Context context) {
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

        TextView musicname;
        TextView desc;
        TextView name;
        TextView title;
        ImageView header;
        ImageView musictype;
        TextView views;
        TextView pl;
        TextView comment;
        TextView like;
//        TextView time;

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
                h.pl = (TextView) v.findViewById(R.id.pl);
                h.name = (TextView) v.findViewById(R.id.name);
                h.title = (TextView) v.findViewById(R.id.title);
                //    h.time = (TextView) v.findViewById(R.id.time);
                h.header = (ImageView) v.findViewById(R.id.header);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.views = (TextView) v.findViewById(R.id.listener);
                h.like = (TextView) v.findViewById(R.id.like);
                h.comment = (TextView) v.findViewById(R.id.comment);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final Sound f = getItem(position);

            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.comment.setText(String.valueOf(f.comments));

            if (f.type == 1) {
                h.musictype.setBackgroundResource(R.mipmap.shengyue);
            } else {
                h.musictype.setBackgroundResource(R.mipmap.boyin);
            }
            h.like.setText(String.valueOf(f.like));

            if (f.stype == 1) {   //
                h.name.setText(f.tecname);
                h.title.setText(f.tecschool);
                PictureUtils.showPicture(mContext, f.tecimage, h.header, 40);

                h.pl.setText("点评了");
            } else {
                h.title.setText(f.stuschool);
                PictureUtils.showPicture(mContext, f.stuimage, h.header, 40);
                h.name.setText(f.stuname);
                h.pl.setText("录制了");
            }


            h.views.setText(f.views + "");

            if (f.islike == 0) {
                DianZanUtils.initDianZan(mContext, h.like, false);

            } else {
                DianZanUtils.initDianZan(mContext, h.like, true);
            }

            h.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppShare.getIsLogin(mContext)) {
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.like,
                                MyNetRequestConfig.like(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
                                "getSoundList", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {
                                        //  LogUtils.LOGE(tag,netResponse.toString());
                                        if (type == TYPE_SUCCESS) {

                                            if (f.islike == 0) {
                                                f.like++;
                                                f.islike = 1;
                                                notifyDataSetChanged();
                                            } else {

                                            }
                                        }

                                    }
                                });
                    } else {
                        Model.startNextAct(mContext,
                                SelectLoginFragment.class.getName());
                        ToastManager.getInstance(mContext).showText("请您登录后在操作");
                    }

                }
            });


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
        Sound f = getItem(arg2 - 1);

        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }


        if (AppShare.getIsLogin(mContext)) {

//            if (f.stype == 1) {   //
//                Intent i = new Intent(mContext, StubActivity.class);
//                i.putExtra("fragment", ItemDetailFragment.class.getName());
//                i.putExtra("data",f.sid+"");
//
//                mContext.startActivity(i);
//
////            h.pl.setText("评论了");
//            } else {
//                Intent i = new Intent(mContext, StubActivity.class);
//                i.putExtra("fragment", ItemDetailFragment.class.getName());
//                i.putExtra("data",f.sid+"");
//                mContext.startActivity(i);
//
////                Bundle b = new Bundle();
////                b.putSerializable("data",f);
////                i.putExtras(b);
//
////                mContext.startActivity(i);
////            h.pl.setText("录制了");
//            }


            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", ItemDetailFragment.class.getName());
            Bundle b = new Bundle();
            b.putSerializable("Sound", f);
            i.putExtras(b);
            mContext.startActivity(i);

//
        } else {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SelectLoginFragment.class.getName());
            ToastManager.getInstance(mContext).showText("请登录后再试");
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
