package com.guang.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.Book;

/**
 * 当前借阅、历史借阅
 */
public class BookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {

    public BookAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, Book item) {
        viewHolder.setText(R.id.tv_book_bookName,item.getName()+" / "+item.getAuthor())
                    .setText(R.id.tv_book_barId,""+item.getBarId())
                    .setText(R.id.tv_book_borrowedTime,""+item.getBorrowedTime())
                    .setText(R.id.tv_book_returnTime, item.getReturnTime())
                    .setText(R.id.tv_book_location, item.getLocation())
                    .addOnClickListener(R.id.tv_book_renew)
        ;
        viewHolder.setVisible(R.id.tv_book_renew,item.getRenewTimes() == 0);
    }
    public void cleanData(){
        super.mData.clear();
    }

}
