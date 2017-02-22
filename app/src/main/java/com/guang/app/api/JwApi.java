package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.HttpResult;
import com.guang.app.model.Schedule;
import com.guang.app.model.Score;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 */
public interface JwApi {

    @POST(AppConfig.Url.getScore)
    Observable<HttpResult< List<Score> >> getScore();

    @POST(AppConfig.Url.getSchedule)
    Observable<HttpResult< List<Schedule> >> getSchedule(@Query("stu_time") String stu_time,@Query("split") int split);
    //split为1代表拆开连堂课为多个item，默认为0，合并成一个

}