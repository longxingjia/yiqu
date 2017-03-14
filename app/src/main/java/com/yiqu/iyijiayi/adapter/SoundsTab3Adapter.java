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
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

import java.util.ArrayList;

public class SoundsTab3Adapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<ComposeVoice> datas = new ArrayList<ComposeVoice>();
    private Context mContext;

    private String tag = "SoundsTab3Adapter";

    public SoundsTab3Adapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

    }


    public void setData(ArrayList<ComposeVoice> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<ComposeVoice> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ComposeVoice getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class HoldChild {

        TextView musicname;
        TextView add_question;
        TextView upload;
        TextView record_time;
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
                h.record_time = (TextView) v.findViewById(R.id.record_time);
                h.add_question = (TextView) v.findViewById(R.id.add_question);
                h.upload = (TextView) v.findViewById(R.id.upload);
                h.image = (ImageView) v.findViewById(R.id.image);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            final ComposeVoice f = getItem(position);
            h.musicname.setText(f.musicname);
            String2TimeUtils string2TimeUtils = new String2TimeUtils();

            h.record_time.setText(String2TimeUtils.longToString(f.createtime,"MM月dd日 HH:mm"));

            h.upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, StubActivity.class);
                    i.putExtra("fragment", UploadXizuoFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("composeVoice", f);
                    i.putExtras(bundle);
                    mContext.startActivity(i);

                }
            });
            h.add_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("composeVoice", f);
                    Intent intent = new Intent(mContext, StubActivity.class);
                    intent.putExtra("fragment", AddQuestionFragment.class.getName());

                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        Music m = getItem(arg2 - 1);
//        Intent i = new Intent(mContext, StubActivity.class);
//        i.putExtra("fragment", DownloadXizuoFragment.class.getName());
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("music", m);
//        i.putExtras(bundle);
//        mContext.startActivity(i);

    }

}
