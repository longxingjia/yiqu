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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
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

public class Tab4NewAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag = "Tab4NewAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private int mCurrent = -1;
    private Intent intent = new Intent();
    private OnPlayClickListener mListener;

    public void setOnPlayClickListener(OnPlayClickListener l) {
        mListener = l;
    }
    public interface OnPlayClickListener {
        public void onPlayClick(Sound sound);
        public void onPauseClick(Sound sound);
    }

    public Tab4NewAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public int getCurrent() {
        return mCurrent;
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
        TextView content;
        TextView time;
        TextView title;
        TextView publish_time;
        TextView comment;
        TextView listener;
        TextView like;
        ImageView icon;
        ListView comments;
        ImageView play_status;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab4_new_adapter, null);
                h.name = (TextView) v.findViewById(R.id.musicname);
                h.content = (TextView) v.findViewById(R.id.desc);
                h.time = (TextView) v.findViewById(R.id.time);
                h.title = (TextView) v.findViewById(R.id.title);
                h.like = (TextView) v.findViewById(R.id.like);
                h.comment = (TextView) v.findViewById(R.id.comment);
                h.comments = (ListView) v.findViewById(R.id.lv_comments);
                h.listener = (TextView) v.findViewById(R.id.listener);

                h.publish_time = (TextView) v.findViewById(R.id.publish_time);
                h.icon = (ImageView) v.findViewById(R.id.header);
                h.play_status = (ImageView) v.findViewById(R.id.play_status);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            final Sound f = getItem(position);
            h.name.setText(f.stuname);
            h.title.setText(f.musicname);
            h.like.setText(String.valueOf(f.like));
            if (TextUtils.isEmpty(f.article_content)) {
                h.content.setText(f.musicname);
            } else {
                h.content.setText(f.article_content);
            }
            h.listener.setText(String.valueOf(f.views));
            h.time.setText(f.soundtime + "\"");

            h.comment.setText(String.valueOf(f.comments));
            h.publish_time.setText(String2TimeUtils.longToString(f.created * 1000, "yyyy/MM/dd HH:mm"));

            Tab4hotCommentsAdapter tab4hotCommentsAdapter = new Tab4hotCommentsAdapter(mContext, String.valueOf(f.sid), String.valueOf(f.fromuid));

            h.comments.setAdapter(tab4hotCommentsAdapter);
            //  h.comments.setItemsCanFocus(false);
            tab4hotCommentsAdapter.setData(f.top_comments);
            setListViewHeightBasedOnChildren(h.comments);

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
            final int pos = position;
            if (mCurrent == position) {
                h.play_status.setImageResource(R.mipmap.music_pause);
            } else {
                h.play_status.setImageResource(R.mipmap.music_play);
            }

            h.play_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrent==pos){
                        mCurrent=-1;
                        if(mListener != null) mListener.onPauseClick(f);
                    }else {
                        if(mListener != null) mListener.onPlayClick(f);
                        mCurrent = pos;
                    }


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
        if (position < 2) {
            return;
        }

        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        Sound f = getItem(position - 2);
//        LogUtils.LOGE(tag, position + "");
        Intent i = new Intent(mContext, StubActivity.class);
        i.putExtra("fragment", ItemDetailFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("Sound", f);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
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
