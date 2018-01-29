package com.leitner.tabbedexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by i0004913 on 24.01.2018.
 */

public class UpdateBotClientStatusAutoStart extends BroadcastReceiver {
    UpdateBotClientStatus alarm = new UpdateBotClientStatus();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.setAlarm(context);
        }
    }
}
