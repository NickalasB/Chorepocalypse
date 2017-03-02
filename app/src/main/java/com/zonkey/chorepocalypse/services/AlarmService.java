package com.zonkey.chorepocalypse.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import com.zonkey.chorepocalypse.ui.activities.FullScreenAlarmActivity;

public class AlarmService extends Service {
    public static final String ACTION_START_ALARM = "com.zonkey.chorepocalypse.services.action.ACTION_START_ALARM";
    public static final String ACTION_DISABLE_ALARM = "com.zonkey.chorepocalypse.services.action.ACTION_DISABLE_ALARM";

    private Ringtone mRingTone;

    @Override
    public void onCreate() {
        super.onCreate();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        mRingTone = RingtoneManager.getRingtone(this, alarmUri);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (ACTION_START_ALARM.equals(intent.getAction())){
                startAlarm(intent);
            } else if (ACTION_DISABLE_ALARM.equals(intent.getAction())) {
                stopAlarm();
            }
        }
        return START_STICKY;
    }

    private void stopAlarm() {
        mRingTone.stop();
    }

    private void startAlarm(Intent intent) {
        mRingTone.play();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(250);
        Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenAlarmActivity.class);
        fullScreenIntent.putExtra("choreKey", intent.getStringExtra("choreKey"));
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(fullScreenIntent);
    }

}
