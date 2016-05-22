package com.example.caseyjones.mydictionary.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.caseyjones.mydictionary.broadcast.UpdateBroadcastReceiver;


/**
 * Created by CaseyJones on 13.05.2016.
 */
public class App extends Application {


    public static final int TEN_SECOND = 10000;
    public static final int SIX_HOUR = 21600000;
    private static final int ONE_MINUTE = 60000;

    @Override
    public void onCreate() {
        super.onCreate();
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, UpdateBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ONE_MINUTE, SIX_HOUR, pi);
    }
}
