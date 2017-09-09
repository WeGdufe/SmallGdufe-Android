package com.guang.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guang.app.R;
import com.guang.app.model.Score;
import com.guang.app.util.CalcUtils;

/**
 * 查询绩点
 */
public class ScoreAdapter extends BaseQuickAdapter<Score, BaseViewHolder> {
    private Context mContext;

    public ScoreAdapter(Context context,int layoutResId) {
        super(layoutResId);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, Score item) {
        viewHolder.setText(R.id.tv_course_name, item.getName())
                .setText(R.id.tv_time, item.getTime())
                .setText(R.id.tv_score, "" + item.getScore())
                .setText(R.id.tv_credit, "" + item.getCredit())
                .setText(R.id.tv_grade_point, CalcUtils.calcScore2Gpa(item.getScore()))
                .setVisible(R.id.score_expandable, false)
                .setVisible(R.id.score_item_separator, false);

        //下拉后的内容
        viewHolder.setText(R.id.tv_classcode, "" + item.getClassCode())
                .setText(R.id.tv_score_exp, "" + item.getExpScore())
                .setText(R.id.tv_score_daily, "" + item.getDailyScore())
                .setText(R.id.tv_score_paper, "" + item.getPaperScore());

        //点击下拉按钮显示/隐藏额外信息，BaseRecyclerViewAdapterHelper框架也能实现，这里手动实现 https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/%E5%88%86%E7%BB%84%E7%9A%84%E4%BC%B8%E7%BC%A9%E6%A0%8F
        viewHolder.setOnCheckedChangeListener(R.id.score_btn_expandable_toggle, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewHolder.setVisible(R.id.score_expandable, isChecked);
                viewHolder.setVisible(R.id.score_item_separator, isChecked);
            }
        });
        //让整个内容条的点击都生效 显示/隐藏信息 ，提高体验
        viewHolder.getView(R.id.layout_score_item_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.score_btn_expandable_toggle) {  //虽然想防止多次点击（听声音） 但好像没什么效果，该条件要不要都行
                    viewHolder.getView(R.id.score_btn_expandable_toggle).performClick();
                }
            }
        });

        colorTextViewByScore(viewHolder, R.id.tv_score, item.getScore());
        colorTextViewByScore(viewHolder, R.id.tv_score_daily, item.getDailyScore());
        colorTextViewByScore(viewHolder, R.id.tv_score_paper, item.getPaperScore());
    }
    //根据分数填充颜色，实验成绩不需要填充，暂没做 重修判断
    private void colorTextViewByScore(BaseViewHolder viewHolder,int textViewId, int score) {
        if( score < 60) {
            viewHolder.setTextColor(textViewId, mContext.getResources().getColor(R.color.goal_item_failed_color));
//            if(textViewId == R.id.tv_score){ //给标题加红的话，下拉上滑后就乱标红色了，框架的复用item有问题
//                viewHolder.setTextColor(R.id.tv_course_name, mContext.getResources().getColor(R.color.goal_item_failed_color));
//            }else{
//                viewHolder.setTextColor(R.id.tv_course_name, mContext.getResources().getColor(R.color.normal_item_passed_text_color));
//            }
        }else{
            viewHolder.setTextColor(textViewId, mContext.getResources().getColor(R.color.goal_item_passed_color));
        }
    }

    public void cleanData(){
        super.mData.clear();
    }


}
