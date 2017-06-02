/**
 * @filename PersonAll.java
 * @author byron(linbochuan@hopsun.cn)
 * @date 2013-9-2
 * @vsersion 1.0
 * Copyright (C) 2013 辉盛科技发展责任有限公司
 */
package com.yiqu.iyijiayi.adapter;

import android.app.Dialog;
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
import com.ui.views.DialogUtil;
import com.utils.L;
import com.yiqu.iyijiayi.CommentActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.CommentsInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.view.CommentListView;

import java.util.ArrayList;

public class Tab1CommentsAdapter extends BaseAdapter implements OnItemClickListener, AdapterView.OnItemLongClickListener {
    private String tag = "Tab1CommentsAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<CommentsInfo> datas = new ArrayList<CommentsInfo>();
    private Context mContext;
    private String sid;
    private String uid;
    private Dialog dialog;

    public Tab1CommentsAdapter(Context context, String sid, String uid) {
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
        return datas.size();
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

        TextView username;
        TextView comment;
        TextView time;
        ImageView header;


    }
    private setDeleteCom mListener;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.remen_comments, null);
                h.username = (TextView) v.findViewById(R.id.username);
                h.comment = (TextView) v.findViewById(R.id.comment);
                h.time = (TextView) v.findViewById(R.id.time);

                h.header = (ImageView) v.findViewById(R.id.header);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();

            CommentsInfo f = getItem(position);
            h.username.setText(f.fromusername);

            if (!uid.equals(f.touid)) {
                h.comment.setText("回复 " + f.tousername + " : " + EmojiCharacterUtil.decode(f.comment));
            } else {
                String s = EmojiCharacterUtil.decode(f.comment);
                h.comment.setText(s);
            }

            h.time.setText(String2TimeUtils.longToString(f.created * 1000, "yyyy-MM-dd HH:mm:ss"));
            PictureUtils.showPicture(mContext, f.fromuserimage, h.header, 47);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final CommentsInfo f = getItem(position);
        L.e(f.toString());
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return true;
        }

        if (AppShare.getIsLogin(mContext)) {
            final String fromuid = AppShare.getUserInfo(mContext).uid;

            if (fromuid.equals(f.fromuid)) {
                dialog = DialogUtil.showMyDialog(mContext, "小消息", "确定删除吗？", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        L.e(f.toString());
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.deleteComment,
                                MyNetRequestConfig.deleteComment(mContext, f.id, fromuid),
                                "worthSoundList", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {
                                        if (type==TYPE_SUCCESS){
                                            if (mListener != null) mListener.onDeleteCom();
                                        }

                                    }
                                }, true, true);
                        dialog.dismiss();
                    }
                }, "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }


        } else {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SelectLoginFragment.class.getName());
            ToastManager.getInstance(mContext).showText("请登录后再试");
            mContext.startActivity(i);
        }
        return true;
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
//                ToastManager.getInstance(mContext).showText("不能对自己评论");
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("sid", sid + "");
                intent.putExtra("fromuid", AppShare.getUserInfo(mContext).uid + "");
                intent.putExtra("touid", String.valueOf(uid));
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("sid", sid);
                intent.putExtra("cid", f.id);
                intent.putExtra("fromuid", AppShare.getUserInfo(mContext).uid);
                intent.putExtra("touid", f.fromuid + "");
                intent.putExtra("toname", f.fromusername + "");
                mContext.startActivity(intent);
            }
        } else {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SelectLoginFragment.class.getName());
            ToastManager.getInstance(mContext).showText("请登录后再试");
            mContext.startActivity(i);
        }
    }

    public interface setDeleteCom{
        public void onDeleteCom();
    }

    public void setOnMoreClickListener(setDeleteCom l) {
        mListener = l;
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
