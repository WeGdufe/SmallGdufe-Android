package com.guang.app.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.CardConsumeItem;

/**
 * 校园卡交易记录
 */
public class CardHistoryAdapter extends BaseQuickAdapter<CardConsumeItem, BaseViewHolder> {
    private Context mContext;

    public CardHistoryAdapter(Context context, int layoutResId) {
        super(layoutResId);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, CardConsumeItem item) {
        viewHolder.setText(R.id.tv_item_card_shop,item.getShop())
                    .setText(R.id.tv_item_card_time,item.getTime())
                    .setText(R.id.tv_item_card_cash,""+item.getCash())
                    .setText(R.id.tv_item_card_amount,""+item.getChange());
//        if(item.getCash().charAt(0) != '-'){
//            viewHolder.getView(R.id.tv_classname).setBackgroundColor(
//                    mContext.getResources().getColor(R.color.goal_item_passed_color));
//        }
//        viewHolder.getView(R.id.tv_classname).setBackgroundColor(
//                mContext.getResources().getColor(R.color.goal_item_failed_color));
    }
    public void cleanData(){
        super.mData.clear();
    }

}
