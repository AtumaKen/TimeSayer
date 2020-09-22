package com.example.timesayer.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;

public class AlarmScheduler {

    public void scheduleAlarm(Context context, long alarmTime, Uri reminderTask){
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask);
        manager.setExact(AlarmManager.RTC, alarmTime,operation);
    }
}
