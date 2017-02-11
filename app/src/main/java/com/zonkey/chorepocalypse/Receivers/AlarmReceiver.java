package com.zonkey.chorepocalypse.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zonkey.chorepocalypse.services.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmServiceIntent = new Intent(context, AlarmService.class);
        alarmServiceIntent.setAction(AlarmService.ACTION_START_ALARM);
        context.startService(alarmServiceIntent);
    }
}
