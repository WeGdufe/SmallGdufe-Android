package com.guang.app.util.widget;

import java.util.Calendar;

/**
 * 通用工具和常量类
 * Created by xiaoguang on 2017/4/16.
 */
public class WidgetCommonUtil {
    /**
     * 获取今天是星期几，返回int格式，星期一为1，星期五为5，星期天为7
     * @return int
     */
    public static int getTodayInWeek(){
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6}; //转换日期，calendar返回的星期天是第一天，改成星期一为第一天这种
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1 ;
        if(today < 0 ) today = 0;
        return weekDays[today];
    }

    /**
     * 返回明天星期几，星期天的下一天是星期一
     * @param day int
     * @return int
     */
    public static int getNextDay(int day){
        if(day == 7){
            return 1;
        }
        return day + 1;
    }


    /**
     * 返回昨天星期几，星期一的昨天是星期天
     * @param day int
     * @return int
     */
    public static int getPreDay(int day){
        if(day == 1){
            return 7;
        }
        return day - 1;
    }

    //点击事件的action名，需要在 AndroidManifest.xml 注册才能在onReceive()里收到
    public class WidgetConstant{
        public static final String INTENT_NEXTDAY = "com.guang.app.widget.NEXTDAY";
        public static final String INTENT_PREDAY = "com.guang.app.widget.PREDAY";
    }
}
