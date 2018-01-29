package com.leitner.tabbedexample;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by i0004913 on 17.10.2017.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> implements View.OnClickListener {
    private ArrayList<Alarm> dataSet;
    Context mContext;
    private Boolean isActual;

    private static class ViewHolder{
        TextView txtTitle;
        TextView txtBody;
        TextView txtWhen;
        ImageView imgType;
        ImageView imgBridgeable;
    }

    public AlarmAdapter(ArrayList<Alarm> data, Context context, Boolean isActual){
        super(context, R.layout.alarm_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.isActual = isActual;
    }

    @Override
    public void onClick(View v){
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Alarm alarm = (Alarm)object;

        switch (v.getId()){
            case R.id.textview_title:
                Snackbar.make(v, "Title: " + alarm.getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Alarm alarm = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.alarm_item, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.textview_title);
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.textview_body);
            viewHolder.txtWhen = (TextView) convertView.findViewById(R.id.textview_when);
            viewHolder.imgType = (ImageView) convertView.findViewById(R.id.imageview_type);
            viewHolder.imgBridgeable = (ImageView) convertView.findViewById(R.id.imageview_bridgeable);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtTitle.setText(alarm.getTitle());
        viewHolder.txtBody.setText(alarm.getBody());
        if (isActual)
            viewHolder.txtWhen.setText(alarm.getWhen(android.text.format.DateFormat.is24HourFormat(getContext())));
        else
            viewHolder.txtWhen.setText(alarm.getGone(android.text.format.DateFormat.is24HourFormat(getContext())));
        switch (alarm.getType()){
            case 1:
                //notification
                viewHolder.imgType.setImageResource(R.mipmap.info);
                break;
            case 2:
                viewHolder.imgType.setImageResource(R.mipmap.warn);
                break;
                //alarm
            case 8:
                viewHolder.imgType.setImageResource(R.mipmap.right);
                break;
                //abschaltung
            case 16:
                viewHolder.imgType.setImageResource(R.mipmap.redright);
                break;
                //notabschaltung
            default:
                viewHolder.imgType.setImageResource(R.mipmap.warn);
                break;
        }
        Log.d("Bridgeable", String.valueOf(alarm.getIsbridgeable()));
        if (alarm.getIsbridgeable()){
            Log.d("Brigdeable", "wieso");
//            viewHolder.imgBridgeable.setVisibility(View.VISIBLE);
            viewHolder.imgBridgeable.setImageResource(R.mipmap.bridgeable);
        }
        else{
            Log.d("Brigdeable", "nit");
            viewHolder.imgBridgeable.setImageResource(R.mipmap.blank);
        }
        return convertView;
    }

}
