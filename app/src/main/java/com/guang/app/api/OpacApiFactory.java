package com.guang.app.api;

import com.guang.app.model.Book;

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

    public void  getCurrentBook(Observer<List<Book>> sub ) {
        ApiUtils.api.create(OpacApi.class).getCurrentBook()
                .map(new HttpResultFunc<List<Book>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }

    public void  getBorrowedBook(Observer<List<Book>> sub ) {
        ApiUtils.api.create(OpacApi.class).getBorrowedBook()
                .map(new HttpResultFunc<List<Book>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
