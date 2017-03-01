package com.yiqu.iyijiayi.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.google.gson.JsonObject;
import com.ui.views.MenuDialog;
import com.ui.views.MenuDialog.OnMenuListener;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private String uid;
    private Context mContext;

    public MenuDialogPicHelper(Fragment f, String uid, BitmapListener mBitmapListener) {
        mFragment = f;
        mContext = mFragment.getActivity();
        String[] items = new String[]{"拍照", "相册选取"};
        mMenuDialog = new MenuDialog(f.getActivity(), "修改头像", items, new OnMenuListener() {
            @Override
            public void onMenuClick(MenuDialog dialog, int which, String item) {
                switch (which) {
                    case 0:
                        catchPicture();//调用具体方法
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
        this.uid = uid;

    }

    public void show(View v, ImageView photo) {
        this.photo = photo;
        mMenuDialog.show(v);
    }

    /**
     * 拍照
     */
    private void catchPicture() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            getFile();

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;

            if (currentapiVersion<24){
                Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
                mFragment.startActivityForResult(getImageByCamera, REQUEST_CODE_INITIAL_PIC_FROM_CAMERA);
            }else {
//                ContentValues contentValues = new ContentValues(1);
//                contentValues.put(MediaStore.Images.Media.DATA, sdcardTempFile.getAbsolutePath());
//                Uri uri = mFragment.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
//                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                mFragment.startActivityForResult(getImageByCamera, REQUEST_CODE_INITIAL_PIC_FROM_CAMERA);

                Uri imageUri = FileProvider.getUriForFile(mContext, "com.yiqu.iyijiayi.fileprovider", sdcardTempFile);//通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                mFragment.startActivityForResult(intent, REQUEST_CODE_INITIAL_PIC_FROM_CAMERA);


            }



        } else {
            Toast.makeText(mFragment.getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 相册
     */
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

    private void getFile() {
        String localFile1 = Variable.StorageDirectoryPath;
        File localFile2 = new File(localFile1, "/image/");
        if (!localFile2.exists()) {
            localFile2.mkdirs();
        }
        sdcardTempFile = new File(localFile2, "photo_tmp.jpg");
        sdcardTempFile.delete();
    }

    /**
     * 拍完后裁剪图片
     */
    private void cropPicture() {
        Intent intent;
        intent = photoCrop(mContext,sdcardTempFile, PIC_PIXLS, PIC_PIXLS);
        mFragment.startActivityForResult(intent, REQUEST_CODE_INITIAL_PIC_FROM_CROP);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data == null||"".equals(data)) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_INITIAL_PIC_FROM_CAMERA:
                LogUtils.LOGE("------",sdcardTempFile.getAbsolutePath());
                cropPicture();
                break;
            case REQUEST_CODE_INITIAL_PIC_FROM_GALLERY:
                try {
                    upload(sdcardTempFile.getAbsolutePath());   
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_INITIAL_PIC_FROM_CROP:
                try {

                    upload(sdcardTempFile.getAbsolutePath());
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

    public void upload(String path) {

        final Map<String, String> params = new HashMap<String, String>();
//        if (AppShare.getUserInfo(mFragment.getActivity()).type.equals("1")) { //1是学生
//            params.put("type", String.valueOf(0));
//        } else {
//            params.put("type", String.valueOf(1));
//        }
        params.put("uid", uid);
        LogUtils.LOGE(",",uid);

        File file = new File(path);
        if (file.exists()) {
            UpLoaderTask upLoaderTask = new UpLoaderTask(MyNetApiConfig.editUser.getPath(), params, file);
            upLoaderTask.execute();
        }

    }

    private class UpLoaderTask extends AsyncTask<Void, Integer, String> {

        private final String TAG = "UpLoaderTask";
        private Map<String, String> params;
        private File file;
        private String mUrl;
        private DialogHelper dialogHelper;

        public UpLoaderTask(String mUrl, Map<String, String> params, File file) {
            super();
            this.params = params;
            this.file = file;
            this.mUrl = mUrl;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(mFragment.getActivity(), this);
                dialogHelper.showProgressDialog();
            }

        }

        @Override
        protected String doInBackground(Void... p) {

            final String request = UploadImage.uploadFile(mUrl, params, file);
            if (TextUtils.isEmpty(request)) {
                return "";
            } else {
                return request;
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(String result) {

            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }

            if (!TextUtils.isEmpty(result)) {

                LogUtils.LOGE(TAG, result);

//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String bool = jsonObject.getString("bool");
//                    String data = jsonObject.getString("data");
//                    String re = jsonObject.getString("result");
//                    if (bool.equals("1")) {
//                        ToastManager.getInstance(mFragment.getActivity()).showText("上传完成");
//                        Bitmap bm = decodeUriAsBitmap(mFragment.getActivity(), file.getAbsolutePath());
//                        photo.setImageBitmap(bm);
//
//                        String url = new JSONObject(data).getString("filepath");
//                        mBitmapListener.onBitmapUrl(url);
//                    } else {
//                        ToastManager.getInstance(mFragment.getActivity()).showText(re);
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            if (isCancelled()) {


            }
            return;
        }

    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * @param tempFile 图片临时缓存地址
     * @param outputX  裁剪要的像素宽度
     * @param outputY  裁剪要的像素高度
     * @return 启动intent
     * @comments 通过相册选择，并裁剪图片
     * @version 1.0
     */
    public static Intent photoAlbumCrop(File tempFile, int outputX, int outputY) {
        Intent intent = new Intent("android.intent.action.PICK");//启动相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));//裁剪缓存图片
        intent.putExtra("crop", "true");//是否裁剪
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);// 输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }

    /**
     * @param tempFile 图片临时缓存地址
     * @param outputX  裁剪要的像素宽度
     * @param outputY  裁剪要的像素高度
     * @return 启动intent
     * @comments 裁剪图片
     * @version 1.0
     */
    public static Intent photoCrop(Context context, File tempFile, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");//启动裁剪

        Uri photoURI = FileProvider.getUriForFile(context, "com.yiqu.iyijiayi.fileprovider", tempFile);
        LogUtils.LOGE("----",photoURI.toString());

     /* 这句要记得写：这是申请权限，之前因为没有添加这个，打开裁剪页面时，一直提示“无法修改低于50*50像素的图片”，
      开始还以为是图片的问题呢，结果发现是因为没有添加FLAG_GRANT_READ_URI_PERMISSION。
      如果关联了源码，点开FileProvider的getUriForFile()看看（下面有），注释就写着需要添加权限。
      */
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(photoURI, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

//        intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
        intent.putExtra("crop", "true");//是否裁剪
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);// 输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
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
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
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
     *
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
