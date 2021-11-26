package com.alphawallet.app.widget.HomeScreenWidget;

import static com.alphawallet.app.service.TickerUpdateService.LOCATION.UPDATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.alphawallet.app.service.TickerUpdateService;

public class TickerWidgetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TickerUpdateService.class);
        String action = intent.getAction();
        int widgetId = intent.getIntExtra("id", 0);
        int state = intent.getIntExtra("state", 0);

        i.putExtra("id", widgetId);
        i.putExtra("state", state);

        if (action != null) {
            if (action.equals("android.intent.action.BOOT_COMPLETED") || action.equals("android.intent.action.QUICKBOOT_POWERON")) {
                Log.i("AUTOMATON", action);
                action = String.valueOf(UPDATE.ordinal());
            }

            i.setAction(action);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(i);
        } else {
            context.startService(i);
        }
    }
}