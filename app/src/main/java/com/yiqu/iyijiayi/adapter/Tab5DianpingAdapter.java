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
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

public class Tab5DianpingAdapter extends BaseAdapter implements OnItemClickListener,AdapterView.OnItemLongClickListener {

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
        LinearLayout ll_question;
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
                h.ll_question = (LinearLayout) v.findViewById(R.id.ll_question);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(EmojiCharacterUtil.decode(f.desc));
            h.time.setText(f.commenttime + "\"");
            h.tea_name.setText(f.tecname);
            h.listener.setText(String.valueOf(f.views));
            h.comment.setText(String.valueOf(f.comments));
            h.tectitle.setText(f.tectitle);
            h.like.setText(String.valueOf(f.like));
            //  LogUtils.LOGE(tag,f.soundpath);
            if (f.type == 1) {
                h.ll_question.setVisibility(View.VISIBLE);
                h.musictype.setImageResource(R.mipmap.shengyue);
            } else if (f.type == 2){
                h.ll_question.setVisibility(View.VISIBLE);
                h.musictype.setImageResource(R.mipmap.boyin);
            }else {
                h.ll_question.setVisibility(View.GONE);
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
            if (f.islike==0){
                initDianZan(h.like,false);
            }else {
                initDianZan(h.like,true);
            }

            h.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppShare.getIsLogin(mContext)){
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.like,
                                MyNetRequestConfig.like(mContext, AppShare.getUserInfo(mContext).uid,String.valueOf(f.sid)),
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
                                        if (type==TYPE_SUCCESS){
                                            f.like ++;
                                            notifyDataSetChanged();
                                        }

                                    }
                                });
                    }else {
                        Model.startNextAct(mContext,
                                SelectLoginFragment.class.getName());
                        ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                    }

                }
            });


            PictureUtils.showPicture(mContext, f.tecimage, h.tea_header);
            PictureUtils.showPicture(mContext, f.stuimage, h.stu_header);


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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position<2){
          return  false;
        }else {
            Sound f = getItem(position-2);//加了头部
            if (!isNetworkConnected(mContext)) {
                ToastManager.getInstance(mContext).showText(
                        R.string.fm_net_call_no_network);
                return false;
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
        L.e(position+"");

        return true;
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
