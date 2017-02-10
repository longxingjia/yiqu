package com.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.byron.framework.R;

/**
 *@comments 联系人常用方法整理
 */
public class ContactUtil {
	/**
	 * 
	 * @comments 跳到电话
	 * @param mActivity
	 * @param number
	 * @version 1.0
	 */
	public static void toPhone(Context c,String mobile){
		try{
			if(mobile == null || mobile.trim().length() == 0){
				return;
			}
			Uri uri = Uri.parse("tel:"+mobile);    
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);     				 
			c.startActivity(intent); 
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(c, R.string.fm_start_failure,
				     Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 
	 * @comments 直接拨出
	 * @param mActivity
	 * @param number
	 * @version 1.0
	 */
	public static void toDetail(Context c,String mobile){
		try{
			if(mobile == null || mobile.trim().length() == 0){
				return;
			}
			Uri uri = Uri.parse("tel:"+mobile);    
			Intent intent = new Intent(Intent.ACTION_CALL, uri);     				 
			c.startActivity(intent); 
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(c, R.string.fm_start_failure,
				     Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 
	 * @comments 跳到短信
	 * @param mActivity
	 * @param mobile
	 * @version 1.0
	 */
	public static void toMessage(Context c,String mobile){
		try{
			if(mobile == null || mobile.trim().length() == 0){
				return;
			}
			Uri uri = Uri.parse("smsto:"+mobile);  
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
			c.startActivity(intent); 
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(c, R.string.fm_start_failure,
				     Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 
	 * @comments 跳到email
	 * @param mActivity
	 * @param email
	 * @version 1.0
	 */
	public static void toEmail(Context c,String email){
		try{
			if(email == null || email.trim().length() == 0){
				return;
			}
			Uri uri = Uri.parse("mailto:"+email);  
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
			c.startActivity(intent); 
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(c, R.string.fm_start_failure,
				     Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * @comments 启动浏览器
	 * @param c
	 * @param url
	 * @version 1.0
	 */
	public static void toBrowser(Context c, String url){
		try{
			 Uri uri = Uri.parse(url);  
			 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//			 intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity"); 
			 c.startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(c, R.string.fm_start_failure,
				     Toast.LENGTH_SHORT).show();
		}
	}
}
