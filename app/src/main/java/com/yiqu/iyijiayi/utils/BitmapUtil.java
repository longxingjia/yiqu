package com.yiqu.iyijiayi.utils;

/**
 * Created by Administrator on 2017/4/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.utils.L;
import com.utils.Variable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 位图处理类
 *
 * @author HalfmanG2
 */
public class BitmapUtil {


    public static Bitmap createColorBitmap(Context context, int color) {
        Bitmap bmp = Bitmap.createBitmap(DensityUtil.dip2px(context, 60), DensityUtil.dip2px(context, 60), Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;

    }

    public static Bitmap blur(Bitmap bitmap, float radius, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap); // 创建输出图片
        RenderScript rs = RenderScript.create(context); // 构建一个RenderScript对象
        ScriptIntrinsicBlur gaussianBlue = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); // 创建高斯模糊脚本
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap); // 创建用于输入的脚本类型
        Allocation allOut = Allocation.createFromBitmap(rs, output); // 创建用于输出的脚本类型
        gaussianBlue.setRadius(radius); // 设置模糊半径，范围0f<radius<=25f
        gaussianBlue.setInput(allIn); // 设置输入脚本类型
        gaussianBlue.forEach(allOut); // 执行高斯模糊算法，并将结果填入输出脚本类型中
        allOut.copyTo(output); // 将输出内存编码为Bitmap，图片大小必须注意
        rs.destroy(); // 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
        return output;
    }

    //图片按比例大小压缩方法（根据路径获取图片并压缩）：
    private static Bitmap getReviewImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是2048*1536分辨率，所以高和宽我们设置为
        float hh = 2048f;//这里设置高度为2048f
        float ww = 1536f;//这里设置宽度为1536f
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
        return bitmap;
    }


    /**
     * 将bitmap 转成Image
     *
     * @param context
     * @param inputPath
     * @return
     */
    public static String decodeUriAsBitmap(Context context, String inputPath) {

        String outPath = Variable.StorageImagePath + System.currentTimeMillis() + ".jpg";
     //   String temPath = Variable.StorageImagePath + "temPath.jpg";

        if (context == null || inputPath == null)
            return "";

        Bitmap bitmap;
        try {
            bitmap = getReviewImage(inputPath);

//            File file = new File(temPath);
//            if (file.exists()) {
//                file.delete();
//            }
//            Uri uri = Uri.fromFile(new File(inputPath));
//            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
         //   FileOutputStream fOutTemPath = new FileOutputStream(temPath);
            FileOutputStream fOut = new FileOutputStream(outPath);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;

            while (baos.toByteArray().length / 1024 > 400) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
           // bitmap.compress(Bitmap.CompressFormat.JPEG, options, fOutTemPath);

             String type =  type(inputPath);  //image/png
            if (type.equals("image/jpeg")){
                int angel = readPictureDegree(inputPath);
                L.e(angel + "");
                Bitmap newBitmap = rotaingImageView(angel, bitmap);
                newBitmap.compress(Bitmap.CompressFormat.JPEG, options, fOut);
            }else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, fOut);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return outPath;
    }

    public static String type(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        String type = options.outMimeType;
        L.e("image type -> ", type);
        return type;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}