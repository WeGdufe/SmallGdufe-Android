package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.FewSztz;
import com.guang.app.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 信息门户API
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface InfoApi {
    //获取信息门户处的素拓信息
    @GET(AppConfig.Url.fewSztz)
    Observable<HttpResult< List<FewSztz> >> getFewSztz();


}