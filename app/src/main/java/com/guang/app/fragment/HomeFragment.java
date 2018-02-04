package com.guang.app.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guang.app.R;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.Schedule;
import com.guang.app.util.CalcUtils;
import com.guang.app.util.FileUtils;
import com.guang.app.widget.EditScheduleDialog;
import com.guang.app.widget.PickerView;
import com.guang.app.widget.ScheduleView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class HomeFragment extends Fragment {
    private static JwApiFactory factory = JwApiFactory.getInstance();

    private String selectedWeek ;   //按周查看的当前选择周数

    @Bind(R.id.scheduleView)
    ScheduleView mScheduleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle(R.string.app_name);
        initViewByDb();

        mScheduleView.setOnClickScheduleListener(new ScheduleView.onClickScheduleListener() {
            @Override
            public void onClickScheduleEdit(Schedule model) {           //编辑课程
                //因ScheduleView.java的点击事件的model获取比较蛋疼，又没法获取getFragmentManager故选择这种回调传参方法，以后考虑换掉ScheduleView控件类
                EditScheduleDialog dialog = EditScheduleDialog.newInstance(model);
                dialog.setOnConfirmListener(new EditScheduleDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        initViewByDb();
                    }
                });
                dialog.show( getFragmentManager(), "Bad Code,Wait for refactoring");
            }
            @Override
            public void onClickScheduleAdd(int dayInWeek,int startSec){ //新增课程
                EditScheduleDialog dialog = EditScheduleDialog.newInstance(dayInWeek,startSec);
                dialog.setOnConfirmListener(new EditScheduleDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        initViewByDb();
                    }
                });
                dialog.show( getFragmentManager(), "ScheduleView.java is not good");
            }
        });
        return view;
    }

    //查数据库的课表 并显示
    private void initViewByDb() {
        int currentWeek = Integer.parseInt(FileUtils.getCurrentWeek(getActivity()));
        List<Schedule> list = DataSupport.findAll(Schedule.class);
        mScheduleView.cleanScheduleData();
        if(list.size() == 0) {
            //空数据，有界面
            mScheduleView.setScheduleData(new ArrayList<Schedule>());
        }else{
            //无设置按周看则显示全部，否则显示特定周数
            if(currentWeek == FileUtils.SP_WEEK_NOT_SET) {
                mScheduleView.setScheduleData(list);
            }else{
                mScheduleView.setScheduleData(list, currentWeek);
            }
        }
    }

    //对话框
    String selectedXuenian;
    String selectedXueqi;
    AlertDialog.Builder pickerBuilder = null;
    //弹出时间选择窗并查课表
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

        int firstYear = CalcUtils.getFirstYear();
        String level = "一二三四";
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
                selectedXuenian = text.substring(0, 9);
            }
        });
        pickerViewXueqi.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedXueqi = text.substring(1, 2);
            }
        });
        pickerBuilder.setNeutralButton("当前学期", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realQuerySchedule("");
            }
        });
        //对话框的确定按钮
        pickerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String studyTime = selectedXuenian + "-" + selectedXueqi;
                realQuerySchedule(studyTime);
            }
        });
        pickerBuilder.setNegativeButton("取消", null);
        pickerBuilder.show();
    }

    private void realQuerySchedule(String studyTime){
        factory.getSchedule(studyTime,JwApiFactory.MERGE_SCHEDULE, new Observer<List<Schedule>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<Schedule> value) {
                if(value.size()==0){
                    Toast.makeText(getActivity(), "没有喔", Toast.LENGTH_SHORT).show();
                    return;
                }
                mScheduleView.cleanScheduleData();
                mScheduleView.setScheduleData(value);
                DataSupport.deleteAll(Schedule.class);
                DataSupport.saveAll(value);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        });
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
            case R.id.menu_schedule_more_import:         //导入课程
                showXueQiPickerDialog(getActivity());
                break;
            case R.id.menu_schedule_more_current_week:  //按周查看
                showSelectWeekDialog(getActivity());
                break;
            case R.id.menu_schedule_more_timetable:     //排课表
                showAddTimeTableDialog(getActivity());
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

    //周数选择
    private void showSelectWeekDialog(final Activity context){
        selectedWeek = "9";
        AlertDialog.Builder pickerBuilder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customLayout = layoutInflater.inflate(R.layout.week_picker, null);
        pickerBuilder.setView(customLayout);
        ArrayList<String> weekArr = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            weekArr.add(i+"");
        }
        PickerView WeekPicker = (PickerView) customLayout.findViewById(R.id.selected_week);
        WeekPicker.setData(weekArr);
        WeekPicker.setSelected(8);

        WeekPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedWeek = text;
            }
        });
        pickerBuilder.setNeutralButton("查看全周表",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FileUtils.expireCurrentWeek(context);
                List<Schedule> list = DataSupport.findAll(Schedule.class);
                if(list.size() == 0) {
                    Toast.makeText(context, "请先导入课表", Toast.LENGTH_SHORT).show();
                    return;
                }
                mScheduleView.cleanScheduleData();
                mScheduleView.setScheduleData(list);
            }
        });
        pickerBuilder.setNegativeButton("取消",null);
        pickerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FileUtils.saveCurrentWeek(context,selectedWeek);
                List<Schedule> list = DataSupport.findAll(Schedule.class);
                if(list.size() == 0) {
                    Toast.makeText(context, "请先导入课表", Toast.LENGTH_SHORT).show();
                    return;
                }
                mScheduleView.cleanScheduleData();
                mScheduleView.setScheduleData(list,Integer.parseInt(selectedWeek));
            }
        });
        pickerBuilder.show();
    }


    /**
     * 添加排课表到周日里，适合非双学位使用
     * @param context
     */
    private void showAddTimeTableDialog (final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("将排课表放入周日空白处")
                .setMessage("在周日无课程的情况下使用，请选择校区")
                .setPositiveButton("佛山校区", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readTimeTableFile(context,"timetable_foshan.json");
                    }
                })
                .setNegativeButton("广州校区", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readTimeTableFile(context,"timetable_guangzhou.json");
                    }
                })
                .setNeutralButton("删除排课表", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Schedule.class,"dayInWeek = ?","7");
                        initViewByDb();
                    }
                })
        ;
        builder.show();
    }

    /**
     *  读json格式时间表文件并写到数据库
     */
    private void readTimeTableFile(Context context,String filename) {
        String timetableStr = FileUtils.getStrFromAssets(context,filename);
        List<Schedule> timetableList = new Gson().fromJson(timetableStr, new TypeToken<List<Schedule>>() {}.getType());
        if(timetableList == null || timetableList.size() == 0){
            return;
        }
        DataSupport.deleteAll(Schedule.class,"dayInWeek = ?","7");
        DataSupport.saveAll(timetableList);
        initViewByDb();
    }
}
