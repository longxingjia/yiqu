package com.yiqu.iyijiayi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.fwrestnet.NetResponse;
import com.jauker.widget.BadgeView;
import com.ui.views.LoadMoreView;
import com.ui.views.LoadMoreView.OnMoreListener;
import com.ui.views.RefreshList;
import com.ui.views.RefreshList.IRefreshListViewListener;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import java.util.ArrayList;

public class Tab3Fragment extends TabContentFragment implements OnMoreListener,IRefreshListViewListener {
	

	private ImageLoaderHm<ImageView> mImageLoaderHm;
//	Tab3Adapter mTab3Adapter;
	
	//分页
	private LoadMoreView mLoadMoreView;
	private int page = 1;
	private int PAGE_SIZE = 10;
	
	//刷新
	private RefreshList listView;
	@Override
	protected int getTitleView() {
		// TODO Auto-generated method stub
		return R.layout.titlebar_tab1;
	}

	@Override
	protected int getBodyView() {
		// TODO Auto-generated method stub
		return R.layout.tab3_fragment;
	}
	
	@Override
	protected void initView(View v) {
		// TODO Auto-generated method stub
//		listView = (RefreshList) v.findViewById(R.id.listView);
//		mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(),300);
//		mTab3Adapter = new Tab3Adapter(getActivity(), mImageLoaderHm);
//		listView.setAdapter(mTab3Adapter);
//		listView.setOnItemClickListener(mTab3Adapter);
//		listView.setRefreshListListener(this);
//
//		mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
//		mLoadMoreView.setOnMoreListener(this);
//		listView.addFooterView(mLoadMoreView);
//		listView.setOnScrollListener(mLoadMoreView);
//		mLoadMoreView.end();
//		mLoadMoreView.setMoreAble(false);
		
	
	}
	
	@Override
	protected boolean isTouchMaskForNetting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
//		setSlidingMenuEnable(true);
//		if(!AppUpdateShare.getIsClick(getActivity())){
//			if (menuOrBack != null) {
//				imageBadgeView = new BadgeView(getActivity());
//				imageBadgeView.setText("");
//				imageBadgeView
//						.setBackgroundResource(R.drawable.pic_menu_reddot);
//				imageBadgeView.setBadgeMargin(0,2,2,0);
//				imageBadgeView.setTargetView(menuOrBack);
//				initCancelRedDotBroadcast();
//			}
//		}
//
//		RestNetCallHelper.callNet(getActivity(),
//				MyNetApiConfig.findAllBbxdxx,
//				MyNetRequestConfig.findAllBbxdxx(getActivity()
//						, AppShare.getToken(getActivity())
//						,  AppShare.getPhone(getActivity())
//						,""+page
//						,""+PAGE_SIZE),
//				"findAllBbxdxx",
//				this);
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mImageLoaderHm.stop();

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
		if(mOnFragmentListener != null){
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
		setTitleText(getString(R.string.label_tab3));
	}

	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		// TODO Auto-generated method stub
//		if("findAllBbxdxx".equals(id)){
//			if(TYPE_SUCCESS == type){
//				ArrayList<FindAllBbxdxx>  list = (ArrayList<FindAllBbxdxx>) netResponse.body;
//				mTab3Adapter.setData(list);
//
//				if(list.size()==PAGE_SIZE){
//					mLoadMoreView.setMoreAble(true);
//				}
//
//				resfreshOk();
//			}else{
//				resfreshFail();
//			}
//		}else if("findAllBbxdxx_more".equals(id)){
//			if(TYPE_SUCCESS == type){
//				// TODO Auto-generated method stub
//
//				ArrayList<FindAllBbxdxx>  list = (ArrayList<FindAllBbxdxx>) netResponse.body;
//				mTab3Adapter.addData(list);
//				mLoadMoreView.end();
//
//				if(list.size() < PAGE_SIZE){
//					mLoadMoreView.setMoreAble(false);
//				}
//			}
//		}
		super.onNetEnd(id, type, netResponse);
	}

	@Override
	public boolean onMore(AbsListView view) {
		// TODO Auto-generated method stub
//		if (mLoadMoreView.getMoreAble()) {
//			if (mLoadMoreView.isloading()) {
//				// 正在加载中
//			} else {
//				mLoadMoreView.loading();
//				page++;
//				RestNetCallHelper.callNet(getActivity(),
//						MyNetApiConfig.findAllBbxdxx,
//						MyNetRequestConfig.findAllBbxdxx(getActivity()
//								, AppShare.getToken(getActivity())
//								,  AppShare.getPhone(getActivity())
//								,""+page
//								,""+PAGE_SIZE),
//						"findAllBbxdxx_more",
//						this,false,true);
//
//			}
//		}
//
		return false;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
//		mLoadMoreView.end();
//		mLoadMoreView.setMoreAble(false);
//		page = 1;
//
//		RestNetCallHelper.callNet(getActivity(),
//				MyNetApiConfig.findAllBbxdxx,
//				MyNetRequestConfig.findAllBbxdxx(getActivity()
//						, AppShare.getToken(getActivity())
//						,  AppShare.getPhone(getActivity())
//						,""+page
//						,""+PAGE_SIZE),
//				"findAllBbxdxx",
//				this,false,true);
	}
	
	public void resfreshOk(){
		listView.refreshOk();
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				listView.stopRefresh();
			}

			
		}.execute();
		
	}
	
	public void resfreshFail(){
		listView.refreshFail();
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				listView.stopRefresh();
			}
	
			
		}.execute();
	}
	

	
	
}
