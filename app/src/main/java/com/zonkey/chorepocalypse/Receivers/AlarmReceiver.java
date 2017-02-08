package com.zonkey.chorepocalypse.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

import com.zonkey.chorepocalypse.ui.activities.FullscreenActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private Ringtone mRingTone;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Received!!", Toast.LENGTH_SHORT).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        mRingTone = RingtoneManager.getRingtone(context, alarmUri);
        mRingTone.play();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(250);
        intent.setClass(context, FullscreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void stopRingTone() {
        mRingTone.stop();

    }
}
