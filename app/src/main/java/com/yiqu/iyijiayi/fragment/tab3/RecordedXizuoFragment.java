package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab1SoundAdapter;
import com.yiqu.iyijiayi.fragment.tab5.InfoFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApi;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.DensityUtil;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.Tools;

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

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordedXizuoFragment extends AbsAllFragment {


    private TextView musicName;
    private TextView sumbit;
    private String tag = "RecordedXizuoFragment";
    private Music music;
    private ProgressBar progressBar;
    private TextView tv_progress;
    private TextView submit;

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
//
        setTitleText("录制习作");

    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.record_xizuo_fragment;
    }

    @Override
    protected void initView(View v) {
        musicName = (TextView) v.findViewById(R.id.musicname);
        tv_progress = (TextView) v.findViewById(R.id.tv_progress);
        submit = (TextView) v.findViewById(R.id.submit);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

//


    }

    @Override
    protected void init(Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        music = (Music) intent.getSerializableExtra("music");
//        LogUtils.LOGE(tag, music.toString());

        musicName.setText(music.musicname + "");
        Tools.DB_PATH = Tools.getCacheDirectory(getActivity(), Environment.DIRECTORY_DOWNLOADS).toString();

//        LogUtils.LOGE(tag, Tools.DB_PATH );
//        File directory = new File(Tools.DB_PATH + "/" + kchbhGame);
//        if(null != directory && directory.exists())
//        {
//            Intent intent = new Intent();
//            intent.setClass(getActivity(), GameActivity.class);
//            intent.putExtra("Lesson", kchbhGame);
//            startActivityForResult(intent,
//                    GameActivity.RESULT_CODE);
//            return;
//        }
        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        String fileName = Url.substring(
                Url.lastIndexOf("/") + 1,
                Url.length());
        fileName = music.musicname + "_" + fileName;

        if (!TextUtils.isEmpty(Tools.DB_PATH)) {
            File mFile = new File(Tools.DB_PATH, fileName);
            if (mFile.exists()) {
                Log.d(tag, "file " + mFile.getName() + " already exits!!");
//                mFile.delete();
            } else {
                DownLoaderTask task = new DownLoaderTask(Url, Tools.DB_PATH, fileName, getActivity());
                task.execute();
            }
        }
        super.init(savedInstanceState);
    }

    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private int mProgress = 0;
        private ProgressReportingOutputStream mOutputStream;
        private Activity mContext = null;
        private int contentLength = 1;

        public DownLoaderTask(String downloadPath, String out, String fileName, Activity context) {
            super();
            mContext = context;
            try {
                mUrl = new URL(downloadPath);

                mFile = new File(out, fileName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);
            tv_progress.setText("");
//
        }

        @Override
        protected Long doInBackground(Void... params) {
            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

//            if (mDialog == null)
//                return;
            if (values.length > 1) {

                contentLength = values[1];
                if (contentLength == -1) {
                    progressBar.setIndeterminate(true);
                } else {
                    progressBar.setMax(contentLength);
                }
            } else {
                progressBar.setProgress(values[0].intValue());
                if (contentLength == -1) {

                } else {
                    tv_progress.setText(values[0].intValue() * 100 / contentLength + "%");
                }
            }

            if (isCancelled()) {
                mFile.delete();
                return;
            }

        }

        @Override
        protected void onPostExecute(Long result) {
            // super.onPostExecute(result);

            //	Log.e(TAG, "下载完");
//             Tools.unzip(mFile, mLesson);
//            mFile.delete();

//            if (mDialog != null && mDialog.isShowing()) {
//                mDialog.dismiss();
//            }

//            Intent intent = new Intent();
//            intent.setClass(mContext, GameActivity.class);
//            intent.putExtra("Lesson", mLesson);
//            startActivityForResult(intent, GameActivity.RESULT_CODE);

            if (isCancelled())
                mFile.delete();
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
                File directory = new File(Tools.DB_PATH);
                if (null != directory && !directory.exists()) {
                    directory.mkdir();
                }

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
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
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
            }

            @Override
            public void write(byte[] buffer, int byteOffset, int byteCount)
                    throws IOException {
                super.write(buffer, byteOffset, byteCount);
                mProgress += byteCount;
                publishProgress(mProgress);
            }

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
