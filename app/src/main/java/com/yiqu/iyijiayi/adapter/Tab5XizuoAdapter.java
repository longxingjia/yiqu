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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.utils.L;
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
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.view.MultiView.ExpandTextView;

import java.util.ArrayList;

public class Tab5XizuoAdapter extends BaseAdapter implements OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    private String tag = "Tab1XizuoAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private int mCurrent = -1;
    private boolean flag = false;

    public Tab5XizuoAdapter(Context context, boolean flag) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.flag = flag;
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    private class HoldChild {

        TextView musicname;
        ExpandTextView content;
        TextView author;
        TextView comment;
        TextView like;
        TextView listener;
        ImageView icon;
        ImageView album;
        TextView publish_time;
        ImageView musictype;
        ImageView play_status;
        ImageView iv_status;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab1_zuoping_adapter, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.content = (ExpandTextView) v.findViewById(R.id.desc);
                h.author = (TextView) v.findViewById(R.id.author);
                h.publish_time = (TextView) v.findViewById(R.id.publish_time);
                h.comment = (TextView) v.findViewById(R.id.comment);
                h.listener = (TextView) v.findViewById(R.id.listener);
                h.like = (TextView) v.findViewById(R.id.like);
                h.icon = (ImageView) v.findViewById(R.id.header);
                h.album = (ImageView) v.findViewById(R.id.album);
                h.musictype = (ImageView) v.findViewById(R.id.musictype);
                h.play_status = (ImageView) v.findViewById(R.id.play_status);
                h.iv_status = (ImageView) v.findViewById(R.id.iv_status);
                v.setTag(h);
            }
            h = (HoldChild) v.getTag();
            final Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.content.setText(EmojiCharacterUtil.decode(f.desc));
            h.comment.setText(String.valueOf(f.comments));
            h.like.setText(String.valueOf(f.like));
            h.listener.setText(String.valueOf(f.views));
            h.author.setText(f.stuname);
            h.publish_time.setText(String2TimeUtils.longToString(f.created * 1000, "yyyy/MM/dd HH:mm"));
            if (f.type == 1) {
                h.musictype.setImageResource(R.mipmap.shengyue);
            } else {
                h.musictype.setImageResource(R.mipmap.boyin);
            }
            PictureUtils.showPicture(mContext, f.stuimage, h.icon, 47);
            PictureUtils.showPictureAlbum(mContext, f.stuimage, h.album, 75);
            if (flag) {
                L.e(String.valueOf(flag));
                final int pos = position;

                h.iv_status.setVisibility(View.VISIBLE);
                h.iv_status.setImageResource(R.mipmap.mine_arrow);
                h.iv_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppShare.getIsLogin(mContext)) {
                            String title = "提示";
                            String[] items = new String[]{"删除"};

                            MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(mContext, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
                                @Override
                                public void onTea(int tea) {
                                    switch (tea) {

                                        case 0:
                                            RestNetCallHelper.callNet(
                                                    mContext,
                                                    MyNetApiConfig.deleteSound,
                                                    MyNetRequestConfig.deleteSound(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
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
                                                            L.e(netResponse.toString());
                                                            if (type == TYPE_SUCCESS) {
                                                                ToastManager.getInstance(mContext).showText(netResponse.result);
                                                                datas.remove(position);
                                                                notifyDataSetChanged();


                                                            }

                                                        }
                                                    });


                                            break;
                                    }
                                }
                            });
                            menuDialogSelectTeaHelper.show(v);
                        } else {
                            Model.startNextAct(mContext,
                                    SelectLoginFragment.class.getName());
                            ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Sound f = getItem(position);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        if (AppShare.getIsLogin(mContext)) {
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", ItemDetailFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putSerializable("Sound", f);
            i.putExtras(bundle);
            mContext.startActivity(i);
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
