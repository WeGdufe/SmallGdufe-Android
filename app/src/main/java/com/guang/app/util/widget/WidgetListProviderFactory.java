package com.guang.app.util.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guang.app.R;
import com.guang.app.activity.MainActivity;
import com.guang.app.model.Schedule;
import com.guang.app.util.CalcUtils;
import com.guang.app.util.FileUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 桌面控件listview的adapter
 * Created by xiaoguang on 2017/4/15.
 */
public class WidgetListProviderFactory implements RemoteViewsService.RemoteViewsFactory {
    private static List<Schedule> mData = new ArrayList<Schedule>();
    private static Context mContext = null;
//    private static int mCurrentWeek = 1;

    public WidgetListProviderFactory(Context context, Intent intent){
        this.mContext = context;
//        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID); //如果需要
    }

    private static void initData(int dayInWeek){
        mData =  DataSupport.where("dayInWeek = ?", ""+dayInWeek).order("startSec").find(Schedule.class);
    }
    private static void initData(int dayInWeek,String week){
        if(week.equals(""+FileUtils.SP_WEEK_NOT_SET)) {
            initData(dayInWeek);
            return;
        }
        mData.clear();
        List<Schedule> temp =  DataSupport.where("dayInWeek = ?", ""+dayInWeek).order("startSec").find(Schedule.class);
        for (Schedule item:temp) {
            if(CalcUtils.isCurrentWeek(item.getPeriod(),Integer.parseInt(week))){
                mData.add(item);
            }
        }
    }
    /**
     * 对外的更新数据为指定星期的方法
     * @param dayInWeek 星期几，数字，星期一:1，星期天：7
     */
    public static void refreshData(int dayInWeek,String currentWeek){
        initData(dayInWeek,currentWeek);
    }


    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.desktop_widget_item);
        Schedule item = mData.get(position);
        remoteView.setTextViewText(R.id.widget_item_name, item.getName());
        remoteView.setTextViewText(R.id.widget_item_time, item.getStartSec()+"-"+item.getEndSec());
        remoteView.setTextViewText(R.id.widget_item_location, item.getLocation());

//        item点击事件调到app，setOnClickFillInIntent的第一个id为item布局的根布局，需要先在CourseAppWidgetProvider调用了setPendingIntentTemplate才生效
        Intent intent = new Intent(mContext, MainActivity.class);
        remoteView.setOnClickFillInIntent(R.id.desktop_widget_layout_item, intent);
        return remoteView;
    }

    @Override
    public void onCreate() {
        initData(WidgetCommonUtil.getTodayInWeek());
    }

    //返回 加载中 view，返回null为用默认view
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        mData.clear();
    }
}
