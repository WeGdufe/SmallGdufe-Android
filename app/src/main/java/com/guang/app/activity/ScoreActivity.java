package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.guang.app.R;
import com.guang.app.model.Score;
import com.guang.app.util.CalcUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 查询绩点
 * Created by xiaoguang on 2017/2/13.
 */
public class ScoreActivity extends QueryActivity {

    @Bind(R.id.score_listView) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setContentView(R.layout.score);
        setTitle(R.string.title_grade);
        ButterKnife.bind(this);
        initAdapterAndScoreData();
    }

    // 计算GPA
    String getGpaSubTitle(List<Score> scores){
        double gpa = 0.0,total_credit = 0.0;

        for (Score score:scores) {
            total_credit += score.getCredit();
            double point = Double.parseDouble(CalcUtils.calcScore(score.getScore()));
            gpa += point*score.getCredit();
        }
        gpa/=total_credit;
        DecimalFormat df1 = new DecimalFormat("#.00");
        DecimalFormat df2 = new DecimalFormat("#");
        return "平均绩点："+df1.format(gpa)+"     学分："+df2.format(total_credit);
    }

    private void initAdapterAndScoreData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        ApiUtils.api.create(JwApi.class).getScore().enqueue(new Callback<WrapperEntity<List<Score>>>() {
//            @Override
//            public void onResponse(Call<WrapperEntity<List<Score>>> call, Response<WrapperEntity<List<Score>>> response) {
////                LogUtils.e(response.body().getData());
//                List<Score> data = response.body().getData();
//                ScoreAdapter mAdapter = new ScoreAdapter(getApplicationContext(),R.layout.score_listitem,data);
//                mAdapter.openLoadAnimation();
//                getSupportActionBar().setSubtitle(getGpaSubTitle(data));
//                mRecyclerView.setAdapter(mAdapter);
//            }
//            @Override
//            public void onFailure(Call<WrapperEntity<List<Score>>> call, Throwable t) {
//                LogUtils.e(t.getMessage());
//            }
//        });
    }


}
