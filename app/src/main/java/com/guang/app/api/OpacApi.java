package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Book;
import com.guang.app.model.HttpResult;
import com.guang.app.model.SearchBook;
import com.guang.app.model.SearchBookStoreItem;
import com.guang.app.model.StrObjectResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface OpacApi {
    //当前借阅
    @GET(AppConfig.Url.currentBook)
    Observable<HttpResult< List<Book> >> getCurrentBook();
    //历史借阅
    @GET(AppConfig.Url.borrowedBook)
    Observable<HttpResult< List<Book> >> getBorrowedBook();
    //搜书（未登录搜索）
    @GET(AppConfig.Url.searchBook)
    Observable<HttpResult< List<SearchBook> >> searchBook(@Query("bookName") String bookName);

    //获取图书续借的验证码
    @GET(AppConfig.Url.getRenewBookVerify)
    Observable<HttpResult<StrObjectResponse>> getRenewBookVerifyCode();
    //图书续借
    @GET(AppConfig.Url.renewBook)
    Observable<HttpResult< StrObjectResponse >> renewBook(@Query("barId") String barId,
                                                            @Query("checkId") String checkId,
                                                            @Query("verify") String verify);
    //获取库存借阅情况
    @GET(AppConfig.Url.getBookStoreDetail)
    Observable<HttpResult< List<SearchBookStoreItem> >> getBookStoreDetail(@Query("macno") String macno);


//    Observable<HttpResult< List<SearchBook> >> searchBook(@Body RequestBody bookName);
//    Observable<HttpResult< List<SearchBook> >> searchBook(@Field(value="bookName") String bookName);

}