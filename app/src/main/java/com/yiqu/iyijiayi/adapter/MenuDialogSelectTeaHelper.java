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

public class MenuDialogSelectTeaHelper {

	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CAMERA = 0;
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_GALLERY = 1;
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CROP = 2;
	private static final int PIC_PIXLS = 300;

	private MenuDialog mMenuDialog;
	TeaListener teaListener;

	public MenuDialogSelectTeaHelper(Context context,String title, String[] items,final TeaListener teaListener){
//		String[] items = new String[]{"找导师请教","免费上传作品"};
		mMenuDialog = new MenuDialog(context, title,items , new OnMenuListener() {
			@Override
			public void onMenuClick(MenuDialog dialog, int which, String item) {
				switch(which){
				case 0:
					teaListener.onTea(0);
					break;
				case 1:
					teaListener.onTea(1);
					break;
				}
			}
			@Override
			public void onMenuCanle(MenuDialog dialog) {
				
			}
		});
		this.teaListener = teaListener;

	}
	
	public void show(View v) {
		mMenuDialog.show(v);
	}

	public interface TeaListener {
		public void onTea(int tea);
	}

	
}
