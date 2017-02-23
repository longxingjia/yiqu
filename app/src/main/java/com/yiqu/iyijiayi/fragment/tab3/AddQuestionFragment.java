package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.UploadVoice;
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

public class AddQuestionFragment extends AbsAllFragment implements View.OnClickListener {

    private EditText content;
    private EditText musicName;
    private Button submit;
    private String tag = "UploadXizuoFragment";
    private Music music;
    private String fileUrl;
    private UploadVoice uploadVoice;
    private LinearLayout ll_select;
    private static int requestCode =1;
    private TextView tea_name;
    private TextView tea_price;

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
        setTitleText("问题描述");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.add_question_fragment;
    }

    @Override
    protected void initView(View v) {
        musicName = (EditText) v.findViewById(R.id.musicname);
        tea_name = (TextView) v.findViewById(R.id.tea_name);
        tea_price = (TextView) v.findViewById(R.id.tea_price);
        content = (EditText) v.findViewById(R.id.content);
        submit = (Button) v.findViewById(R.id.submit);
        ll_select = (LinearLayout) v.findViewById(R.id.ll_select);
//       v.findViewById(R.id.rl_select).setOnClickListener(this);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getActivity().getIntent();
        uploadVoice = (UploadVoice) intent.getSerializableExtra("uploadVoice");
        fileUrl = Variable.StorageDirectoryPath + uploadVoice.voicename;

//        ll_select.setOnClickListener(this);
//
        ll_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", TeacherListFragment.class.getName());
                getActivity().startActivityForResult(i,requestCode);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           String  musicNameStr = musicName.getText().toString().trim();
           String  contentStr = content.getText().toString().trim();
           String  tea_nameStr = tea_name.getText().toString().trim();

                if (musicNameStr.length() == 0 ||contentStr.length()==0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请您填写您的问题");
                    return;
                }

                if (tea_nameStr.length()==0){
                    ToastManager.getInstance(getActivity()).showText(
                            "请您选择您的导师");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onClick(View v) {

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
                            MyNetRequestConfig.addSound(getActivity(),"1", uploadVoice),
                            "addSound", AddQuestionFragment.this);

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
