package com.yiqu.iyijiayi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.base.utils.ToastManager;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab3.RecordInfoFragment;
import com.yiqu.iyijiayi.fragment.tab3.Tab3Activity;
<<<<<<< HEAD
import com.yiqu.iyijiayi.fragment.tab3.TextQuestionFragment;
=======
>>>>>>> 429a4c1dac7c9590b80443b9eb9e50e4abf32725
import com.yiqu.iyijiayi.fragment.tab5.ApplyTeacherFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.utils.AppShare;

public class Tab3Fragment extends TabContentFragment implements View.OnClickListener {
	

	@Override
	protected int getTitleView() {
		return R.layout.titlebar_tab1;
	}

	@Override
	protected int getBodyView() {
		return R.layout.tab3_fragment;
	}
	
	@Override
	protected void initView(View v) {
		v.findViewById(R.id.rl_add1).setOnClickListener(this);
		v.findViewById(R.id.rl_add2).setOnClickListener(this);
		v.findViewById(R.id.rl_add_text).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppShare.getIsLogin(getActivity())){
					Model.startNextAct(getActivity(),
							TextQuestionFragment.class.getName());

				}else {
					Intent i = new Intent(getActivity(), StubActivity.class);
					i.putExtra("fragment", SelectLoginFragment.class.getName());
					getActivity().startActivity(i);
					ToastManager.getInstance(getActivity()).showText("请您登录后在操作");
				}
			}
		});

	}
	
	@Override
	protected boolean isTouchMaskForNetting() {
		return false;
	}

	@Override
	protected void init(Bundle savedInstanceState) {


	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("录制和提问"); //统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("录制和提问");
	}
	

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected int getTitleBarType() {
		return FLAG_TXT ;
	}
	@Override
	protected boolean onPageBack() {
		if(mOnFragmentListener != null){
			mOnFragmentListener.onFragmentBack(this);
		}
		return true;
	}
	@Override
	protected boolean onPageNext() {
		pageNextComplete();
		return true;
	}
	@Override
	protected void initTitle() {
		setTitleText("录制和提问");
	}


	@Override
	public void onClick(View v) {
		if (AppShare.getIsLogin(getActivity())){
			Intent intent = new Intent(getActivity(), Tab3Activity.class);
			startActivity(intent);
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", RecordInfoFragment.class.getName());
//			startActivity(in);


		}else {
			Intent i = new Intent(getActivity(), StubActivity.class);
			i.putExtra("fragment", SelectLoginFragment.class.getName());
			getActivity().startActivity(i);
			ToastManager.getInstance(getActivity()).showText("请您登录后在操作");
		}

	}
}
