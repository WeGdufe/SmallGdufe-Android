package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.AppTips;
import com.guang.app.model.HttpResult;
import com.guang.app.model.StrObjectResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface WorkApi {
    //反馈
    @GET(AppConfig.Url.feedback)
    Observable<HttpResult<StrObjectResponse>> submitFeedback(
            @Query("contact") String contact, @Query("content") String content,
            @Query("devBrand") String deviceBrand, @Query("devModel") String deviceModel,
            @Query("osVersion") String osVersion
    );

    //获取头像（根据名字的第一个字符生成）
    @GET(AppConfig.Avatar_URL)
    Observable<ResponseBody> getAvatarIcon(@Query("char") String nickname, @Query("size") int size, @Query("cache") int cache);

    //获取每日提醒
    @GET(AppConfig.Url.appTips)
    Observable<HttpResult<AppTips>> getAppTips();

    //服务器版退出登陆
    @GET(AppConfig.Url.allLogout)
    Observable<HttpResult<StrObjectResponse>> allLgout();

    @GET(AppConfig.Url.getDocument)
    Observable<ResponseBody> getDocumentFile(@Query("fileCode") String fileCode);


    //提速账号登陆web版drcom
    @FormUrlEncoded
    @POST(AppConfig.Drcom.DrcomWebLogin)
    Observable<ResponseBody> loginDrcomWeb(@Field("DDDDD") String username, @Field("upass") String password, @Field("0MKKey") String key, @Field("Submit") String button);

    @GET(AppConfig.Drcom.DrcomWebLogout)
    Observable<ResponseBody>logoutDrcomWeb();

}