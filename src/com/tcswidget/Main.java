package com.tcswidget;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static AtomicLong counter = new AtomicLong(0);

    public static void main(String[] args) {
        for(int i = 0; i< 1000;i++)
        System.out.println("" + counter.getAndIncrement());
    }
}
