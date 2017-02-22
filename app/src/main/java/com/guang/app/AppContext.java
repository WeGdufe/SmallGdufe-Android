package com.guang.app;

import com.apkfuns.logutils.LogUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import butterknife.ButterKnife;

public class AppContext   extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        LogUtils.configAllowLog = true;
        LitePal.initialize(this);
    }

}
