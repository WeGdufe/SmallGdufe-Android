package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.adapter.SearchBookStoreAdapter;
import com.guang.app.api.OpacApiFactory;
import com.guang.app.model.SearchBookStoreItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 书本借阅情况，从搜书界面点item进入
 * Created by xiaoguang on 2017/3/13.
 */
public class SearchBookStoreActivity  extends QueryActivity {
    public static final String intentBookMacNo = "macno";
    public static final String intentBookName = "name";
    private static OpacApiFactory factory = OpacApiFactory.getInstance();

    @Bind(R.id.common_recycleView)
    RecyclerView mRecyclerView;

    private SearchBookStoreAdapter mAdapter;
    private String mMacNo;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();

        mMacNo = getIntent().getStringExtra(intentBookMacNo);
        String bookName = getIntent().getStringExtra(intentBookName);
        setTitle(bookName);
        setContentView(R.layout.common_listview);

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new SearchBookStoreAdapter(R.layout.search_book_store_item);
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(R.layout.layout_empty_data, (ViewGroup) mRecyclerView.getParent());
        mAdapter.isUseEmpty(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void loadData() {

        factory.getBookStoreDetail(mMacNo, new Observer<List<SearchBookStoreItem>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<SearchBookStoreItem> value) {
                mAdapter.addData(value);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                Toast.makeText(SearchBookStoreActivity.this, "获取详情失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
