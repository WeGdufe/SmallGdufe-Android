package com.guang.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.activity.BookActivity;
import com.guang.app.model.Book;
import com.guang.app.util.TimeUtils;

/**
 * 当前借阅、历史借阅
 */
public class BookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    private long curTime ; //当前时间戳，秒级别
    private int doWhat;

    public BookAdapter(int layoutResId,int doWhat) {
        super(layoutResId);
        curTime = System.currentTimeMillis() / 1000;
        this.doWhat = doWhat;
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
        //当前借阅的情况下考虑显示续借按钮和过期书籍的红色字体显示
        if(doWhat == BookActivity.doCurrentBook){
            if(curTime > TimeUtils.timeString2TimeStamp(item.getReturnTime()) ){  //过期书籍
                viewHolder.setTextColor(R.id.tv_book_returnTime, mContext.getResources().getColor(R.color.goal_item_failed_color));
            }else{
                viewHolder.setVisible(R.id.tv_book_renew,item.getRenewTimes() == 0);    //当前借阅且是未到期情况下 续借按钮可见
                viewHolder.setTextColor(R.id.tv_book_returnTime, mContext.getResources().getColor(R.color.normal_item_text_color)); //恢复颜色，不然会显示bug，这个adapter框架的问题
            }
        }else{
            viewHolder.setVisible(R.id.tv_book_renew,false);    //BaseQuickAdapter的bug，还是得手动设置
        }
    }
    public void cleanData(){
        super.mData.clear();
    }

}
