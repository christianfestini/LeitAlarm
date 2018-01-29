package com.leitner.tabbedexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by i0004913 on 24.01.2018.
 */

public class UpdateBotClientStatus extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean("server_online", false).commit();
        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        if (sharedPreferences.getBoolean("application_foreground", false)){
            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (Tab3Settings.imageViewBotClientStatus != null){
                        if (sharedPreferences.getBoolean("server_online", false))
                            Tab3Settings.imageViewBotClientStatus.setImageResource(R.mipmap.server_online);
                        else
                            Tab3Settings.imageViewBotClientStatus.setImageResource(R.mipmap.server_offline);
                    }
                }
            });
        }

        wl.release();
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
