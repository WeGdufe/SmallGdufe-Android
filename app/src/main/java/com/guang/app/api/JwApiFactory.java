package com.guang.app.api;

import com.guang.app.model.Score;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoguang on 2017/2/18.
 */
public class JwApiFactory extends ApiUtils {
    private JwApi service;

    private JwApiFactory() {
        super();
        service = ApiUtils.api.create(JwApi.class);
    }

    public static JwApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final JwApiFactory INSTANCE = new JwApiFactory();
    }

    public void  getScore(Observer< List<Score> > sub ) {
        service.getScore()
        .map(new HttpResultFunc< List<Score> >())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(sub);
    }
}
