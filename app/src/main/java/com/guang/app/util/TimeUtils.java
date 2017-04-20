package com.guang.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * 两个日期所在周中间差了几周（上周五所在周 和 下周一所在周 差一周）
     * @param  weekOld String
     * @param  weekNew String
     * @return long
     */
    public static long weekBetweenTwoDateString(String weekOld, String weekNew,
                                                String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date1 = formatter.parse(weekOld);
            Date date2 = formatter.parse(weekNew);
            long mondayDiff = convertToMonday(date2) - convertToMonday(date1);
            long weeks = mondayDiff / (7 * 24 * 60 * 60 * 1000);
            return weeks;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将日期转为当周的星期一，返回getTime()的long值
     * @param time Date
     * @return long
     * @throws ParseException
     */
    private static long convertToMonday(Date time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = sdf.format(cal.getTime());	//所在周星期一的日期
        return sdf.parse(imptimeBegin).getTime();
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
