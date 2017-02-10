package com.fwrestnet.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

/**
 *@comments 模拟数据处理类，与各个接口对接
 */
public class MockData {
	
	/**
	 * @comments 通过文件名获取模拟数据
	 * @param c
	 * @param file 文件名
	 * @return
	 * @version 1.0
	 */
	public static String getMock(Context c, String file){
		String re = "";
		AssetManager a = c.getAssets();
		try {
			InputStream inputStream = a.open("mock/" + file);
			re = readTextFile(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	//通过流得到字符窜
	private static String readTextFile(InputStream inputStream) {
		String s = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			s = outputStream.toString();
			s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);  
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				outputStream.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return s;

	}

}

