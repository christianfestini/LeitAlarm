package com.leitner.tabbedexample;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by i0004913 on 12.10.2017.
 */

public class Tab3Settings extends Fragment {
    private Switch switchMuteNotifications;
    String[] items = new String[1];
    final ArrayList itemsSelected = new ArrayList();
    private Switch switchUseCalendar;
    private EditText timeFrom;
    private EditText timeTo;
    private Button buttonReset;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    public static ImageView imageViewBotClientStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3settings, container, false);
        return rootView;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = sharedPreferences.edit();
        items[0] = getResources().getString(R.string.reset_app_unregister);
        imageViewBotClientStatus = (ImageView) getView().findViewById(R.id.image_botclient_status);
        if (sharedPreferences.getBoolean("server_online", false))
            Tab3Settings.imageViewBotClientStatus.setImageResource(R.mipmap.server_online);
        else
            Tab3Settings.imageViewBotClientStatus.setImageResource(R.mipmap.server_offline);
        buttonReset = (Button) getView().findViewById(R.id.button_reset_app);

        switchMuteNotifications = (Switch) getView().findViewById(R.id.switch_mute_notifications);
        switchUseCalendar = (Switch) getView().findViewById(R.id.switch_use_calendar);
        if (sharedPreferences.contains("mute_notification"))
            switchMuteNotifications.setChecked(sharedPreferences.getBoolean("mute_notification", false));
        if (sharedPreferences.contains("use_calendar"))
            switchUseCalendar.setChecked(sharedPreferences.getBoolean("use_calendar", false));
        timeFrom = (EditText) getView().findViewById(R.id.editText_time_from); timeFrom.setClickable(true); timeFrom.setFocusable(false);
        timeTo = (EditText) getView().findViewById(R.id.editText_time_to); timeTo.setClickable(true); timeTo.setFocusable(false);
        //buttonRestart = (Button) getView().findViewById(R.id.button_reset_app);

        if (switchMuteNotifications.isChecked()){
            timeFrom.setEnabled(true);
            timeTo.setEnabled(true);
        }
        timeFrom.setText(sharedPreferences.getString("time_from", "21:00"));
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                final String[] timeFromValues = timeFrom.getText().toString().split(":");
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour < 10 && selectedMinute < 10){
                            timeFrom.setText("0" + selectedHour + ":0" + selectedMinute);
                        }
                        else if (selectedMinute < 10){
                            timeFrom.setText( selectedHour + ":0" + selectedMinute);
                        }
                        else if (selectedHour < 10){
                            timeFrom.setText("0" + selectedHour + ":" + selectedMinute);
                        }
                        else{
                            timeFrom.setText(selectedHour + ":" + selectedMinute);
                        }
//                        getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("time_from", timeFrom.getText().toString()).apply();
                        editor.putString("time_from", timeFrom.getText().toString());
                        editor.commit();
                    }
                }, Integer.parseInt(timeFromValues[0]), Integer.parseInt(timeFromValues[1]), true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        timeTo.setText(sharedPreferences.getString("time_to", "06:00"));
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                String[] timeToValues = timeTo.getText().toString().split(":");
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour < 10 && selectedMinute < 10){
                            timeTo.setText("0" + selectedHour + ":0" + selectedMinute);
                        }
                        else if (selectedMinute < 10){
                            timeTo.setText( selectedHour + ":0" + selectedMinute);
                        }
                        else if (selectedHour < 10){
                            timeTo.setText("0" + selectedHour + ":" + selectedMinute);
                        }
                        else{
                            timeTo.setText(selectedHour + ":" + selectedMinute);
                        }
//                        getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("time_to", timeTo.getText().toString()).apply();
                        editor.putString("time_to", timeTo.getText().toString());
                        editor.commit();
                    }
                }, Integer.parseInt(timeToValues[0]), Integer.parseInt(timeToValues[1]), true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        switchMuteNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (switchUseCalendar.isChecked()) {
                        switchUseCalendar.setChecked(false);
                        editor.putBoolean("use_calendar", switchUseCalendar.isChecked());
                        editor.commit();
                    }
                    timeFrom.setEnabled(true);
                    timeTo.setEnabled(true);
                }
                else{
                    timeFrom.setEnabled(false);
                    timeTo.setEnabled(false);
                }
                editor.putBoolean("mute_notification", isChecked);
                editor.commit();
                if (!(sharedPreferences.contains("time_from") && sharedPreferences.contains("time_to"))){
                    editor.putString("time_from", timeFrom.getText().toString());
                    editor.commit();
                    editor.putString("time_to", timeTo.getText().toString());
                    editor.commit();
                }
            }
        });
        switchUseCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchMuteNotifications.setChecked(false);
                    editor.putBoolean("mute_notifications", switchMuteNotifications.isChecked());
                    editor.commit();
                    timeFrom.setEnabled(false);
                    timeTo.setEnabled(false);
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_CALENDAR)){

                        }else{
                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CALENDAR}, 1);
//                    int permissionsGranted[] = onRequestPermissionsResult(1, new String[]{Manifest.permission.READ_CALENDAR});
                        }
                    }
                }
                else{
                    if (switchMuteNotifications.isChecked()){
                        timeFrom.setEnabled(true);
                        timeTo.setEnabled(true);
                    }
                    else {
                        timeFrom.setEnabled(false);
                        timeTo.setEnabled(false);
                    }
                }
                editor.putBoolean("use_calendar", isChecked);
                editor.commit();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.reset_app_message)
                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked){
                                    itemsSelected.add(which);
                                }else if (itemsSelected.contains(which)){
                                    itemsSelected.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        //.setMessage(R.string.reset_app_message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (itemsSelected.size() > 0) {
//                                    editor.clear().commit();
                                    UnRegister unRegister = new UnRegister(getContext());
                                    unRegister.execute();
                                }
                                AlarmDB alarmDB = new AlarmDB(getContext());
                                alarmDB.getWritableDatabase();
                                alarmDB.clearAlarms();
                                alarmDB.close();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    class UnRegister extends AsyncTask<String, Void, String> {
        private Context mContext;
        public UnRegister(Context context){this.mContext = context;}

        @Override
        protected void onPreExecute(){
            switchUseCalendar.setEnabled(false);
            timeFrom.setEnabled(false);
            timeTo.setEnabled(false);
            buttonReset.setEnabled(false);
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(mContext.getResources().getString(R.string.unregistering));
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            try {
                String token = FirebaseInstanceId.getInstance().getToken();
                url = new URL("http://185.131.252.18/Notifications/api/resort");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("API_KEY", "qwertzasdfg");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("accept", "application/json");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("PUT");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                JSONObject unRegisterObject = new JSONObject();
                unRegisterObject.put("token", token);
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(unRegisterObject.toString());
                wr.flush();

                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8")
                );
                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }

                bufferedReader.close();
                Log.d("Web Response", stringBuilder.toString());
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (Exception e){}

            return  "Error";
        }

        protected void onPostExecute(String result){
            switchUseCalendar.setEnabled(true);
            timeFrom.setEnabled(true);
            timeTo.setEnabled(true);
            buttonReset.setEnabled(true);
            progressDialog.dismiss();
            if (!result.equals("Error")){
                editor.clear().commit();

                Intent main = new Intent(mContext, MainActivity.class);
                startActivity(main);
                getActivity().finish();
            }
            else
                Toast.makeText(mContext, "No connection to server", Toast.LENGTH_LONG).show();
        }
    }


}
