package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Tool.Global.Variable;
import com.yiqu.Control.Main.RecordActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.db.DownloadMusicInfoDBHelper;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.AppShare;
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

public class DownloadXizuoFragment extends AbsAllFragment {


    private TextView musicName;
    private Button submit;
    private String tag = "DownloadXizuoFragment";
    private Music music;
    private ProgressBar progressBar;
    private TextView tv_progress;

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
        setTitleText(getString(R.string.record_zuopin));
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.download_xizuo_fragment;
    }

    @Override
    protected void initView(View v) {
        musicName = (TextView) v.findViewById(R.id.musicname);
        tv_progress = (TextView) v.findViewById(R.id.tv_progress);
        submit = (Button) v.findViewById(R.id.submit);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        submit.setClickable(false);
        submit.setEnabled(false);
        Intent intent = getActivity().getIntent();
        music = (Music) intent.getSerializableExtra("music");

        musicName.setText(music.musicname + "");


        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        String fileName = Url.substring(
                Url.lastIndexOf("/") + 1,
                Url.length());
        fileName = music.musicname + "_" + fileName;
//        AppShare


        File mFile = new File(Variable.StorageMusicPath, fileName);
        if (mFile.exists()) {
         //   Log.d(tag, "file " + mFile.getName() + " already exits!!");
            nextPage();
        } else {
            if (Tools.isNetworkAvailable(getActivity())) {
                DownLoaderTask task = new DownLoaderTask(Url, Variable.StorageMusicPath, fileName, getActivity());
                task.execute();
            }
        }

        super.init(savedInstanceState);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

    }

    private void nextPage() {
        Intent i = new Intent(getActivity(), RecordActivity.class);
//        i.putExtra("fragment", RecordXizuoFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("music", music);
        i.putExtras(bundle);
        getActivity().startActivity(i);
        getActivity().finish();
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
            submit.setClickable(true);
            submit.setEnabled(true);
            submit.setBackgroundResource(R.drawable.red_box);
            DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(getActivity());
            downloadMusicInfoDBHelper.insert(music);

            if (isCancelled()) {
                mFile.delete();
                submit.setClickable(false);
            }
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
                File directory = new File(Variable.StorageMusicPath);
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

}
