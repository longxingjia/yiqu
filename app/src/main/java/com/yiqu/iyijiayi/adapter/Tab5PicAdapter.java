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
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utils.L;
import com.yiqu.iyijiayi.ImagePagerActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailPicFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.utils.Tab5DelDialog;
import com.yiqu.iyijiayi.view.MultiView.ExpandTextView;
import com.yiqu.iyijiayi.view.MultiView.MultiImageView;

import java.math.MathContext;
import java.security.PolicySpi;
import java.util.ArrayList;

public class Tab5PicAdapter extends BaseAdapter implements OnItemClickListener,
        AdapterView.OnItemLongClickListener, NetCallBack {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;

    private String tag = "Tab5PicAdapter";
    private boolean flag = false;

    public Tab5PicAdapter(Context context,boolean flag ) {
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
        return 0;
    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        L.e(netResponse.toString());
        if (id.equals("deleteSound")) {
            if (type == TYPE_SUCCESS) {

            }
        }
    }

    private class HoldChild {
        ImageView header;
        ImageView iv_status;
        TextView author;
        TextView publish_time;
        ExpandTextView content;
        MultiImageView multiImageView;
        TextView listener;
        TextView comment;
        TextView like;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild holder;
            if (v == null) {
                holder = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab1_adapter_image, null);
                holder.header = (ImageView) v.findViewById(R.id.header);
                holder.iv_status = (ImageView) v.findViewById(R.id.iv_status);
                holder.author = (TextView) v.findViewById(R.id.author);
                holder.publish_time = (TextView) v.findViewById(R.id.publish_time);
                holder.content = (ExpandTextView) v.findViewById(R.id.desc);
                holder.multiImageView = (MultiImageView) v.findViewById(R.id.multiImagView);
                holder.comment = (TextView) v.findViewById(R.id.comment);
                holder.listener = (TextView) v.findViewById(R.id.listener);
                holder.like = (TextView) v.findViewById(R.id.like);
                v.setTag(holder);
            }

            holder = (HoldChild) v.getTag();
            final Sound f = getItem(position);
//        L.e(f.desc,f.images);
            if (TextUtils.isEmpty(f.images)) {
                holder.multiImageView.setVisibility(View.GONE);
            } else {
                final ArrayList<String> imagesList = new Gson().fromJson(f.images, new TypeToken<ArrayList<String>>() {
                }.getType());
                holder.multiImageView.setVisibility(View.VISIBLE);
                holder.multiImageView.setList(imagesList);
                holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                        ImagePagerActivity.startImagePagerActivity((mContext), imagesList, position, imageSize);
                    }
                });
            }

            holder.author.setText(f.stuname);
            holder.content.setText(f.desc);
            holder.comment.setText(f.comments);
            holder.like.setText(String.valueOf(f.like));
            holder.listener.setText(String.valueOf(f.views));
            holder.publish_time.setText(String2TimeUtils.longToString(f.created * 1000, "yyyy/MM/dd HH:mm"));
            PictureUtils.showPicture(mContext, f.stuimage, holder.header, 47);
//            L.e(String.valueOf(flag));

            if (flag){
                L.e(String.valueOf(flag));
                final int pos=position;

                holder.iv_status.setVisibility(View.VISIBLE);
                holder.iv_status.setImageResource(R.mipmap.mine_arrow);
                holder.iv_status.setOnClickListener(new View.OnClickListener() {
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
                        }else {
                            Model.startNextAct(mContext,
                                    SelectLoginFragment.class.getName());
                            ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                        }
                    }
                });
            }

            if (f.islike == 0) {
                initDianZan(holder.like, false);
            } else {
                initDianZan(holder.like, true);
            }
            holder.like.setOnClickListener(new View.OnClickListener() {
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
                        ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    private void initDianZan(TextView textView, boolean t) {
        Drawable leftDrawable;
        if (t) {
            leftDrawable = mContext.getResources().getDrawable(R.mipmap.dianzan_pressed_new);

        } else {
            leftDrawable = mContext.getResources().getDrawable(R.mipmap.dianzan__new);

        }
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        textView.setCompoundDrawables(leftDrawable, null, null, null); //(Drawable left, Drawable top, Drawable right, Drawable bottom)
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        L.e(position+"");
        if (position < 2) {
            return false;
        } else {
            Sound f = getItem(position - 2);//加了头部
            if (!isNetworkConnected(mContext)) {
                ToastManager.getInstance(mContext).showText(
                        R.string.fm_net_call_no_network);
                return false;
            }
            if (AppShare.getIsLogin(mContext)) {

//                RestNetCallHelper.callNet(mContext,
//                        MyNetApiConfig.deleteSound,
//                        MyNetRequestConfig.deleteSound(mContext
//                                , AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
//                        "deleteSound",
//                        this);


            } else {
                Intent i = new Intent(mContext, StubActivity.class);
                i.putExtra("fragment", SelectLoginFragment.class.getName());
                ToastManager.getInstance(mContext).showText("请登录后再试");
                mContext.startActivity(i);

            }

        }
        L.e(position + "");

        return true;
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
            i.putExtra("fragment", ItemDetailPicFragment.class.getName());

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
