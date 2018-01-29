package com.leitner.tabbedexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by i0004913 on 12.10.2017.
 */

public class Tab2History extends Fragment {
    private ListView historicAlarms;
//    private RecyclerView historicAlarms;
    private ArrayAdapter<CharSequence> historicAlarmsAdapter;
    AlarmDB db;
    public static AlarmAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        Tab2History frag;
//        if (savedInstanceState != null){
//            frag = (Tab2History)getFragmentManager().findFragmentByTag("TAB2HISTORY");
//        }else {
//            frag = new Tab2History();
//            frag.setArguments(getI);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2history, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        db = new AlarmDB(this.getContext());
//        db.getWritableDatabase();
        historicAlarms = (ListView) getView().findViewById(R.id.listview_historic_alarms);
//        historicAlarms = (RecyclerView) getView().findViewById(R.id.listview_historic_alarms);
        final Context context = this.getContext();

//        historicAlarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder builder;
//                Alarm alarm = (Alarm) parent.getAdapter().getItem(position);
//                builder = new AlertDialog.Builder(getContext());
//                builder.setTitle(alarm.getTitle())
//                        .setMessage(alarm.getWhen(android.text.format.DateFormat.is24HourFormat(context)) + "\n" + alarm.getBody())
//                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //Do Nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_menu_info_details)
//                        .show();
//            }
//        });
//
//        historicAlarms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final Alarm alarm = (Alarm) parent.getAdapter().getItem(position);
//                Intent intent = new Intent(getContext(), WebFullscreenActivity.class);
//                Bundle b = new Bundle();
//                b.putString("action_bar_title", alarm.getBody());
//                b.putString("url", "http://192.168.1.1");
//                intent.putExtras(b);
//                startActivity(intent);
//                return true;
//            }
//        });

//        SharedPreferences preferences = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        preferences.edit().remove("historic_alarms").commit();
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 21; i++) {
//            list.add("Historic " + i);
//        }
//        JSONArray jsonArray = new JSONArray(list);
//        preferences.edit().putString("historic_alarms", jsonArray.toString()).commit();
//
//        List<String> jsonList = fromJSONToList();
//
//        historicAlarms = (ListView) getView().findViewById(R.id.listview_historic_alarms);
//        historicAlarmsAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, jsonList);
//        historicAlarms.setAdapter(historicAlarmsAdapter);
//        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) historicAlarms.getLayoutParams();
//        mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//        historicAlarmsAdapter.insert("Tralalala", 1);

//        ArrayList<Alarm> jsonList = db.getAllAlarmsHistoric();
//        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
//        if (jsonList != null){
//            adapter = new AlarmAdapter(jsonList, getContext());
//            historicAlarms = (ListView) getView().findViewById(R.id.listview_historic_alarms);
//            historicAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) historicAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//        }
        RefreshView();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("fragmentSTATE", "history start");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d("fragmentSTATE", "actual pause");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d("fragmentSTATE", "actual stop");
    }

    @Override
    public void onResume(){
        super.onResume();
        db = new AlarmDB(getActivity().getApplication().getApplicationContext());
        Log.d("fragmentSTATE", "actual resume");
//        db.getWritableDatabase();
//        ArrayList<Alarm> jsonList = db.getAllAlarmsHistoric();
//        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
//        if (jsonList != null){
//            adapter = new AlarmAdapter(jsonList, getContext());
//            historicAlarms = (ListView) getView().findViewById(R.id.listview_historic_alarms);
//            historicAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) historicAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//        }
       // RefreshView();
    }

    public void RefreshView(){
        db = new AlarmDB(getActivity().getApplication().getApplicationContext());
        db.getWritableDatabase();
        ArrayList<Alarm> jsonList = db.getAllAlarmsHistoric();
        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
        Log.d("REFRESHED", "history");
        if (jsonList.size() > 0){
            adapter = new AlarmAdapter(jsonList, getContext(), false);
            historicAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) historicAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//            noAlarms.setVisibility(View.GONE);
//            Log.d("VISIBILITY", String.valueOf(noAlarms.getVisibility()));
        }
//        else
            adapter = new AlarmAdapter(jsonList, getContext(), false);
        historicAlarms.setAdapter(adapter);
    }

    public void RefreshViewContext(Context context){
        db = new AlarmDB(getActivity().getApplication().getApplicationContext());
        db.getWritableDatabase();

        ArrayList<Alarm> jsonList = db.getAllAlarmsHistoric();
        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
        if (jsonList.size() > 0){
            adapter = new AlarmAdapter(jsonList, getContext(), false);
            historicAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) historicAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//            noAlarms.setVisibility(View.GONE);
//            Log.d("VISIBILITY", String.valueOf(noAlarms.getVisibility()));
        }
//        else
//            noAlarms.setVisibility(View.VISIBLE);
    }

    public List<String> fromJSONToList(){
        List<String> jsonArray = new ArrayList<String>();
        SharedPreferences preferences = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        try{
            JSONArray fromPrefs = new JSONArray(preferences.getString("historic_alarms", null));

            for (int i = 0; i < fromPrefs.length(); i++) {
                jsonArray.add(fromPrefs.getString(i));
//                Log.d("JSON_ARRAY", jsonArray1.get(i).toString());
            }
            return jsonArray;
        }catch (JSONException e){}
        return jsonArray;
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
}
