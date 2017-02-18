package com.guang.app;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;

import butterknife.ButterKnife;

public class AppContext extends Application {

    public static String userName;
    public static String eduSysPassword;
    public static int    server = 4;


    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        LogUtils.configAllowLog = true;
        LogUtils.configTagPrefix = "abc-";
    }

}
