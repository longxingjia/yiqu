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
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Listened;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.util.ArrayList;

public class Tab5WotingAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Listened> datas = new ArrayList<Listened>();
    private Context mContext;

    private String tag = "Tab1ListenedAdapter";

    public Tab5WotingAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

    }


    public void setData(ArrayList<Listened> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Listened> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Listened getItem(int position) {
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
        TextView like;
        TextView time;

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
                h.name = (TextView) v.findViewById(R.id.name);
                h.title = (TextView) v.findViewById(R.id.title);
                h.time = (TextView) v.findViewById(R.id.time);
                h.header = (ImageView) v.findViewById(R.id.header);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.views = (TextView) v.findViewById(R.id.views);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final Listened f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.name.setText(f.username);
            h.title.setText(f.title);
            h.like.setText(String.valueOf(f.like));

            if (f.type == 1) {
                h.musictype.setBackgroundResource(R.mipmap.shengyue);
            } else {
                h.musictype.setBackgroundResource(R.mipmap.boyin);
            }
            PictureUtils.showPicture(mContext, f.userimage, h.header, 50);

            h.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initHomepage(mContext, String.valueOf(f.uid));
                }
            });

//            h.like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    RestNetCallHelper.callNet(mContext,
//                            MyNetApiConfig.like, MyNetRequestConfig
//                                    .like(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
//                            "like", new NetCallBack() {
//                                @Override
//                                public void onNetNoStart(String id) {
//
//                                }
//
//                                @Override
//                                public void onNetStart(String id) {
//
//                                }
//
//                                @Override
//                                public void onNetEnd(String id, int type, NetResponse netResponse) {
//                                    if (type == NetCallBack.TYPE_SUCCESS) {
//
////                                        if (likesIndex == -1) {
////                                            Like l = new Like();
////                                            l.sid = sid;
////                                            l.islike = 1;
////                                            if (likes == null) {
////                                                likes = new ArrayList<Like>();
////                                            }
////                                            likes.add(l);
////                                            like.setText(String.valueOf(sound.like + 1));
////                                            AppShare.setLikeList(getActivity(), likes);
////                                            initDianZan();
////                                        }
//
//
//                                    }
//
//                                }
//                            }, false, true);
//                }
//            });

            h.views.setText(f.views + "");

            String2TimeUtils string2TimeUtils = new String2TimeUtils();
            long currentTimeMillis = System.currentTimeMillis() / 1000;
            long time = currentTimeMillis - f.edited;
            h.time.setText(string2TimeUtils.long2Time(time));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void initHomepage(final Context mContext, String uid) {
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

        Listened f = getItem(arg2 - 1);

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
