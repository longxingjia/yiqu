package com.yiqu.iyijiayi.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.yiqu.iyijiayi.R;


/**
 * Created by Arron
 */
public class DialogHelper {

    private ProgressDialog prDialog;
    private AsyncTask task;
    private ProgressBar progress;

//    public DialogHelper(Context context, AsyncTask task) {
//        this.task = task;
//        prDialog = new ProgressDialog(context);
//        prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        prDialog.setMessage(context.getText(R.string.common_dialog_loading_data));
//        prDialog.setCancelable(true);
//        prDialog.setCanceledOnTouchOutside(false);
//        prDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface paramDialogInterface) {
//                if(DialogHelper.this.task!=null) {
//                    DialogHelper.this.task.cancel(false);
//                    prDialog.cancel();
//                }
//            }
//        });
//    }

    public DialogHelper(Context context, AsyncTask task) {
        this.task = task;
        //重置对话框
        if (prDialog != null && prDialog.isShowing()) {
            prDialog.dismiss();
        }
        //显示对话框
        prDialog = ProgressDialog.show(context, null, null, false, false);
        //注册按键事件
        prDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                //当用户松开BACK按键时，对话框会取消
                if (KeyEvent.ACTION_UP == event.getAction() && keyCode == KeyEvent.KEYCODE_BACK) {
                    prDialog.dismiss();
                }
                return false;
            }
        });
        prDialog.setCancelable(true);
        prDialog.setCanceledOnTouchOutside(false);
        //监听对话框停止事件，把对话框停止视作为接口的取消
        prDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface arg0) {
                if (DialogHelper.this.task != null) {
                    DialogHelper.this.task.cancel(false);
                    prDialog.cancel();
                }

            }
        });
        //设置对话框布局
        prDialog.setContentView(R.layout.progress_overlay);
        prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TextView t = (TextView) prDialog.findViewById(com.byron.framework.R.id.text);
        //设置对话框描述文字为接口的语言配置
        t.setText(context.getText(R.string.common_dialog_loading_data));


    }


    public DialogHelper(Context context, AsyncTask task, String message) {
        this.task = task;
        prDialog = new ProgressDialog(context);
        prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prDialog.setMessage(message);
        prDialog.setCancelable(true);
        prDialog.setCanceledOnTouchOutside(false);
        prDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface paramDialogInterface) {
                DialogHelper.this.task.cancel(false);
                prDialog.cancel();
            }
        });
    }

    public void showProgressDialog() {
        prDialog.show();
    }

    public void dismissProgressDialog() {
        prDialog.cancel();
    }

    public void setDialogText(String input) {
        prDialog.setMessage(input);
    }

    public DialogHelper(Context context, AsyncTask task,int max) {
        this.task = task;
        //重置对话框
        if (prDialog != null && prDialog.isShowing()) {
            prDialog.dismiss();
        }
        //显示对话框
        prDialog = ProgressDialog.show(context, null, null, false, false);
        //注册按键事件
        prDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //当用户松开BACK按键时，对话框会取消
                if (KeyEvent.ACTION_UP == event.getAction() && keyCode == KeyEvent.KEYCODE_BACK) {
                    prDialog.dismiss();
                }
                return false;
            }
        });
        prDialog.setCancelable(true);
        prDialog.setCanceledOnTouchOutside(false);
        //监听对话框停止事件，把对话框停止视作为接口的取消
        prDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface arg0) {
                if (DialogHelper.this.task != null) {
                    DialogHelper.this.task.cancel(false);
                    prDialog.cancel();
                }

            }
        });
        //设置对话框布局
        prDialog.setContentView(R.layout.progress);
        prDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        TextView t = (TextView) prDialog.findViewById(com.byron.framework.R.id.text);
        //设置对话框描述文字为接口的语言配置
        t.setText(context.getText(R.string.common_dialog_loading_data));
        progress = (ProgressBar) prDialog.findViewById(com.byron.framework.R.id.progress);

        progress.setMax(max);

    }

    public DialogHelper(Context context,int max) {

        //重置对话框
        if (prDialog != null && prDialog.isShowing()) {
            prDialog.dismiss();
        }
        //显示对话框
        prDialog = ProgressDialog.show(context, null, null, false, false);
        //注册按键事件
//        prDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                //当用户松开BACK按键时，对话框会取消
//                if (KeyEvent.ACTION_UP == event.getAction() && keyCode == KeyEvent.KEYCODE_BACK) {
//                    prDialog.dismiss();
//                }
//                return false;
//            }
//        });
        prDialog.setCancelable(false);
        prDialog.setCanceledOnTouchOutside(false);
        //监听对话框停止事件，把对话框停止视作为接口的取消
//        prDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            public void onDismiss(DialogInterface arg0) {
//                if (DialogHelper.this.task != null) {
//                    DialogHelper.this.task.cancel(false);
//                    prDialog.cancel();
//                }
//
//            }
//        });
        //设置对话框布局
        prDialog.setContentView(R.layout.progress);
        prDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        TextView t = (TextView) prDialog.findViewById(com.byron.framework.R.id.text);
        //设置对话框描述文字为接口的语言配置
        t.setText(context.getText(R.string.common_dialog_loading_data));
        progress = (ProgressBar) prDialog.findViewById(com.byron.framework.R.id.progress);

        progress.setMax(max);

    }

    public void setProgress(int p) {
        progress.setProgress(p);
    }
    public void setMax(int max) {
        progress.setMax(max);
    }

}
