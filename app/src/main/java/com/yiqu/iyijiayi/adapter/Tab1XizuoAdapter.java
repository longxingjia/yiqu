
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
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;


import java.util.ArrayList;

public class Tab1XizuoAdapter extends BaseAdapter implements OnItemClickListener {
    private String tag = "Tab1XizuoAdapter";
    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private String fragmentName;
    private int mCurrent = -1;
    private ImageView mPlay;
    private TextView mMusicname;
    private TextView mAuthor;
    private LinearLayout mRoot;
    private Intent intent = new Intent();

    public Tab1XizuoAdapter( Context mContext,String fragmentName ,LinearLayout root, ImageView play, TextView musicname, TextView author) {

        this. mContext = mContext;
        this.fragmentName = fragmentName;
        mLayoutInflater = LayoutInflater.from(mContext);
        mPlay = play;
        mMusicname = musicname;
        mAuthor = author;
        mRoot = root;
//        fragment.getClass().getName()

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

        TextView musicname;
        TextView content;
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
                h.content = (TextView) v.findViewById(R.id.desc);
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
            h.content.setText(f.desc);
            h.comment.setText(f.comments);
            h.like.setText(String.valueOf(f.like));
            h.listener.setText(String.valueOf(f.views));
            h.author.setText(f.stuname);
            h.publish_time.setText(String2TimeUtils.longToString(f.created*1000,"yyyy/MM/dd HH:mm"));
            if (f.type == 1) {
                h.musictype.setImageResource(R.mipmap.shengyue);
            } else {
                h.musictype.setImageResource(R.mipmap.boyin);
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
                        ToastManager.getInstance(mContext).showText("请您登录后在操作");
                    }

                }
            });

            //  LogUtils.LOGE(fragmentName,fragmentName);

            if (fragmentName.equals("Tab1Fragment")){
                if (position<9){
                    h.iv_status.setVisibility(View.VISIBLE);
                    h.iv_status.setBackgroundResource(R.mipmap.icon_new);
                }else {
                    h.iv_status.setVisibility(View.GONE);
                }

            }else if (fragmentName.equals("Tab1XizuoListFragment")){

                if (position<9){
                    h.iv_status.setVisibility(View.VISIBLE);
                    h.iv_status.setBackgroundResource(R.mipmap.icon_hot);
                }else {
                    h.iv_status.setVisibility(View.GONE);
                }


            }

            if (mCurrent == position) {
                // h.play_status.setImageResource(R.mipmap.xizuo_pause);
                h.musicname.setTextColor(mContext.getResources().getColor(R.color.redMain));
                mMusicname.setText(f.musicname);
                mAuthor.setText(f.stuname);
                String url = MyNetApiConfig.ImageServerAddr + f.soundpath;
//                intent.setClass(mContext, MusicService.class);
//                //      mContext.stopService(intent);
//                intent.putExtra("choice", "play");
//                intent.putExtra("url", url);
//                mContext.startService(intent);

            } else {
                //   h.play_status.setImageResource(R.mipmap.xizuo_play);
                h.musicname.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));

            }

            h.play_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    h.play_status =
                    mCurrent = position;
                    mRoot.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                }
            });

            PictureUtils.showPicture(mContext, f.stuimage, h.icon, 47);
            PictureUtils.showPicture(mContext, f.stuimage, h.album, 75);


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
        Sound f = getItem(position - 2);
//
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        Intent i = new Intent(mContext, StubActivity.class);
        i.putExtra("fragment", ItemDetailFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("Sound", f);

        i.putExtras(bundle);
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
