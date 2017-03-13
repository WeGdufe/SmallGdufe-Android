package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.Book;
import com.guang.app.model.SearchBook;
import com.guang.app.model.SearchBookStoreItem;
import com.guang.app.model.StrObjectResponse;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoguang on 2017/2/18.
 */
public class OpacApiFactory extends ApiUtils {

    private OpacApiFactory() {
        super();
    }

    public static OpacApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final OpacApiFactory INSTANCE = new OpacApiFactory();
    }
    public void  searchBook(String bookName,Observer<List<SearchBook>> sub) {
        ApiUtils.getApi(AppConfig.idsPwd).create(OpacApi.class).searchBook(bookName)
                .map(new HttpResultFunc<List<SearchBook>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
    public void  getCurrentBook(Observer<List<Book>> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(OpacApi.class).getCurrentBook()
                .map(new HttpResultFunc<List<Book>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    public void  getBorrowedBook(Observer<List<Book>> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(OpacApi.class).getBorrowedBook()
                .map(new HttpResultFunc<List<Book>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
    public void  getRenewBookVerifyCode(Observer<StrObjectResponse> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(OpacApi.class).getRenewBookVerifyCode()
                .map(new HttpResultFunc<StrObjectResponse>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
    public void  renewBook(String barId,String checkId,String verify,Observer<StrObjectResponse> sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(OpacApi.class).renewBook(barId,checkId,verify)
                .map(new HttpResultFunc<StrObjectResponse>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
    public void  getBookStoreDetail(String macno,Observer< List<SearchBookStoreItem> > sub ) {
        ApiUtils.getApi(AppConfig.idsPwd).create(OpacApi.class).getBookStoreDetail(macno)
                .map(new HttpResultFunc< List<SearchBookStoreItem> >())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }


}
