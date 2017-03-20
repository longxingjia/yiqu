package com.Control.Start;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.Tool.Common.CommonApplication;
import com.Tool.Common.CommonThreadPool;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.Control.Main.RecordActivity;
import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.fragment.Tab1Fragment;
import com.yiqu.iyijiayi.model.UpdateInformation;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
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

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 * Created by 郑童宇 on 2016/05/24.
 */
public class WelcomePageActivity extends Activity {
    private Intent intent;

    private Handler handler;
    private String apkName;
    private CommonApplication application;
    private UpdateInformation updateInfo;
    private String fileName;
    private String filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(R.layout.activity_welcome_page);
    }

    private void init(int layoutResourceId) {
        setContentView(layoutResourceId);
        initData();
    }

    private void initData() {
        application = CommonApplication.getInstance();

        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                application.initialiseInUIThread();

                startActivity(intent);

                finish();
            }
        };
//        PermissionGen.needPermission(this, 200, Manifest.permission.CAMERA);

        CommonThreadPool.getThreadPool().addFixedTask(initialiseThread);
    }

    @Override
    protected void onResume() {

        PermissionGen.needPermission(this, 100, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
        });

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    private void begin() {

        intent = new Intent(this, MainActivity.class);

        Message.obtain(handler).sendToTarget();
    }

    private Runnable initialiseThread = new Runnable() {
        @Override
        public void run() {
            application.initialise();

//            PermissionGen.with(WelcomePageActivity.this)
//                    .addRequestCode(100)
//                    .permissions(
//                            Manifest.permission.READ_CONTACTS,
//                            Manifest.permission.RECEIVE_SMS,
//                            Manifest.permission.WRITE_CONTACTS)
//                    .request();


//            begin();


        }
    };

    @PermissionSuccess(requestCode = 200)
    public void openCamera() {
        Toast.makeText(this, "Camera permission is  granted", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 200)
    public void failOpenCamera() {
        Toast.makeText(this, "Camera permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        RestNetCallHelper.callNet(
                this,
                MyNetApiConfig.checkUpdate,
                MyNetRequestConfig.checkUpdate(this),
                "checkUpdate", new NetCallBack() {




                    @Override
                    public void onNetNoStart(String id) {

                    }

                    @Override
                    public void onNetStart(String id) {

                    }

                    @Override
                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                        updateInfo = new Gson().fromJson(netResponse.data, UpdateInformation.class);
                        LogUtils.LOGE("1",updateInfo.toString());
//                        fileName = "艺加艺" + System.currentTimeMillis() + ".zip";
                        fileName = "艺加艺" + System.currentTimeMillis() + ".apk";

                        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";

                        String versionName = getPackageInfo(WelcomePageActivity.this).versionName;
                        if (!updateInfo.version.equals(versionName)) {
//                            if (updateInfo.ismust.equals("1")) {
//                                forceUpdate(WelcomePageActivity.this);
//                            } else {
//                                normalUpdate(WelcomePageActivity.this);
//                            }

                            forceUpdate(WelcomePageActivity.this);

                        }else {
                            begin();
                        }

                    }
                }, false, true);


//        begin();
    }

    private void installApk(File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + apkFile.toString());
        i.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(i);

    }


    /**
     * 强制升级 ，如果不点击确定升级，直接退出应用
     *
     * @param context
     */
    private void forceUpdate(final Context context) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新");
        if (!TextUtils.isEmpty(updateInfo.content)) {
            mDialog.setMessage(updateInfo.content);
        }

        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoaderTask task = new DownLoaderTask(updateInfo.url, filePath, fileName, WelcomePageActivity.this);
                task.execute();
                dialog.dismiss();
            }
        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 直接退出应用
                System.exit(0);
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 正常升级，用户可以选择是否取消升级
     *
     * @param context
     */
    private void normalUpdate(final Context context) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新");
        if (!TextUtils.isEmpty(updateInfo.content)) {
            mDialog.setMessage(updateInfo.content);
        }

        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent mIntent = new Intent(context, UpdateService.class);
//                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //传递数据
//                mIntent.putExtra("appname", UpdateInformation.appname);
//                mIntent.putExtra("appurl", UpdateInformation.updateurl);
//                context.startService(mIntent);

                DownLoaderTask task = new DownLoaderTask(updateInfo.url, filePath, fileName, WelcomePageActivity.this);
                task.execute();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                begin();
            }
        }).create().show();
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private int mProgress = 0;
        private ProgressReportingOutputStream mOutputStream;
        private Activity mContext = null;
        private int contentLength = 1;
        private DialogHelper dialogHelper;

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
//            progressBar.setProgress(0);
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(mContext, this, 100);
                dialogHelper.showProgressDialog();
            }

        }

        @Override
        protected Long doInBackground(Void... params) {
            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {


            if (values.length > 1) {
                contentLength = values[1];
                if (contentLength == -1) {
//                    progressBar.setIndeterminate(true);
                } else {
//                    progressBar.setMax(contentLength);
                }
            } else {
//                progressBar.setProgress(values[0].intValue());
                int percent = values[0].intValue() * 100 / contentLength;
                dialogHelper.setProgress(percent);

            }
            if (isCancelled()) {
                mFile.delete();
                return;
            }


        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }
            if (isCancelled()) {
                mFile.delete();
                return;
            }

//            try {
//                Tools.UnZipFolder(mFile.getAbsolutePath(),filePath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            mFile.delete();
            installApk(mFile);


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


    @PermissionFail(requestCode = 100)
    public void failContact() {

        Toast.makeText(this, getResources().getString(R.string.permission_white_external_hint), Toast.LENGTH_SHORT).show();
        PermissionUtils.openSettingActivity(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}