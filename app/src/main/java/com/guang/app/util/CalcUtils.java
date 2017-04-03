package com.guang.app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    /**
     * 将base64编码后的图片进行解码回Bitmap
     * @param str
     * @return
     */
    public static Bitmap base64String2Bitmap(String str) {
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(str, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取版本号 1.0.0
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0.1";
        }
    }
}
