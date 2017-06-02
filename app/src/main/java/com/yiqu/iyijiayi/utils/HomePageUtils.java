package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.utils.L;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;

/**
 * Created by Administrator on 2017/6/1.
 */

public class HomePageUtils {
    public static void initHomepage(final Context mContext, String uid) {
        String mUid = "0";
        if (AppShare.getIsLogin(mContext)) {
            mUid = AppShare.getUserInfo(mContext).uid;
        }
        RestNetCallHelper.callNet(mContext,
                MyNetApiConfig.getUserPage,
                MyNetRequestConfig.getUserPage(mContext
                        , uid, mUid),
                "getUserPage",
                new NetCallBack() {
                    @Override
                    public void onNetNoStart(String id) {

                    }

                    @Override
                    public void onNetStart(String id) {

                    }

                    @Override
                    public void onNetEnd(String id, int type, NetResponse netResponse) {

                        L.e(netResponse.toString());
                        if (TYPE_SUCCESS == type) {
                            Gson gson = new Gson();
                            HomePage homePage = gson.fromJson(netResponse.data, HomePage.class);
                            Intent i = new Intent(mContext, StubActivity.class);
                            i.putExtra("fragment", HomePageFragment.class.getName());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", homePage);
                            i.putExtras(bundle);
                            mContext.startActivity(i);
                        }

                    }
                });
    }
}
