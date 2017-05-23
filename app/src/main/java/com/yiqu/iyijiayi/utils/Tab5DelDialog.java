package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Tab5DelDialog {
    public static void showDialog(final Context context, View v, final String uid, final String sid) {

        String title = "提示";
        String[] items = new String[]{"删除"};

        MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(context, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
            @Override
            public void onTea(int tea) {
                switch (tea) {
//                    case 1:
//                        Intent intent = new Intent(instance, StubActivity.class);
//                        intent.putExtra("fragment", AddQuestionFragment.class.getName());
//                        intent.putExtras(bundle);
//                        instance.startActivityForResult(intent, REQUESTUPLOAD);
//                        break;
                    case 0:
//                        Intent i = new Intent(instance, StubActivity.class);
//                        i.putExtra("fragment", UploadXizuoFragment.class.getName());
//                        i.putExtras(bundle);
//                        i.putExtra("eid", eid);
//                        instance.startActivityForResult(i, REQUESTUPLOAD);

                        RestNetCallHelper.callNet(
                                context,
                                MyNetApiConfig.deleteSound,
                                MyNetRequestConfig.deleteSound(context, uid, String.valueOf(sid)),
                                "getSoundList", new NetCallBack() {
                                    @Override
                                    public void onNetNoStart(String id) {

                                    }

                                    @Override
                                    public void onNetStart(String id) {

                                    }

                                    @Override
                                    public void onNetEnd(String id, int type, NetResponse netResponse) {
                                        //  LogUtils.LOGE(tag,netResponse.toString());
                                        L.e(netResponse.toString());
                                        if (type == TYPE_SUCCESS) {


                                        }

                                    }
                                });


                        break;
                }
            }
        });
        menuDialogSelectTeaHelper.show(v);
    }
}
