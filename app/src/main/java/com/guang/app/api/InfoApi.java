package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.FewSztz;
import com.guang.app.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface InfoApi {
//    @GET("index.php?r=jw/get-grade?sno={sno}&pwd={pwd}")
//    @POST("sutuo.php")
//    @POST("index.php?r=info/test.php")
    @POST(AppConfig.Url.fewSztz)
    Observable<HttpResult< List<FewSztz> >> getFewSztz();

//    Call<HttpResult<List<FewSztz>>> getFewSztz();
//    Call<HttpResult<Score>> getScore(@Path("sno") String sno, @Path("pwd") String pwd);
//    @POST("echo.php")
//    Observable<String> testEcho();

}