package com.example.timesayer.reminders;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.timesayer.MainActivity;
import com.example.timesayer.R;
import com.example.timesayer.TodoDatabase;

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    public ReminderAlarmService() {
        super(TAG);
    }

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        Intent action = new Intent(this, MainActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        TodoDatabase db = new TodoDatabase(getApplicationContext());
        db.open();

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()){
                description = db.getData();
            }
        }finally{
            if (cursor !=null) {
                cursor.close();
                db.close();
            }
        }
        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle("Task Reminder")
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_eyecon)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);
    }

}
