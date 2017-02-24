package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.DensityUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/24.
 */

public class LongHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    protected MARK mark;

    protected enum MARK {
        NORMAL, DELETE
    }

    private LongHolder(Context context, int layoutId) {
        mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
        mConvertView.setTag(this);
    }

    public static LongHolder getInstance(Context context, int layoutId, View convertView) {
        boolean needInflate = convertView == null || ((LongHolder) convertView.getTag()).mark == MARK.DELETE;
        if (needInflate) {
            return new LongHolder(context, layoutId);
        }
        return (LongHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public LongHolder putText(int viewId, String text) {
        View v = getView(viewId);
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
        return this;
    }

    public LongHolder putText(int viewId, int resId) {
        View v = getView(viewId);
        if (v instanceof TextView) {
            ((TextView) v).setText(resId);
        }
        return this;
    }

    public LongHolder putImg(int viewId, int resId) {
        View v = getView(viewId);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageResource(resId);
        }
        return this;
    }

    public LongHolder putImg(int viewId, Bitmap bitmap) {
        View v = getView(viewId);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageBitmap(bitmap);
        }
        return this;
    }

    public LongHolder putImg(int viewId, Drawable drawable) {
        View v = getView(viewId);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * 网络加载图片；（使用了开源库：Picasso）[Picasso](https://github.com/square/picasso)
     *
     * @param viewId     要设置的ImageView或者ImageButton
     * @param url 要显示的图片地址
     * @param roundShape 是否设置为圆角；
     * @return 返回自己，链式编程；
     */
    public LongHolder putImg(final int viewId, final String url, boolean roundShape) {

        // 如果 view 不是继承自 ImageView 或者 url为null, 则不做任何处理；
        if (!(getView(viewId) instanceof ImageView) || url == null) {
            return this;
        }

        final ImageView imageView = getView(viewId);
        final Context context = imageView.getContext();

        if (!roundShape) {
            // 该库已经做了错位处理了（如果只是将加载的图片加载到ImageView的话，就不需要错位问题）；
            Picasso.with(context).load(url).placeholder(R.mipmap.menu_head).into(imageView);
            return this;
        } else {

            // 首先设置默认图片
//            Bitmap bitmap = ViewUtil.getBitmapByXfermode(context, R.mipmap.menu_head,
//                    Color.parseColor("#993382"),
//                    DensityUtil.dip2px(context, 48),
//                    DensityUtil.dip2px(context, 48),
//                    PorterDuff.Mode.SRC_IN);
            imageView.setImageBitmap(bitmap);

            // 由于只是从网络获取图片，没有处理错位问题，这里需要单独处理；
            // 防止图片过多导致显示错乱（用url来作为验证）；
            imageView.setTag(url);
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = null;
                    try {
                        // 获取网络图片，不可在主线程中操作；
                        bitmap = Picasso.with(context).load(url).placeholder(R.mipmap.menu_head).get();
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap == null) {
                        return;
                    }
//                    bitmap = ViewUtil.getBitmapByXfermode(context, bitmap,
//                            Color.parseColor("#993382"),
//                            DensityUtil.dip2px(context, 48),
//                            DensityUtil.dip2px(context, 48),
//                            PorterDuff.Mode.SRC_IN);

                    // 防止图片错乱；
                    String url2 = (String) imageView.getTag();
                    if (url.equals(url2)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }.execute();
        }
        return this;
    }

}