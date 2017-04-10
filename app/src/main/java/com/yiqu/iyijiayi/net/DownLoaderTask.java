package com.yiqu.iyijiayi.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.yiqu.Control.Main.RecordComActivity;
import com.yiqu.iyijiayi.adapter.DialogHelper;

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
 * Created by Administrator on 2017/4/10.
 */

public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {
    private URL mUrl;
    private File mFile;
    private final String TAG = "DownLoaderTask";
    private ProgressReportingOutputStream mOutputStream;
    private int mProgress = 0;
    private Context mContext;
    private DialogHelper dialogHelper;

    public DownLoaderTask(Context context,String downloadPath, String fileName){
        try {
            mUrl = new URL(downloadPath);
            mFile = new File(fileName);
            mContext = context;
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        dialogHelper = new DialogHelper(mContext, this);
        dialogHelper.showProgressDialog();
    }

    @Override
    protected Long doInBackground(Void... params) {

        return download();
    }
        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }

            if (isCancelled())
                return;
        }

    private long download() {
        URLConnection connection = null;
        int bytesCopied = 0;
        try {
            connection = mUrl.openConnection();
            int length = connection.getContentLength();
            if (mFile.exists()/* && length == mFile.length()*/) {
                Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                mFile.delete();
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
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
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

    private final class ProgressReportingOutputStream extends FileOutputStream {

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
