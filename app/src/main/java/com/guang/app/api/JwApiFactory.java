package com.guang.app.api;

import com.guang.app.model.Schedule;
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

    //split为1代表拆开连堂课为多个item，默认为0，合并成一个
    public void  getSchedule(String xueqi,int split,Observer< List<Schedule> > sub) {
        service.getSchedule(xueqi,split)
                .map(new HttpResultFunc< List<Schedule> >())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
