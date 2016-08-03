package com.example.android.sunshine.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.sync.SunshineSyncAdapter;

/**
 * Created by Jin Yoon on 8/2/2016.
 */
public class TodayWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for(int appWidgetId: appWidgetIds){
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_today_small);
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
        context.startService(new Intent(context, TodayWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, TodayWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if(SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())){
            context.startService(new Intent(context, TodayWidgetIntentService.class));
        }
    }
}
