package com.example.learnenglish;

import static com.example.learnenglish.home.MainActivity.createNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String body = intent.getStringExtra("body");
        createNotification(context, body);
    }
}
