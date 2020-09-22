package com.example.timesayer.reminders;

import android.app.AlarmManager;
import android.content.Context;

public class AlarmManagerProvider {
    private static final String TAG = AlarmManagerProvider.class.getSimpleName();
    private static AlarmManager alarmManager;
    public static synchronized void injectAlarmManager(AlarmManager sAlarmManager){
        if (alarmManager == null){
            throw new IllegalStateException("Alarm Manager Already Set");
        }
        alarmManager = sAlarmManager;
    }

    static synchronized AlarmManager getAlarmManager(Context context){
        if (alarmManager == null){
            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        }
        return alarmManager;
    }
}
