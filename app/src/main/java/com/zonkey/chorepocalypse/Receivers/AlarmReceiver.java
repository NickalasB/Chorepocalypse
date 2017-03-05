package com.zonkey.chorepocalypse.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zonkey.chorepocalypse.services.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CHORE_KEY = "choreKey";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmServiceIntent = new Intent(context, AlarmService.class);
        alarmServiceIntent.putExtra(CHORE_KEY, intent.getStringExtra(CHORE_KEY));
        alarmServiceIntent.setAction(AlarmService.ACTION_START_ALARM);
        context.startService(alarmServiceIntent);
    }
}