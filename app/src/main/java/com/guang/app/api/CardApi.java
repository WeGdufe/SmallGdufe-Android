package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.CardBasic;
import com.guang.app.model.CardConsumeItem;
import com.guang.app.model.Electric;
import com.guang.app.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 */
public interface CardApi {

    //饭卡余额
    @GET(AppConfig.Url.getCurrentCash)
    Observable<HttpResult<CardBasic>> getCurrentCash();

    //校园卡当天消费记录，需要提供校园卡卡号，可从getCurrentCash返回中获得
    @GET(AppConfig.Url.cardConsumeToday)
    Observable<HttpResult< List<CardConsumeItem> >> getCardConsumeToday(@Query("cardNum") String cardNum);

    //电控信息
    @GET(AppConfig.Url.getElectric)
    Observable<HttpResult< List<Electric> >> getElectric(@Query("building") String building, @Query("room") String room);

}