package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Book;
import com.guang.app.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by xiaoguang on 2017/2/14.
 * 注意导入 import io.reactivex.Observable;
 */
public interface OpacApi {
    @POST(AppConfig.Url.currentBook)
    Observable<HttpResult< List<Book> >> getCurrentBook();
    @POST(AppConfig.Url.borrowedBook)
    Observable<HttpResult< List<Book> >> getBorrowedBook();

}