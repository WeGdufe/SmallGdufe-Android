package com.guang.app.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.Schedule;
import com.guang.app.widget.PickerView;
import com.guang.app.widget.ScheduleView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

//import android.support.v7.app.AlertDialog;


public class HomeFragment extends Fragment {
    private static JwApiFactory factory = JwApiFactory.getInstance();
    private static final int MERGE_SCHEDULE = 0;
    private static final int SPLIT_SCHEDULE = 1;

    @Bind(R.id.scheduleView)
    ScheduleView mScheduleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("APP");

        List<Schedule> list= DataSupport.findAll(Schedule.class);
        if(list.size() != 0) {
            mScheduleView.setScheduleData(list);
        }
        return view;
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
        ArrayList<String> xuenianArr = new ArrayList<>();
        ArrayList<String> xueqiArr = new ArrayList<>();
        int firstYear = Integer.parseInt(AppConfig.sno.substring(0, 2));
        String level = "一二三四五";
        for (int i = 0; i < 5; i++) {
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
                selectedXuenian = text.substring(0, 9);
            }
        });
        pickerViewXueqi.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedXueqi = text.substring(1, 2);
            }
        });

        //对话框的确定按钮
        pickerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String studyTime = selectedXuenian + "-" + selectedXueqi;
                factory.getSchedule(studyTime,MERGE_SCHEDULE, new Observer<List<Schedule>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Schedule> value) {
                        LogUtils.e(value);
                        mScheduleView.cleanScheduleData();
                        mScheduleView.setScheduleData(value);
                        DataSupport.deleteAll(Schedule.class);
                        DataSupport.saveAll(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                        Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }
        });
        pickerBuilder.setNegativeButton("取消", null);
        pickerBuilder.show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_schedule_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_schedule_more_import:
                showXueQiPickerDialog(getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        //关掉选择时间的窗口
        if (pickerBuilder != null && pickerBuilder.create().isShowing()) {
            pickerBuilder.create().dismiss();
        }
        super.onDestroy();
    }

}
