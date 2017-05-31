package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.adapter.CardHistoryAdapter;
import com.guang.app.api.CardApiFactory;
import com.guang.app.model.CardConsumeItem;

import java.util.List;

import butterknife.Bind;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 校园卡当天消费记录
 * Created by xiaoguang on 2017/3/10.
 */
public class CardHistoryActivity extends QueryActivity {
    private static CardApiFactory factory = CardApiFactory.getInstance();
    private static String mCardNum; //校园卡卡号
    public static final String intentCardNum = "cardNum";

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;
    private CardHistoryAdapter mAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_card_history);
        setContentView(R.layout.common_listview);

        initAdapter();
        mCardNum = getIntent().getStringExtra(intentCardNum);
    }

    @Override
    protected void loadData() {
        getCardConsumeToday(mCardNum);
    }

    private void initAdapter() {
        mAdapter = new CardHistoryAdapter(this,R.layout.card_history_item);
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(R.layout.layout_empty_data, (ViewGroup) mRecyclerView.getParent());
        mAdapter.isUseEmpty(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 访问
     * @param cardNum
     */
    private void getCardConsumeToday(String cardNum){
        factory.getCardConsumeToday(cardNum,new Observer<List<CardConsumeItem>>() {
            @Override
            public void onSubscribe(Disposable d) {
                startLoadingProgess();
            }
            @Override
            public void onNext(List<CardConsumeItem> value) {
                if(value.size() == 0){
                    Toast.makeText(CardHistoryActivity.this, "你今天没有校园卡交易喔", Toast.LENGTH_SHORT).show();
                    return;
                }
                LogUtils.w(value);
                mAdapter.cleanData();
                mAdapter.addData(value);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                Toast.makeText(CardHistoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onComplete() {
                mAdapter.isUseEmpty(true);
                stopLoadingProgess();
            }
        });
    }
}
