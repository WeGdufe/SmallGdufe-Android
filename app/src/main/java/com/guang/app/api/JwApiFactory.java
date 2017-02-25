package com.guang.app.api;

import com.guang.app.model.BasicInfo;
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
//    private JwApi service;

    private JwApiFactory() {
        super();
//        service = ApiUtils.getApi().create(JwApi.class);
    }

    public static JwApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final JwApiFactory INSTANCE = new JwApiFactory();
    }

    //stu_time格式：2013-2014-1，或者为空字符串表示查询整个大学的成绩
    public void  getScore(String stu_time,Observer< List<Score> > sub ) {
        ApiUtils.getApi().create(JwApi.class).getScore(stu_time)
        .map(new HttpResultFunc< List<Score> >())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(sub);
    }

    //split为1代表拆开连堂课为多个item，默认为0，合并成一个
    public void  getSchedule(String stu_time,int split,Observer< List<Schedule> > sub) {
        ApiUtils.getApi().create(JwApi.class).getSchedule(stu_time,split)
                .map(new HttpResultFunc< List<Schedule> >())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    //获取个人基本信息
    public void  getBasicInfo(Observer<BasicInfo> sub) {
        ApiUtils.getApi().create(JwApi.class).getBasicInfo()
                .map(new HttpResultFunc< BasicInfo >())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
