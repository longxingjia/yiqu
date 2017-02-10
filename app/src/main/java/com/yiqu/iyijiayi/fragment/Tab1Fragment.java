package com.yiqu.iyijiayi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.jauker.widget.BadgeView;
import com.yiqu.iyijiayi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab1Fragment extends TabContentFragment implements OnClickListener {

	private static final int TAB_1 = 1;
//	private ArrayList<BookStyle> style = new ArrayList<BookStyle>();
	private BadgeView imageBadgeView;

	ImageView btn_home_one;
	ImageView btn_home_two;
	ImageView btn_home_three;
	ImageView btn_home_four;
	ImageView btn_home_five;
	ImageView btn_home_six;
	TextView btn_home_one_text;
	TextView btn_home_two_text;
	TextView btn_home_three_text;
	TextView btn_home_four_text;
	TextView btn_home_five_text;
	TextView btn_home_six_text;

	@Override
	protected int getTitleView() {
		// TODO Auto-generated method stub

		return R.layout.titlebar_tab1;
	}

	@Override
	protected int getBodyView() {
		// TODO Auto-generated method stub
		return R.layout.tab1_fragment;
	}

	@Override
	protected void initView(View v) {
		// changeDot(false);
		// TODO Auto-generated method stub
		btn_home_one = (ImageView) v.findViewById(R.id.btn_home_one);
		btn_home_two = (ImageView) v.findViewById(R.id.btn_home_two);
		btn_home_three = (ImageView) v.findViewById(R.id.btn_home_three);
		btn_home_four = (ImageView) v.findViewById(R.id.btn_home_four);
		btn_home_five = (ImageView) v.findViewById(R.id.btn_home_five);
		btn_home_six = (ImageView) v.findViewById(R.id.btn_home_six);

		btn_home_one_text = (TextView) v.findViewById(R.id.btn_home_one_text);
		btn_home_two_text = (TextView) v.findViewById(R.id.btn_home_two_text);
		btn_home_three_text = (TextView) v
				.findViewById(R.id.btn_home_three_text);
		btn_home_four_text = (TextView) v.findViewById(R.id.btn_home_four_text);
		btn_home_five_text = (TextView) v.findViewById(R.id.btn_home_five_text);
		btn_home_six_text = (TextView) v.findViewById(R.id.btn_home_six_text);

		v.findViewById(R.id.btn_home_one).setOnClickListener(this);
		v.findViewById(R.id.btn_home_two).setOnClickListener(this);
		v.findViewById(R.id.btn_home_three).setOnClickListener(this);
		v.findViewById(R.id.btn_home_four).setOnClickListener(this);
		v.findViewById(R.id.btn_home_five).setOnClickListener(this);
		v.findViewById(R.id.btn_home_six).setOnClickListener(this);
		v.findViewById(R.id.btn_tab_1).setOnClickListener(this);

	}

	@Override
	protected boolean isTouchMaskForNetting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		setSlidingMenuEnable(true);
//		initADDRedDotBroadcast();
//		initCancelRedDotBroadcast();
//		imageBadgeView = new BadgeView(getActivity());
//
//		if (!AppUpdateShare.getIsClick(getActivity())) {
//			if (menuOrBack != null) {
//
//				imageBadgeView.setText("");
//				imageBadgeView
//						.setBackgroundResource(R.drawable.pic_menu_reddot);
//				imageBadgeView.setBadgeMargin(0, 2, 2, 0);
//				imageBadgeView.setTargetView(menuOrBack);
//
//			}
//		}
//
//		RestNetCallHelper.callNet(
//				getActivity(),
//				MyNetApiConfig.coursethemeList,
//				MyNetRequestConfig.coursethemeList(getActivity(),
//						AppShare.getToken(getActivity()),
//						AppShare.getPhone(getActivity()), "01"),
//				"coursethemeList", this);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected int getTitleBarType() {
		// TODO Auto-generated method stub
		return FLAG_TXT | FLAG_BACK;
	}

	@Override
	protected boolean onPageBack() {
		// TODO Auto-generated method stub
		if (mOnFragmentListener != null) {
			mOnFragmentListener.onFragmentBack(this);
		}
		return true;
	}

	@Override
	protected boolean onPageNext() {
		// TODO Auto-generated method stub
		pageNextComplete();
		return true;
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
	//	setTitleText(getString(R.string.label_tab1_title));
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		if (arg0.getId() == R.id.btn_home_one) {
//			BookStyle b = getId("01");
//			if (b == null) {
//				ToastManager.getInstance(getActivity()).showText("请重新获取当前页面数据");
//				return;
//			}
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			// in.putExtra("fragment", KeChenListFragment.class.getName());
//			in.putExtra("fragment", KeChenViewPagerFragment.class.getName());
//			in.putExtra("shfmf", b.shfmf);
//			in.putExtra("ztbh", b.ztbh);
//			in.putExtra("ztmch", b.ztmch);
//			getActivity().startActivity(in);
//		} else if (arg0.getId() == R.id.btn_home_two) {
//			BookStyle b = getId("02");
//			if (b == null) {
//				ToastManager.getInstance(getActivity()).showText("请重新获取当前页面数据");
//				return;
//			}
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", KeChenViewPagerFragment.class.getName());
//			in.putExtra("shfmf", b.shfmf);
//			in.putExtra("ztbh", b.ztbh);
//			in.putExtra("ztmch", b.ztmch);
//
//			getActivity().startActivity(in);
//		} else if (arg0.getId() == R.id.btn_home_three) {
//			BookStyle b = getId("03");
//			if (b == null) {
//				ToastManager.getInstance(getActivity()).showText("请重新获取当前页面数据");
//				return;
//			}
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", KeChenViewPagerFragment.class.getName());
//			in.putExtra("shfmf", b.shfmf);
//			in.putExtra("ztbh", b.ztbh);
//			in.putExtra("ztmch", b.ztmch);
//			getActivity().startActivity(in);
//		} else if (arg0.getId() == R.id.btn_home_four) {
//			BookStyle b = getId("04");
//			if (b == null) {
//				ToastManager.getInstance(getActivity()).showText("请重新获取当前页面数据");
//				return;
//			}
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", KeChenViewPagerFragment.class.getName());
//			in.putExtra("shfmf", b.shfmf);
//			in.putExtra("ztbh", b.ztbh);
//			in.putExtra("ztmch", b.ztmch);
//			getActivity().startActivity(in);
//		} else if (arg0.getId() == R.id.btn_home_five) {
//			BookStyle b = getId("05");
//			if (b == null) {
//				ToastManager.getInstance(getActivity()).showText("请重新获取当前页面数据");
//				return;
//			}
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", KeChenViewPagerFragment.class.getName());
//			in.putExtra("shfmf", b.shfmf);
//			in.putExtra("ztbh", b.ztbh);
//			in.putExtra("ztmch", b.ztmch);
//			getActivity().startActivity(in);
//		} else if (arg0.getId() == R.id.btn_home_six) {
//			BookStyle b = getId("06");
//			if (b == null) {
//				ToastManager.getInstance(getActivity()).showText("请重新获取当前页面数据");
//				return;
//			}
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", KeChenViewPagerFragment.class.getName());
//			in.putExtra("shfmf", b.shfmf);
//			in.putExtra("ztbh", b.ztbh);
//			in.putExtra("ztmch", b.ztmch);
//			getActivity().startActivity(in);
//		} else if (arg0.getId() == R.id.btn_tab_1) {
//			Intent in = new Intent(getActivity(), StubActivity.class);
//			in.putExtra("fragment", ProtectFragment.class.getName());
//			getActivity().startActivity(in);
//		}
	}

//	public BookStyle getId(String name) {
//		for (int i = 0; i < style.size(); i++) {
//			if (name.equals(style.get(i).ztbh)) {
//				return style.get(i);
//			}
//		}
//		return null;
//	}

	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		// TODO Auto-generated method stub
//		if ("coursethemeList".equals(id)) {
//			if (TYPE_SUCCESS == type) {
//				try {
//					JSONArray j = new JSONArray(netResponse.body.toString());
//
//					for (int i = 0; i < j.length(); i++) {
//						JSONObject k = new JSONObject(j.get(i).toString());
//						BookStyle mBookStyle = new BookStyle();
//						mBookStyle.shfmf = "" + k.get("shfmf");
//						mBookStyle.ztbh = "" + k.get("ztbh");
//						mBookStyle.ztmch = "" + k.get("ztmch");
//						style.add(mBookStyle);
//
//					}
//
//					for (int i = 0; i < style.size(); i++) {
//						if ("01".equals(style.get(i).ztbh)) {
//							btn_home_one.setImageDrawable(getResources()
//									.getDrawable(R.drawable.p1));
//							btn_home_one_text.setText(style.get(i).ztmch);
//						}
//						if ("02".equals(style.get(i).ztbh)) {
//							btn_home_two.setImageDrawable(getResources()
//									.getDrawable(R.drawable.p6));
//							btn_home_two_text.setText(style.get(i).ztmch);
//						}
//						if ("03".equals(style.get(i).ztbh)) {
//							btn_home_three.setImageDrawable(getResources()
//									.getDrawable(R.drawable.p2));
//							btn_home_three_text.setText(style.get(i).ztmch);
//						}
//						if ("04".equals(style.get(i).ztbh)) {
//							btn_home_four.setImageDrawable(getResources()
//									.getDrawable(R.drawable.p3));
//							btn_home_four_text.setText(style.get(i).ztmch);
//						}
//						if ("05".equals(style.get(i).ztbh)) {
//							btn_home_five.setImageDrawable(getResources()
//									.getDrawable(R.drawable.p4));
//							btn_home_five_text.setText(style.get(i).ztmch);
//						}
//						if ("06".equals(style.get(i).ztbh)) {
//							btn_home_six.setImageDrawable(getResources()
//									.getDrawable(R.drawable.p5));
//							btn_home_six_text.setText(style.get(i).ztmch);
//						}
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//			}

//		}

		super.onNetEnd(id, type, netResponse);
	}
	




}
