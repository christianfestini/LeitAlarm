package com.leitner.tabbedexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by i0004913 on 26.01.2018.
 */

public class AlarmAdapterRecycle extends RecyclerView.Adapter<AlarmAdapterRecycle.ViewHolder> {

    private List<Alarm> alarmList;
    private Context mContext;
    private Boolean isActual;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtBody;
        TextView txtWhen;
        ImageView imgType;
        ImageView imgBridgeable;
        public ViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.textview_title);
            txtBody = (TextView) view.findViewById(R.id.textview_body);
            txtWhen = (TextView) view.findViewById(R.id.textview_when);
            imgType = (ImageView) view.findViewById(R.id.imageview_type);
            imgBridgeable = (ImageView) view.findViewById(R.id.imageview_bridgeable);
        }
    }

    public AlarmAdapterRecycle(List<Alarm> data, Context context, Boolean isActual){
        this.alarmList = data;
        this.mContext = context;
        this.isActual = isActual;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.txtTitle.setText(alarm.getTitle());
        holder.txtTitle.setText(alarm.getTitle());
        holder.txtBody.setText(alarm.getBody());
        if (isActual)
            holder.txtWhen.setText(alarm.getWhen(android.text.format.DateFormat.is24HourFormat(mContext)));
        else
            holder.txtWhen.setText(alarm.getGone(android.text.format.DateFormat.is24HourFormat(mContext)));
        switch (alarm.getType()){
            case 1:
                //notification
                holder.imgType.setImageResource(R.mipmap.info);
                break;
            case 2:
                holder.imgType.setImageResource(R.mipmap.warn);
                break;
            //alarm
            case 8:
                holder.imgType.setImageResource(R.mipmap.right);
                break;
            //abschaltung
            case 16:
                holder.imgType.setImageResource(R.mipmap.redright);
                break;
            //notabschaltung
            default:
                holder.imgType.setImageResource(R.mipmap.warn);
                break;
        }
        Log.d("Bridgeable", String.valueOf(alarm.getIsbridgeable()));
        if (alarm.getIsbridgeable()){
            holder.imgBridgeable.setImageResource(R.mipmap.bridgeable);
        }
        else{
            holder.imgBridgeable.setImageResource(R.mipmap.blank);
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
