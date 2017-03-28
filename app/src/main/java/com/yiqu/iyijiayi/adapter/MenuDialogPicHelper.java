package com.yiqu.iyijiayi.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.yiqu.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.google.gson.Gson;
import com.ui.views.MenuDialog;
import com.ui.views.MenuDialog.OnMenuListener;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MenuDialogPicHelper {

    protected static final int REQUEST_FROM_CAMERA = 0;
    protected static final int REQUEST_FROM_GALLERY = 1;
    protected static final int REQUEST_FROM_CAMERA_CROP = 2;
    private static final int PIC_PIXLS = 1000;

    private Fragment mFragment;
    private String tempFile;  //原图文件
    private MenuDialog mMenuDialog;
    private ImageView photo;
    private BitmapListener mBitmapListener;
    private String uid;
    private Context mContext;
    private Uri imageUri;  //原图保存地址
    private String cropFile; //裁剪文件
    private String url;

    public MenuDialogPicHelper(Fragment f, String uid, String title, String url, BitmapListener mBitmapListener) {
        mFragment = f;
        mContext = mFragment.getActivity();
        String[] items = new String[]{"拍照", "相册选取"};
        mMenuDialog = new MenuDialog(f.getActivity(), title, items, new OnMenuListener() {
            @Override
            public void onMenuClick(MenuDialog dialog, int which, String item) {
                switch (which) {
                    case 0:
                        catchPicture();//调用具体方法
                        break;
                    case 1:
                        selectFromAlbum();
                        break;
                }
            }

            @Override
            public void onMenuCanle(MenuDialog dialog) {

            }
        });
        this.mBitmapListener = mBitmapListener;
        this.uid = uid;
        this.url = url;

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
            File file = new File(tempFile);
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(mContext, "com.yiqu.iyijiayi.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            } else {
                imageUri = Uri.fromFile(file);
            }
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            mFragment.startActivityForResult(intent, REQUEST_FROM_CAMERA);
        }

    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        getFile();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mFragment.startActivityForResult(intent,
                REQUEST_FROM_GALLERY);
    }

    private void getFile() {
        String localFile1 = Variable.StorageDirectoryPath;
        File localFile2 = new File(localFile1, "/image/");
        if (!localFile2.exists()) {
            localFile2.mkdirs();
        }
        File file = new File(localFile2, System.currentTimeMillis() + "_temp.jpg");
        file.delete();
        File file2 = new File(localFile2, System.currentTimeMillis() + ".jpg");
        tempFile = file.getAbsolutePath();
        cropFile = file2.getAbsolutePath();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
//        if (data == null || "".equals(data)) {
//            return;
//        }

        switch (requestCode) {
            case REQUEST_FROM_CAMERA:
                // LogUtils.LOGE("------", tempFile.getAbsolutePath());
                cropPhoto(PIC_PIXLS, PIC_PIXLS);
                break;
            case REQUEST_FROM_GALLERY:
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_FROM_CAMERA_CROP:
                upload(cropFile);
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {

        tempFile = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(mContext, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                tempFile = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                tempFile = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            tempFile = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            tempFile = imageUri.getPath();
        }

        cropPhoto(PIC_PIXLS, PIC_PIXLS);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        tempFile = getImagePath(imageUri, null);
        cropPhoto(PIC_PIXLS, PIC_PIXLS);
    }

    public void upload(String path) {

        final Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);

        File file = new File(path);
        if (file.exists()) {
            UpLoaderTask upLoaderTask = new UpLoaderTask(url, params, file);
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

            return UploadImage.uploadFile(mUrl, params, file);

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
                LogUtils.LOGE("--", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String bool = jsonObject.getString("bool");
                    String data = jsonObject.getString("data");
                    String re = jsonObject.getString("result");
                    if (bool.equals("1")) {
                        ToastManager.getInstance(mFragment.getActivity()).showText(mContext.getString(R.string.net_success));
                        Bitmap bm = decodeUriAsBitmap(mFragment.getActivity(), file.getAbsolutePath());
                        photo.setImageBitmap(bm);

                        if (data.contains("filepath")) {
                            JSONObject j = new JSONObject(data);
                            String filepath = j.getString("filepath");
                            mBitmapListener.onBitmapUrl(filepath);
                        } else {
                            UserInfo userInfo = new Gson().fromJson(data, UserInfo.class);
                            AppShare.setUserInfo(mContext, userInfo);

                        }
                        new File(tempFile).delete();

                    } else {
                        ToastManager.getInstance(mFragment.getActivity()).showText(re);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (isCancelled()) {


            }
            return;
        }

    }

    /**
     * @param outputX 裁剪要的像素宽度
     * @param outputY 裁剪要的像素高度
     * @return 启动intent
     * @comments 裁剪图片
     * @version 1.0
     */
    private void cropPhoto(int outputX, int outputY) {
        File file = new File(cropFile);
        Uri outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);// 输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mFragment.startActivityForResult(intent, REQUEST_FROM_CAMERA_CROP);
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


}
