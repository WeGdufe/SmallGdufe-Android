package com.guang.app.api;

import com.guang.app.model.FewSztz;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoguang on 2017/2/18.
 */
public class InfoApiFactory extends ApiUtils {

    private InfoApiFactory() {
        super();
    }

    public static InfoApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final InfoApiFactory INSTANCE = new InfoApiFactory();
    }

    public void  getFewSztz(Observer<List<FewSztz>> sub ) {
        ApiUtils.api.create(InfoApi.class).getFewSztz()
                .map(new HttpResultFunc<List<FewSztz>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
