package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.yiqu.iyijiayi.model.Constant;
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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private DownloadManager downloadManager;
    private DownloadManager.Query query;
    private ScheduledExecutorService scheduledExecutorService;
    private Future<?> future;
    long fileTotalSize;
    private long downloadId = -1;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.QUERY:

                    Cursor cursor = downloadManager.query(query);

                    if (cursor != null && cursor.moveToFirst()) {
                        //此处直接查询文件大小
                        int bytesDLIdx =
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int bytesDL = cursor.getInt(bytesDLIdx);

                        //获取文件下载总大小
                        fileTotalSize = cursor.getLong(cursor.getColumnIndex(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        cursor.close();

//                        Log.w(tag, "downloaded size: " + bytesDL);
//                        Log.w(tag, "total size: " + fileTotalSize);

                        if (fileTotalSize != 0) {
                            int percentage = (int) (bytesDL * 100 / fileTotalSize);
                            progressBar.setProgress(percentage);
                            tv_progress.setText(percentage + "%");
//                            statusText.setText(percentage + "%");

                        }

                        //终止轮询task
                        if (fileTotalSize == bytesDL)
                            future.cancel(true);
                    }
                    break;


                default:
                    break;
            }

        }
    };

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

        File mFile = new File(Variable.StorageMusicCachPath, fileName);
        LogUtils.LOGE(tag,Variable.StorageMusicCachPath);

        if (mFile.exists()) {
         //   Log.d(tag, "file " + mFile.getName() + " already exits!!");
            nextPage();
        } else {
            if (Tools.isNetworkAvailable(getActivity())) {
                dowoload(Url,fileName);
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

    private void dowoload(String downloadUrl, String fileName) {
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        //downloadUrl为下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        //设置文件下载目录和文件名
        //   request.setDestinationInExternalPublicDir("Android/data/com.yiqu.iyijiayi/cache/musiccach", fileName);
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName);

        //设置下载中通知栏提示的标题
        request.setTitle(fileName);

        //设置下载中通知栏提示的介绍
        request.setDescription(fileName);
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(false);

        /*
        表示下载进行中和下载完成的通知栏是否显示，默认只显示下载中通知，
        VISIBILITY_HIDDEN表示不显示任何通知栏提示，
        这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        /*
        表示下载允许的网络类型，默认在任何网络下都允许下载，
        有NETWORK_MOBILE、NETWORK_WIFI、NETWORK_BLUETOOTH三种及其组合可供选择；
        如果只允许wifi下载，而当前网络为3g，则下载会等待
        */
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //移动网络情况下是否允许漫游
        //request.setAllowedOverRoaming(true);

        //表示允许MediaScanner扫描到这个文件，默认不允许
        // request.allowScanningByMediaScanner();

        /*
        设置下载文件的mineType,
        因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件，
        所以我们可以利用这个属性。比如上面设置了mimeType为application/package.name，
        我们可以同时设置某个Activity的intent-filter为application/package.name，用于响应点击的打开文件
        */
        //request.setMimeType("application/package.name");

        //添加请求下载的网络链接的http头，比如User-Agent，gzip压缩等
        //request.addRequestHeader(String header, String value);
        downloadId = downloadManager.enqueue(request);
        query = new DownloadManager.Query();

        query.setFilterById(downloadId);//筛选下载任务，传入任务ID，可变参数

        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        //每过1000ms通知handler去查询下载状态
        future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = Constant.QUERY;
                mHandler.sendMessage(msg);
            }
        }, 500, 300, TimeUnit.MILLISECONDS);

    }

    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (completeDownloadId == downloadId) {

                if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
                    future.cancel(true);
                    scheduledExecutorService.shutdown();
                    scheduledExecutorService = null;
                }
                downloadId = -1;
                tv_progress.setText(100 + "%");
                progressBar.setProgress(100);
                submit.setClickable(true);
                submit.setEnabled(true);
                submit.setBackgroundResource(R.drawable.red_box);
                DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(getActivity());
                downloadMusicInfoDBHelper.insert(music);

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        /** 注册下载完成接收广播 **/
        getActivity().registerReceiver(downloadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(downloadCompleteReceiver);
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            future.cancel(true);
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;

        }
        super.onPause();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downloadManager != null && downloadId != -1){
            downloadManager.remove(downloadId);
        }
    }
}
