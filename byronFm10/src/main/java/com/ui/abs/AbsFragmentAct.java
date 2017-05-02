package com.ui.abs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.base.utils.ToastManager;
import com.base.utils.Utils;
import com.byron.framework.R;
import com.service.DownloadService;
import com.service.PlayService;
import com.ui.App;

import org.apache.http.util.LangUtils;

public abstract class AbsFragmentAct extends FragmentActivity {
//public abstract class AbsFragmentAct extends SlidingFragmentActivity {

	protected boolean isActVisibile;
	protected PlayService mPlayService;
	protected DownloadService mDownloadService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(getContentView());
			
			initView();
			init(savedInstanceState);

			bindService(new Intent(this, DownloadService.class), mDownloadServiceConnection,Context.BIND_AUTO_CREATE);
		} catch (Exception e) {
			ToastManager.getInstance(this).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		unbindService(mDownloadServiceConnection);
		super.onDestroy();
	}

	/**
	 * @comments activity是否resume
	 * @return
	 * @version 1.0
	 */
	protected boolean isActVisibile() {
		return isActVisibile;
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
	protected abstract void initView();

	/**
	 * @comments 界面初始化数据的地方
	 * @param savedInstanceState
	 * @version 1.0
	 */
	protected abstract void init(Bundle savedInstanceState);

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (!Utils.isAppOnForeground(this)) {
			App.isActive = false;
		}
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isActVisibile = true;
		if (!App.isActive) {
			App.isActive = true;
			onForeground();
		}
		super.onResume();
	}

	/**
	 * @comments 应用程序从后台切换到前台的回调函数
	 * @version 1.0
	 */
	protected void onForeground() {

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isActVisibile = false;
		super.onPause();
	}

	private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mPlayService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mPlayService = ((PlayService.PlayBinder) service).getService();
			mPlayService.setOnMusicEventListener(mMusicEventListener);
			onChange(mPlayService.getPlayingPosition());
		}
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
	 * 音乐播放服务回调接口的实现类
	 */
	private PlayService.OnMusicEventListener mMusicEventListener =
			new PlayService.OnMusicEventListener() {
				@Override
				public void onPublish(int progress) {
					AbsFragmentAct.this.onPublish(progress);
				}

				@Override
				public void onChange(int position) {
					AbsFragmentAct.this.onChange(position);
				}
			};

	/**
	 * Fragment的view加载完成后回调
	 */
	public void allowBindService() {
		bindService(new Intent(this, PlayService.class), mPlayServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	/**
	 * fragment的view消失后回调
	 */
	public void allowUnbindService() {
		unbindService(mPlayServiceConnection);
	}

	public DownloadService getDownloadService() {
		return mDownloadService;
	}

	/**
	 * 更新进度
	 * @param progress 进度
	 */
	public abstract void onPublish(int progress);
	/**
	 * 切换歌曲
	 * @param position 歌曲在list中的位置
	 */
	public abstract void onChange(int position);

}
