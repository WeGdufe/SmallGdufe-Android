package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Cet;
import com.guang.app.model.HttpResult;
import com.guang.app.model.XiaoLi;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 */
public interface JwcApi {

    @POST(AppConfig.Url.getXiaoLi)
    Observable<HttpResult<XiaoLi>> getXiaoLi();

    @POST(AppConfig.Url.getCet)
    Observable<HttpResult<Cet>> getCet(@Query("zkzh") String zkzh, @Query("xm") String name);

}