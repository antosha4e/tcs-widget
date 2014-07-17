package com.tcswidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.tcswidget.R;
import com.tcswidget.data.DataManager;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: antosha
 * Date: 7/4/14
 */
public class TcsWidgetTest extends AppWidgetProvider {
    private static AtomicLong counter = new AtomicLong(0);

    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context, "Widget created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Создаем новый RemoteViews
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        //Подготавливаем Intent для Broadcast
        Intent active = new Intent(context, TcsWidgetTest.class);
        active.setAction(ACTION_WIDGET_RECEIVER);
        active.putExtra("msg", "Hello Habrahabr");

        //создаем наше событие
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

        //регистрируем наше событие
        remoteViews.setOnClickPendingIntent(R.id.textView1, actionPendingIntent);

        //обновляем виджет
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    public void onReceive(Context context, Intent intent) {

        //Ловим наш Broadcast, проверяем и выводим сообщение
        final String action = intent.getAction();
        if(ACTION_WIDGET_RECEIVER.equals(action)) {
            // Manual or automatic widget update started

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            // Update text, images, whatever - here
            remoteViews.setTextViewText(R.id.textView1, "" + counter.getAndIncrement());
//            remoteViews.setTextViewText(R.id.textView1, "" + new Random().nextInt(1000));
//            remoteViews.setTextViewText(R.id.textView1, "" + this);

            // Trigger widget layout update
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, TcsWidgetTest.class), remoteViews);
        }
        super.onReceive(context, intent);
    }

    public void onReceive1(Context context, Intent intent) {

        //Ловим наш Broadcast, проверяем и выводим сообщение
        final String action = intent.getAction();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
            String msg = "null";
            try {
                msg = intent.getStringExtra("msg");
            } catch (Exception e) {
                Log.e("Error", "msg = null, WTF!!!");
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
}