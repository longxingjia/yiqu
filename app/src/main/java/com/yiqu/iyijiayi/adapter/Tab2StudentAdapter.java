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
import com.squareup.picasso.Picasso;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab2.Tab2ListFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Student;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ZhaoRen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import java.util.ArrayList;

public class Tab2StudentAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Student> datas = new ArrayList<Student>();
    private Context mContext;
    private String uid ;
    private ZhaoRen zhaoRen;
    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private  String tag="Tab2TeacherAdapter";

    public Tab2StudentAdapter(Context context, ImageLoaderHm<ImageView> m, String uid) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mImageLoaderHm = m;
        this.uid = uid;
    }

    public void setData(ZhaoRen list) {
        datas = list.student;
        zhaoRen =list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Student> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Student getItem(int position) {
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
           final Student f = getItem(position);


            h.name.setText(f.username);
            h.content.setText(f.title);

            h.icon.setTag(f.userimage);


//            if (f.userimage!=null) {
//                mImageLoaderHm.DisplayImage(MyNetApiConfig.ImageServerAddr + f.userimage, h.icon);
//            }
            if (f.userimage!=null) {
                Picasso.with(mContext).load(MyNetApiConfig.ImageServerAddr + f.userimage).placeholder(R.mipmap.menu_head).into(h.icon);
            }

            if (f.isfollow.equals("0")){  //没有关注
                h.follow.setBackgroundResource(R.mipmap.follow);


            }else {
                h.follow.setBackgroundResource(R.mipmap.followed);
            }

            h.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (uid.equals("0")){
                        Intent i = new Intent(mContext, StubActivity.class);
                        i.putExtra("fragment", SelectLoginFragment.class.getName());
                        mContext.startActivity(i);
                        ToastManager.getInstance(mContext).showText("请登录后在操作");

                        return;
                    }


                    if (f.isfollow.equals("0")){  //没有关注
//                        h.follow.setBackgroundResource(R.mipmap.follow);
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.addfollow,
                                MyNetRequestConfig.addfollow(mContext,uid,f.uid ),
                                "teacher", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                                        if (netResponse!=null){
                                            if(netResponse.bool==1){
                                                f.isfollow = "1";
                                                zhaoRen.student = datas;
                                                AppShare.setZhaoRenList(mContext,zhaoRen);
                                                notifyDataSetChanged();
                                            }else {
                                               ToastManager.getInstance(mContext).showText(netResponse.result);
                                            }

                                        }

                                    }
                                });

                    }else {
//                        if (f.isfollow.equals("0")){  //没有关注
//                        h.follow.setBackgroundResource(R.mipmap.follow);
                        RestNetCallHelper.callNet(
                                mContext,
                                MyNetApiConfig.delfollow,
                                MyNetRequestConfig.delfollow(mContext,uid,f.uid ),
                                "delfollow", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                                        if (netResponse!=null){
                                            if(netResponse.bool==1){
                                                f.isfollow = "0";
                                                notifyDataSetChanged();
                                            }else {
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
        // TODO Auto-generated method stub

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
