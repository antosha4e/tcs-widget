package com.tcswidget.activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.tcswidget.R;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void parse(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        Uri uriSMSURI = Uri.parse("content://sms/inbox");

        String selection = "address = 'TCS Bank'";

        Cursor cur = getContentResolver().query(uriSMSURI, new String[] { "_id", "address", "date",
                "body", "read" }, selection, null, null);
        String sms = "";
        int i = 0;
        while(cur != null && cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            if(body != null && body.contains("Dostupno")) {
                int i1 = body.indexOf("Dostupno"), i2 = body.indexOf("RUB", i1);
                sms = body.substring(i1, i2 + 3);
                break;
            }
            if(i++ > 20) break;
//            sms += "From :" + cur.getString(cur.getColumnIndexOrThrow("address")) + " : " + cur.getString(cur.getColumnIndexOrThrow("body")) +"\n";
        }
        view.setText(sms);
        setContentView(view);
    }
}
