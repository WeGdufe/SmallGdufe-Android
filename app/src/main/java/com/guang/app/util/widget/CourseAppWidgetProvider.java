package com.guang.app.util.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.activity.MainActivity;
import com.guang.app.fragment.MeFragment;
import com.guang.app.model.BasicInfo;

import org.litepal.crud.DataSupport;

/**
 * 课程表桌面小组件入口管理类
 * Created by xiaoguang on 2017/4/15.
 */
public class CourseAppWidgetProvider  extends AppWidgetProvider {

    private RemoteViews remoteViews = null;
    private static final String[] weekName = {"无","一", "二", "三", "四", "五", "六", "天"};
    private static int currentDay = 3 ;    //当前选择 是星期几


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtils.i("onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        LogUtils.i("====onUpdate");

        setRemoteView(context);
        currentDay = WidgetCommonUtil.getTodayInWeek();
        updateDataView(context, appWidgetIds[0]);   //固定[0]

        //添加点击事件
        SetOnClickPendingIntent(context,R.id.widget_btn_nextday, WidgetCommonUtil.WidgetConstant.INTENT_NEXTDAY);
        SetOnClickPendingIntent(context,R.id.widget_btn_preday, WidgetCommonUtil.WidgetConstant.INTENT_PREDAY);
        SetOnClickPendingIntent(context,R.id.widget_tv_weekday, WidgetCommonUtil.WidgetConstant.INTENT_NEXTDAY); //点击星期几也显示明天的

//        //更新全部widget的界面，这个代码没效果，不过以后如果有遇到其他代码更新无效的情况可以试试
//        for (int widgetId : appWidgetIds) {
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//        }

        //  item点击事件调到app，与WidgetListProviderFactory的setOnClickFillInIntent()共同使用
        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.desktop_widget_list, clickPI);
    }

    /**
     * 初始化数据和适配器等
     * @param context
     * @param appWidgetId
     */
    private void updateDataView(Context context, int appWidgetId) {
        setRemoteView(context);
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);  //固定的
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));      //固定的
        remoteViews.setRemoteAdapter(R.id.desktop_widget_list,intent);


        remoteViews.setEmptyView(R.id.desktop_widget_list,R.id.desktop_widget_empty_view);

        //设置学院文本
        BasicInfo basicInfo = DataSupport.find(BasicInfo.class, MeFragment.localId);
        if(basicInfo != null && !TextUtils.isEmpty(basicInfo.getDepartment() )){
            remoteViews.setTextViewText(R.id.widget_tv_department,basicInfo.getDepartment());
        }else{
            remoteViews.setTextViewText(R.id.widget_tv_department,"学院获取失败");
        }
        notifyUpdateUI(context);

    }

    /**
     * 创建 remoteViews 对象，因多次使用而放这里，减少实例数
     * @param context Context
     */
    private void setRemoteView(Context context) {
        if(remoteViews == null){
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.desktop_widget); //获取listview对应视图
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        LogUtils.i(intent.getAction());

        //更换日期且更新对应数据
        if (intent.getAction().equals(WidgetCommonUtil.WidgetConstant.INTENT_NEXTDAY)){
            currentDay = WidgetCommonUtil.getNextDay(currentDay);
            WidgetListProviderFactory.refreshData(currentDay);
        }else if (intent.getAction().equals(WidgetCommonUtil.WidgetConstant.INTENT_PREDAY)) {
            currentDay = WidgetCommonUtil.getPreDay(currentDay);
            WidgetListProviderFactory.refreshData(currentDay);
        }else{ //其他广播不需要处理
        }

        LogUtils.i("after change currentDay:"+currentDay + " "+ "星期" + weekName[currentDay]);

        //更新 星期几 的文字说明
        setRemoteView(context);
        remoteViews.setTextViewText(R.id.widget_tv_weekday,"星期" + weekName[currentDay]);
        notifyUpdateUI(context);
        notifyListUpdateUI(context);

    }

    /**
     * 通知界面去更新UI，需要在修改UI后调用（不含列表数据）
     * @param context
     */
    private void notifyUpdateUI(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, CourseAppWidgetProvider.class), remoteViews);
    }
    /**
     * 通知界面去更新列表的UI，需要在修改列表数据后调用
     *  notifyAppWidgetViewDataChanged()会调用WidgetListProviderFactory的onDataSetChanged()方法
     * @param context
     */
    private void notifyListUpdateUI(Context context) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        ComponentName cn = new ComponentName(context, CourseAppWidgetProvider.class);
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.desktop_widget_list);
    }

    /**
     * 为 指定id 的控件设置点击时发送action广播，在 onReceive 里会响应事件
     * @param context Context
     * @param viewId int 控件id
     * @param actionName String action名，来自 WidgetCommonUtil.WidgetConstant
     */
    private void SetOnClickPendingIntent(Context context,int viewId,String actionName){
        setRemoteView(context);

        Intent intent = new Intent(actionName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2333, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
    }
}
