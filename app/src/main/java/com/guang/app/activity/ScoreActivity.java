package com.guang.app.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.adapter.ScoreAdapter;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.Score;
import com.guang.app.util.CalcUtils;
import com.guang.app.util.FileUtils;
import com.guang.app.widget.PickerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 查询绩点
 * Created by xiaoguang on 2017/2/13.
 */
public class ScoreActivity extends QueryActivity {
    private static JwApiFactory factory = JwApiFactory.getInstance();
    private static final int SCORE_QUERY_MAJOR = 0;
    private static final int SCORE_QUERY_MINOR = 1;

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;
    private ScoreAdapter mAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_grade);
        setContentView(R.layout.common_listview);

        initAdapter();
    }

    @Override
    protected void loadData() {
        showXueQiPickerDialog(this);
    }

    private void initAdapter() {
        mAdapter = new ScoreAdapter(this,R.layout.score_listitem);
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(R.layout.layout_empty_data, (ViewGroup) mRecyclerView.getParent());
        mAdapter.isUseEmpty(false);
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
            case R.id.menu_change_calc_help:
                Toast.makeText(this, "挂科的不计算学分和GPA，补考通过按补考分算，如补考90则为4.0", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_change_socre_target:
                int target = FileUtils.getScoreQueryTarget(this);
                target = 1 - target;
                FileUtils.setScoreQueryTarget(this,target);
                if(target == SCORE_QUERY_MAJOR){
                    Toast.makeText(this, "切换至主修查询，请重修查一次", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "切换至辅修查询，请重修查一次", Toast.LENGTH_SHORT).show();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // 计算GPA，返回样例： 平均绩点：3。8 学分：20
    private String getGpaSubTitle(List<Score> scores){
        double gpa = 0.0,total_credit = 0.0;
        for (Score score:scores) {
            double point = Double.parseDouble(CalcUtils.calcScore2Gpa(score.getScore()));
            if( Math.abs(point-0.0) < 1e-3){    //挂科的point为0，不能算入学分
                continue;
            }
            double dCredit = score.getCredit();
            total_credit += dCredit ;
            gpa += point * dCredit;
        }
        gpa /= total_credit;
        DecimalFormat df1 = new DecimalFormat("#.00");
        DecimalFormat df2 = new DecimalFormat("#.0");
        return "平均："+df1.format(gpa)+"  学分："+df2.format(total_credit);
    }

    //对话框
    String selectedXuenian;
    String selectedXueqi;
    AlertDialog.Builder pickerBuilder = null;
    public void showXueQiPickerDialog(final Activity act) {
        final String allYear = "整个学年";
        pickerBuilder = new AlertDialog.Builder(act);
        LayoutInflater layoutInflater = act.getLayoutInflater();
        View customLayout = layoutInflater.inflate(R.layout.xueqi_picker, null);
        pickerBuilder.setView(customLayout);

        PickerView pickerViewXuenian = (PickerView) customLayout.findViewById(R.id.picker_xuenian);
        PickerView pickerViewXueqi = (PickerView) customLayout.findViewById(R.id.picker_xueqi);

        //定义滚动选择器的数据项
        final ArrayList<String> xuenianArr = new ArrayList<>();
        ArrayList<String> xueqiArr = new ArrayList<>();

        int firstYear = CalcUtils.getFirstYear();
        String level = "一二三四"; //大五就不要了233
        for (int i = 0; i < 4; i++) {
            xuenianArr.add("20" + (firstYear + i) + "-20" + (firstYear + i + 1) + "(大" + level.charAt(i) + ")");
        }
        pickerViewXuenian.setData(xuenianArr);
        xueqiArr.add(allYear);
        xueqiArr.add("第1学期");
        xueqiArr.add("第2学期");
        pickerViewXueqi.setData(xueqiArr);

        pickerViewXuenian.setSelected(0);
        pickerViewXueqi.setSelected(0);
        selectedXuenian = xuenianArr.get(0).substring(0, 9);
        selectedXueqi = "";

        final int queryTarget = FileUtils.getScoreQueryTarget(act);
        String allContent = "整个大学(主修)";
        if(queryTarget == SCORE_QUERY_MINOR){
            allContent = "整个大学(辅修)";
        }
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
                if(text.equals(allYear)){
                    selectedXueqi = "";
                }else {
                    selectedXueqi = text.substring(1, 2);
                }
                LogUtils.e(selectedXueqi+"===");

            }
        });
        pickerBuilder.setNeutralButton(allContent, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realQueryGrade("",queryTarget);
            }
        });
        //对话框的确定按钮
        pickerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String studyTime = selectedXuenian + "-" + selectedXueqi;
                if(TextUtils.isEmpty(selectedXueqi)){
                    studyTime  = selectedXuenian ;
                }
                LogUtils.e(studyTime);
                realQueryGrade(studyTime,queryTarget);//studyTime:2013-2014-1
            }
        });
        pickerBuilder.setNegativeButton("取消", null);
        pickerBuilder.show();
    }

    /**
     * 实际发请求查询
     */
    private void realQueryGrade(String studyTime,int minor){
        factory.getScore(studyTime,minor,new Observer<List<Score>>() {
            @Override
            public void onSubscribe(Disposable d) {
                startLoadingProgess();
            }
            @Override
            public void onNext(List<Score> value) {
                if(value.size() == 0){
                    Toast.makeText(ScoreActivity.this, "别闹", Toast.LENGTH_SHORT).show();
                    return;
                }                LogUtils.e(value.size()+"个");

                mAdapter.cleanData();
                mAdapter.addData(value);
                mAdapter.notifyDataSetChanged();
                getSupportActionBar().setSubtitle(getGpaSubTitle(value));
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                if("unexpected end of stream".equals(e.getMessage())){  //okhttp的锅
                    Toast.makeText(ScoreActivity.this, "网络抖动了下，再试一次", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ScoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onComplete() {
                mAdapter.isUseEmpty(true);
                stopLoadingProgess();
            }
        });
    }
}
