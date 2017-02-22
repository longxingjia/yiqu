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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab3.DownloadXizuoFragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import java.util.ArrayList;

public class SoundsTab1Adapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Music> datas = new ArrayList<Music>();
    private Context mContext;
    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private String tag = "SoundsTab1Adapter";

    public SoundsTab1Adapter(Context context, ImageLoaderHm<ImageView> m) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mImageLoaderHm = m;
    }


    public void setData(ArrayList<Music> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Music> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Music getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {

        TextView musicname;
        TextView accompaniment;
        ImageView image;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.tab3_sounds_list, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.accompaniment = (TextView) v.findViewById(R.id.accompaniment);
                h.image = (ImageView) v.findViewById(R.id.image);

                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            Music f = getItem(position);
            h.musicname.setText(f.musicname);
            h.accompaniment.setText(f.accompaniment);
            //  LogUtils.LOGE(tag,f.toString());
            if (f.image != null) {
                mImageLoaderHm.DisplayImage(MyNetApiConfig.ImageServerAddr + f.image, h.image);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Music m = getItem(arg2 - 1);
        Intent i = new Intent(mContext, StubActivity.class);
        i.putExtra("fragment", DownloadXizuoFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("music", m);
        i.putExtras(bundle);
        mContext.startActivity(i);

    }

}
