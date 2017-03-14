package com.yiqu.iyijiayi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class String2TimeUtils {
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public String2TimeUtils() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    public String stringForTimeS(int timeS) {
        int totalSeconds = timeS;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);

        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();

        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();

        }
    }

    public String stringForTimeMs(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);

        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();

        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();

        }
    }


    public String long2Time(long seconds) {

        long minutes = seconds / 60;
        long hours = seconds / 3600;
        long days = seconds / 86400;
        long weeks = seconds / 604800;
        long months = seconds / 2592000;

        mFormatBuilder.setLength(0);

        if (months > 0) {
            return mFormatter.format("%d", months).toString() + "月前";
        } else if (weeks > 0) {
            return mFormatter.format("%d", weeks).toString() + "星期前";

        } else if (days > 0) {
            return mFormatter.format("%d", days).toString() + "天前";

        } else if (hours > 0) {
            return mFormatter.format("%d", hours).toString() + "小时前";

        }
        if (minutes > 0) {
            return mFormatter.format("%d", minutes).toString() + "分钟前";

        } else {
            return "刚刚";

        }
    }

    public static String longToString(long currentTime,String formatType)
            throws ParseException {

        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {

        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 得到系统当前时间
     *
     * @return
     */
    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(new Date());

    }
}
