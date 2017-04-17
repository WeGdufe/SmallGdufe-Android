package com.guang.app.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关的管理类
 * Created by xiaoguang on 2017/2/15.
 */
public class TimeUtils {
    /**
     * 返回当前时间，以 yyyy-MM-dd HH:mm:ss 的格式
     * @return
     */
    public static String getCurrentDateString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
    /**
     * 返回指定格式的今天时间，传入时间格式，如yyyyMMdd
     * @return
     */
    public static String getDateStringWithFormat(String format) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    /**
     * 比较两个 yyyy-MM-dd HH:mm:ss 格式的时间字符串，如果第一个时间早就返回小于0的数，等则为0，晚则返回大于0的数
     * @param date1 String
     * @param date2 String
     * @return int
     */
    public static int compareDateString(String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = formatter.parse(date1);
            Date dt2 = formatter.parse(date2);
            return dt1.compareTo(dt2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * yyyy-mm-dd 格式字符串转时间戳，秒级别
     * @param timeString
     * @return
     */
    public static long timeString2TimeStamp(String timeString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(timeString).getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
