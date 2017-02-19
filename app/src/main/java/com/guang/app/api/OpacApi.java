package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Book;
import com.guang.app.model.HttpResult;
import com.guang.app.model.SearchBook;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface OpacApi {
    @GET(AppConfig.Url.currentBook)
    Observable<HttpResult< List<Book> >> getCurrentBook();
    @GET(AppConfig.Url.borrowedBook)
    Observable<HttpResult< List<Book> >> getBorrowedBook();
    @GET(AppConfig.Url.searchBook)
//    @GET(AppConfig.Url.searchBook+"bookName={bookName}")
    Observable<HttpResult< List<SearchBook> >> searchBook(@Query("bookName") String bookName);
//    Observable<HttpResult< List<SearchBook> >> searchBook(@Body SearchBookQueryModel queryBookModel);

//    Observable<HttpResult< List<SearchBook> >> searchBook(@Body RequestBody bookName);
//    Observable<HttpResult< List<SearchBook> >> searchBook(@Field(value="bookName") String bookName);

}