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
//    @GET("index.php?r=jw/get-grade?sno={sno}&pwd={pwd}")
//    @POST("sutuo.php")
//    @POST("index.php?r=info/test.php")
    @GET(AppConfig.Url.feedback)
    Observable<HttpResult< Object >> submitFeedback(@Query("contact") String contact, @Query("content") String content);

//    Call<HttpResult<List<FewSztz>>> getFewSztz();
//    Call<HttpResult<Score>> getScore(@Path("sno") String sno, @Path("pwd") String pwd);
//    @POST("echo.php")
//    Observable<String> testEcho();

}