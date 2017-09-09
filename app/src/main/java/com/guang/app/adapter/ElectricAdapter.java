package com.guang.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.Electric;

/**
 * 电控
 */
public class ElectricAdapter extends BaseQuickAdapter<Electric, BaseViewHolder> {

    public ElectricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, Electric item) {
        viewHolder.setText(R.id.tv_electric_time,item.getTime())
                    .setText(R.id.tv_electric_electric,""+item.getElectric() + " °")
                    .setText(R.id.tv_electric_money,""+item.getMoney()+" 元")
        ;
    }

    public void cleanData(){
        super.mData.clear();
    }
}
