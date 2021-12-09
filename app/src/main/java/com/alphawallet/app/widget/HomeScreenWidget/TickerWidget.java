package com.alphawallet.app.widget.HomeScreenWidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.alphawallet.app.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TickerWidgetConfigureActivity TickerWidgetConfigureActivity}
 */
public class TickerWidget extends AppWidgetProvider {

    @SuppressLint("NewApi")
    private RemoteViews inflateLayout(Context context, int appWidgetId)
    {
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction("startWidget" + appWidgetId);
        resultIntent.putExtra("id", appWidgetId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent rPI = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_crypto);
        remoteViews.setOnClickPendingIntent(R.id.relLayout, rPI);

        //Call the service
        Intent service = new Intent(context, CryptoUpdateService.class);
        service.setAction(String.valueOf(CryptoUpdateService.LOCATION.UPDATE.ordinal()));
        context.startService(service);

        return remoteViews;
    }

    @SuppressLint("NewApi")
    private void setRemoteView(AppWidgetManager appWidgetManager, Context context,
                               int appWidgetId, RemoteViews remoteView)
    {
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction("startWidget" + appWidgetId);
        resultIntent.putExtra("id", appWidgetId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent rPI = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.relLayout, rPI);
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
    }

    private RemoteViews getRemoteViewFromState(Context context, int widgetId)
    {
        RemoteViews remoteView = inflateLayout(context, widgetId);
        Intent startIntent = new Intent(context, xyz.automatons.cryptowidget.CryptoUpdateService.class);
        startIntent.setAction(String.valueOf(ACTION_TOGGLE.ordinal()));
        return remoteView;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = TickerWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ticker_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            TickerWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}