package com.guang.app.api;

import com.guang.app.model.HttpResult;
import com.guang.app.model.Score;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by xiaoguang on 2017/2/14.
 */
public interface JwApi {
//    @GET("index.php?r=jw/get-grade?sno={sno}&pwd={pwd}")
    @GET("score.php")
    Call<HttpResult< List<Score> >> getScore( );
//    Call<WrapperEntity<Score>> getScore(@Path("sno") String sno, @Path("pwd") String pwd);

}