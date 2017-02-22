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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MenuDialogPicHelper {
	
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CAMERA = 0;
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_GALLERY = 1;
	protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CROP = 2;
	private static final int PIC_PIXLS = 300;
	
	private Fragment mFragment;
	private File sdcardTempFile;
	private MenuDialog mMenuDialog;
	private ImageView photo;
	private BitmapListener mBitmapListener;

	public MenuDialogPicHelper(Fragment f, BitmapListener mBitmapListener){
		mFragment = f;
		String[] items = new String[]{"拍照","相册"};
		mMenuDialog = new MenuDialog(f.getActivity(), "",items , new OnMenuListener() {
			@Override
			public void onMenuClick(MenuDialog dialog, int which, String item) {
				switch(which){
				case 0:
					catchPicture();
					break;
				case 1:
					selectPicture();
					break;
				}
			}
			@Override
			public void onMenuCanle(MenuDialog dialog) {
				
			}
		});
		this.mBitmapListener = mBitmapListener;

	}
	
	public void show(View v, ImageView photo) {
		this.photo = photo;
		mMenuDialog.show(v);
	}
	
	/** 拍照 */
	private void catchPicture() {
	   String state = Environment.getExternalStorageState();  
       if (state.equals(Environment.MEDIA_MOUNTED)) {  
    	   getFile();
           Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
           getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
           mFragment.startActivityForResult(getImageByCamera, REQUEST_CODE_INITIAL_PIC_FROM_CAMERA);  
       }  
       else {  
           Toast.makeText(mFragment.getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();  
       }  
	}

	/** 相册 */
	private void selectPicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			getFile();
			Intent intent;
			intent = photoAlbumCrop(sdcardTempFile, PIC_PIXLS, PIC_PIXLS);
			mFragment.startActivityForResult(intent,
					REQUEST_CODE_INITIAL_PIC_FROM_GALLERY);
		} else {
			Toast.makeText(mFragment.getActivity(), "请确认已经插入SD卡",
					Toast.LENGTH_LONG).show();
		}

	}
	private void getFile(){
		File localFile1 = Environment.getExternalStorageDirectory();
		File localFile2 = new File(localFile1, "/test/");
		if(!localFile2.exists()){
			localFile2.mkdirs();
		}
		sdcardTempFile = new File(localFile2, "photo_tmp.jpg");
		sdcardTempFile.delete();
	}
	
	/** 拍照 */
	private void cropPicture() {
		Intent intent;
		intent = photoCrop(sdcardTempFile, PIC_PIXLS, PIC_PIXLS);
		mFragment.startActivityForResult(intent, REQUEST_CODE_INITIAL_PIC_FROM_CROP);  
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_CODE_INITIAL_PIC_FROM_CAMERA:
			cropPicture();
			break;
		case REQUEST_CODE_INITIAL_PIC_FROM_GALLERY:
			try {
				//upload(sdcardTempFile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case REQUEST_CODE_INITIAL_PIC_FROM_CROP:
			try {
				//upload(sdcardTempFile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
	
//	public void upload(String path){
//		Bitmap bm = decodeUriAsBitmap(mFragment.getActivity(), sdcardTempFile.getAbsolutePath());
//		photo.setImageBitmap(bm );//提前显示，客户嫌头像显示慢，这里不需要保存，只需要显示
//		String key = System.currentTimeMillis()+".jpg";
//		String token = AppShare.getQiniuToken(mFragment.getActivity());
//		uploadManager.put(Bitmap2Bytes(bm), key, token, new UpCompletionHandler() {
//			@Override
//			public void complete(String s,
//					com.qiniu.android.http.ResponseInfo responseInfo,
//					JSONObject jsonObject) {
//				if(responseInfo.statusCode==200){
//					if(mBitmapListener != null){
//						//{"hash":"Fu98plsHtDQauI5GyzlCQZELwtpI","key":"1445530466425.jpg"}
//						String key = "";
//						try {
//							key = ""+jsonObject.get("key");
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						Bitmap bm = decodeUriAsBitmap(mFragment.getActivity(), sdcardTempFile.getAbsolutePath());
//						photo.setImageBitmap(bm );
//						mBitmapListener.onBitmapUrl(key);
//						Toast.makeText(mFragment.getActivity(), "上传完成", Toast.LENGTH_SHORT).show();
//					}
//				}else {
//					Toast.makeText(mFragment.getActivity(), "上传失败",  Toast.LENGTH_SHORT).show();
//				}
//			}
//		}, null);
//	}
	
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * @comments 通过相册选择，并裁剪图片
	 * @param tempFile 图片临时缓存地址
	 * @param outputX  裁剪要的像素宽度
	 * @param outputY  裁剪要的像素高度
	 * @return 启动intent
	 * @version 1.0
	 */
	public static Intent photoAlbumCrop(File tempFile, int outputX, int outputY){
		Intent intent = new Intent("android.intent.action.PICK");//启动相册
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		intent.putExtra("output", Uri.fromFile(tempFile));//裁剪缓存图片
		intent.putExtra("crop", "true");//是否裁剪
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);// 输出图片大小
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale",true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}
	/**
	 * @comments 裁剪图片
	 * @param tempFile 图片临时缓存地址
	 * @param outputX  裁剪要的像素宽度
	 * @param outputY  裁剪要的像素高度
	 * @return 启动intent
	 * @version 1.0
	 */
	public static Intent photoCrop(File tempFile, int outputX, int outputY){
		Intent intent = new Intent("com.android.camera.action.CROP");//启动裁剪
		intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
		intent.putExtra("crop", "true");//是否裁剪
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);// 输出图片大小
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale",true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}
	
	public static Bitmap decodeUriAsBitmap(Context context, String path) {
        if (context == null || path == null) return null;

        Bitmap bitmap;
        try {
            Uri uri = Uri.fromFile(new File(path));
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri ));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
	
	public interface BitmapListener {
		public void onBitmapUrl(String url);
	}
	
	/**
	 * bitmap转为base64
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
