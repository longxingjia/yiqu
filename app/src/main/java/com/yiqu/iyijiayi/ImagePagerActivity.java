package com.yiqu.iyijiayi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.base.utils.ToastManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.utils.L;
import com.utils.Variable;
import com.yiqu.YWActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends Activity {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";

    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    public ImageSize imageSize;
    private static Context mContext;
    private int startPos;
    private ArrayList<String> imgUrls;

    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_imagepager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);

        getIntentData();

        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < guideViewList.size(); i++) {
                    guideViewList.get(i).setSelected(i == position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);

        addGuideView(guideGroup, startPos, imgUrls);

//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.e("ff");
//                finish();
//            }
//        });

    }

    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {

                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i == startPos ? true : false);
                final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.gudieview_width),
                        getResources().getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
//                L.e("ff");

                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Activity context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;
        private Bitmap bitmap;

        public void setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Activity context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (datas == null) return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            final int pos = position;

            if (view != null) {
                final PhotoView imageView = (PhotoView) view.findViewById(R.id.image);

                if (imageSize != null) {

                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }
                String url = datas.get(position);
                final String imgurl;
                if (url.contains("http://wx.qlogo.cn")) {
                    imgurl = url;
                } else {
                    imgurl = MyNetApiConfig.ImageServerAddr + url;
                }

                imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        context.finish();
                    }
                });

                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String title = "提示";
                        String[] items = new String[]{"保存"};

                        MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(mContext, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
                            @Override
                            public void onTea(int tea) {
                                switch (tea) {
                                    case 0:
                                        if (bitmap!=null){
                                            saveImageToGallery(mContext, bitmap);
                                        }

                                        break;
                                }
                            }
                        });
                        menuDialogSelectTeaHelper.show(v);


                        return true;
                    }
                });


                //loading
//                final ProgressBar loading = new ProgressBar(context);
//                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                loadingLayoutParams.gravity = Gravity.CENTER;
//                loading.setLayoutParams(loadingLayoutParams);
//                ((FrameLayout) view).addView(loading);


//                Glide.with(context)
//                        .load(imgurl)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
//                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
//                        .error(R.mipmap.ic_launcher)
//                        .into(new GlideDrawableImageViewTarget(imageView) {
//                            @Override
//                            public void onLoadStarted(Drawable placeholder) {
//                                super.onLoadStarted(placeholder);
//                               /* if(smallImageView!=null){
//                                    smallImageView.setVisibility(View.VISIBLE);
//                                    Glide.with(context).load(imgurl).into(smallImageView);
//                                }*/
//                                loading.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                /*if(smallImageView!=null){
//                                    smallImageView.setVisibility(View.GONE);
//                                }*/
//                                loading.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                                super.onResourceReady(resource, animation);
//                                loading.setVisibility(View.GONE);
//                                /*if(smallImageView!=null){
//                                    smallImageView.setVisibility(View.GONE);
//                                }*/
//                            }
//                        });

                Glide.with(context)
                        .load(imgurl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存多个尺寸
                        //       .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.mipmap.ic_launcher)
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                           //     L.e("ff");
                                bitmap = resource;
                                imageView.setImageBitmap(resource);
//                                int imageHeight = resource.getHeight();
//                                if (imageHeight > 4096) {
//                                    imageHeight = 4096;
//                                    ViewGroup.LayoutParams para = imageview.getLayoutParams();
//                                    para.width = LayoutParams.MATCH_PARENT;
//                                    para.height = imageHeight;
//                                    imageview.setLayoutParams(para);
//
//                                    Glide.with(context)
//                                            .load(url)
//                                            .placeholder(R.drawable. default)
//                            .dontAnimate()
//                                                .centerCrop()
//                                                .into(imageview);
//                                } else {
//                                    Glide.with(context)
//                                            .load(url)
//                                            .placeholder(R.drawable. default)
//                            .dontAnimate()
//                                                .into(imageview);
//                                }
                            }

                        });


                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


        public void saveImageToGallery(Context context, Bitmap bmp) {
            // 首先保存图片
            String fileName =  System.currentTimeMillis() + ".jpg";
            File currentFile = new File(Variable.StorageSaveImagePath(),fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(currentFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                ToastManager.getInstance(mContext).showText("保存成功");
            } catch (FileNotFoundException e) {
                ToastManager.getInstance(mContext).showText("保存失败");
                e.printStackTrace();
            } catch (IOException e) {
                ToastManager.getInstance(mContext).showText("保存失败");
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    currentFile.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(currentFile.getPath()))));
        }
    }

    @Override
    protected void onDestroy() {
        guideViewList.clear();
        super.onDestroy();
    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
