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
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class SearchUserAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag ="Tab1XizuoAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<UserInfo> datas = new ArrayList<UserInfo>();
    private Context mContext;

    public SearchUserAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

    }


    public void setData(ArrayList<UserInfo> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<UserInfo> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class HoldChild {

        TextView name;
        TextView desc;
        ImageView header;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.search_sound, null);
                h.name = (TextView) v.findViewById(R.id.musicname);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.header = (ImageView) v.findViewById(R.id.header);

                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            UserInfo f = getItem(position);
            h.name.setText(f.username);
            h.desc.setText(f.desc);

            PictureUtils.showPicture(mContext,f.userimage,h.header,47);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position-1<0){
            return;
        }
        UserInfo f = getItem(position-1);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (AppShare.getIsLogin(mContext)) {

            RestNetCallHelper.callNet(mContext,
                    MyNetApiConfig.getUserPage,
                    MyNetRequestConfig.getUserPage(mContext
                            , f.uid, AppShare.getUserInfo(mContext).uid),
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
                                bundle.putSerializable("data",homePage);
                                i.putExtras(bundle);
                                mContext.startActivity(i);
                            }

                        }
                    });




        }else {
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
