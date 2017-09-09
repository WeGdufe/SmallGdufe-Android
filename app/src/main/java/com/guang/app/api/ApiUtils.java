package com.guang.app.api;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.model.HttpResult;
import com.guang.app.util.BasicParamsInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 单例构造Retrofit对象，处理请求返回的code和msg部分
 * Created by xiaoguang on 2017/2/14.
 */
public class ApiUtils {

    protected static Retrofit.Builder apiBuilder = null;
//    protected static Retrofit.Builder apiBuilder = null;
    private static final int DEFAULT_TIMEOUT = 2;
//    protected String password = AppConfig.idsPwd;

    static{

//        BasicParamsInterceptor basicParamsInterceptor =
//                new BasicParamsInterceptor.Builder()
////                        .addHeaderParam("device_id", DeviceUtils.getDeviceId())
//                        .addParam("sno", AppConfig.sno)
//                        .addParam("pwd",AppConfig.idsPwd)
//                        .build();
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
//                .addInterceptor(basicParamsInterceptor)
//                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        apiBuilder = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
//                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
//                .build();
    }
    public static Retrofit getApi(String password){

        BasicParamsInterceptor basicParamsInterceptor =
                new BasicParamsInterceptor.Builder()
                        .addParam("sno", AppConfig.sno)
                        .addParam("pwd",password)
                        .addParam("from","android")
                        .addParam("app_ver",AppConfig.appVer)
                        .build();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(basicParamsInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit api = apiBuilder
                .client(httpClientBuilder.build())
                .build();
        return  api;
    }
    /**
     * 用来统一处理返回的code,并将HttpResult的Data部分剥离出来返回给subscriber
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public  static class HttpResultFunc<T> implements Function<HttpResult<T>, T> {
        @Override
        public T apply(HttpResult<T> httpResult) throws Exception {
            if (!httpResult.isSuccess()) {  //业务错误到onError里获取
                LogUtils.e("httpres "+httpResult.getMsg());
                throw new ApiException(httpResult.getCode(),httpResult.getMsg());
            }
            return httpResult.getData();
        }
    }
    private static class ApiException extends RuntimeException{
        public int code;
        public String message;

        public ApiException(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        //返回APi的code，因getCode()没法在回调里被调用，故随便找了个父类的方法重写
        @Override
        public String getLocalizedMessage() {
            return getCode()+"";
        }

        public int getCode() {
            return code;
        }
    }
}