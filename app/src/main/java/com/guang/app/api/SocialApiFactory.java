package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.AppTips;
import com.guang.app.model.Feed;
import com.guang.app.model.StrObjectResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by xiaoguang on 2018/2/28.
 */
public class SocialApiFactory extends ApiUtils {

    private SocialApiFactory() {
        super();
    }

    public static SocialApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final SocialApiFactory INSTANCE = new SocialApiFactory();
    }

    public void getImFeedList(int pageNo,int pageNum, Observer< List<Feed> > sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(SocialApi.class).getImFeedList(pageNo, pageNum )
                .map(new HttpResultFunc<List<Feed>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

//    public void createImFeedList(String content, ArrayList<String> photos, Observer< StrObjectResponse > sub ) {
    public void createImFeedList(Feed feed, Observer< StrObjectResponse > sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(SocialApi.class).createImFeed(feed.getContent(), feed.getPhotos() )
                .map(new HttpResultFunc<StrObjectResponse>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
