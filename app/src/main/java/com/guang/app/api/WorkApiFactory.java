package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.StrObjectResponse;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by xiaoguang on 2017/2/26.
 */
public class WorkApiFactory extends ApiUtils {

    private WorkApiFactory() {
        super();
    }

    public static WorkApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final WorkApiFactory INSTANCE = new WorkApiFactory();
    }

    public void submitFeedback(String contact,String content,Observer<StrObjectResponse> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(WorkApi.class).submitFeedback(contact,content)
                .map(new HttpResultFunc<StrObjectResponse>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    public void getAvatarIcon(String nickname,Observer<ResponseBody> sub ) {
        int size = 42;          //图片大小，不过在xml里写死了
        int cacheTime = 3600;  //他服务器缓存的，不影响客户端
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.Avator_URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(WorkApi.class).getAvatarIcon(nickname,size,cacheTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
