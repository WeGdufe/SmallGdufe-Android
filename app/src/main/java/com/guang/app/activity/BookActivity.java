package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.adapter.BookAdapter;
import com.guang.app.api.OpacApiFactory;
import com.guang.app.model.Book;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 当前借阅和历史借阅共用Activity，界面也一样，Intent参数去区分
 * Created by xiaoguang on 2017/2/18.
 */
public class BookActivity extends QueryActivity {
    private static OpacApiFactory factory = OpacApiFactory.getInstance();
    private BookAdapter mAdapter;
    public static final String doWhat = "what";
    public static final int doBorrowedBook = 1;
    public static final int doCurrentBook = 0;

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setContentView(R.layout.common_listview);
        ButterKnife.bind(this);
        initAdapter();
    }

    @Override
    protected void loadData() {
        startLoadingProgess();
        int from = getIntent().getIntExtra(doWhat,0);
        if(from == BookActivity.doBorrowedBook) {
            setTitle(R.string.title_borrowedBook);
            factory.getBorrowedBook(new Observer<List<Book>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    startLoadingProgess();

                }

                @Override
                public void onNext(List<Book> value) {
                    mAdapter.addData(value);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(BookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    BookActivity.this.finish();
                }

                @Override
                public void onComplete() {
                    stopLoadingProgess();
                }
            });
        }else{
            setTitle(R.string.title_currentBook);
            factory.getCurrentBook(new Observer<List<Book>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    startLoadingProgess();
                }

                @Override
                public void onNext(List<Book> value) {
                    mAdapter.addData(value);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(BookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    BookActivity.this.finish();
                }

                @Override
                public void onComplete() {
                    stopLoadingProgess();
                }
            });
        }
    }

    private void initAdapter() {
        mAdapter = new BookAdapter(R.layout.book_listitem);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}