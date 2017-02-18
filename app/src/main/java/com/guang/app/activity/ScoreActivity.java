package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.adapter.ScoreAdapter;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.Score;
import com.guang.app.util.CalcUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 查询绩点
 * Created by xiaoguang on 2017/2/13.
 */
public class ScoreActivity extends QueryActivity {
    private static JwApiFactory factory = JwApiFactory.getInstance();

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setContentView(R.layout.common_listview);
        setTitle(R.string.title_grade);
        ButterKnife.bind(this);
        initAdapterAndData();
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

    private void initAdapterAndData() {
        final ScoreAdapter mAdapter = new ScoreAdapter(this,R.layout.score_listitem);
        mAdapter.openLoadAnimation();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        LogUtils.e("xxx");
        factory.getScore(new Observer<List<Score>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(List<Score> value) {

                LogUtils.e(value);
                mAdapter.addData(value);
                getSupportActionBar().setSubtitle(getGpaSubTitle(value));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                Toast.makeText(ScoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }


}
