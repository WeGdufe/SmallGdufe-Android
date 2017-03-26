package com.guang.app;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import butterknife.ButterKnife;

public class AppContext  extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        LogUtils.configAllowLog = true;
        LitePal.initialize(this);
        //友盟
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //自动更新
        UpdateConfig.getConfig()
                .init(this)
                // 数据更新接口数据，此时默认为使用GET请求
                .url(AppConfig.BASE_URL+AppConfig.Url.updateURL)
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        // 此处根据上面url接口返回的数据response进行update类组装。框架内部会使用此
                        // 组装的update实例判断是否需要更新以做进一步工作
                        LogUtils.e(response);
                        Update update = new Gson().fromJson(response,Update.class);
                        return update;
                    }
                });
    }

}
