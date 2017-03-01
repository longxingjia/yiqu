package com.Control.Start;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.Tool.Common.CommonApplication;
import com.Tool.Common.CommonThreadPool;

import com.yiqu.Control.Main.RecordActivity;
import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.PermissionUtils;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 * Created by 郑童宇 on 2016/05/24.
 */
public class WelcomePageActivity extends Activity {
    private Intent intent;

    private Handler handler;

    private CommonApplication application;

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

        PermissionGen.needPermission(this, 100, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        begin();
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