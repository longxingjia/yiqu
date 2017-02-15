package com.yiqu.iyijiayi.fragment.tab5;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.views.CircleImageView;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;

/**
 * Created by Administrator on 2017/2/15.
 */

public class InfoFragment extends AbsAllFragment implements View.OnClickListener{

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

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK|FLAG_TXT;
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
        return R.layout.titlebar_tab1;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_info_fragment;
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

        CircleImageView edit_head = (CircleImageView) v.findViewById(R.id.edit_head);
        edit_name = (TextView) v.findViewById(R.id.edit_name);
        edit_title = (TextView) v.findViewById(R.id.edit_title);
        edit_sex = (TextView) v.findViewById(R.id.edit_sex);
        edit_school = (TextView) v.findViewById(R.id.edit_school);
        edit_introduction = (TextView) v.findViewById(R.id.edit_introduction);

        rl_edit_head.setOnClickListener(this);
        rl_edit_name.setOnClickListener(this);
        rl_edit_title.setOnClickListener(this);
        rl_edit_sex.setOnClickListener(this);
        rl_edit_school.setOnClickListener(this);
        rl_edit_background.setOnClickListener(this);
        rl_edit_photo.setOnClickListener(this);
        rl_edit_introduction.setOnClickListener(this);
        rl_edit_apply.setOnClickListener(this);

    }

    @Override
    protected void init(Bundle savedInstanceState) {


        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_edit_head:
                break;
            case R.id.rl_edit_name:
                break;
            case R.id.rl_edit_title:
                break;
            case R.id.rl_edit_sex:
                break;
            case R.id.rl_edit_school:
                break;
            case R.id.rl_edit_background:
                break;
            case R.id.rl_edit_photo:
                break;
            case R.id.rl_edit_introduction:
                break;
            case R.id.rl_edit_apply:
                break;
        }

    }
}
