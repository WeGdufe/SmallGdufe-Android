package com.guang.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.SearchBookStoreItem;

/**
 * 搜书后点击的库存详情
 * Created by xiaoguang on 2017/3/13.
 */
public class SearchBookStoreAdapter extends BaseQuickAdapter<SearchBookStoreItem, BaseViewHolder> {

    public SearchBookStoreAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, SearchBookStoreItem item) {
        viewHolder.setText(R.id.tv_search_book_store_barId, item.getBarId())
                .setText(R.id.tv_search_book_location, "" + item.getLocation())
                .setText(R.id.tv_search_book_volume, "" + item.getVolume())
                .setText(R.id.tv_search_book_state, item.getState())
                .setText(R.id.tv_search_book_store_serial, item.getSerial())
        ;
        if(item.getState().equals("可借")){
            viewHolder.setTextColor(R.id.tv_search_book_state, mContext.getResources().getColor(R.color.goal_item_passed_color));
        }

    }

    public void cleanData() {
        super.mData.clear();
    }
}
