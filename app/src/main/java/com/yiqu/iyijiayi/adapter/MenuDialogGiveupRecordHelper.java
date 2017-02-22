package com.yiqu.iyijiayi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ui.views.MenuDialog;
import com.ui.views.MenuDialog.OnMenuListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MenuDialogGiveupRecordHelper {

	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CAMERA = 0;
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_GALLERY = 1;
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CROP = 2;
	private static final int PIC_PIXLS = 300;

	private MenuDialog mMenuDialog;
	private ImageView photo;
	private MenuDialogListerner menuDialogListerner;

	public MenuDialogGiveupRecordHelper(Context context, final MenuDialogListerner menuDialogListerner){

		String[] items = new String[]{"重新录制","放弃录制"};
		mMenuDialog = new MenuDialog(context, "确定要放弃录制吗",items , new OnMenuListener() {
			@Override
			public void onMenuClick(MenuDialog dialog, int which, String item) {
				switch(which){
				case 0:
					menuDialogListerner.onSelected(0);
					break;
				case 1:
					menuDialogListerner.onSelected(1);
					break;
				}
			}
			@Override
			public void onMenuCanle(MenuDialog dialog) {
				
			}
		});
		this.menuDialogListerner = menuDialogListerner;

	}
	
	public void show(View v) {

		mMenuDialog.show(v);
	}
	

}
