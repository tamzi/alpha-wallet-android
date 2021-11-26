package com.alphawallet.app.widget.HomeScreenWidget;

import static com.alphawallet.app.service.TickerUpdateService.LOCATION.ACTION_TOGGLE;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.alphawallet.app.R;
import com.alphawallet.app.service.TickerUpdateService;
import com.alphawallet.app.widget.CryptoWidget;

public class TickerWidgetProvider extends AppWidgetProvider
{
    @SuppressLint("NewApi")
    private RemoteViews inflateLayout(Context context, int appWidgetId)
    {
        Intent resultIntent = new Intent(context, CryptoWidget.class);
        resultIntent.setAction("startWidget" + appWidgetId);
        resultIntent.putExtra("id", appWidgetId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(CryptoWidget.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent rPI = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_ticker_widget);
        remoteViews.setOnClickPendingIntent(R.id.ticker_widget, rPI);

        //Call the service
        Intent service = new Intent(context, TickerUpdateService.class);
        service.setAction(String.valueOf(TickerUpdateService.LOCATION.UPDATE.ordinal()));
        context.startService(service);

        return remoteViews;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager man, int[] appWidgetIds)
    {
        try
        {
            RemoteViews remoteView;

            //we only refresh the widget here if there's not an active service
            for (int widgetId : appWidgetIds)
            {
                remoteView = getRemoteViewFromState(context, widgetId);

                if (remoteView != null)
                {
                    setRemoteView(man, context, widgetId, remoteView);
                }
            }
        }
        catch (Exception e)
        {

        }

        super.onUpdate(context, man, appWidgetIds);
    }

    @SuppressLint("NewApi")
    private void setRemoteView(AppWidgetManager appWidgetManager, Context context,
                               int appWidgetId, RemoteViews remoteView)
    {
        Intent resultIntent = new Intent(context, CryptoWidget.class);
        resultIntent.setAction("startWidget" + appWidgetId);
        resultIntent.putExtra("id", appWidgetId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(CryptoWidget.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent rPI = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.ticker_widget, rPI);
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
    }

    private RemoteViews getRemoteViewFromState(Context context, int widgetId)
    {
        RemoteViews remoteView = inflateLayout(context, widgetId);
        Intent startIntent = new Intent(context, TickerUpdateService.class);
        startIntent.setAction(String.valueOf(ACTION_TOGGLE.ordinal()));
        return remoteView;
    }
}

