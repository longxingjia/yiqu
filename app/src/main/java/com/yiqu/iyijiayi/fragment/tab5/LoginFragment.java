package com.yiqu.iyijiayi.fragment.tab5;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;

public class LoginFragment extends AbsAllFragment {

	private EditText username;
	private EditText pass;

	@Override
	protected int getTitleView() {

		return R.layout.titlebar_tab1;
	}

	@Override
	protected int getBodyView() {

		return R.layout.login_fragment;
	}

	@Override
	protected int getTitleBarType() {

		return FLAG_NONE;
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

		setTitleText("登录");
	}

	@Override
	protected void initView(View v) {

		username = (EditText)v.findViewById(R.id.username);
		pass = (EditText)v.findViewById(R.id.pass);
		
		v.findViewById(R.id.btn01).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				login();
				
			}
		});
		v.findViewById(R.id.txt01).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {

					//	Model.startNextAct(getActivity(), ForgetPassFragment.class.getName());
					}
				});
		v.findViewById(R.id.txt02).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

//				Model.startNextAct(getActivity(), RegisterFragment.class.getName());
				Intent i = new Intent(getActivity(), StubActivity.class);
				i.putExtra("fragment", RegisterFragment.class.getName());
				startActivityForResult(i, 888);
			}
		});
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == getActivity().RESULT_OK){
//			if(requestCode == 888){
//				String phone = data.getExtras().getString("phone");
//				String pa = data.getExtras().getString("pass");
//
//				username.setText(phone);
//				pass.setText(pa);
//
//				login();
//			}
			
//		}
	}

	private void login() {
		String a = username.getText().toString().trim();
		String p = pass.getText().toString().trim();
		if(a.length() == 0){
			ToastManager.getInstance(getActivity()).showText("请输入您的用户名");
			return;
		}
		if(p.length() == 0){
			ToastManager.getInstance(getActivity()).showText("请输入您的密码");
			return;
		}

//		RestNetCallHelper.callNet(getActivity(),
//				MyNetApiConfig.login,
//				MyNetRequestConfig.login(getActivity(), a, p),
//				"login",
//				this);
	}

	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		// TODO Auto-generated method stub
		if(TYPE_SUCCESS == type){
//			try {
//			//	JSONObject j = new JSONObject( netResponse.body.toString());
//
//
//
//			} catch (JSONException e) {
//
//				e.printStackTrace();
//			}
			

		}
		
		super.onNetEnd(id, type, netResponse);
	}

    

	
	
	 /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
	
}
