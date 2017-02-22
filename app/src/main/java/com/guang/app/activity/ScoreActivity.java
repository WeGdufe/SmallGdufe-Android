package com.guang.app.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.adapter.ScoreAdapter;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.Score;
import com.guang.app.util.CalcUtils;
import com.guang.app.widget.PickerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private ScoreAdapter mAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_grade);
        setContentView(R.layout.common_listview);
        ButterKnife.bind(this);
        initAdapter();
    }

    @Override
    protected void loadData() {
        showXueQiPickerDialog(this);
    }

    private void initAdapter() {
        mAdapter = new ScoreAdapter(this,R.layout.score_listitem);
        mAdapter.openLoadAnimation();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    //点击actionbar文字继续选择时间查询
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_score_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_socre_time:
                showXueQiPickerDialog(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 计算GPA，返回样例： 平均绩点：3。8 学分：20
    private String getGpaSubTitle(List<Score> scores){
        double gpa = 0.0,total_credit = 0.0;
        for (Score score:scores) {
            total_credit += score.getCredit();
            double point = Double.parseDouble(CalcUtils.calcScore2Gpa(score.getScore()));
            gpa += point*score.getCredit();
        }
        gpa/=total_credit;
        DecimalFormat df1 = new DecimalFormat("#.00");
        DecimalFormat df2 = new DecimalFormat("#");
        return "平均绩点："+df1.format(gpa)+"     学分："+df2.format(total_credit);
    }

    //对话框
    String selectedXuenian;
    String selectedXueqi;
    AlertDialog.Builder pickerBuilder = null;
    public void showXueQiPickerDialog(final Activity act) {
        pickerBuilder = new AlertDialog.Builder(act);
        LayoutInflater layoutInflater = act.getLayoutInflater();
        View customLayout = layoutInflater.inflate(R.layout.xueqi_picker, null);
        pickerBuilder.setView(customLayout);

        PickerView pickerViewXuenian = (PickerView) customLayout.findViewById(R.id.picker_xuenian);
        PickerView pickerViewXueqi = (PickerView) customLayout.findViewById(R.id.picker_xueqi);

        //定义滚动选择器的数据项
        final ArrayList<String> xuenianArr = new ArrayList<>();
        ArrayList<String> xueqiArr = new ArrayList<>();
        int firstYear = Integer.parseInt(AppConfig.sno.substring(0, 2));

        String level = "一二三四"; //大五就不要了233
        for (int i = 0; i < 4; i++) {
            xuenianArr.add("20" + (firstYear + i) + "-20" + (firstYear + i + 1) + "(大" + level.charAt(i) + ")");
        }
        pickerViewXuenian.setData(xuenianArr);
        xueqiArr.add("第1学期");
        xueqiArr.add("第2学期");
        pickerViewXueqi.setData(xueqiArr);

        pickerViewXuenian.setSelected(0);
        pickerViewXueqi.setSelected(0);
        selectedXuenian = xuenianArr.get(0).substring(0, 9);
        selectedXueqi = "1";

        pickerViewXuenian.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                //2013-2014(大一) 转成2013-2014
                selectedXuenian = text.substring(0, 9);
            }
        });
        pickerViewXueqi.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                //第1学期 转成1
                selectedXueqi = text.substring(1, 2);
            }
        });
        pickerBuilder.setNeutralButton("整个大学", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realQueryGrade("");
            }
        });
        //对话框的确定按钮
        pickerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String studyTime = selectedXuenian + "-" + selectedXueqi;
                realQueryGrade(studyTime);//studyTime:2013-2014-1
            }
        });
        pickerBuilder.setNegativeButton("取消", null);
        pickerBuilder.show();
    }

    /**
     * 实际发请求查询
     */
    private void realQueryGrade(String studyTime){
        factory.getScore(studyTime,new Observer<List<Score>>() {
            @Override
            public void onSubscribe(Disposable d) {
                startLoadingProgess();
            }
            @Override
            public void onNext(List<Score> value) {
                LogUtils.e(value);
                mAdapter.cleanData();
                mAdapter.addData(value);
                mAdapter.notifyDataSetChanged();
                getSupportActionBar().setSubtitle(getGpaSubTitle(value));
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ScoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onComplete() {
                stopLoadingProgess();
            }
        });
    }
}
