package com.guang.app.widget;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.model.Schedule;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 新增/编辑课程的dialog页
 * Created by xiaoguang on 2017/5/31.
 */
public class EditScheduleDialog extends DialogFragment {

    @Bind(R.id.ed_schedule_edit_name)
    EditText edName;
    @Bind(R.id.ed_schedule_edit_location)
    EditText edLocation;
    @Bind(R.id.ed_schedule_edit_section)
    EditText edSection;
    @Bind(R.id.ed_schedule_edit_period)
    EditText edPeriod;    
    @Bind(R.id.ed_schedule_edit_dayinweek)
    EditText edDayInWeek;
    @Bind(R.id.ed_schedule_edit_teacher)
    EditText edTeacher;
    
    private static Schedule mSchedule = null;   //为空表示新增
    private static boolean isAddingSchedule = false;

    public static EditScheduleDialog newInstance(int dayInWeek,int startSec){ //新增课程用
        EditScheduleDialog dialog = new EditScheduleDialog();
        mSchedule = null;
        isAddingSchedule = true;
        Bundle args = new Bundle();     //传给onCreateDialog() 省定义成员变量
        args.putInt("dayInWeek",dayInWeek);
        args.putInt("startSec",startSec); 
        dialog.setArguments(args);
        return dialog;
    }

    public static EditScheduleDialog newInstance(Schedule schedule){ //编辑课程需要当前课程信息
        EditScheduleDialog dialog = new EditScheduleDialog();
        mSchedule = schedule;   //直接赋值，就不通过Bundle传参了，不然还得给Schedule实现序列化接口
        isAddingSchedule = false;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_schedule_edit, null);
        ButterKnife.bind(this, root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(root);
        builder.setNegativeButton("取消",null);
        if(isAddingSchedule){
            builder.setNeutralButton("重新导入课程将重置", null);
        }else{
            builder.setNeutralButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSchedule.delete();
                    mConfirmListener.onConfirmListener();
                }
            });
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(TextUtils.isEmpty(edPeriod.getText().toString())
                        || TextUtils.isEmpty(edLocation.getText().toString())
                        || TextUtils.isEmpty(edName.getText().toString())
                        || TextUtils.isEmpty(edDayInWeek.getText().toString())
                        || TextUtils.isEmpty(edSection.getText().toString())
                  ) {
                    Toast.makeText(getActivity(), "不能为空哟", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isAddingSchedule){       //新增课程
                    mSchedule = new Schedule();
                }
                mSchedule.setTeacher(edTeacher.getText().toString());
                mSchedule.setPeriod(edPeriod.getText().toString());
                mSchedule.setLocation(edLocation.getText().toString());
                mSchedule.setName(edName.getText().toString());
                try {
                    mSchedule.setDayInWeek(Integer.parseInt(edDayInWeek.getText().toString()));
                    mSchedule.setStartSec(Integer.parseInt(edSection.getText().toString().split("-")[0]));
                    mSchedule.setEndSec(Integer.parseInt(edSection.getText().toString().split("-")[1]));
                    if(mSchedule.getStartSec() > mSchedule.getEndSec()){
                        Toast.makeText(getActivity(), "节数不对，别闹", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (Exception e){
                    //防止乱输入格式不对
                    Toast.makeText(getActivity(), "请严格参照格式，不然App炸了你就用不了，出问题重新导入课表可恢复", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSchedule.save();
                mConfirmListener.onConfirmListener();
            }
        });

        if(!isAddingSchedule && mSchedule != null){ //编辑课程才显示
            edDayInWeek.setText(""+mSchedule.getDayInWeek());
            edName.setText(mSchedule.getName());
            edLocation.setText(mSchedule.getLocation());
            edPeriod.setText(mSchedule.getPeriod());
            edSection.setText(mSchedule.getStartSec()+"-"+mSchedule.getEndSec());
            edTeacher.setText(mSchedule.getTeacher());
        }
        if(isAddingSchedule){  //编辑课程不显示
            builder.setMessage("新增课程");
            int currentWeekInDay = getArguments().getInt("dayInWeek");
            int currentStartSec = getArguments().getInt("startSec");
            edDayInWeek.setText(""+currentWeekInDay);
            edSection.setText(""+currentStartSec+"-"+ (currentStartSec+1));
            edPeriod.setText("1-16(周)");
        }

        return builder.create();
    }

    //对话框确定按钮监听器 给调用者用的
    private OnConfirmListener mConfirmListener;
    public void setOnConfirmListener(OnConfirmListener listener){
        this.mConfirmListener = listener;
    }
    public interface OnConfirmListener{
        void onConfirmListener();
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
