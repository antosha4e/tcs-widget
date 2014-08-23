package com.tcswidget.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import com.tcswidget.R;
import com.tcswidget.data.DataManager;

import java.util.Arrays;

/**
 * User: antosha
 * Date: 7/4/14
 */
public class TcsWidget extends AppWidgetProvider {
    private static final String TAG = TcsWidget.class.getName();
    public static String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
//    private static final String TCS_NAME = "TCS Bank";
//    private static final String TCS_NAME = "123";
    private static final String TCS_NAME = Build.FINGERPRINT.startsWith("generic") ? "123" : "TCS Bank";


    //SMS-kod: 2553 Operatsiya: vhod v Internet-bank. Nikomu ne govorite etot kod! www.tcsbank.ru


    private DataManager dataManager = DataManager.getInstance();

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context, "Widget created", Toast.LENGTH_SHORT).show();
        init(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews;
//        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
//        watchWidget = new ComponentName(context, TcsWidget.class);
        String balance = dataManager.getBalance("card1");
        balance = balance == null ? "0" : balance;
//        remoteViews.setTextViewText(R.id.textView1, balance);
        //appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_LONG).show();
        //Log.e(TcsWidget.class.getName(), "SUKA!!!!! - " + intent.getAction());
        //Ловим наш Broadcast, проверяем и выводим сообщение
        final String action = intent.getAction();
        String balance = "";

        //Telephony.Sms.Intents.SMS_RECEIVED_ACTION
        if(SMS_RECEIVED.equals(action)) {
//            Log.e(TcsWidget.class.getName(), "SMS CAME!");
            //---get the SMS message passed in---
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg = "", from = "";

            if (bundle != null) {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    from = msgs[i].getOriginatingAddress();
                    msg = msgs[i].getMessageBody();
                    if(TCS_NAME.equalsIgnoreCase(from)) {
                        balance = parseMsg(msg);
                        if(balance != null) {
                            setText(context, balance);
                        }
                    }
                }

                Log.e(TAG, "FROM: " + from);
                Log.e(TAG, "MSG: " + msg);
            }
        }
        super.onReceive(context, intent);
    }

    private String parseMsg(String msg) {
        String balance = null;
        if(msg != null && msg.contains("Dostupno")) {
            int i1 = msg.indexOf("Dostupno"), i2 = msg.indexOf("RUB", i1);
            if(i1 > -1 && i2 > -1) {
                balance = msg.substring(i1 + 8, i2 + 3);
            }
        }
        return balance;
    }

    private void init(Context context) {
//        TextView view = new TextView(this);
        Uri uriSMSURI = Uri.parse("content://sms/inbox");

        String selection = "address = '"+ TCS_NAME +"'";

        Cursor cur = context.getContentResolver().query(uriSMSURI, new String[] {"_id", "address", "date", "body", "read"}, selection, null, "date desc limit 100");
        String sms = "";
        int i = 0;
        while(cur != null && cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            Log.e(TAG, "BODY: " + body);
            body = parseMsg(body);
            if(body != null) {
                setText(context, body);
                break;
            }
        }
    }

    private void setText(Context context, String balance) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, TcsWidget.class));
//            Log.e(TAG, "IDs: " + Arrays.toString(appWidgetIds));
        remoteViews.setTextViewText(R.id.textView1, balance);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        Log.e(TAG, "MSG: kinda update view");
    }
}