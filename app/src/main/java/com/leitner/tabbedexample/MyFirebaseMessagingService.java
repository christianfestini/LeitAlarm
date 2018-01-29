package com.leitner.tabbedexample;

/**
 * Created by i0004913 on 12.10.2017.
 */

import android.*;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFireBaseMsgService";

    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.sgetNotification().getBody());

//        sendNotification(remoteMessage);

        final RemoteMessage rm = remoteMessage;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String messageBody = "";
//        switch (Locale.getDefault().getLanguage()){
//            case "de":
//                messageBody = rm.getData().get("deu");
//                break;
//            case "it":
//                messageBody = rm.getData().get("ita");
//                break;
//            default:
//                messageBody = rm.getData().get("eng");
//                break;
//        }
//        final String actualError = messageBody;#
//        ArrayList<Alarm> jsonArray = new ArrayList<Alarm>();
//        if (sharedPreferences.contains("actual_alarms")){
//            sharedPreferences.getString("actual_alarms", null);
//
//            try {
//                JSONArray fromPrefs = new JSONArray(sharedPreferences.getString("actual_alarms", null));
//                for (int i = 0; i < fromPrefs.length(); i++) {
//                    JSONObject object = fromPrefs.getJSONObject(i);
//                    jsonArray.add(new Alarm(object.getString("title"), object.getString("en"), object.getString("de"), object.getString("it"), object.getString("when"), object.getInt("type"), object.getBoolean("isBridgeable")));
//                }
//            }catch (JSONException e){}
//        }
        Alarm a;
        AlarmDB db = new AlarmDB(getApplicationContext());
        db.getWritableDatabase();
        int notificationID = 0;
        if (rm.getData().containsKey("status")){
            editor.putBoolean("server_online", Boolean.parseBoolean(rm.getData().get("status"))).commit();
            if (sharedPreferences.getBoolean("application_foreground", false)){
                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (Tab3Settings.imageViewBotClientStatus != null){
                            if (sharedPreferences.getBoolean("server_online", false)){
                                Tab3Settings.imageViewBotClientStatus.setImageResource(R.mipmap.server_online);
//                                UpdateBotClientStatus alarm = new UpdateBotClientStatus();
//                                alarm.cancelAlarm(getApplicationContext());
//                                alarm.setAlarm(getApplicationContext());
                            }
                            else
                                Tab3Settings.imageViewBotClientStatus.setImageResource(R.mipmap.server_offline);
                        }
                    }
                });
            }
        }
        else{
        if (rm.getData().containsKey("deleteAll")){
            //Remove all alarms from actual
            db.setAllAlarmsHistoric("");
            cancelAll();
            if (sharedPreferences.getBoolean("application_foreground", false)){

                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "all archived", Toast.LENGTH_LONG).show();
                    }
                });
            }

            //If application is running in foreground refresh views
            if (sharedPreferences.getBoolean("application_foreground", false)) {
                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AlarmDB db = new AlarmDB(getApplicationContext());
                        db.getReadableDatabase();
                        if (!Tab1Actual.adapter.isEmpty()) {
                            Tab1Actual.adapter.clear();
                        }
                        Tab1Actual.adapter.addAll(db.getAllAlarmsActual());
                        if (Tab1Actual.adapter.getCount() >= 1)
                            Tab1Actual.noAlarms.setVisibility(View.INVISIBLE);
                        else
                            Tab1Actual.noAlarms.setVisibility(View.VISIBLE);
                        Log.d("RM", "actual");
                        if (!Tab2History.adapter.isEmpty()) {
                            Tab2History.adapter.clear();
                        }

                        Tab2History.adapter.addAll(db.getAllAlarmsHistoric());
                        Log.d("RM", "history");
                        db.close();
                    }
                });
            }
        }
        else if (rm.getData().get("gone").equals("01/01/1970 00:00:00") || rm.getData().get("gone").equals("01.01.1970 00:00:00")){
            Log.d("RMBRIDGEABLE", String.valueOf(Boolean.valueOf(rm.getData().get("isBridgeable"))));
            Log.d("TYPE", rm.getData().get("type"));
            //New incoming alarm
            //Alarm gets created and added to database, in return we get a notificationID which will be used to fire the notification
            a = new Alarm(rm.getData().get("title"),
                    rm.getData().get("en"),
                    rm.getData().get("de"),
                    rm.getData().get("it"),
                    rm.getData().get("when"),
                    Integer.parseInt(rm.getData().get("type")),
                    Boolean.valueOf(rm.getData().get("isBridgeable")),
                    rm.getData().get("msgID"),
                    rm.getData().get("gone"));
            notificationID = db.addOrUpdateAlarm(a, true);
            Log.d("RM", "actualadded" + notificationID);

            if (sharedPreferences.getBoolean("application_foreground", false)){
                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AlarmDB db = new AlarmDB(getApplicationContext());
                        db.getReadableDatabase();
                        if (!Tab1Actual.adapter.isEmpty()){
                            Tab1Actual.adapter.clear();
                        }
                        Tab1Actual.adapter.addAll(db.getAllAlarmsActual());
                        if (Tab1Actual.adapter.getCount() >= 1)
                            Tab1Actual.noAlarms.setVisibility(View.INVISIBLE);
                        else
                            Tab1Actual.noAlarms.setVisibility(View.VISIBLE);
                        Log.d("RM", "actual");
                        if (!Tab2History.adapter.isEmpty()){
                            Tab2History.adapter.clear();
                        }

                        Tab2History.adapter.addAll(db.getAllAlarmsHistoric());
                        Log.d("RM", "history");
                        db.close();
                    }
                });
            }
            else {
                //If google calendar events shall be used to fire notifications
                if (sharedPreferences.getBoolean("use_calendar", false)) {
                    Cursor mCursor = null;
                    //Check if any relevant calendar events are present to fire the notification
                    final String[] Cols = new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};
                    try {
                        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_CALENDAR);
                        permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CALENDAR);

                        mCursor = getApplicationContext().getContentResolver().query(CalendarContract.Events.CONTENT_URI, Cols, CalendarContract.Events.DTSTART + " BETWEEN '" + (System.currentTimeMillis() - 86400000) +
                                "' AND '" + (System.currentTimeMillis() + 86400000) + "' AND " + CalendarContract.Events.TITLE + " LIKE '%LeitAlarm%'", null, null);
                        mCursor.moveToFirst();
                        Log.d("CALENDAR", "in here");
                        Log.d("CALENDAR", String.valueOf(mCursor.getColumnCount()));
                        if (mCursor.moveToFirst()) {
                            do {
                                if (System.currentTimeMillis() > Long.parseLong(mCursor.getString(1)) && System.currentTimeMillis() < Long.parseLong(mCursor.getString(2))) {
                                    //Log.d("CALENDAR", mCursor.getString(0) + " is happening right now!");
                                    //A relevant event was found in the application and a notification gets fired
                                    if (notificationID != 0)
                                        sendNotification(a, notificationID, sharedPreferences.getString("resort", ""), a.getType());
                                }
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(Long.parseLong(mCursor.getString(1)));
                                Log.d("CALENDAR", mCursor.getString(0) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.YEAR));
                            } while (mCursor.moveToNext());
                        } else {
                            Log.d("CALENDAR", "No event in the specified time found");
                        }
                        mCursor.close();
                    } catch (SecurityException se) {
                        Log.d("CALENDAR", se.getMessage());
                    }
                } else {
                    //If notifications will be fired based on schedule selected in settings fragment
                    if (sharedPreferences.getBoolean("mute_notification", false)) {
                        try {
//                            Date timeFrom = new SimpleDateFormat("HH:mm").parse(sharedPreferences.getString("time_from", null));
//                            Calendar calendar1 = Calendar.getInstance();
//                            calendar1.setTime(timeFrom);
//                            Date timeTo = new SimpleDateFormat("HH:mm").parse(sharedPreferences.getString("time_to", null));
//                            Calendar calendar2 = Calendar.getInstance();
//                            calendar2.setTime(timeTo);
//                            calendar2.add(Calendar.DATE, 1);
//
//                            Calendar calendar3 = Calendar.getInstance();
//                            Calendar calendar = Calendar.getInstance();
//                            String actualTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
//                            Date date3 = new SimpleDateFormat("HH:mm").parse(actualTime);
//                            calendar3.setTime(date3);
//                            calendar3.add(Calendar.DATE, 1);
//
//                            Date x = calendar3.getTime();
//                            Date from = calendar1.getTime();
//                            Date to = calendar2.getTime();
//                            if (x.before(calendar2.getTime()) && x.after(calendar1.getTime())) {
//                                if (notificationID != 0)
//                                    sendNotification(a, notificationID, sharedPreferences.getString("resort", ""), a.getType());
//                            }
//                            else
//                                Log.d("TIME", "somethings wrong");
                            String string1 = sharedPreferences.getString("time_from", "");
                            Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(time1);

                            String string2 = sharedPreferences.getString("time_to", "");
                            Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTime(time2);
                            calendar2.add(Calendar.DATE, 1);

                            Calendar calendar = Calendar.getInstance();
                            String actualTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                            Date d = new SimpleDateFormat("HH:mm:ss").parse(actualTime);
                            Calendar calendar3 = Calendar.getInstance();
                            calendar3.setTime(d);
                            calendar3.add(Calendar.DATE, 1);

                            Date x = calendar3.getTime();
                            if (isTimeBetweenTwoTime(calendar1.getTime().toString(), calendar2.getTime().toString(), x.toString())){
                                Log.d("time", "true");
                                sendNotification(a, notificationID, sharedPreferences.getString("resort", ""), a.getType());
                            }
                            else
                                Log.d("time", "false");
                            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                                //checkes whether the current time is between 14:49:00 and 20:11:13.
                                System.out.println(true);
                            }

                        } catch (ParseException e) {
                        }
                    } else {
                        //If notifications are not muted
                        if (notificationID != 0)
                            sendNotification(a, notificationID, sharedPreferences.getString("resort", ""), a.getType());
                    }
                }
            }
        }
        else {
            Log.d("HISTORY", String.valueOf(rm.getData().get("type")));
            Log.d("HISTORY", String.valueOf(rm.getData().get("msgID")));
            Log.d("HISTORY", String.valueOf(rm.getData().get("gone")));
//        if (rm.getData().containsKey("gone")){
            List<Integer> indexes = db.setHistorybyMsgID(rm.getData().get("msgID"), rm.getData().get("gone"));
            if (sharedPreferences.getBoolean("application_foreground", false)){

                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(getApplicationContext(), rm.getData().get("msgID") + " archived", Toast.LENGTH_LONG).show();
                                 }
                });
            }
            if (sharedPreferences.getBoolean("application_foreground", false)){
                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AlarmDB db = new AlarmDB(getApplicationContext());
                        db.getReadableDatabase();
                        if (!Tab1Actual.adapter.isEmpty()){
                            Tab1Actual.adapter.clear();
                        }
                        Tab1Actual.adapter.addAll(db.getAllAlarmsActual());
                        if (Tab1Actual.adapter.getCount() >= 1)
                            Tab1Actual.noAlarms.setVisibility(View.INVISIBLE);
                        else
                            Tab1Actual.noAlarms.setVisibility(View.VISIBLE);
                        Log.d("RM", "actual");
                        if (!Tab2History.adapter.isEmpty()){
                            Tab2History.adapter.clear();
                        }

                        Tab2History.adapter.addAll(db.getAllAlarmsHistoric());
                        Log.d("RM", "history");
                        db.close();
                    }
                });
            }
            for (int index : indexes) {
                Log.d("RM", "sethistoric" + index);
                cancelNotification(index);
            }
        }
