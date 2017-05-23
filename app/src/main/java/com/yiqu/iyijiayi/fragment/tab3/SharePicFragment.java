package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.model.ComposeVoice;
import com.model.Music;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.utils.LogUtils;
import com.utils.Variable;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.ImageShowGridAdapter;
import com.yiqu.iyijiayi.adapter.UploaderTask;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.adapter.ImageGridAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SharePicFragment extends AbsAllFragment {


    public static String TAG = "SharePicFragment";
    private ImageShowGridAdapter mImageAdapter;
    @BindView(R.id.grid)
    public GridView gridView;
    @BindView(R.id.content)
    public EditText content;
    private ArrayList<String> mSelectPath;
    private int maxNum = 9;
    private int currentNum = 0;
    private static final int REQUEST_IMAGE = 2;
    String desc = "";

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT | FLAG_BTN;
    }

    @Override
    protected boolean onPageBack() {

        return false;
    }

    @Override
    protected boolean onPageNext() {
        if (TextUtils.isEmpty(content.getText().toString())) {
            desc = "分享图片";
        } else {
            desc = content.getText().toString();
        }
        mSelectPath = mImageAdapter.getData();
        if (mSelectPath != null && mSelectPath.size() > 0) {
            upload(mSelectPath);
        } else {
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.addTextImage,
                    MyNetRequestConfig.addTextImage(getActivity()
                            , desc, AppShare.getUserInfo(getActivity()).uid
                            , ""),
                    "addTextImage",
                    SharePicFragment.this);
        }
        return false;
    }

    @Override
    protected void initTitle() {

        setTitleText(getString(R.string.share_pic));
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.share_pic)); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), getString(R.string.share_pic));
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.share_pic));
        JAnalyticsInterface.onPageEnd(getActivity(), getString(R.string.share_pic));
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab3_publish;
    }

    @Override
    protected int getBodyView() {
        return R.layout.share_pic_fragment;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);

//        gridView.setNumColumns();

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mImageAdapter = new ImageShowGridAdapter(getActivity(), true, 4);
        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (mImageAdapter.isShowAdd()) {
                    if (mSelectPath != null && position < mSelectPath.size()) {
                        String image = (String) parent.getAdapter().getItem(position);
//                            selectImageFromGrid(image, mode);
                    } else {
                        showAddAction();
                    }

                } else {
//                    Image image = (Image) adapterView.getAdapter().getItem(i);
//                    selectImageFromGrid(image, mode);
                }


            }
        });
    }

    private void showAddAction() {
        boolean showCamera = true;
        MultiImageSelector selector = MultiImageSelector.create(getActivity());
        selector.showCamera(showCamera);
        selector.count(maxNum);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(SharePicFragment.this, REQUEST_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {

                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                mImageAdapter.setData(mSelectPath);

            }
        }
    }

    private void upload(List<String> paths) {
        String url = MyNetApiConfig.uploadSounds.getPath();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("uid", AppShare.getUserInfo(getActivity()).uid);
        UpLoadTask u = new UpLoadTask(url, params, paths);
        u.execute();

    }

    private class UpLoadTask extends AsyncTask<Void, Integer, List<String>> {

        private final String TAG = "UpLoaderTask";
        private Map<String, String> params;
        private String mUrl;
        private DialogHelper dialogHelper;
        private List<String> paths;
        private List<String> pathsServer;

        public UpLoadTask(String mUrl, Map<String, String> params, List<String> paths) {
            super();
            this.params = params;
            this.mUrl = mUrl;
            this.paths = paths;
            pathsServer = new ArrayList<String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(getActivity(), this);
                dialogHelper.showProgressDialog();
            }

        }

        @Override
        protected List<String> doInBackground(Void... p) {
            for (int i = 0; i < paths.size(); i++) {

                String outPath = Variable.StorageImagePath + System.currentTimeMillis() + ".jpg";
                BitmapUtil.decodeUriAsBitmap(getActivity(), paths.get(i), outPath);
                File file = new File(outPath);
                String re = UploadImage.uploadFile(mUrl, params, file);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(re);
                    re = jsonObject.getString("result");
                    String bool = jsonObject.getString("bool");
                    String data = jsonObject.getString("data");

                    if (bool.equals("1")) {
                        String filepath = new JSONObject(data).getString("filepath");
                        pathsServer.add(i, filepath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return pathsServer;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(List<String> result) {

            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }
            Gson gson = new Gson();
            String images = gson.toJson(pathsServer);
            L.e(images);

            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.addTextImage,
                    MyNetRequestConfig.addTextImage(getActivity()
                            , desc, AppShare.getUserInfo(getActivity()).uid
                            , images),
                    "addTextImage",
                    SharePicFragment.this);

            if (isCancelled()) {
                return;
            }

        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        L.e(netResponse.toString());
        if (type == NetCallBack.TYPE_SUCCESS) {

            getActivity().finish();//此处一定要调用finish()方法
        } else {
            ToastManager.getInstance(getActivity()).showText(netResponse.result);
        }


    }
}
