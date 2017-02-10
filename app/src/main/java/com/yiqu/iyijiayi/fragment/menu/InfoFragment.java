package com.yiqu.iyijiayi.fragment.menu;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;


import org.json.JSONException;
import org.json.JSONObject;

public class InfoFragment extends AbsAllFragment {
	EditText name;
	EditText age;
	EditText address;
	TextView sex_man;
	TextView sex_woman;
	
	/**
	 * true:男,false:女
	 */
	boolean flagSex = true;
	//private MenuDialogPicHelper mMenuDialogPicHelper;
	private ImageView head;
	protected String headBase64;
	private ImageLoaderHm<ImageView> mImageLoaderHm;
	@Override
	protected int getTitleView() {
		// TODO Auto-generated method stub
		return R.layout.titlebar_back;
	}

	@Override
	protected int getBodyView() {
		// TODO Auto-generated method stub
		return R.layout.info_fragment;
	}

	@Override
	protected int getTitleBarType() {
		// TODO Auto-generated method stub
		return FLAG_BACK | FLAG_TXT;
	}

	@Override
	protected boolean onPageBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean onPageNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("修改资料");
	}

//	public static String stringFilter(String str)throws PatternSyntaxException{      
//	    // 只允许字母和数字和汉字      
//		String   regEx  =  "[^a-zA-Z0-9]"; 
//	    Pattern   p   =   Pattern.compile(regEx);      
//	    Matcher   m   =   p.matcher(str);      
//	    return   m.replaceAll("").trim();      
//	} 
	
	@Override
	protected void initView(View v) {
		// TODO Auto-generated method stub
		name = (EditText)v.findViewById(R.id.name);
//		name.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				 String editable = name.getText().toString();   
//		         String str = stringFilter(editable.toString()); 
//		         if(!editable.equals(str)){ 
//		        	 name.setText(str); 
//		        	 name.setSelection(str.length()); 
//		         } 
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		age = (EditText)v.findViewById(R.id.age);
		address = (EditText)v.findViewById(R.id.address);
//		sex_man = (TextView)v.findViewById(R.id.sex_man);
//		sex_woman = (TextView)v.findViewById(R.id.sex_woman);
//
//		sex_man.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				Drawable choose_icon , nochoose_icon;
////				Resources res = getResources();
////				choose_icon = res.getDrawable(R.drawable.ic_radio_select);
////				choose_icon.setBounds(0, 0, choose_icon.getMinimumWidth(), choose_icon.getMinimumHeight());
////				nochoose_icon = res.getDrawable(R.drawable.ic_radio_nomal);
////				nochoose_icon.setBounds(0, 0, nochoose_icon.getMinimumWidth(), nochoose_icon.getMinimumHeight());
////				sex_man.setCompoundDrawables(choose_icon, null, null, null);
////				sex_woman.setCompoundDrawables(nochoose_icon, null, null, null);
//
//				flagSex = true;
////				sex_man.setCompoundDrawablePadding(20);
////				sex_woman.setCompoundDrawablePadding(20);
//			}
//		});
//
//		sex_woman.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				Drawable choose_icon , nochoose_icon;
////				Resources res = getResources();
////				choose_icon = res.getDrawable(R.drawable.ic_radio_select);
////				choose_icon.setBounds(0, 0, choose_icon.getMinimumWidth(), choose_icon.getMinimumHeight());
////				nochoose_icon = res.getDrawable(R.drawable.ic_radio_nomal);
////				nochoose_icon.setBounds(0, 0, nochoose_icon.getMinimumWidth(), nochoose_icon.getMinimumHeight());
////				sex_man.setCompoundDrawables(nochoose_icon, null, null, null);
////				sex_woman.setCompoundDrawables(choose_icon, null, null, null);
//
//				flagSex = false;
////				sex_man.setCompoundDrawablePadding(20);
////				sex_woman.setCompoundDrawablePadding(20);
//			}
//		});
		
		v.findViewById(R.id.finish).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String n = name.getText().toString().trim();
				String ag = age.getText().toString().trim();
				String ad = address.getText().toString().trim();
				
				if(n.length() == 0){
					ToastManager.getInstance(getActivity()).showText("昵称输入不能为空");
					return;
				}
				
				
//				if(ag.length() == 0){
//					ToastManager.getInstance(getActivity()).showText("年龄输入不能为空");
//					return;
//				}
//				if(ad.length() == 0){
//					ToastManager.getInstance(getActivity()).showText("地址输入不能为空");
//					return;
//				}
				String s;
				if(flagSex){
					s = "男";
				}else{
					s = "女";
				}
				
//				if("".equals(headBase64)||headBase64 == null||"null".equals(headBase64)){
//					headBase64 = AppShare.getyhtx(getActivity());
//				}
				
//				RestNetCallHelper.callNet(getActivity(),
//						MyNetApiConfig.updateUserInfo,
//						MyNetRequestConfig.updateUserInfo(getActivity()
//								, AppShare.getToken(getActivity())
//								, AppShare.getPhone(getActivity())
//								, headBase64
//								, n
//								, s
//								, ag
//								, ad),
//						"updateUserInfo",
//						InfoFragment.this);
			}
		});
		
