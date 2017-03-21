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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.SoundItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class Tab1SoundAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private Fragment fragment;
    private String tag = "Tab1SoundAdapter";

    public Tab1SoundAdapter(Context context, Fragment fragment) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.fragment = fragment;

    }


    public void setData(ArrayList<Sound> list) {
        LogUtils.LOGE(tag, list.toString());
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
        ImageView musictype;
        TextView stu_listen;
        TextView listener;
        TextView tea_listen;

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
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.listener = (TextView) v.findViewById(R.id.listener);
                h.tea_listen = (TextView) v.findViewById(R.id.tea_listen);
                h.tea_header = (ImageView) v.findViewById(R.id.tea_header);
                h.stu_listen = (TextView) v.findViewById(R.id.stu_listen);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.soundtime.setText(f.soundtime + "\"");
            h.tea_name.setText(f.tecname);
            h.listener.setText(f.views + "");
            h.tectitle.setText(f.tectitle);

            long time = System.currentTimeMillis() / 1000 - f.created;

            if (time < 2 * 24 * 60 * 60 && time > 0) {
                h.tea_listen.setText("限时免费听");
            } else {
                if (f.listen == 1) {
                    h.tea_listen.setText("已付费");
                } else {
                    h.tea_listen.setText("1元偷偷听");
                }

            }


            if (f.type == 1) {
                h.musictype.setBackgroundResource(R.mipmap.shengyue);
            } else {
                h.musictype.setBackgroundResource(R.mipmap.boyin);
            }

            PictureUtils.showPicture(mContext, f.tecimage, h.tea_header);
            PictureUtils.showPicture(mContext, f.stuimage, h.stu_header);

            h.tea_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initHomepage(String.valueOf(f.touid));
                }
            });
            h.stu_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initHomepage(String.valueOf(f.fromuid));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void initHomepage(String uid) {
        String mUid = "0";
        if (AppShare.getIsLogin(mContext)) {
            mUid = AppShare.getUserInfo(mContext).uid;
        }
        RestNetCallHelper.callNet(mContext,
                MyNetApiConfig.getUserPage,
                MyNetRequestConfig.getUserPage(mContext
                        , uid, mUid),
                "getUserPage",
                new NetCallBack() {
                    @Override
                    public void onNetNoStart(String id) {

                    }

                    @Override
                    public void onNetStart(String id) {

                    }

                    @Override
                    public void onNetEnd(String id, int type, NetResponse netResponse) {
                        if (TYPE_SUCCESS == type) {
                            Gson gson = new Gson();
                            HomePage homePage = gson.fromJson(netResponse.data, HomePage.class);
                            Intent i = new Intent(mContext, StubActivity.class);
                            i.putExtra("fragment", HomePageFragment.class.getName());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", homePage);
                            i.putExtras(bundle);
                            mContext.startActivity(i);
                        }

                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 < 2) {
            return;
        }
        Sound f = getItem(arg2 - 2);//加了头部
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }

        if (AppShare.getIsLogin(mContext)) {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SoundItemDetailFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putSerializable("Sound", f);
            i.putExtras(bundle);
            i.putExtra("position",arg2 - 2);
            fragment.startActivityForResult(i, 0);
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

    public void updateSingleRow(ListView listView, int sid) {

        if (listView != null) {
            int start = listView.getFirstVisiblePosition() + 1;
            LogUtils.LOGE("s",start+"");
            int j = listView.getLastVisiblePosition();
            for (int i = start; i <= j; i++) {
                Sound sound = (Sound) listView.getItemAtPosition(i);
                LogUtils.LOGE("sss",sound.toString());
                if (sid == sound.sid) {

                    View view = listView.getChildAt(i - start+2);
                    HoldChild h = (HoldChild) view.getTag();
                    h.tea_listen = (TextView) view.findViewById(R.id.tea_listen);
                    h.tea_listen.setText("已付费");
                    LogUtils.LOGE("s",i+"");
                    getView(i, view, listView);
                    break;
                }
            }
//            View view = listView.getChildAt(position);
//            getView(position, view, listView);
        }
    }


}