//        }
//        else {
//            a = new Alarm(rm.getData().get("title"),
//                    rm.getData().get("en"),
//                    rm.getData().get("de"),
//                    rm.getData().get("it"),
//                    rm.getData().get("when"),
//                    Integer.parseInt(rm.getData().get("type")),
//                    Boolean.valueOf(rm.getData().get("isBridgeable")),
//                    rm.getData().get("msgID"));
//                    db.addAlarm(a, true);
//        }
//        jsonArray.add(0, new Alarm(rm.getData().get("title"),
//                rm.getData().get("en"),
//                rm.getData().get("de"),
//                rm.getData().get("it"),
//                rm.getData().get("when"),
//                Integer.parseInt(rm.getData().get("type")),
//                Boolean.valueOf(rm.getData().get("isBridgeable"))));
        db.close();


//        JSONArray alarmarray = new JSONArray();
//        Log.d("JSONARRAY", String.valueOf(alarmarray.length()));
//        for (int i = 0; i < jsonArray.size(); i++) {
//            try{
//                alarmarray.put(0, jsonArray.get(i).getJSONObject());
//            }catch (JSONException e){}
//        }
//        editor.putString("actual_alarms", alarmarray.toString());
//        editor.commit();
//        getSharedPreferences("myPrefs", MODE_PRIVATE).edit().putString("actual_alarms", alarmarray.toString()).commit();
        if (sharedPreferences.contains("actual_alarms")){
            Log.d("NULL", "Finally not null");
        }
        else{
            Log.d("NULL", "null");
        }
        }

