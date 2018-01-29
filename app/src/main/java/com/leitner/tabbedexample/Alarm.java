package com.leitner.tabbedexample;

import android.app.Application;
import android.provider.CalendarContract;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by i0004913 on 17.10.2017.
 */

public class Alarm {
    String title;
    String body;
    String en;
    String de;
    String it;
    String when;
    String gone;
    String msgID;
    int type;
    boolean isBridgeable;

    public Alarm(String title, String english, String german, String italian, String when, int type, boolean isBridgeable, String msgID, String gone){
        this.title = title;
//        this.body = body;
        this.en = english;
        this.de = german;
        this.it = italian;
        this.when = when;
        this.type = type;
        this.isBridgeable = isBridgeable;
        this.msgID = msgID;
        this.gone = gone;
    }
    public Alarm(String title, String english, String german, String italian, String when, int type, boolean isBridgeable, String msgID){
        this(title, english, german, italian, when, type, isBridgeable, msgID, "01.01.1970 00:00.00");
    }

    public String getTitle(){
        if (title!=null && title != "")
            return title;
        else
            return "title";
    }
    public String getBody(){
        switch (Locale.getDefault().getLanguage()){
            case "en":
                return en;
            case "de":
                return de;
            case "it":
                return it;
            default:
                return "No Body";
        }
    }
    public String getBodyEnglish(){
        return en;
    }
    public String getBodyGerman(){
        return de;
    }
    public String getBodyItalian(){
        return it;
    }
    public String getWhen(Boolean is24Hours){
        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        //originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try{
            Date date = originalFormat.parse(when);
            //originalFormat.setTimeZone(TimeZone.getDefault());
            if (is24Hours)
                return originalFormat.format(date);
            else {
                SimpleDateFormat parseFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
                //parseFormat.setTimeZone(TimeZone.getDefault());
                return parseFormat.format(date);
            }
        }catch (ParseException e){}
        return "";
//        Calendar calendar = new GregorianCalendar();
//        TimeZone timeZone = calendar.getTimeZone();
//        Log.d("OFFSET", String.valueOf(timeZone.getRawOffset()));
//        return when + timeZone.getRawOffset();
    }
    public String getWhenFormatted(){
        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        try{
            Date date = originalFormat.parse(when);
            formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e){
            Log.d("EXCEPTION", "Errorr", e);
        }
        return formattedDate;
    }
    public String getGone(Boolean is24Hours){
        if (gone == null)
            return "";
        else{
            DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            //originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try{
                Date date = originalFormat.parse(gone);
                //originalFormat.setTimeZone(TimeZone.getDefault());
                if (is24Hours)
                    return originalFormat.format(date);
                else{
                    SimpleDateFormat parseFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
                    //parseFormat.setTimeZone(TimeZone.getDefault());
                    return parseFormat.format(date);
                }
            }catch (ParseException e){}
        }
        return "";
    }
    public String getGoneFormatted() {
        DateFormat originalFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        try{
            Date date = originalFormat.parse(gone);
            formattedDate = targetFormat.format(date);

        } catch (ParseException e){
            //return "01.01.1970 00:00:00";
//            Log.d("EXCEPTION", "Errorr", e);
        }
        return formattedDate;
//        return formattedDate;
    }
    public String getMsgID(){ return msgID; }
    public int getType(){
        return type;
    }
    public boolean getIsbridgeable(){
        return isBridgeable;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
//            obj.put("body", body);
            obj.put("en", en);
            obj.put("de", de);
            obj.put("it", it);
            obj.put("when", when);
            obj.put("type", type);
            obj.put("isBridgeable", isBridgeable);
        } catch (JSONException e) {
            //trace("DefaultListItem.toString JSONException: "+e.getMessage());
        }
        return obj;
    }

}
