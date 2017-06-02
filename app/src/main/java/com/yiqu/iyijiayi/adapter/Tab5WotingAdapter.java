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
import com.google.gson.Gson;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Listened;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.DianZanUtils;
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.HomePageUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.text.ParseException;
import java.util.ArrayList;

public class Tab5WotingAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;

    private String tag = "Tab1ListenedAdapter";

    public Tab5WotingAdapter(Context context) {
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

        ImageView stu_header;
        TextView stu_name;
        TextView created_time;
        TextView desc;
        ImageView musictype;
        ImageView tea_header;
        TextView musicname;
        TextView tea_listen;
        TextView time;
        TextView tea_name;
        TextView tectitle;
        TextView comment;

        TextView listener;
        TextView like;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab5_woting, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.like = (TextView) v.findViewById(R.id.like);
                h.tea_name = (TextView) v.findViewById(R.id.tea_name);
                h.created_time = (TextView) v.findViewById(R.id.created_time);
                h.tea_listen = (TextView) v.findViewById(R.id.tea_listen);
                h.tectitle = (TextView) v.findViewById(R.id.tectitle);
                h.time = (TextView) v.findViewById(R.id.time);
                h.stu_name = (TextView) v.findViewById(R.id.stu_name);

                h.stu_header = (ImageView) v.findViewById(R.id.stu_header);
                h.tea_header = (ImageView) v.findViewById(R.id.tea_header);

                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.listener = (TextView) v.findViewById(R.id.listener);
                h.comment = (TextView) v.findViewById(R.id.comment);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
//            final Listened f = getItem(position);
            final Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.stu_name.setText(f.stuname);
            h.desc.setText(EmojiCharacterUtil.decode(f.desc));
            h.time.setText(f.commenttime + "\"");
            h.tea_name.setText(f.tecname);
            h.listener.setText(String.valueOf(f.views));
            h.tectitle.setText(f.tectitle);
            h.like.setText(String.valueOf(f.like));
            h.comment.setText(String.valueOf(f.comments));

            try {
                h.created_time.setText(String2TimeUtils.longToString(f.created * 1000));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long time = System.currentTimeMillis() / 1000 - f.edited;

            if (time < 2 * 24 * 60 * 60 * 1000 && time > 0) {
                h.tea_listen.setText("限时免费听");
            } else {
                if (f.listen == 1) {
                    h.tea_listen.setText("已付费");
                } else {
                    h.tea_listen.setText("1元偷偷听");
                }

            }

            if (f.islike == 0) {
                DianZanUtils.initDianZan(mContext,h.like, false);
            } else {
                DianZanUtils.initDianZan(mContext,h.like, true);
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
                                            f.like++;
                                            notifyDataSetChanged();
                                        }

                                    }
                                });
                    } else {
                        Model.startNextAct(mContext,
                                SelectLoginFragment.class.getName());
                        ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                    }

                }
            });
            if (f.type == 1) {
                h.musictype.setImageResource(R.mipmap.shengyue);
            } else if (f.type == 2){
                h.musictype.setImageResource(R.mipmap.boyin);
            }

            PictureUtils.showPicture(mContext, f.tecimage, h.tea_header);
            PictureUtils.showPicture(mContext, f.stuimage, h.stu_header);

//            h.tea_header.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    L.e(f.touid+"");
//                    HomePageUtils.initHomepage(mContext,String.valueOf(f.touid));
//                }
//            });
//            h.stu_header.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HomePageUtils.initHomepage(mContext,String.valueOf(f.fromuid));
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Sound f = getItem(arg2 - 1);

        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        Intent i = new Intent(mContext, StubActivity.class);
        i.putExtra("fragment", ItemDetailFragment.class.getName());
        i.putExtra("data", f.sid + "");

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
