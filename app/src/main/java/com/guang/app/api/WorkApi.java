package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface WorkApi {
    //反馈
    @GET(AppConfig.Url.feedback)
    Observable<HttpResult< Object >> submitFeedback(@Query("contact") String contact, @Query("content") String content);

}