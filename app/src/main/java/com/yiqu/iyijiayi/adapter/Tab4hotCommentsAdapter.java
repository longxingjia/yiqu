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
import com.yiqu.iyijiayi.CommentActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.CommentsInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.util.ArrayList;

public class Tab4hotCommentsAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag ="Tab1CommentsAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<CommentsInfo> datas = new ArrayList<CommentsInfo>();
    private Context mContext;
    private String sid;
    private String uid;

    public Tab4hotCommentsAdapter(Context context, String sid, String uid) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.sid = sid;
        this.uid = uid;

    }


    public void setData(ArrayList<CommentsInfo> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CommentsInfo> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Math.min(3,datas.size());
    }

    @Override
    public CommentsInfo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class HoldChild {

        TextView fromuser;
        TextView comment;
        TextView tv_to;
        TextView touser;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab4_hot_comments_adapter, null);
                h.fromuser = (TextView) v.findViewById(R.id.fromuser);
                h.comment = (TextView) v.findViewById(R.id.comment);
                h.tv_to = (TextView) v.findViewById(R.id.tv_to);
                h.touser = (TextView) v.findViewById(R.id.touser);


                v.setTag(h);
            }
            h = (HoldChild) v.getTag();

            CommentsInfo f = getItem(position);

            h.fromuser.setText(f.fromusername);

            if (!uid.equals(f.touid)){
                h.tv_to.setVisibility(View.VISIBLE);
                h.touser.setText(f.tousername);
            }else {
                h.tv_to.setVisibility(View.GONE);

            }
            String s = EmojiCharacterUtil.decode(f.comment);
            h.comment.setText(s);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommentsInfo f = getItem(position);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (AppShare.getIsLogin(mContext)) {
            String fromuid = AppShare.getUserInfo(mContext).uid;
            if (fromuid.equals(f.fromuid)) {
                ToastManager.getInstance(mContext).showText("不能对自己评论");
                return;
            }
            Intent intent = new Intent(mContext, CommentActivity.class);
            intent.putExtra("sid", sid);
            intent.putExtra("fromuid", AppShare.getUserInfo(mContext).uid);
            intent.putExtra("touid", f.fromuid + "");
            intent.putExtra("toname", f.fromusername + "");
            mContext.startActivity(intent);
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
