package com.zonkey.chorepocalypse.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
