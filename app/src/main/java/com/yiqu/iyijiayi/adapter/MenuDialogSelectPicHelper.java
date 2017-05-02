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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.utils.Variable;
import com.ui.views.MenuDialog;
import com.ui.views.MenuDialog.OnMenuListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MenuDialogSelectPicHelper {

    protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CAMERA = 0;
    protected static final int REQUEST_CODE_INITIAL_PIC_FROM_GALLERY = 1;
    protected static final int REQUEST_CODE_INITIAL_PIC_FROM_CROP = 2;
    private static final int PIC_PIXLS = 300;

    private MenuDialog mMenuDialog;
    private ImageView photo;
    private MenuDialogListerner menuDialogListerner;
    private Context context;
    private File sdcardTempFile;
    private Fragment fragment;

    public MenuDialogSelectPicHelper(Fragment fragment, final MenuDialogListerner menuDialogListerner) {

        String[] items = new String[]{"拍照", "相册选取"};
        this.context = fragment.getActivity();
        mMenuDialog = new MenuDialog(context, "修改头像", items, new OnMenuListener() {
            @Override
            public void onMenuClick(MenuDialog dialog, int which, String item) {
                switch (which) {
                    case 0:
                        catchPicture();
                        break;
                    case 1:
//					menuDialogListerner.onSelected(1);
                        break;
                }
            }

            @Override
            public void onMenuCanle(MenuDialog dialog) {

            }
        });
        this.menuDialogListerner = menuDialogListerner;

        this.fragment = fragment;

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
            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
            fragment.startActivityForResult(getImageByCamera, REQUEST_CODE_INITIAL_PIC_FROM_CAMERA);
        } else {
            Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
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

    /**
     * 拍照
     */
    private void cropPicture() {
        Intent intent;
        intent = photoCrop(sdcardTempFile, PIC_PIXLS, PIC_PIXLS);
        fragment.startActivityForResult(intent, REQUEST_CODE_INITIAL_PIC_FROM_CROP);
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

    public String getReviewPath() {
        String url = sdcardTempFile.getAbsolutePath();
        String path = url.substring(
                url.lastIndexOf("/") + 1,
                url.length()) + "_review.jpg";

        return path;
    }


    /**
     * @param tempFile 图片临时缓存地址
     * @param outputX  裁剪要的像素宽度
     * @param outputY  裁剪要的像素高度
     * @return 启动intent
     * @comments 裁剪图片
     * @version 1.0
     */
    public static Intent photoCrop(File tempFile, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");//启动裁剪
        intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
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

    /**
     * 将bitmap 转成Image
     *
     * @param context
     * @param path
     * @return
     */
    public Bitmap decodeUriAsBitmap(Context context, String path) {
        if (context == null || path == null) return null;

        Bitmap bitmap;
        try {
            Uri uri = Uri.fromFile(new File(path));
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileOutputStream fOut = new FileOutputStream(sdcardTempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;

            while (baos.toByteArray().length / 1024 > 400) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, options, fOut);
            getReviewImage(sdcardTempFile.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    //图片按比例大小压缩方法（根据路径获取图片并压缩）：
    private Bitmap getReviewImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是2048*1536分辨率，所以高和宽我们设置为
        float hh = 288f;//这里设置高度为2048f
        float ww = 384f;//这里设置宽度为1536f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;


        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //  bitmap.compress(Bitmap.CompressFormat.JPEG,100,null);//压缩好比例大小后再进行质量压缩
        return compressImage(bitmap);
    }

    //质量压缩方法
    private Bitmap compressImage(Bitmap image) {

        try {
            FileOutputStream fOut = new FileOutputStream(sdcardTempFile.getAbsoluteFile());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

            }
            image.compress(Bitmap.CompressFormat.JPEG, options, fOut);
            //  getReviewImage(getCameraPath());

//            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            //   return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void upload(String path) {
        Bitmap bm = decodeUriAsBitmap(context, sdcardTempFile.getAbsolutePath());
        photo.setImageBitmap(bm);//提前显示，客户嫌头像显示慢，这里不需要保存，只需要显示

    }

}
