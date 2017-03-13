package com.guang.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.guang.app.R;
import com.guang.app.adapter.SearchBookAdapter;
import com.guang.app.api.OpacApiFactory;
import com.guang.app.model.SearchBook;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 查找图书
 * Created by xiaoguang on 2017/2/18.
 */
public class SearchBookActivity extends QueryActivity {
    private static OpacApiFactory factory = OpacApiFactory.getInstance();

    @Bind(R.id.common_recycleView)
    RecyclerView mRecyclerView;
    @Bind(R.id.book_searchview)
    SearchView searchView;
    private SearchBookAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_searchBook);
        setContentView(R.layout.search_book);
        ButterKnife.bind(this);
        initAdapter();
    }

    @Override
    protected void loadData() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.getTrimmedLength(query) == 0) return false;
                searchView.setIconified(true);  //清空文本，防止操作一次却调用两次onQueryTextSubmit

                factory.searchBook(query, new Observer<List<SearchBook>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        startLoadingProgess();
                    }

                    @Override
                    public void onNext(List<SearchBook> value) {
                        if (value.size() == 0) {
                            Toast.makeText(SearchBookActivity.this, "没有搜到结果喔", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(SearchBookActivity.this, "共搜到" + value.size() + "个结果", Toast.LENGTH_SHORT).show();

                        mAdapter.cleanData();
                        mAdapter.addData(value);
                        mAdapter.notifyDataSetChanged();
                        searchView.setIconified(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SearchBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        stopLoadingProgess();
                        mAdapter.isUseEmpty(true);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initAdapter() {
        mAdapter = new SearchBookAdapter(R.layout.search_book_item);
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(R.layout.layout_empty_data, (ViewGroup) mRecyclerView.getParent());
        mAdapter.isUseEmpty(false); //避免一开始就出现空页面
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setIconifiedByDefault(false);//是否自动缩小为图标
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("书名");

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener( ){

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, final View view, final int position) {

                SearchBook bean = mAdapter.getItem(position);
                if(bean.getNumAll().equals("0")){
                    Toast.makeText(SearchBookActivity.this, "此书刊可能正在订购中或者处理中", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SearchBookActivity.this,SearchBookStoreActivity.class);
                intent.putExtra(SearchBookStoreActivity.intentBookName,bean.getName());
                intent.putExtra(SearchBookStoreActivity.intentBookMacNo,bean.getMacno());
                startActivity(intent);
            }
        });
    }


}