package com.yiqu.iyijiayi.fragment.tab5;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.ImagePagerActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.MenuDialogPicHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogSexHelper;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Administrator on 2017/2/15.
 */

public class InfoFragment extends AbsAllFragment implements View.OnClickListener {

    private RelativeLayout rl_edit_name;
    private RelativeLayout rl_edit_title;
    private RelativeLayout rl_edit_sex;
    private RelativeLayout rl_edit_school;
    private RelativeLayout rl_edit_background;
    private RelativeLayout rl_edit_photo;
    private RelativeLayout rl_edit_introduction;
    private RelativeLayout rl_edit_apply;
    private TextView edit_name;
    private TextView edit_title;
    private TextView edit_sex;
    private TextView edit_school;
    private TextView edit_introduction;
    private RelativeLayout rl_edit_head;
    private UserInfo userInfo;
    private Intent in;
    private CircleImageView edit_head;
    private MenuDialogPicHelper menuDialogPicHelper;
    private String tag = "InfoFragment";
    private ImageView edit_background;
    private TextView edit_price;
    private RelativeLayout rl_edit_price;

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
        setTitleText("修改资料");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_info_fragment;
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人信息");
        JAnalyticsInterface.onPageEnd(getActivity(), "个人信息");
    }

    @Override
    protected void initView(View v) {
        rl_edit_head = (RelativeLayout) v.findViewById(R.id.rl_edit_head);
        rl_edit_name = (RelativeLayout) v.findViewById(R.id.rl_edit_name);
        rl_edit_title = (RelativeLayout) v.findViewById(R.id.rl_edit_title);
        rl_edit_sex = (RelativeLayout) v.findViewById(R.id.rl_edit_sex);
        rl_edit_school = (RelativeLayout) v.findViewById(R.id.rl_edit_school);
        rl_edit_background = (RelativeLayout) v.findViewById(R.id.rl_edit_background);
        rl_edit_photo = (RelativeLayout) v.findViewById(R.id.rl_edit_photo);
        rl_edit_introduction = (RelativeLayout) v.findViewById(R.id.rl_edit_introduction);
        rl_edit_apply = (RelativeLayout) v.findViewById(R.id.rl_edit_apply);
        rl_edit_price = (RelativeLayout) v.findViewById(R.id.rl_edit_price);
        edit_background = (ImageView) v.findViewById(R.id.edit_background);
        edit_head = (CircleImageView) v.findViewById(R.id.edit_head);
        edit_name = (TextView) v.findViewById(R.id.edit_name);
        edit_title = (TextView) v.findViewById(R.id.edit_title);
        edit_sex = (TextView) v.findViewById(R.id.edit_sex);
        edit_price = (TextView) v.findViewById(R.id.edit_price);
        edit_school = (TextView) v.findViewById(R.id.edit_school);
        edit_introduction = (TextView) v.findViewById(R.id.edit_introduction);

        in = new Intent(getActivity(), StubActivity.class);
        rl_edit_head.setOnClickListener(this);
        rl_edit_name.setOnClickListener(this);
        rl_edit_title.setOnClickListener(this);
        rl_edit_sex.setOnClickListener(this);
        rl_edit_school.setOnClickListener(this);
        rl_edit_background.setOnClickListener(this);
        rl_edit_photo.setOnClickListener(this);
        rl_edit_introduction.setOnClickListener(this);
        rl_edit_apply.setOnClickListener(this);
        rl_edit_price.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人信息"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), "个人信息");

        if (AppShare.getIsLogin(getActivity())) {
            userInfo = AppShare.getUserInfo(getActivity());
        } else {
            Model.startNextAct(getActivity(),
                    SelectLoginFragment.class.getName());
            return;
        }

        edit_name.setText(userInfo.username);
        edit_introduction.setText(userInfo.desc);
        edit_school.setText(userInfo.school);

        if (userInfo.sex.equals("1")) {
            edit_sex.setText("男");
        } else {
            edit_sex.setText("女");
        }

        edit_title.setText(userInfo.specialities);

        if (userInfo.type.equals("2")) {
            edit_price.setText(userInfo.price);
            rl_edit_apply.setVisibility(View.GONE);
        } else {
            rl_edit_price.setVisibility(View.GONE);
        }

        PictureUtils.showPicture(getActivity(), userInfo.userimage, edit_head);
        PictureUtils.showPicture(getActivity(), userInfo.backgroundimage, edit_background);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        menuDialogPicHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit_head:
                PermissionGen.needPermission(this, 100, Manifest.permission.CAMERA);

                break;
            case R.id.rl_edit_name:

                in.putExtra("fragment", EditInfoFragment.class.getName());
                in.putExtra("data", "username");
                getActivity().startActivity(in);

                break;
            case R.id.rl_edit_title:
                in.putExtra("fragment", EditInfoFragment.class.getName());
                in.putExtra("data", "specialities");
                getActivity().startActivity(in);

                break;
            case R.id.rl_edit_sex:
                MenuDialogSexHelper menuDialogSexHelper = new MenuDialogSexHelper(this, new MenuDialogSexHelper.SexListener() {
                    @Override
                    public void onSex(String sex) {
                        RestNetCallHelper.callNet(getActivity(),
                                MyNetApiConfig.editUser, MyNetRequestConfig
                                        .editUser(getActivity(), userInfo.uid, "sex", sex),
                                "editUser", InfoFragment.this);

                    }
                });
                menuDialogSexHelper.show(rl_edit_sex);

                break;
            case R.id.rl_edit_school:

                in.putExtra("fragment", EditInfoFragment.class.getName());
                in.putExtra("data", "school");
                getActivity().startActivity(in);
                break;
            case R.id.rl_edit_background:
                PermissionGen.needPermission(this, 200, Manifest.permission.CAMERA);
                break;
            case R.id.rl_edit_photo:
                final ArrayList<String> imagesList = new ArrayList<String>();
                String url = userInfo.userimage;
                imagesList.add(url);
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(rl_edit_photo.getMeasuredWidth(), rl_edit_photo.getMeasuredHeight());
                ImagePagerActivity.startImagePagerActivity((getActivity()), imagesList, 0, imageSize);

                break;
            case R.id.rl_edit_price:
                in.putExtra("fragment", EditInfoFragment.class.getName());
                in.putExtra("data", "price");
                getActivity().startActivity(in);
                break;
            case R.id.rl_edit_introduction:
                in.putExtra("fragment", EditInfoFragment.class.getName());
                in.putExtra("data", "desc");
                getActivity().startActivity(in);

                break;
            case R.id.rl_edit_apply:
                in.putExtra("fragment", ApplyTeacherFragment.class.getName());

                getActivity().startActivity(in);

                break;
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        if (id.equals("editUser")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                //   LogUtils.LOGE(tag,netResponse.result);
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(netResponse.data, UserInfo.class);
                AppShare.setUserInfo(getActivity(), userInfo);
                if (userInfo.sex.equals("1")) {
                    edit_sex.setText("男");
                } else {
                    edit_sex.setText("女");
                }
            } else if (type == NetCallBack.TYPE_ERROR) {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }
        } else if (id.equals("setPhoto")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                ToastManager.getInstance(getActivity()).showText(getString(R.string.net_success));
            } else if (type == NetCallBack.TYPE_ERROR) {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }
        }

        super.onNetEnd(id, type, netResponse);
    }

    @PermissionSuccess(requestCode = 100)
    public void openCamere() {
        menuDialogPicHelper = new MenuDialogPicHelper(this, userInfo.uid, "修改头像", MyNetApiConfig.editUser.getPath(), new MenuDialogPicHelper.BitmapListener() {
            @Override
            public void onBitmapUrl(String url) {


            }
        });
        menuDialogPicHelper.show(edit_head, edit_head);
    }

    @PermissionFail(requestCode = 100)
    public void failCamere() {
        Toast.makeText(getActivity(), getResources().getString(R.string.permission_white_external_hint), Toast.LENGTH_SHORT).show();
        PermissionUtils.openSettingActivity(getActivity());
    }

    @PermissionSuccess(requestCode = 200)
    public void openCamere2() {
        menuDialogPicHelper = new MenuDialogPicHelper(this, userInfo.uid, "修改背景", MyNetApiConfig.uploadSounds.getPath(), new MenuDialogPicHelper.BitmapListener() {
            @Override
            public void onBitmapUrl(String url) {

                LogUtils.LOGE(tag, url);
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.setPhoto, MyNetRequestConfig
                                .setPhoto(getActivity(), userInfo.uid, url, "backgroundimage"),
                        "setPhoto", InfoFragment.this);


            }
        });
        menuDialogPicHelper.show(edit_head, edit_background);
    }


    @PermissionFail(requestCode = 200)
    public void failCamere2() {
        Toast.makeText(getActivity(), getResources().getString(R.string.permission_white_external_hint), Toast.LENGTH_SHORT).show();
        PermissionUtils.openSettingActivity(getActivity());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
