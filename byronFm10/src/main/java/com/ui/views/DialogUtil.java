package com.ui.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.byron.framework.R;

/**
 * @ClassName: DialogUtil
 * @Description: TODO
 */

public class DialogUtil {

    public static AlertDialog showDialog(Context c, String msg) {
        return showDialog(c, null, msg);
    }

    public static AlertDialog showDialog(Context c, String title, String msg) {
        return showDialog(c, title, msg, null, null);
    }

    public static AlertDialog showDefaultDialog(Context c, String msg) {
        return showDialog(c, c.getString(R.string.fm_dialog_default_titile),
                msg, c.getString(R.string.fm_dialog_default_btnok), null);
    }

    public static AlertDialog showDefaultDialog(Context c, String msg,
                                                OnClickListener ok) {
        return showDialog(c, c.getString(R.string.fm_dialog_default_titile),
                msg, c.getString(R.string.fm_dialog_default_btnok), ok, c.getString(R.string.fm_dialog_default_btncancel), null);
    }

    public static AlertDialog showDialog(Context c, String title, String msg, String btn1, OnClickListener l1) {
        return showDialog(c, title, msg, btn1, l1, null, null);
    }

    public static AlertDialog showDialog(Context c, String title, String msg, String btn1, OnClickListener l1, String btn2, OnClickListener l2) {
        AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.Theme_fm__Default_AlertDialog));
        b.setTitle(title);
        b.setMessage(msg);
        b.setPositiveButton(btn1, l1);
        b.setNegativeButton(btn2, l2);
        b.show();
        return b.create();
    }

    public static AlertDialog showMyDialog(Context c, String title, String msg, String btn1, View.OnClickListener l1, String btn2, View.OnClickListener l2) {
        AlertDialog dialog = new AlertDialog.Builder(c).create();
        dialog.setView(LayoutInflater.from(c).inflate(R.layout.dialog, null));
        dialog.show();
      //  dialog.setCancelable(false);
        dialog.getWindow().setContentView(R.layout.dialog);
      //  dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
         TextView tvTitle = (TextView) dialog.findViewById(R.id.title);
        tvTitle.setText(title);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.msg);
        tvMsg.setText(msg);
        TextView dialog_button_cancel = (TextView) dialog.findViewById(R.id.dialog_button_cancel);
        TextView dialog_button_ok = (TextView) dialog.findViewById(R.id.dialog_button_ok);
        dialog_button_cancel.setText(btn1);
        dialog_button_ok.setText(btn2);
        dialog_button_cancel.setOnClickListener(l1);
        dialog_button_ok.setOnClickListener(l2);
        return dialog;
    }

    public static AlertDialog.Builder creatBuilder(Context c) {
        AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.Theme_fm__Default_AlertDialog));
        return b;
    }
}
