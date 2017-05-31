package com.yiqu.iyijiayi.fragment.tab5;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.bumptech.glide.Glide;
import com.db.ComposeVoiceInfoDBHelper;
import com.db.DownloadMusicInfoDBHelper;
import com.ui.views.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.utils.Variable;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.utils.DataCleanManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by Administrator on 2017/2/9.
 */

public class ClearCacheFragment extends AbsAllFragment implements View.OnClickListener {
    @BindView(R.id.image_size)
    TextView image_size;
    @BindView(R.id.play_size)
    TextView play_size;
    @BindView(R.id.record_size)
    TextView record_size;
    @BindView(R.id.back_music_size)
    TextView back_music_size;
    private File folderImage;
    private File folderPlay;
    private File folderRecord;
    private File folderMusic;
    private static final int IMAGE = 0;
    private static final int PLAY = 1;
    private static final int RECORD = 2;
    private static final int MUSIC = 3;
    int type = -1;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_cache_fragment;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText("缓存");
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("缓存"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), "缓存");
    }

    @Override
    public void onPause() {
        super.onPause();
        JAnalyticsInterface.onPageEnd(getActivity(), "缓存");
        MobclickAgent.onPageEnd("缓存");
    }


    @Override
    protected void initView(View v) {

        ButterKnife.bind(this, v);
        v.findViewById(R.id.ll_image).setOnClickListener(this);
        v.findViewById(R.id.ll_play).setOnClickListener(this);
        v.findViewById(R.id.ll_record).setOnClickListener(this);
        v.findViewById(R.id.ll_music).setOnClickListener(this);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        folderImage = new File(Variable.StorageImagePath(getActivity()));
        folderPlay = new File(Variable.StorageQandAPath(getActivity()));
        folderRecord = new File(Variable.StorageMusicPath(getActivity()));
        folderMusic = new File(Variable.StorageMusicCachPath(getActivity()));

        try {
            String sizeImage = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderImage) + DataCleanManager.getCacheSize(getActivity()));
            String sizePlay = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderPlay));
            String sizeRecord = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderRecord));
            String sizeMusic = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderMusic));
            image_size.setText(sizeImage);
            play_size.setText(sizePlay);
            record_size.setText(sizeRecord);
            back_music_size.setText(sizeMusic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        String title = "小提示";
        String content = "确定要删除";


        switch (v.getId()) {
            case R.id.ll_image:
                DialogUtil.showDialog(getActivity(), title, content + "图片缓存吗", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = IMAGE;
                        DelCacheTask delCacheTask = new DelCacheTask(getActivity(), type);
                        delCacheTask.execute();
                    }
                });
                L.e("ff");
                break;
            case R.id.ll_play:
                DialogUtil.showDialog(getActivity(), title, content + "播放缓存吗", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = PLAY;
                        DelCacheTask delCacheTask = new DelCacheTask(getActivity(), type);
                        delCacheTask.execute();
                    }
                });


                break;
            case R.id.ll_record:
                DialogUtil.showDialog(getActivity(), title, content + "录音缓存吗", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = RECORD;
                        DelCacheTask delCacheTask = new DelCacheTask(getActivity(), type);
                        delCacheTask.execute();
                    }
                });
                break;
            case R.id.ll_music:
                DialogUtil.showDialog(getActivity(), title, content + "伴奏缓存吗", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = MUSIC;
                        DelCacheTask delCacheTask = new DelCacheTask(getActivity(), type);
                        delCacheTask.execute();
                    }
                });
                break;
        }


    }

    class DelCacheTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private int type;
        private DialogHelper dialog;

        public DelCacheTask(Context context, int type) {
            this.mContext = context;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            dialog = new DialogHelper(mContext, this);
            dialog.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (type) {
                case IMAGE:
//                    DataCleanManager.clearImageDiskCache(getActivity());
                    Glide.get(mContext).clearDiskCache();
                    DataCleanManager.deleteFilesByDirectory(folderImage);
                    break;
                case MUSIC:
                    DataCleanManager.deleteFilesByDirectory(folderMusic);
                    DownloadMusicInfoDBHelper  downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(getActivity());
                    downloadMusicInfoDBHelper.deleteAll();
                    downloadMusicInfoDBHelper.close();
                    break;

                case RECORD:
                    DataCleanManager.deleteFilesByDirectory(folderRecord);
                    ComposeVoiceInfoDBHelper dbHelper = new ComposeVoiceInfoDBHelper(getActivity());
                    dbHelper.deleteAll();
                    dbHelper.close();
                    break;
                case PLAY:
                    DataCleanManager.deleteFilesByDirectory(folderPlay);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void vo) {
            if (dialog != null) {
                dialog.dismissProgressDialog();
            }
            switch (type) {
                case IMAGE:
                    try {
                        String sizeImage = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderImage) + DataCleanManager.getCacheSize(getActivity()));
                        image_size.setText(sizeImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case MUSIC:
                    try {
                        String sizeMusic = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderMusic));
                        back_music_size.setText(sizeMusic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case RECORD:
                    try {
                        String sizeRecord = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderRecord));
                        record_size.setText(sizeRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case PLAY:
                    try {
                        String sizePlay = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(folderPlay));
                        play_size.setText(sizePlay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            ToastManager.getInstance(getActivity()).showText("清除成功");


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }

}
