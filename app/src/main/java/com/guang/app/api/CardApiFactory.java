package com.guang.app.api;

import com.guang.app.AppConfig;
import com.guang.app.model.CardBasic;
import com.guang.app.model.CardConsumeItem;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoguang on 2017/3/10.
 */
public class CardApiFactory extends ApiUtils {

    private CardApiFactory() {
        super();
    }

    public static CardApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CardApiFactory INSTANCE = new CardApiFactory();
    }
    public void  getCurrentCash(Observer<CardBasic> sub) {
        ApiUtils.getApi(AppConfig.idsPwd).create(CardApi.class).getCurrentCash()
                .map(new HttpResultFunc<CardBasic>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
    public void  getCardConsumeToday(String cardNum,Observer<List<CardConsumeItem>> sub) {
        ApiUtils.getApi(AppConfig.idsPwd).create(CardApi.class).getCardConsumeToday(cardNum)
                .map(new HttpResultFunc<List<CardConsumeItem>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(sub);
    }
}
