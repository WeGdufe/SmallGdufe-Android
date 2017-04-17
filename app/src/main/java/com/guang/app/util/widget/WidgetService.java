package com.guang.app.util.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * 后台服务，获取数据
 * Created by xiaoguang on 2017/4/15.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        int appWidgetId = intent.getIntExtra(
//                AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);
        return new WidgetListProviderFactory(this.getApplicationContext(), intent);
    }

}

