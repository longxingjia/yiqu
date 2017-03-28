package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqu.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.UploaderTask;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.Order;
import com.yiqu.iyijiayi.model.PayInfo;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.Wx_arr;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class AddQuestionFragment extends AbsAllFragment implements View.OnClickListener {

    private EditText content;
    private TextView musicName;
    private Button submit;
    private String tag = "AddQuestionFragment";
    private Music music;
    private String fileUrl;
    private ComposeVoice composeVoice;
    private LinearLayout ll_select;
    private static int requestCode = 1;
    private TextView tea_name;
    private TextView tea_price;
    private Teacher teacher;
    private UserInfo userInfo;

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("提问");

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("提问");

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
        musicName = (TextView) v.findViewById(R.id.musicname);
        tea_name = (TextView) v.findViewById(R.id.tea_name);
        tea_price = (TextView) v.findViewById(R.id.tea_price);
        content = (EditText) v.findViewById(R.id.content);
        submit = (Button) v.findViewById(R.id.submit);
        ll_select = (LinearLayout) v.findViewById(R.id.ll_select);
        v.findViewById(R.id.payfor).setOnClickListener(this);
        submit.setOnClickListener(this);

        if (AppShare.getIsLogin(getActivity())) {
            userInfo = AppShare.getUserInfo(getActivity());
            submit.setText("免费提问(剩余" + userInfo.free_question + ")");

        }

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getActivity().getIntent();
        composeVoice = (ComposeVoice) intent.getSerializableExtra("composeVoice");
        fileUrl = Variable.StorageMusicPath + composeVoice.voicename;
        musicName.setText(composeVoice.musicname);


        ll_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", TeacherListFragment.class.getName());
                startActivityForResult(i, requestCode);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                //str即为回传的值
                teacher = (Teacher) b.getSerializable("teacher");
                tea_name.setText(teacher.username);
                tea_price.setText(teacher.price);
                break;
            case Constant.FINISH:
                getActivity().finish();
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        String musicNameStr = musicName.getText().toString().trim();
        String contentStr = content.getText().toString().trim();
        String tea_nameStr = tea_name.getText().toString().trim();

        if (musicNameStr.length() == 0 || contentStr.length() == 0) {
            ToastManager.getInstance(getActivity()).showText(
                    "请您填写您的问题");
            return;
        }

        if (tea_nameStr.length() == 0) {
            ToastManager.getInstance(getActivity()).showText(
                    "请您选择您的导师");
            return;
        }

        composeVoice.questionprice = teacher.price;
        composeVoice.touid = Integer.parseInt(teacher.uid);
        composeVoice.musicname = musicNameStr;
        composeVoice.desc = contentStr;

        final Map<String, String> params = new HashMap<String, String>();

        if (userInfo.type.equals("1")) {
            params.put("type", String.valueOf(0));
        } else {
            params.put("type", String.valueOf(1));
        }

        File file = new File(fileUrl);

        switch (v.getId()) {
            case R.id.payfor:

                if (file.exists()) {
                    UTask upLoaderTask = new UTask(getActivity(),MyNetApiConfig.uploadSounds.getPath(), params, file, "0");
                    upLoaderTask.execute();
                } else {
                    ToastManager.getInstance(getActivity()).showText("文件已经损坏，请您重新录制");
                }
                break;

            case R.id.submit:
                if (file.exists()) {
                    UTask uTask = new UTask(getActivity(),MyNetApiConfig.uploadSounds.getPath(), params, file, "1");
                    uTask.execute();
                } else {
                    ToastManager.getInstance(getActivity()).showText("文件已经损坏，请您重新录制");
                }
                break;
        }

    }

    private class UTask extends UploaderTask {

        private final String TAG = "uTask";

        private String isfree;
        private DialogHelper dialogHelper;


        public UTask(Context context, String RequestURL, Map<String, String> params, File file,String isfree) {
            super(context, RequestURL, params, file);
            this.isfree = isfree;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(getActivity(), this,100);
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
                        MyNetRequestConfig.addSound(getActivity(), userInfo.uid, "1", isfree, composeVoice),
                        "addSound", AddQuestionFragment.this);

            } else{
                ToastManager.getInstance(getActivity()).showText(getString(R.string.net_error));

            }

        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

        if (id.equals("addSound")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {
                    JSONObject jsonObject = new JSONObject(netResponse.data);
                    String sid = jsonObject.getString("sid");

                    RestNetCallHelper.callNet(
                            getActivity(),
                            MyNetApiConfig.getNewOrder,
                            MyNetRequestConfig.getNewOrder(getActivity(), composeVoice.fromuid, sid, teacher.price),
                            "getNewOrder", AddQuestionFragment.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }
        } else if (id.equals("getNewOrder")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag,netResponse.toString());
                try {

                    if (netResponse.data.contains("wx_arr")){
                        PayInfo payInfo = new Gson().fromJson(netResponse.data, PayInfo.class);
                        Order order = payInfo.order;
                        Wx_arr wx_arr = payInfo.wx_arr;

                        Intent i = new Intent(getActivity(), StubActivity.class);
                        i.putExtra("fragment", PayforFragment.class.getName());
                        Bundle b = new Bundle();
                        b.putSerializable("data", payInfo);
                        i.putExtras(b);
                        startActivityForResult(i, requestCode);
                    }else {
                        Order order = new Gson().fromJson(netResponse.data, Order.class);
                        RestNetCallHelper.callNet(
                                getActivity(),
                                MyNetApiConfig.orderQuery,
                                MyNetRequestConfig.orderQuery(getActivity(), order.order_number),
                                "orderQuery", AddQuestionFragment.this);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (id.equals("orderQuery")) {
            LogUtils.LOGE(tag,netResponse.result);
            if (type == NetCallBack.TYPE_SUCCESS) {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
                userInfo.free_question--;
                AppShare.setUserInfo(getActivity(), userInfo);
                getActivity().finish();
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }
        }
    }
}
