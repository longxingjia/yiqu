package com.yiqu.iyijiayi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class String2TimeUtils {
	private StringBuilder mFormatBuilder;
	private Formatter mFormatter;
	
	public String2TimeUtils(){
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder,Locale.getDefault());
		
	}
	
	public String stringForTime(int timeMs){
		int totalSeconds = timeMs/1000;
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds /60) % 60;
		int hours = totalSeconds /3600;
		
		mFormatBuilder.setLength(0);
		
		if(hours >0){
			return mFormatter.format("%d:%02d:%02d", hours,minutes,seconds).toString();
			
		}else{
			return mFormatter.format("%02d:%02d", minutes,seconds).toString();

		}
	}


	public String long2Time(long seconds){

		long minutes = seconds /60;
		long hours = seconds /3600;
		long days = seconds /86400;
		long weeks = seconds /604800;
		long months = seconds /2592000;

		mFormatBuilder.setLength(0);

		if (months>0){
			return mFormatter.format("%d:", months).toString()+"天";
		}else if(weeks >0){
			return mFormatter.format("%d", weeks).toString()+"星期";

		}else if(days >0){
			return mFormatter.format("%d", days).toString()+"天";

		}else if(hours >0){
			return mFormatter.format("%d", hours).toString()+"小时";

		}if(minutes >0){
			return mFormatter.format("%d", minutes).toString()+"分钟";

		}else{
			return mFormatter.format("%d", seconds).toString()+"秒";

		}
	}



	/**
	 * 得到系统当前时间
	 * @return
	 */
	public String getSystemTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return format.format(new Date());

	}
}
