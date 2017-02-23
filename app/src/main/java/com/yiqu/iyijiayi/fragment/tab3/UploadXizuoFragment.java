package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetRequest;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.yiqu.Control.Main.RecordActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.db.DownloadMusicInfoDBHelper;
import com.yiqu.iyijiayi.fragment.Tab1Fragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.UploadVoice;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */

public class UploadXizuoFragment extends AbsAllFragment {

    private EditText content;
    private EditText musicName;
    private Button submit;
    private String tag = "UploadXizuoFragment";
    private Music music;
    private String fileUrl;
    private UploadVoice uploadVoice;

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT;
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
//
        setTitleText("上传习作");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.upload_xizuo_fragment;
    }

    @Override
    protected void initView(View v) {
        musicName = (EditText) v.findViewById(R.id.musicname);
        content = (EditText) v.findViewById(R.id.content);
        submit = (Button) v.findViewById(R.id.submit);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getActivity().getIntent();
        uploadVoice = (UploadVoice) intent.getSerializableExtra("uploadVoice");
        fileUrl = Variable.StorageDirectoryPath + uploadVoice.voicename;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           String  musicNameStr = musicName.getText().toString().trim();
           String  contentStr = content.getText().toString().trim();

                if (musicNameStr.length() == 0 ||contentStr.length()==0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请您输入完后再上传");
                    return;
                }
                uploadVoice.musicname = musicNameStr;
                uploadVoice.desc = contentStr;

                final Map<String, String> params = new HashMap<String, String>();
                if (AppShare.getUserInfo(getActivity()).type.equals("1")) {
                    params.put("type", String.valueOf(0));
                } else {
                    params.put("type", String.valueOf(1));
                }

                File file = new File(fileUrl);
                if (file.exists()) {
                    UpLoaderTask upLoaderTask = new UpLoaderTask(MyNetApiConfig.uploadSounds.getPath(), params, file);
                    upLoaderTask.execute();
                }

            }
        });

    }

    private class UpLoaderTask extends AsyncTask<Void, Integer, String> {

        private final String TAG = "UpLoaderTask";
        private Map<String, String> params;
        private File file;
        private String mUrl;

        public UpLoaderTask(String mUrl, Map<String, String> params, File file) {
            super();
            this.params = params;
            this.file = file;
            this.mUrl = mUrl;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

            if (!TextUtils.isEmpty(result)) {

                LogUtils.LOGE(tag, result);
//                JsonObject jsonObject = new JsonObject();
                try {
                    JSONObject jsonObject =new JSONObject(result);
                    String bool = jsonObject.getString("bool");
                    String data = jsonObject.getString("data");
                    String re = jsonObject.getString("result");
                    if (bool .equals("1") ) {

                    String url = new JSONObject(data).getString("filepath");
                    uploadVoice.soundpath = url;

                    RestNetCallHelper.callNet(
                            getActivity(),
                            MyNetApiConfig.addSound,
                            MyNetRequestConfig.addSound(getActivity(),"2", uploadVoice),
                            "addSound", UploadXizuoFragment.this);

                } else {
                    ToastManager.getInstance(getActivity()).showText(re);
                }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

//

            }

            Log.e(TAG, "下载完");


            if (isCancelled()) {


            }
            return;
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (type == NetCallBack.TYPE_SUCCESS){
           ToastManager.getInstance(getActivity()).showText("上传成功");
            getActivity().finish();
        }
//            LogUtils.LOGE(tag, netResponse.toString());

    }
}
