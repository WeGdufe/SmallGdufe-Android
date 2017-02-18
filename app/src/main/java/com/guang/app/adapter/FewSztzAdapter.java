package com.guang.app.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.FewSztz;

import java.util.List;

/**
 * 查询绩点
 */
public class FewSztzAdapter extends BaseQuickAdapter<FewSztz, BaseViewHolder> {
    private Context mContext;
    public FewSztzAdapter(Context context, int layoutResId) {
        super(layoutResId);
        mContext = context;
    }

    @Override
    public void addData(List<FewSztz> data) {
        super.addData(data);
    }
//
//    public FewSztzAdapter(Context context, int layoutResId, List<FewSztz> data) {
//        super(layoutResId, data);
//        mContext = context;
//    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, FewSztz item) {
        viewHolder.setText(R.id.tv_fewsztz_name,item.getName())
                    .setText(R.id.tv_fewsztz_min_credit,""+item.getRequireScore())
                    .setText(R.id.tv_fewsztz_has_credit,""+item.getScore());
    }
}
