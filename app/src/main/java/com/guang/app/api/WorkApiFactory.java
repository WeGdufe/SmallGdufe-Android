package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.StrObjectResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoguang on 2017/2/26.
 */
public class WorkApiFactory extends ApiUtils {

    private WorkApiFactory() {
        super();
//        password = "";
    }

    public static WorkApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final WorkApiFactory INSTANCE = new WorkApiFactory();
    }

    public void  submitFeedback(String contact,String content,Observer<StrObjectResponse> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(WorkApi.class).submitFeedback(contact,content)
                .map(new HttpResultFunc<StrObjectResponse>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

}
