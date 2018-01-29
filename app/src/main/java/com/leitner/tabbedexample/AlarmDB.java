package com.leitner.tabbedexample;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Switch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static java.lang.System.in;

/**
 * Created by i0004913 on 20.10.2017.
 */

public class AlarmDB extends SQLiteOpenHelper {

    public static final String TABLE_ALARMS = "ALARMS";

    public static final String _ID = "_id";
    public static final String ALARM_TITLE = "alarm_title";
    public static final String ALARM_BODY_ENGLISH = "alarm_body_english";
    public static final String ALARM_BODY_GERMAN = "alarm_body_german";
    public static final String ALARM_BODY_ITALIAN = "alarm_body_italian";
    public static final String ALARM_WHEN = "alarm_when";
    public static final String ALARM_GONE = "alarm_gone";
    public static final String ALARM_TYPE = "alarm_type";
    public static final String ALARM_IS_BRIDGEABLE = "alarm_is_bridgeable";
    public static final String ALARM_IS_ACTUAL = "alarm_is_actual";
    public static final String ALARM_MSG_ID = "alarm_msg_id";

    static final String DB_NAME = "ALARMAPP_ALARMS.DB";

    static final int DB_VERSION = 1;

    public AlarmDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        Log.d("DBCONTEXT", context.toString());
    }

    private static final String CREATE_TABLE = "create table " + TABLE_ALARMS + " ("
     + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ALARM_TITLE + " TEXT NOT NULL, "
            + ALARM_BODY_ENGLISH + " TEXT NOT NULL, "
            + ALARM_BODY_GERMAN + " TEXT NOT NULL, "
            + ALARM_BODY_ITALIAN + " TEXT NOT NULL, "
            + ALARM_WHEN + " DATETIME NOT NULL, "
            + ALARM_GONE + " DATETIME NOT NULL, "
            + ALARM_TYPE + " INT NOT NULL, "
            + ALARM_IS_BRIDGEABLE + " BOOL NOT NULL, "
            + ALARM_IS_ACTUAL + " BOOL NOT NULL, "
            + ALARM_MSG_ID + " TEXT NOT NULL)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE", "onCreate");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("delete from "+ TABLE_ALARMS);
    }

    public int addOrUpdateAlarm(Alarm alarm, boolean isActual){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID FROM " + TABLE_ALARMS + " WHERE " + ALARM_MSG_ID + "='" + alarm.getMsgID() + "' AND " + ALARM_IS_ACTUAL + "=1;", null);
        int i = 0;
        if (cursor.moveToFirst()){
            do{
                i++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (i == 0){
            ContentValues values = new ContentValues();
            values.put(ALARM_TITLE, alarm.getTitle());
            values.put(ALARM_BODY_ENGLISH, alarm.getBodyEnglish());
            values.put(ALARM_BODY_GERMAN, alarm.getBodyGerman());
            values.put(ALARM_BODY_ITALIAN, alarm.getBodyItalian());
            values.put(ALARM_WHEN, alarm.getWhenFormatted());
            values.put(ALARM_GONE, alarm.getGoneFormatted());
            values.put(ALARM_TYPE, alarm.getType());
            if (alarm.getIsbridgeable()){
                Log.d("add", "bridgeable");
                values.put(ALARM_IS_BRIDGEABLE, 1);
            }
            else{
                Log.d("add", "not bridgeable");
                values.put(ALARM_IS_BRIDGEABLE, 0);
            }
            if (isActual)
                values.put(ALARM_IS_ACTUAL, 1);
            else
                values.put(ALARM_IS_ACTUAL, 0);
            values.put(ALARM_MSG_ID, alarm.getMsgID());
            long result = db.insert(TABLE_ALARMS, null, values);
            Log.d("DBSET", String.valueOf(result));
            db.close();
            return (int)result;
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put(ALARM_WHEN, alarm.getWhenFormatted());
            contentValues.put(ALARM_TYPE, alarm.getType());
            long index = db.update(TABLE_ALARMS, contentValues,ALARM_MSG_ID+"='"+alarm.getMsgID()+"' AND " + ALARM_IS_ACTUAL + "=1", null);
            cursor = db.rawQuery("SELECT _ID FROM " + TABLE_ALARMS + " WHERE " + ALARM_MSG_ID + "='" + alarm.getMsgID() + "' AND " + ALARM_IS_ACTUAL + "=1;", null);
            index = 0;
            if (cursor.moveToFirst()){
                do{
                    index = cursor.getInt(0);
                }while (cursor.moveToNext());
            }
            Log.d("DBSET", String.valueOf(index));
            db.close();
            return (int)index;
        }
    }

    public List<Integer> setHistoric(Alarm alarm){
        List<Integer> indexes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARM_IS_ACTUAL, 0);
        values.put(ALARM_GONE, alarm.getGoneFormatted());
//        db.update(TABLE_ALARMS, values, ALARM_WHEN + "='" + alarm.getWhenFormatted()+"' AND " + ALARM_MSG_ID + "='" + alarm.getMsgID() + "'", null);
//        db.update(TABLE_ALARMS, values, ALARM_MSG_ID + "='" + alarm.getMsgID() + "' AND " + ALARM_WHEN + "='" + alarm.getWhenFormatted() + "'", null);
        db.update(TABLE_ALARMS, values, ALARM_MSG_ID + "='" + alarm.getMsgID() + "'", null);
        String updateQuery = "UPDATE " + TABLE_ALARMS + " SET " + ALARM_IS_ACTUAL + "=0, " +
                ALARM_GONE +  "='" + alarm.getGoneFormatted() + "' where " + ALARM_WHEN + "='" + alarm.getWhenFormatted()+"' AND " + ALARM_MSG_ID + "='" + alarm.getMsgID() + "';";
//        Cursor cursor = db.rawQuery("SELECT _ID FROM " + TABLE_ALARMS + " WHERE " + ALARM_WHEN + "='" + alarm.getWhenFormatted() + "' AND " + ALARM_MSG_ID + "='" + alarm.getMsgID() + "';", null);
        Cursor cursor = db.rawQuery("SELECT _ID FROM " + TABLE_ALARMS + " WHERE " + ALARM_MSG_ID + "='" + alarm.getMsgID() + "';", null);
        int i = 0;
        if (cursor.moveToFirst()){
            do{
                indexes.add(cursor.getInt(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        for (int index : indexes){
            Log.d("DBSET", String.valueOf(index));
        }
        return indexes;
//        db.update(updateQuery, null);

    }

    public List<Integer> setHistorybyMsgID(String msgID, String gone){
        List<Integer> indexes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT _ID FROM " + TABLE_ALARMS + " WHERE " + ALARM_MSG_ID + "='" + msgID + "' AND " + ALARM_IS_ACTUAL + "=1;", null);
        if (cursor.moveToFirst()){
            do{
                indexes.add(cursor.getInt(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        ContentValues values = new ContentValues();
        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        try{
            Date date = originalFormat.parse(gone);
            gone = targetFormat.format(date);
        }catch (ParseException e){}
        values.put(ALARM_GONE, gone);
        values.put(ALARM_IS_ACTUAL, 0);
        db.update(TABLE_ALARMS, values, ALARM_MSG_ID+"='"+msgID+"' AND " + ALARM_IS_ACTUAL + "=1", null);
        return indexes;
    }

    public void setAllAlarmsHistoric(String gone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARM_GONE, "");
        values.put(ALARM_IS_ACTUAL, 0);
        db.update(TABLE_ALARMS, values, ALARM_IS_ACTUAL + "=1", null);
    }

    public void clearAlarms(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + TABLE_ALARMS);
    }

    public Alarm getAlarm(int id){
        String ALARM_BODY_ACTUAL_LANGUAGE = "alarm_body_english";
        SQLiteDatabase db = this.getReadableDatabase();

        switch (Locale.getDefault().getLanguage()){
            case "de":
                ALARM_BODY_ACTUAL_LANGUAGE = "alarm_body_german";
            case "it":
                ALARM_BODY_ACTUAL_LANGUAGE = "alarm_body_italian";
            default:
                ALARM_BODY_ACTUAL_LANGUAGE = "alarm_body_english";
        }
        Cursor cursor = db.query(TABLE_ALARMS, new String[]{_ID, ALARM_TITLE, ALARM_BODY_ENGLISH, ALARM_BODY_GERMAN, ALARM_BODY_ITALIAN, ALARM_WHEN, ALARM_TYPE, ALARM_IS_BRIDGEABLE },
                                    _ID + "=?",
                                    new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = "";
        String formattedDateGone = "";
//                    formattedDate = targetFormat.format(cursor.getString(5));

        try{
            Date date = originalFormat.parse(cursor.getString(5));
            formattedDate = targetFormat.format(date);
            date = originalFormat.parse(cursor.getString(6));
            formattedDateGone = targetFormat.format(date);
        } catch (ParseException e){
            Log.d("EXCEPTION", "Errorr", e);
        }

        Alarm alarm = new Alarm(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                formattedDate,
                Integer.parseInt(cursor.getString(5)),
                Boolean.getBoolean(cursor.getString(6)),
                formattedDateGone);
        cursor.close();
        return alarm;
    }

    public ArrayList<Alarm> getAllAlarmsActual(){
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();

        String selectQuery = "SELECT * FROM " + TABLE_ALARMS + " WHERE alarm_is_actual = 1 order by " + ALARM_WHEN + " DESC LIMIT 200;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String formattedDate = "";
                String formattedDateGone = "";
//                    formattedDate = targetFormat.format(cursor.getString(5));

                try{
                    Date date = originalFormat.parse(cursor.getString(5));
                    formattedDate = targetFormat.format(date);
                    date = originalFormat.parse(cursor.getString(6));
                    formattedDateGone = targetFormat.format(date);
                } catch (ParseException e){
                    Log.d("EXCEPTION", "Errorr", e);
                    formattedDateGone = "01.01.1970 00:00.00";
                }
//                Log.d("GETALARM", cursor.getColumnName(1));
//                Log.d("GETALARM", cursor.getColumnName(2));
//                Log.d("GETALARM", cursor.getColumnName(3));
//                Log.d("GETALARM", cursor.getColumnName(4));
//                Log.d("GETALARM", cursor.getColumnName(5));
//                Log.d("GETALARM", cursor.getColumnName(6));
//                Log.d("GETALARM", cursor.getColumnName(7));
//                Log.d("GETALARM", cursor.getColumnName(8));
//                Log.d("GETALARM", cursor.getColumnName(9));
//                Log.d("GETALARM", cursor.getColumnName(10));

                boolean isBridgeable = false;
                if (cursor.getString(8).equals("1"))
                    isBridgeable = true;
                else
                    isBridgeable = false;

                Alarm alarm = new Alarm(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        formattedDate,
                        Integer.parseInt(cursor.getString(7)),
                        isBridgeable,
                        cursor.getString(10),
                        formattedDateGone);
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarmList;
    }

    public int getAlarmsActualCount(){
        String countQuery = "SELECT * FROM " + TABLE_ALARMS + " WHERE " + ALARM_IS_ACTUAL + "='1';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<Alarm> getAllAlarmsHistoric(){
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();

        String selectQuery = "SELECT * FROM " + TABLE_ALARMS + " WHERE alarm_is_actual = 0 order by " + ALARM_WHEN + " DESC LIMIT 200;";
        Log.d("SELECT", selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String formattedDate = "";
                String formattedDateGone = "";
//                    formattedDate = targetFormat.format(cursor.getString(5));

                try{
                    Date date = originalFormat.parse(cursor.getString(5));
                    formattedDate = targetFormat.format(date);
                    date = originalFormat.parse(cursor.getString(6));
                    formattedDateGone = targetFormat.format(date);
                } catch (ParseException e){
                    Log.d("EXCEPTION", "Errorr", e);
                    formattedDateGone = "01.01.1970 00:00.00";
                }
                Log.d("GETALARM", cursor.getColumnName(8));
                Log.d("GETALARM", cursor.getString(8));
                Log.d("GETALARM", String.valueOf(Boolean.valueOf(cursor.getString(8))));
                Boolean isBridgeable;
                if (cursor.getString(8).equals("1"))
                    isBridgeable = true;
                else
                    isBridgeable = false;
                Alarm alarm = new Alarm(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        formattedDate,
                        Integer.parseInt(cursor.getString(7)),
                        isBridgeable,
                        cursor.getString(10),
                        formattedDateGone);
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("HISCOUNT", String.valueOf(alarmList.size()));
        return alarmList;
    }

    public void deleteItemsFromTwoDaysAgo(){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_ALARMS + " WHERE " + ALARM_WHEN + " <= date('now', '-2 day') AND "+ALARM_GONE +" ;";
        db.execSQL(deleteQuery);
        db.close();
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DROP TABLE IF EXISTS " + TABLE_ALARMS + ";";
        db.execSQL(deleteQuery);
        db.close();
    }
}
