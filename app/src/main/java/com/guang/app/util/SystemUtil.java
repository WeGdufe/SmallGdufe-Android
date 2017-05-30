package com.guang.app.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * http://blog.csdn.net/zhuwentao2150/article/details/51946387
 * Created by xiaoguang on 2017/5/30.
 */
public class SystemUtil {

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要 android.permission.READ_PHONE_STATE 权限) 且对于6.0以上要运行时弹窗拿权限，故不用
     *
     * @return  手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }
}
