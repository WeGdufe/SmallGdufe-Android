package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.AppTips;
import com.guang.app.model.StrObjectResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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
    public void submitFeedback(String contact, String content,
                               String deviceBrand, String deviceModel,
                               String osVersion,
                               Observer<StrObjectResponse> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(WorkApi.class).submitFeedback(contact,content,
                deviceBrand,deviceModel,osVersion)
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

    public void getAppTips(Observer<AppTips> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(WorkApi.class).getAppTips()
                .map(new HttpResultFunc<AppTips>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    public void allLogout(Observer<StrObjectResponse> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(WorkApi.class).allLgout()
                .map(new HttpResultFunc<StrObjectResponse>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    public void getDocumentFile(String fileCode, Observer<ResponseBody> sub ) {
        ApiUtils.getApi("").create(WorkApi.class).getDocumentFile(fileCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }


    //web版登陆登出
    public void loginDrcomWeb(String username,String pwd,Observer<ResponseBody> sub ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.Drcom.DrcomWebBase)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(WorkApi.class).loginDrcomWeb(username, pwd, "%25B5%25C7%25C2%25BC%2BLogin","%E7%99%BB%E9%99%86")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    public void logoutDrcomWeb(Observer<ResponseBody> sub ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.Drcom.DrcomWebBase)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(WorkApi.class).logoutDrcomWeb()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

}
