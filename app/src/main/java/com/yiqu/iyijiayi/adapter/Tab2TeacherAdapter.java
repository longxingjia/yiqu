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
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ZhaoRen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class Tab2TeacherAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Teacher> datas = new ArrayList<Teacher>();
    private Context mContext;
    private String uid;
    private ZhaoRen zhaoRen;
    private String tag = "Tab2TeacherAdapter";

    public Tab2TeacherAdapter(Context context, String uid) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.uid = uid;
    }

    public void setData(ZhaoRen list) {
        datas = list.teacher;
        zhaoRen = list;
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
        return position;
    }

    private class HoldChild {
        TextView name;
        TextView content;
        ImageView icon;
        ImageView follow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.zhaoren, null);
                h.name = (TextView) v.findViewById(R.id.name);
                h.content = (TextView) v.findViewById(R.id.desc);
                h.icon = (ImageView) v.findViewById(R.id.header);
                h.follow = (ImageView) v.findViewById(R.id.add_follow);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            final Teacher f = getItem(position);
            h.name.setText(f.username);
            h.content.setText(f.title);
            if (f.isfollow.equals("0")) {  //没有关注
                h.follow.setBackgroundResource(R.mipmap.follow);


            } else {
                h.follow.setBackgroundResource(R.mipmap.followed);
            }

            PictureUtils.showPicture(mContext, f.userimage, h.icon);

            h.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (uid.equals("0")) {
                        Intent i = new Intent(mContext, StubActivity.class);
                        i.putExtra("fragment", SelectLoginFragment.class.getName());
                        mContext.startActivity(i);
                        ToastManager.getInstance(mContext).showText("请登录后在操作");

                        return;
                    }


                    if (f.isfollow.equals("0")) {  //没有关注
//                        h.follow.setBackgroundResource(R.mipmap.follow);
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.addfollow,
                                MyNetRequestConfig.addfollow(mContext, uid, f.uid),
                                "teacher", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                                        if (netResponse != null) {
                                            if (netResponse.bool == 1) {
                                                f.isfollow = "1";
                                                zhaoRen.teacher = datas;
                                                AppShare.setZhaoRenList(mContext, zhaoRen);
                                                notifyDataSetChanged();
                                            } else {
                                                ToastManager.getInstance(mContext).showText(netResponse.result);
                                            }

                                        }

                                    }
                                });

                    } else {
//                        if (f.isfollow.equals("0")){  //没有关注
//                        h.follow.setBackgroundResource(R.mipmap.follow);
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.delfollow,
                                MyNetRequestConfig.delfollow(mContext, uid, f.uid),
                                "delfollow", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                                        if (netResponse != null) {
                                            if (netResponse.bool == 1) {
                                                f.isfollow = "0";
                                                notifyDataSetChanged();
                                            } else {
                                                ToastManager.getInstance(mContext).showText(netResponse.result);
                                            }

                                        }

                                    }
                                });
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

        if (arg2<1){
            return;
        }
        Teacher f = getItem(arg2 - 1);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        String uid = "0";
        if (AppShare.getIsLogin(mContext)) {
            uid = AppShare.getUserInfo(mContext).uid;
        }
        RestNetCallHelper.callNet(mContext,
                MyNetApiConfig.getUserPage,
                MyNetRequestConfig.getUserPage(mContext
                        , f.uid, uid),
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
