package com.guang.app.api;

import com.guang.app.model.XiaoLi;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoguang on 2017/2/18.
 */
public class JwcApiFactory extends ApiUtils {
    private JwcApi service;

    private JwcApiFactory() {
        super();
        service = ApiUtils.api.create(JwcApi.class);
    }

    public static JwcApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final JwcApiFactory INSTANCE = new JwcApiFactory();
    }

    public void getXiaoLi(Observer< XiaoLi > sub ) {
        service.getXiaoLi()
        .map(new HttpResultFunc<XiaoLi>())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(sub);
    }
}