//		mMenuDialogPicHelper = new MenuDialogPicHelper(this, new BitmapListener() {
//			@Override
//			public void onBitmapUrl(String url) {
//				// TODO Auto-generated method stub
//				headBase64 = url;
//			}
//		});
		head = (ImageView)v.findViewById(R.id.head);
		v.findViewById(R.id.picLay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	mMenuDialogPicHelper.show(v, head);
			}
		});
		
		mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity());
		
		callNet();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		mMenuDialogPicHelper.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void callNet(){
//		RestNetCallHelper.callNet(getActivity(),
//				MyNetApiConfig.findUserByShjhm,
//				MyNetRequestConfig.findUserByShjhm(getActivity()
//						, AppShare.getToken(getActivity())
//						, AppShare.getPhone(getActivity())),
//				"findUserByShjhm",
//				this);
	}
	
//	@Override
//	public void onNetEnd(String id, int type, NetResponse netResponse) {
//		// TODO Auto-generated method stub
//		if("findUserByShjhm".equals(id)){
//			if(TYPE_SUCCESS == type){
//				try {
//					JSONObject j = new JSONObject( netResponse.body.toString());
//
//					if(j.has("age")){
//						age.setText(""+j.get("age"));
//					}
//					if(j.has("sex")){
//						if("男".equals(""+j.get("sex"))){
//							Drawable choose_icon , nochoose_icon;
//							Resources res = getResources();
//							choose_icon = res.getDrawable(R.drawable.ic_radio_select);
//							choose_icon.setBounds(0, 0, choose_icon.getMinimumWidth(), choose_icon.getMinimumHeight());
//							nochoose_icon = res.getDrawable(R.drawable.ic_radio_nomal);
//							nochoose_icon.setBounds(0, 0, nochoose_icon.getMinimumWidth(), nochoose_icon.getMinimumHeight());
//							sex_man.setCompoundDrawables(choose_icon, null, null, null);
//							sex_woman.setCompoundDrawables(nochoose_icon, null, null, null);
//							flagSex = true;
//						}else{
//							Drawable choose_icon , nochoose_icon;
//							Resources res = getResources();
//							choose_icon = res.getDrawable(R.drawable.ic_radio_select);
//							choose_icon.setBounds(0, 0, choose_icon.getMinimumWidth(), choose_icon.getMinimumHeight());
//							nochoose_icon = res.getDrawable(R.drawable.ic_radio_nomal);
//							nochoose_icon.setBounds(0, 0, nochoose_icon.getMinimumWidth(), nochoose_icon.getMinimumHeight());
//							sex_man.setCompoundDrawables(nochoose_icon, null, null, null);
//							sex_woman.setCompoundDrawables(choose_icon, null, null, null);
//							flagSex = false;
//						}
//					}
//					if(j.has("shjhm")){
//						AppShare.setPhone(getActivity(), ""+j.get("shjhm"));
//					}
//					if(j.has("xxdzh")){
//						address.setText(""+j.get("xxdzh"));
//					}
//					if(j.has("yhtx")){
//						String yhtx = j.getString("yhtx");
//						AppShare.saveyhtx(getActivity(),yhtx);
//						mImageLoaderHm.DisplayImage(yhtx, head);
//					}
//					if(j.has("yhzhch")){
//						AppShare.setName(getActivity(), ""+j.get("yhzhch"));
//						name.setText(""+j.get("yhzhch"));
//					}
//
//					if(j.has("qiniuToken")){
//						AppShare.setQiniuToken(getActivity(), ""+j.get("qiniuToken"));
//					}
//
//					ChatInfoDBHelper mChatInfoDBHelper = new ChatInfoDBHelper(getActivity());
//					String tid = mChatInfoDBHelper.getId(AppShare.getPhone(getActivity()));
//					if(tid == null){
//						ChatInfo cc = new ChatInfo();
//						cc.id = AppShare.getPhone(getActivity());
//						cc.name = AppShare.getName(getActivity());
//						cc.head = AppShare.getyhtx(getActivity());
//						mChatInfoDBHelper.insert(cc );
//					}else{
//						ChatInfo cc = new ChatInfo();
//						cc.id = AppShare.getPhone(getActivity());
//						cc.name = AppShare.getName(getActivity());
//						cc.head = AppShare.getyhtx(getActivity());
//						mChatInfoDBHelper.update(cc, tid);
//					}
//
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//
//			}
//		}else if("updateUserInfo".equals(id)){
//			if(TYPE_SUCCESS == type){
//				try {
//					JSONObject j = new JSONObject( netResponse.body.toString());
//
//					if(j.has("shjhm")){
//						AppShare.setPhone(getActivity(), ""+j.get("shjhm"));
//					}
//
//					if(j.has("yhzhch")){
//						AppShare.setName(getActivity(), ""+j.get("yhzhch"));
//					}
//					if(j.has("yhtx")){
//						String yhtx = j.getString("yhtx");
//						AppShare.saveyhtx(getActivity(), yhtx);
//					}
//
//
//					getActivity().finish();
//					ToastManager.getInstance(getActivity()).showText("修改成功");
//
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//
//
//		super.onNetEnd(id, type, netResponse);
//	}
}
