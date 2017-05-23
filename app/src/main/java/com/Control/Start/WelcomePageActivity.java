package com.Control.Start;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kaolafm.record.AudioMixerNative;
import com.m3b.rbaudiomixlibrary.AudioRecorderNative;
import com.service.DownloadService;
import com.service.PlayService;
import com.utils.LogUtils;
import com.yiqu.Tool.Common.CommonApplication;
import com.yiqu.Tool.Common.CommonThreadPool;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.fragment.tab3.SelectArticalFragment;
import com.yiqu.iyijiayi.model.UpdateInformation;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.service.JpushReceiver;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.NetWorkUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 * Created by  on 2016/05/24.
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

        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        init(R.layout.activity_welcome_page);
        Intent it = new Intent(this, PlayService.class);
        startService(it);
        startService(new Intent(this, DownloadService.class));

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
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA
        });
        MobclickAgent.onPageStart("启动页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
        if (AppShare.getIsLogin(this)){
            LogUtils.LOGE("Alias",AppShare.getUserInfo(this).uid);
            JPushInterface.setAliasAndTags(getApplicationContext(),
                    AppShare.getUserInfo(this).uid,
                    null,
                    mAliasCallback);
        }

        super.onResume();
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    Log.i(TAG, logs);
//                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//                    break;
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.i(TAG, logs);
//                    // 延迟 60 秒来调用 Handler 设置别名
//                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    break;
//                default:
//                    logs = "Failed with errorCode = " + code;
//            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("启动页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    private void begin() {

        //      intent = new Intent(this, RecordAacActivity.class);//
        intent = new Intent(this, MainActivity.class);//

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
        if (NetWorkUtils.isNetworkAvailable(this)) {
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
                            if (type == TYPE_SUCCESS) {


                                updateInfo = new Gson().fromJson(netResponse.data, UpdateInformation.class);
                                //  LogUtils.LOGE("1",updateInfo.toString());
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

                                } else {
                                    begin();
                                }
                            }
                        }
                    }, false, true);

        } else {
            ToastManager.getInstance(this).showText("网络故障，请检查网络再试");

        }


//        begin();
    }

//    private void installApk(File apkFile) {
//        if (!apkFile.exists()) {
//            return;
//        }
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.parse("file://" + apkFile.toString());
//        i.setDataAndType(uri, "application/vnd.android.package-archive");
//        startActivity(i);
//
//    }

    private void installApk(String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }


        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(WelcomePageActivity.this, "com.yiqu.iyijiayi.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        startActivity(intent);
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
            installApk(mFile.getAbsolutePath());


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


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {

        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}