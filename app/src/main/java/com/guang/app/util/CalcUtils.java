package com.guang.app.util;

import java.text.DecimalFormat;

/**
 * Created by xiaoguang on 2017/2/15.
 */
public class CalcUtils {
    /**
     * 求score分数对应的绩点，如90分为4.0，返回字符串"4.0"
     * @param score
     * @return
     */
    public static String calcScore2Gpa(int score) {
        if (score < 60) return "0.0";
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(1.0 + (score - 60) * 0.1);
    }
}
