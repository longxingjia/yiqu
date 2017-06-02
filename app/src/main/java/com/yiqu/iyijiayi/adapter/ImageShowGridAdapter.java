package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.fragment.tab3.SharePicFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorFragment;
import me.nereo.multi_image_selector.bean.Image;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 */
public class ImageShowGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showAdd = true;
    private OnDelListener mListener;

    private ArrayList<String> mImages = new ArrayList<String>();
//    private List<Image> mSelectedImages = new ArrayList<>();

    final int mGridWidth;

    public ImageShowGridAdapter(Context context, boolean showAdd, int column) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showAdd = showAdd;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / column;
    }


    public void setShowAdd(boolean b) {
        if (showAdd == b) return;
        showAdd = b;
        notifyDataSetChanged();
    }

    public boolean isShowAdd() {
        return showAdd;
    }

    public interface OnDelListener {
        public void onDelClick(int size);

    }

    public void setOnDelListener(OnDelListener l) {
        mListener = l;
    }

//    /**
//     * 通过图片路径设置默认选择
//     * @param resultList
//     */
//    public void setDefaultSelected(ArrayList<String> resultList) {
//        for(String path : resultList){
//            Image image = getImageByPath(path);
//            if(image != null){
//                mSelectedImages.add(image);
//            }
//        }
//        if(mSelectedImages.size() > 0){
//            notifyDataSetChanged();
//        }
//    }

//    private Image getImageByPath(String path) {
//        if (mImages != null && mImages.size() > 0) {
//            for (Image image : mImages) {
//                if (image.path.equalsIgnoreCase(path)) {
//                    return image;
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 设置数据集
     *
     * @param images
     */
    public void setData(ArrayList<String> images) {

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 设置数据集
     */
    public ArrayList<String> getData() {
        return mImages;
    }

    @Override
    public int getCount() {
        return showAdd ? mImages.size() + 1 : mImages.size();
    }

    @Override
    public String getItem(int i) {

        return mImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

//        View v = convertView;
//        try {
//            ViewHolder h;
//            if (v == null) {
//                h = new ViewHolder();
//                v = mInflater.inflate(R.layout.item_image, null);
//                h.image = (ImageView) v.findViewById(R.id.image);
//                v.setTag(h);
//            }
//            h = (ViewHolder) v.getTag();
        View v = mInflater.inflate(R.layout.item_image, null);
        ImageView image = (ImageView) v.findViewById(R.id.image);
        ImageView del_pic = (ImageView) v.findViewById(R.id.del_pic);
        final int pos = i;

        if (i < mImages.size()) {
            File imageFile = new File(getItem(i));
            if (imageFile.exists()) {

                Picasso.with(mContext)
                        .load(imageFile)
                        .placeholder(me.nereo.multi_image_selector.R.drawable.mis_default_error)
                        .tag(SharePicFragment.TAG)
                        .resize(mGridWidth, mGridWidth)
                        .centerCrop()
                        .into(image);
            } else {
                image.setImageResource(me.nereo.multi_image_selector.R.drawable.mis_default_error);
            }
            del_pic.setVisibility(View.VISIBLE);

            del_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImages.remove(pos);
                    if (mListener!=null){
                        mListener.onDelClick(mImages.size());
                    }
                    setShowAdd(true);
                    notifyDataSetChanged();
                }
            });


        } else {
            Picasso.with(mContext)
                    .load(R.mipmap.pic_add)
                    .tag(SharePicFragment.TAG)
                    .resize(mGridWidth, mGridWidth)
                    .centerCrop()
                    .into(image);
            del_pic.setVisibility(View.GONE);

        }

        return v;
    }

    class ViewHolder {
        ImageView image;
    }

}
