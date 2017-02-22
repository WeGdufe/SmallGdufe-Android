package com.guang.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.SearchBook;

/**
 * 搜索图书
 */
public class SearchBookAdapter extends BaseQuickAdapter<SearchBook, BaseViewHolder> {

    public SearchBookAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, SearchBook item) {
        viewHolder.setText(R.id.tv_bookName,item.getName()+" / "+item.getAuthor())
                    .setText(R.id.tv_serial,""+item.getSerial())
                    .setText(R.id.tv_numAll,""+item.getNumAll())
                    .setText(R.id.tv_numCan, item.getNumCan())
                    .setText(R.id.tv_publisher, item.getPublisher())
        ;
    }

    public void cleanData(){
        super.mData.clear();
    }
}
