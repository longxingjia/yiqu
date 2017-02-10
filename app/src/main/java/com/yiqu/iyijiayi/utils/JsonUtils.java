package com.yiqu.iyijiayi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

public class JsonUtils {
	AssetManager assetManager;
	public JsonUtils(Context context){
		assetManager = context.getAssets();
				
	}
	
	public String getJson(String name){
		try {
			InputStream is = assetManager.open(name);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer stringBuffer = new StringBuffer();
			String str = null;
			while((str = br.readLine())!=null){
				stringBuffer.append(str);
			}
			is.close();

			return stringBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}


}
