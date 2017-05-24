package com.ui.abs;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.base.utils.ToastManager;
import com.byron.framework.R;
import com.service.DownloadService;
import com.service.PlayService;
import com.utils.L;
import com.utils.LogUtils;

public abstract class AbsFragment extends Fragment {

	private static String tag = "AbsFragment";
	protected PlayService mPlayService;
	protected DownloadService mDownloadService;
	public View menuOrBack;

	protected String getLogTag() {
		return "AbsFragment";
	}

	protected OnFragmentListener mOnFragmentListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tag = getLogTag();
		View v = null;
		try {
		//	allowBindService(getActivity());
			v = inflater.inflate(getContentView(), container, false);
			initView(v);
			init(savedInstanceState);
		} catch (Exception e) {
			ToastManager.getInstance(getActivity()).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
//		Log.i(tag, "onCreateView");
		return v;
	}

	public ServiceConnection mPlayServiceConnection = new  ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mPlayService = null;
		//	LogUtils.LOGE("abs","onServiceDisconnected");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mPlayService = ((PlayService.PlayBinder) service).getService();
			mPlayService.setOnMusicEventListener(mMusicEventListener);
	//		LogUtils.LOGE("abs","onServiceConnected");
			onChange(mPlayService.getPlayingPosition());
		}
	};

	public DownloadService getDownloadService() {
		return mDownloadService;
	}

	/**
	 * 音乐播放服务回调接口的实现类
	 */
	private PlayService.OnMusicEventListener mMusicEventListener =
			new PlayService.OnMusicEventListener() {
				@Override
				public void onPublish(int progress) {
					AbsFragment.this.onPublish(progress);
				}

				@Override
				public void onChange(int position) {
					AbsFragment.this.onChange(position);
				}

				@Override
				public void OnCompletion() {
					AbsFragment.this.OnCompletion();
				}

			};
	/**
	 * Fragment的view加载完成后回调
	 */
	public void allowBindService(Context context) {
		context.bindService(new Intent(context, PlayService.class), mPlayServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	/**
	 * fragment的view消失后回调
	 */
	public void allowUnbindService(Context context) {
		context.unbindService(mPlayServiceConnection);
	}

	/**
	 * 更新进度
	 * @param progress 进度
	 */
	public  void onPublish(int progress){

	};


	/**
	 */
	public  void OnCompletion(){

	};


	/**
	 * 切换歌曲
	 * @param position 歌曲在list中的位置
	 */
	public  void onChange(int position){

	};



	private ServiceConnection mDownloadServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mDownloadService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e("","ssss");
			mDownloadService = ((DownloadService.DownloadBinder) service).getService();
		}
	};

	/**
	 * Fragment的view加载完成后回调
	 */
	public void allowBindDownloadService(Context context) {
		context.bindService(new Intent(context, DownloadService.class), mDownloadServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	/**
	 * fragment的view消失后回调
	 */
	public void allowUnbindDownloadService(Context context) {
		context.unbindService(mDownloadServiceConnection);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
//		Log.i(tag, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
//		Log.i(tag, "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
//		Log.i(tag, "onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
//		Log.i(tag, "onStart");
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Log.i(tag, "onViewCreated");
		if (mOnFragmentListener != null) {
			mOnFragmentListener.onFragmentCreated(null);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
//		Log.i(tag, "getView");
		return super.getView();
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
//		Log.i(tag, "onCreateAnimation");
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Log.i(tag, "onInflate");
		super.onInflate(activity, attrs, savedInstanceState);
	}

	/**
	 * @comments 界面的布局文件
	 * @return
	 * @version 1.0
	 */
	protected abstract int getContentView();

	/**
	 * @comments 界面初始化组件的地方
	 * @version 1.0
	 */
	protected abstract void initView(View v);

	/**
	 * @comments 界面初始化数据的地方
	 * @param savedInstanceState
	 * @version 1.0
	 */
	protected void init(Bundle savedInstanceState) {

	}

	public void setOnFragmentListener(OnFragmentListener l) {
		mOnFragmentListener = l;
	}

	public boolean onBackPressed() {
		return false;
	}

	public void onShow() {
		// TODO Auto-generated method stub
		Log.i(tag, "onShow");
	}


}
