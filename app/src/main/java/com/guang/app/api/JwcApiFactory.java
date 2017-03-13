package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Cet;
import com.guang.app.model.XiaoLi;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 教务处网站 jwc.gdufe.edu.cn
 * Created by xiaoguang on 2017/2/18.
 */
public class JwcApiFactory extends ApiUtils {

    private JwcApiFactory() {
        super();
    }

    public static JwcApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final JwcApiFactory INSTANCE = new JwcApiFactory();
    }

    public void getXiaoLi(Observer< XiaoLi > sub ) {
        ApiUtils.getApi(AppConfig.jwPwd).create(JwcApi.class).getXiaoLi()
        .map(new HttpResultFunc<XiaoLi>())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(sub);
    }
    public void getCet(String zkzh,String name, Observer<Cet> sub ) {
        ApiUtils.getApi(AppConfig.jwPwd).create(JwcApi.class).getCet(zkzh,name)
        .map(new HttpResultFunc<Cet>())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(sub);
    }
}
