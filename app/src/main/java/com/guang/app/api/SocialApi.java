package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Book;
import com.guang.app.model.Feed;
import com.guang.app.model.HttpResult;
import com.guang.app.model.SearchBook;
import com.guang.app.model.SearchBookStoreItem;
import com.guang.app.model.StrObjectResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2018/2/28.
 */
public interface SocialApi {
    //当前借阅
//    @GET(AppConfig.Url.currentBook)
//    Observable<HttpResult< List<FeedRely> >> getImFeedReplyList();

    @GET(AppConfig.Url.getImFeedList)
    Observable<HttpResult< List<Feed> >> getImFeedList(@Query("pageNo") int pageNo, @Query("pageNum") int pageNum);

    @GET(AppConfig.Url.createImFeed)
    Observable<HttpResult<StrObjectResponse>>  createImFeed(@Query("content") String content, @Query("photos") ArrayList<String> photos);

    @GET(AppConfig.Url.createImFeedRelay)
    Observable<HttpResult<StrObjectResponse>>  createImFeedRelay();

    @GET(AppConfig.Url.createImFeed)
    Observable<HttpResult<StrObjectResponse>>  createImFeed();



//    Observable<HttpResult< List<SearchBook> >> searchBook(@Body RequestBody bookName);
//    Observable<HttpResult< List<SearchBook> >> searchBook(@Field(value="bookName") String bookName);

}