package com.guang.app.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.Score;
import com.guang.app.util.CalcUtils;

import java.util.List;

/**
 * 查询绩点
 */
public class ScoreAdapter extends BaseQuickAdapter<Score, BaseViewHolder> {
    private Context mContext;

    public ScoreAdapter(Context context,int layoutResId, List<Score> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, Score item) {
        viewHolder.setText(R.id.tv_classname,item.getName())
                    .setText(R.id.tv_score,""+item.getScore())
                    .setText(R.id.tv_credit,""+item.getCredit())
                    .setText(R.id.tv_grade_point, CalcUtils.calcScore(item.getScore()))
                    .setVisible(R.id.expandable, false);
        //  .setVisible(R.id.expandable, viewHolder.getView(R.id.expandable_toggle_button).is())
        //重修判断 60分为通过？
        int score = item.getScore();
        if( score < 60) {
            viewHolder.getView(R.id.tv_classname).setBackgroundColor(
                    mContext.getResources().getColor(R.color.goal_item_failed_color));
        }
    }
}
