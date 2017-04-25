package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.SelectArticle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ShowArticalFragment extends AbsAllFragment {
    private static final String TAG = "ShowArticalFragment";


    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.author)
    public TextView author;
    @BindView(R.id.content)
    public TextView content;
    private SelectArticle selectArticle;

    @OnClick(R.id.submit)
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", selectArticle);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                getActivity().finish();//此处一定要调用finish()方法

                break;
            default:
                break;
        }
    }


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.show_artical_fragment;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        Intent intent = getActivity().getIntent();
        selectArticle = (SelectArticle) intent.getSerializableExtra("data");
        title.setText(selectArticle.title);
        author.setText(selectArticle.author);
        content.setText(selectArticle.content);

    }


    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("范文");

    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("范文");


    }

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
        setTitleText("范文");

    }


}