//        Handler add = new Handler(Looper.getMainLooper());
//        add.post(new Runnable() {
//            @Override
//            public void run() {
//                Tab1Actual.adapter.insert(new Alarm(rm.getData().get("title"),
//                        rm.getData().get("en"),
//                        rm.getData().get("de"),
//                        rm.getData().get("it"),
//                        rm.getData().get("when"),
//                        Integer.parseInt(rm.getData().get("type")),
//                        Boolean.valueOf(rm.getData().get("isBridgeable"))), 0);
//            }
//        });
//        Log.d("APPLICATION STATE", .)




        //TODO: https://medium.com/@Miqubel/mastering-firebase-notifications-36a3ffe57c41
//        Tab1Actual.adapter.notifyDataSetChanged();

    }

    public static boolean isTimeBetweenTwoTime(String argStartTime,
                                               String argEndTime, String argCurrentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        //
        argCurrentTime = argCurrentTime + ":00";
        argStartTime = argStartTime + ":00";
        argEndTime = argEndTime + ":00";
        if (argStartTime.matches(reg) && argEndTime.matches(reg)
                && argCurrentTime.matches(reg)) {
            boolean valid = false;
            // Start Time
            java.util.Date startTime = new SimpleDateFormat("HH:mm:ss")
                    .parse(argStartTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);

            // Current Time
            java.util.Date currentTime = new SimpleDateFormat("HH:mm:ss")
                    .parse(argCurrentTime);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentTime);

            // End Time
            java.util.Date endTime = new SimpleDateFormat("HH:mm:ss")
                    .parse(argEndTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);

            //
            if (currentTime.compareTo(endTime) < 0) {

                currentCalendar.add(Calendar.DATE, 1);
                currentTime = currentCalendar.getTime();

            }

            if (startTime.compareTo(endTime) < 0) {

                startCalendar.add(Calendar.DATE, 1);
                startTime = startCalendar.getTime();

            }
            //
            if (currentTime.before(startTime)) {

                System.out.println(" Time is Lesser ");

                valid = false;
            } else {

                if (currentTime.after(endTime)) {
                    endCalendar.add(Calendar.DATE, 1);
                    endTime = endCalendar.getTime();

                }

                System.out.println("Comparing , Start Time /n " + startTime);
                System.out.println("Comparing , End Time /n " + endTime);
                System.out
                        .println("Comparing , Current Time /n " + currentTime);

                if (currentTime.before(endTime)) {
                    System.out.println("RESULT, Time lies b/w");
                    valid = true;
                } else {
                    valid = false;
                    System.out.println("RESULT, Time does not lies b/w");
                }

            }
            return valid;

        } else {
            throw new IllegalArgumentException(
                    "Not a valid time, expecting HH:MM:SS format");
        }

    }

    private void sendNotification(Alarm alarm, int id, String resort, int type){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//        notificationBuilder.setSmallIcon(R.mipmap.demac_lenko);
        switch (type){
            case 1:
                notificationBuilder.setSmallIcon(R.mipmap.nc_info);
                break;
            case 2:
                notificationBuilder.setSmallIcon(R.mipmap.nc_warn);
                break;
            //alarm
            case 8:
                notificationBuilder.setSmallIcon(R.mipmap.nc_right);
                break;
            //abschaltung
            case 16:
                notificationBuilder.setSmallIcon(R.mipmap.nc_right);
                break;
            //notabschaltung
            default:
                break;
        }
        notificationBuilder.setContentTitle(alarm.getBody());
        notificationBuilder.setContentText(alarm.getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setVibrate(new long[]{1000, 1000});
        notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        notificationBuilder.setContentIntent(pendingIntent);
        if (resort.equals("Prinoth"))
            notificationBuilder.setLights(Color.RED, 500, 500);
        else
            notificationBuilder.setLights(Color.BLUE, 500, 500);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilder.build());
        Log.d("Notification", "notify " + id);

    }

    private void cancelNotification(int id){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
        Log.d("Notification", "cancel " + id);
    }

    private void cancelAll(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


}

