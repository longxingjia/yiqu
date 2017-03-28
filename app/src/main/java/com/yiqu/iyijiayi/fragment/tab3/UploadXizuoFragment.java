package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yiqu.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.UploaderTask;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */

public class UploadXizuoFragment extends AbsAllFragment {

    private EditText content;
    private TextView musicName;
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
        setTitleText("上传作品");
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("上传作品"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("上传作品");
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
        musicName = (TextView) v.findViewById(R.id.musicname);
        content = (EditText) v.findViewById(R.id.content);
        submit = (Button) v.findViewById(R.id.submit);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getActivity().getIntent();
        composeVoice = (ComposeVoice) intent.getSerializableExtra("composeVoice");

        fileUrl = Variable.StorageMusicPath + composeVoice.voicename;

        musicName.setText(composeVoice.musicname);

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

                params.put("type", String.valueOf(1));

                File file = new File(fileUrl);
                if (file.exists()) {
                    UpTask upLoaderTask = new UpTask(getActivity(),MyNetApiConfig.uploadSounds.getPath(), params, file);
                    upLoaderTask.execute();
                } else {
                    ToastManager.getInstance(getActivity()).showText("文件已经损坏，请您重新录制");
                }

            }
        });

    }

    private class UpTask extends UploaderTask {

        private DialogHelper dialogHelper;

        public UpTask(Context context, String RequestURL, Map<String, String> params, File file) {
            super(context, RequestURL, params, file);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(getActivity(), this, 100);
                dialogHelper.showProgressDialog();
            }
        }

        @Override
        protected String doInBackground(Void... p) {
            return super.doInBackground(p);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialogHelper.setProgress(values[0]);
         //   LogUtils.LOGE(tag,values[0]+"");
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }

            if (isCancelled()) {
                return;
            }
            if (!TextUtils.isEmpty(result)) {

                composeVoice.soundpath = result;

                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.addSound,
                        MyNetRequestConfig.addSound(getActivity(), AppShare.getUserInfo(getActivity()).uid, "2", "0", composeVoice),
                        "addSound", UploadXizuoFragment.this);

            } else{
                ToastManager.getInstance(getActivity()).showText(getString(R.string.net_error));

            }

        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (type == NetCallBack.TYPE_SUCCESS) {
            ToastManager.getInstance(getActivity()).showText("上传成功");
            getActivity().finish();
        } else {
            ToastManager.getInstance(getActivity()).showText(netResponse.result);
        }
//            LogUtils.LOGE(tag, netResponse.toString());

    }
}
