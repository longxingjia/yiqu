package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    private ComposeVoice composeVoice;

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
        composeVoice = (ComposeVoice) intent.getSerializableExtra("composeVoice");
        fileUrl = Variable.StorageDirectoryPath + composeVoice.voicename;


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String musicNameStr = musicName.getText().toString().trim();
                String contentStr = content.getText().toString().trim();

                if (musicNameStr.length() == 0 || contentStr.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请您输入完后再上传");
                    return;
                }
                composeVoice.musicname = musicNameStr;
                composeVoice.desc = contentStr;

                final Map<String, String> params = new HashMap<String, String>();
                if (AppShare.getUserInfo(getActivity()).type.equals("1")) { //1是学生
                    params.put("type", String.valueOf(0));
                } else {
                    params.put("type", String.valueOf(1));
                }
                params.put("isfree", String.valueOf(0));

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
                    JSONObject jsonObject = new JSONObject(result);
                    String bool = jsonObject.getString("bool");
                    String data = jsonObject.getString("data");
                    String re = jsonObject.getString("result");
                    if (bool.equals("1")) {

                        String url = new JSONObject(data).getString("filepath");
                        composeVoice.soundpath = url;
                        LogUtils.LOGE(tag,composeVoice.toString());

                        RestNetCallHelper.callNet(
                                getActivity(),
                                MyNetApiConfig.addSound,
                                MyNetRequestConfig.addSound(getActivity(), "2", composeVoice),
                                "addSound", UploadXizuoFragment.this);

                    } else {
                        ToastManager.getInstance(getActivity()).showText(re);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//

            }

            if (isCancelled()) {


            }
            return;
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        LogUtils.LOGE(tag,netResponse.toString());
        if (type == NetCallBack.TYPE_SUCCESS) {
            ToastManager.getInstance(getActivity()).showText("上传成功");
            getActivity().finish();
        }
//            LogUtils.LOGE(tag, netResponse.toString());

    }
}
