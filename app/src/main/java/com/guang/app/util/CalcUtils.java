package com.guang.app.util;

import java.text.DecimalFormat;

/**
 * Created by xiaoguang on 2017/2/15.
 */
public class CalcUtils {
    public static String calcScore(int score){
        if(score < 60) return "0.0";
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(1.0+(score-60)*0.1);
    }
}
