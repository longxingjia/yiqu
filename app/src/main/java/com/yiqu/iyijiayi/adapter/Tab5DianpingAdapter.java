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
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class Tab5DianpingAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private String uid;

    private String tag = "Tab5DianpingAdapter";

    public Tab5DianpingAdapter(Context context, String uid) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.uid = uid;
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
        TextView time;
        TextView tea_name;
        TextView tectitle;
        ImageView stu_header;
        ImageView tea_header;
        ImageView musictype;
        TextView comment;
        TextView listener;
        TextView tea_listen;
        TextView like;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab1_sound_list_adapter, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.time = (TextView) v.findViewById(R.id.time);
                h.tea_name = (TextView) v.findViewById(R.id.tea_name);
                h.tectitle = (TextView) v.findViewById(R.id.tectitle);
                h.stu_header = (ImageView) v.findViewById(R.id.stu_header);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.listener = (TextView) v.findViewById(R.id.listener);
                h.tea_listen = (TextView) v.findViewById(R.id.tea_listen);
                h.like = (TextView) v.findViewById(R.id.like);
                h.tea_header = (ImageView) v.findViewById(R.id.tea_header);
                h.comment = (TextView) v.findViewById(R.id.comment);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.time.setText(f.commenttime + "\"");
            h.tea_name.setText(f.tecname);
            h.listener.setText(String.valueOf(f.views));
            h.tectitle.setText(f.tectitle);
            h.like.setText(String.valueOf(f.like));
            //  LogUtils.LOGE(tag,f.soundpath);
            if (f.type==1){
                h.musictype.setBackgroundResource(R.mipmap.shengyue);
            }else {
                h.musictype.setBackgroundResource(R.mipmap.boyin);
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

//            if (f.isLike == 1) {
//
//                initDianZan(h.like, true);
//            } else {
//                initDianZan(h.like, false);
//            }

            h.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!AppShare.getIsLogin(mContext)) {
                        Intent i = new Intent(mContext, StubActivity.class);
                        i.putExtra("fragment", SelectLoginFragment.class.getName());
                        ToastManager.getInstance(mContext).showText("请登录后再试");
                        mContext.startActivity(i);
                        return;
                    }


                    RestNetCallHelper.callNet(mContext,
                            MyNetApiConfig.like, MyNetRequestConfig
                                    .like(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
                            "like", new NetCallBack() {
                                @Override
                                public void onNetNoStart(String id) {

                                }

                                @Override
                                public void onNetStart(String id) {

                                }

                                @Override
                                public void onNetEnd(String id, int type, NetResponse netResponse) {
                                    LogUtils.LOGE(tag, f.isLike + "");
                                    if (f.isLike == 1) {

                                    } else {

                                        Like l = new Like();
                                        l.sid = f.sid;
                                        l.islike = 1;
                                        f.isLike = 1;
                                        f.like++;

                                        notifyDataSetChanged();
//                                        if (likes == null) {
//                                            likes = new ArrayList<Like>();
//                                        }
//                                        likes.add(l);
//                                        AppShare.setLikeList(mContext, likes);
                                    }

                                }
                            }, false, true);
                }
            });



            PictureUtils.showPicture(mContext, f.tecimage, h.tea_header);
            PictureUtils.showPicture(mContext, f.stuimage, h.stu_header);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2<2){
            return;
        }
        Sound f = getItem(arg2-2);//加了头部
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (AppShare.getIsLogin(mContext)){
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", ItemDetailFragment.class.getName());
            i.putExtra("data",f.sid+"");

            mContext.startActivity(i);
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
