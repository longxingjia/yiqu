/**
 * @filename PersonAll.java
 * @author byron(linbochuan@hopsun.cn)
 * @date 2013-9-2
 * @vsersion 1.0
 * Copyright (C) 2013 辉盛科技发展责任有限公司
 */
package com.yiqu.iyijiayi.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.squareup.picasso.Picasso;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.SoundItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab5.InfoFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Tab1SoundAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;

    private String tag ="Tab1SoundAdapter";

    public Tab1SoundAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;

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
        TextView soundtime;
        TextView tea_name;
        TextView tectitle;
        ImageView stu_header;
        ImageView tea_header;
        TextView stu_listen;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            HoldChild h;
            if (v == null) {
                h = new HoldChild();
                v = mLayoutInflater.inflate(R.layout.remen_sound, null);
                h.musicname = (TextView) v.findViewById(R.id.musicname);
                h.desc = (TextView) v.findViewById(R.id.desc);
                h.soundtime = (TextView) v.findViewById(R.id.soundtime);
                h.tea_name = (TextView) v.findViewById(R.id.tea_name);
                h.tectitle = (TextView) v.findViewById(R.id.tectitle);
                h.stu_header = (ImageView) v.findViewById(R.id.stu_header);
                h.tea_header = (ImageView) v.findViewById(R.id.tea_header);
                h.stu_listen = (TextView) v.findViewById(R.id.stu_listen);
                v.setTag(h);
            }

            h = (HoldChild) v.getTag();
            Sound f = getItem(position);
            h.musicname.setText(f.musicname);
            h.desc.setText(f.desc);
            h.soundtime.setText(f.soundtime+"\"");
            h.tea_name.setText(f.tecname);
            h.tectitle.setText(f.tectitle);
          //  LogUtils.LOGE(tag,f.soundpath);


            PictureUtils.showPicture(mContext,f.tecimage,h.tea_header);
            PictureUtils.showPicture(mContext,f.stuimage,h.stu_header);

            h.stu_listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private ProgressDialog mDialog;
        private int mProgress = 0;
        private ProgressReportingOutputStream mOutputStream;
        private Activity mContext = null;
        private String mLesson;

        public DownLoaderTask(String downloadPath, String packageName,
                              String out, Activity context) {
            super();
            if (context != null) {
                mDialog = new ProgressDialog(context);

                mContext = context;
            } else {
                mDialog = null;
            }

            mLesson = packageName;

            try {
                mUrl = new URL(downloadPath);
                String fileName = downloadPath.substring(
                        downloadPath.lastIndexOf("/") + 1,
                        downloadPath.length());
                mFile = new File(out, fileName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
            if (mDialog != null) {
                mDialog.setTitle("Downloading...");
                // mDialog.setMessage(mGame.getGameName());
                mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
//                        Tools.delete(mFile);
                    }
                });
                mDialog.show();
            }
        }

        @Override
        protected Long doInBackground(Void... params) {
            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            if (mDialog == null)
                return;
            if (values.length > 1) {
                int contentLength = values[1];
                if (contentLength == -1) {
                    mDialog.setIndeterminate(true);
                } else {
                    mDialog.setMax(contentLength);
                }
            } else {
                mDialog.setProgress(values[0].intValue());
            }
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);

            //	Log.e(TAG, "下载完");

           // Tools.unzip(mFile, mLesson);
            mFile.delete();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

//            Intent intent = new Intent();
//            intent.setClass(mContext, GameActivity.class);
//            intent.putExtra("Lesson", mLesson);
//            startActivityForResult(intent, GameActivity.RESULT_CODE);

            if (isCancelled())
                return;
        }

        private long download() {
            URLConnection connection = null;
            int bytesCopied = 0;
            try {
                connection = mUrl.openConnection();
                int length = connection.getContentLength();
                if (mFile.exists()/* && length == mFile.length() */) {
                    Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                    mFile.delete();
                }
//                File directory = new File(Tools.DB_PATH);
//                if (null != directory && !directory.exists()) {
//                    directory.mkdir();
//                }

                mOutputStream = new ProgressReportingOutputStream(mFile);
                publishProgress(0, length);
                bytesCopied = copy(connection.getInputStream(), mOutputStream);
                if (bytesCopied != length && length != -1) {
                    Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied
                            + ", length" + length);
                }
                mOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bytesCopied;
        }

        private int copy(InputStream input, OutputStream output) {
            byte[] buffer = new byte[1024 * 8];
            BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
            BufferedOutputStream out = new BufferedOutputStream(output,
                    1024 * 8);
            int count = 0, n = 0;
            try {
                while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                    out.write(buffer, 0, n);
                    count += n;
                }
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return count;
        }

        private final class ProgressReportingOutputStream extends
                FileOutputStream {

            public ProgressReportingOutputStream(File file)
                    throws FileNotFoundException {
                super(file);
                // TODO Auto-generated constructor stub
            }

            @Override
            public void write(byte[] buffer, int byteOffset, int byteCount)
                    throws IOException {
                // TODO Auto-generated method stub
                super.write(buffer, byteOffset, byteCount);
                mProgress += byteCount;
                publishProgress(mProgress);
            }

        }

    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Sound f = getItem(arg2-2);//加了头部
//        if (!isNetworkConnected(mContext)) {
//            ToastManager.getInstance(mContext).showText(
//                    R.string.fm_net_call_no_network);
//            return;
//        };
        if (AppShare.getIsLogin(mContext)){
            Intent i = new Intent(mContext, StubActivity.class);
            i.putExtra("fragment", SoundItemDetailFragment.class.getName());
            i.putExtra("data",f.sid+"");

            mContext.startActivity(i);
        }else {
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
