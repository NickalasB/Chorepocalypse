package com.zonkey.chorepocalypse.ui.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.ui.activities.MainActivity;

/**
 * Created by nickbradshaw on 2/25/17.
 */

public class ChoreWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.chore_widget_layout);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.chore_widget_frame_layout, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }

            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = android.support.v4.app.TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            views.setEmptyView(R.id.widget_list, R.id.widget_empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            super.onUpdate(context, appWidgetManager, appWidgetIds);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
        }
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context.getApplicationContext(), DetailWidgetRemoteViewsService.class));
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context.getApplicationContext(), DetailWidgetRemoteViewsService.class));
    }
}
