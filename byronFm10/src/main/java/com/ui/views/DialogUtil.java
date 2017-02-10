package com.ui.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.ContextThemeWrapper;

import com.byron.framework.R;

/**
 * @ClassName: DialogUtil
 * @Description: TODO
 *
 */

public class DialogUtil {

	public static AlertDialog showDialog(Context c, String msg){
		return showDialog(c, null,msg);
	}
	
	public static AlertDialog showDialog(Context c, String title, String msg){
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
	
	public static AlertDialog showDialog(Context c, String title, String msg, String btn1, OnClickListener l1){
		return showDialog(c, title, msg, btn1, l1, null, null);
	}
	
	public static AlertDialog showDialog(Context c, String title, String msg, String btn1, OnClickListener l1, String btn2, OnClickListener l2){
		AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.Theme_fm__Default_AlertDialog));
		b.setTitle(title);
		b.setMessage(msg);
		b.setPositiveButton(btn1, l1);
		b.setNegativeButton(btn2, l2);
		b.show();
		return b.create();
	}
	
	public static AlertDialog.Builder creatBuilder(Context c){
		AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.Theme_fm__Default_AlertDialog));
		return b;
	}
}
