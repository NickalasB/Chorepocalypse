package com.zonkey.chorepocalypse.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.zonkey.chorepocalypse.ui.widget.WidgetListProvider;

public class DetailWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new WidgetListProvider(this.getApplicationContext(), intent));
    }
}
