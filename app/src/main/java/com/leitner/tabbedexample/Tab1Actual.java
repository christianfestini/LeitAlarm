package com.leitner.tabbedexample;

/**
 * Created by i0004913 on 12.10.2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Handler;

import static android.content.Context.MODE_PRIVATE;


public class Tab1Actual extends Fragment {
    private ListView actualAlarms;
    private ArrayAdapter<CharSequence> actualAlarmsAdapter;
    public static TextView noAlarms;
    AlarmDB db;
    public static AlarmAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1actual, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

//        db = new AlarmDB(this.getContext());
//        db.getWritableDatabase();
//        this.setRetainInstance(true);
        final Context context = this.getContext();
        noAlarms = (TextView) getView().findViewById(R.id.textView_no_alarms);
        actualAlarms = (ListView) getView().findViewById(R.id.listview_actual_alarms);
        actualAlarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder;
                final Alarm alarm = (Alarm) parent.getAdapter().getItem(position);
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(alarm.getMsgID())
                        .setMessage(alarm.getWhen(android.text.format.DateFormat.is24HourFormat(context)) + "\n" +
                                alarm.getBody())
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.d("HiSTORIFIED", String.valueOf(db.setHistoric(alarm)));
//                                final ArrayList<Alarm> refreshedList = db.getAllAlarmsActual();
//                                adapter.clear();
//                                adapter.addAll(refreshedList);
//                                final ArrayList<Alarm> refreshedListHistory = db.getAllAlarmsHistoric();
//
//                                RefreshView();
////                                frag.RefreshViewContext(frag.getContext());
//
////                                frag.getContext();
//                               android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
////                                        Tab2History frag = (Tab2History) getFragmentManager().findFragmentById(R.id.tab_2_history);
////                                        frag.RefreshView();
//                                        if (Tab2History.adapter != null){
//                                            Tab2History.adapter.clear();
//                                            Tab2History.adapter.addAll(refreshedListHistory);
//                                        }
//                                        //Tab2History.adapter.addAll(refreshedListHistory);
//
//                                    }
//                                });
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .show();
            }
        });

        actualAlarms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Alarm alarm = (Alarm) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getContext(), WebFullscreenActivity.class);
                Bundle b = new Bundle();
                b.putString("action_bar_title", alarm.getBody());
                b.putString("url", "https://www.google.com");
                intent.putExtras(b);
                startActivity(intent);
                return true;
            }
        });

        Log.d("fragmentSTATE", "actual viewcreated");
        RefreshView();
//        noAlarms.setFocusable(true);
//        noAlarms.setVisibility(View.GONE);
//
//        //Add alarms to db in case there are none
//        if (!(db.getAllAlarmsActual().size() > 0 && db.getAllAlarmsHistoric().size() > 0)){
//            Calendar calendar = Calendar.getInstance();
//            List<Alarm> list = new ArrayList<Alarm>();
//            for (int i = 0; i < 21; i++) {
//                list.add(new Alarm("Actual", "Sweeeetz", "Süßigkeiten", "Dolci", calendar.get(Calendar.DAY_OF_MONTH) + "."
//                                                            + (calendar.get(Calendar.MONTH) + 1) + "."
//                                                            + calendar.get(Calendar.YEAR) + " "
//                                                            + calendar.get(Calendar.HOUR_OF_DAY) + ":"
//                                                            + calendar.get(Calendar.MINUTE) + "."
//                                                            + calendar.get(Calendar.SECOND), 1, true));
//            }
//            for(Alarm alarm: list){
//                db.addAlarm(alarm);
//            }
//        }
//
//        Log.d("DB COUNT", String.valueOf(db.getAlarmsActualCount()));
//        ArrayList<Alarm> jsonList = db.getAllAlarmsActual();
//        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
//        if (jsonList.size() > 0){
//            adapter = new AlarmAdapter(jsonList, getContext());
//            actualAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) actualAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//            Log.d("VISIBILITY", String.valueOf(noAlarms.getVisibility()));
//        }
//        else
//            noAlarms.setVisibility(View.VISIBLE);
//        //noAlarms.setVisibility(View.VISIBLE);
      //  new AddToListEvery10Seconds().execute();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        Log.d("fragmentSTATE", "actual create");
        if (actualAlarms != null){
            RefreshView();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("fragmentSTATE", "actual resume");
//        db = new AlarmDB(getActivity().getApplication().getApplicationContext());
//        db.getWritableDatabase();
//        ArrayList<Alarm> jsonList = db.getAllAlarmsActual();
//        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
//        adapter = new AlarmAdapter(jsonList, getContext(), true);
//        actualAlarms.setAdapter(adapter);

//        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) actualAlarms.getLayoutParams();
//        mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//        if (jsonList.size() > 0){
//            Log.d("VISIBILITY", String.valueOf(noAlarms.getVisibility()));
//            noAlarms.setVisibility(View.GONE);
//        }
//        else
//            noAlarms.setVisibility(View.VISIBLE);
//        if (jsonList != null){
//            adapter = new AlarmAdapter(jsonList, getContext());
//            actualAlarms = (ListView) getView().findViewById(R.id.listview_actual_alarms);
//            actualAlarmsAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, jsonList);
//            actualAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) actualAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//            noAlarms.setVisibility(View.GONE);
//        }
//        else{
//            noAlarms.setVisibility(View.VISIBLE);
//        }
//        Log.d("STATE", "resume");
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d("fragmentSTATE", "actual start");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("fragmentSTATE", "actual pause");
//        getSharedPreferences("myPrefs", MODE_PRIVATE).edit().putString("application_state", "pause");
    }
    @Override
    public void onStop(){
        Log.d("fragmentSTATE", "actual stop");
//        getSharedPreferences("myPrefs", MODE_PRIVATE).edit().putString("application_state", "stop");
        super.onStop();
    }

    @Override
    public void onDetach(){
        Log.d("fragmentSTATE", "actual detached");
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity){
        Log.d("fragmentSTATE", "actual attached");
        super.onAttach(activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void finishOnBoarding(){
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        preferences.edit()
                .putBoolean("onboarding_complete", false).apply();
    }
//    }

    public void RefreshView(){
        db = new AlarmDB(getActivity().getApplication().getApplicationContext());
        db.getWritableDatabase();
        //db.deleteTable();
        ArrayList<Alarm> jsonList = db.getAllAlarmsActual();
        Log.d("REFRESHED", "actual");
        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
        if (jsonList.size() > 0){
            adapter = new AlarmAdapter(jsonList, getContext(), true);
            actualAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) actualAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
            noAlarms.setVisibility(View.GONE);
            Log.d("VISIBILITY", String.valueOf(noAlarms.getVisibility()));
        }
        else
            noAlarms.setVisibility(View.VISIBLE);
    }

    private int getSoftButtonsBarHeight() {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            windowManager.getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static void insertInList(Alarm alarm){
        adapter.insert(alarm, 0);
    }
}
