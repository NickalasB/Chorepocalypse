package com.zonkey.chorepocalypse.ui.fragments;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zonkey.chorepocalypse.Receivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by nickbradshaw on 2/8/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    final static int REQUEST_1 = 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.setTitle("Choose Chore Time");
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {

        Calendar calNow = Calendar.getInstance();
        Calendar calFuture = (Calendar) calNow.clone();
        calFuture.set(Calendar.HOUR_OF_DAY, hour);
        calFuture.set(Calendar.MINUTE, min);
        calFuture.set(Calendar.SECOND, 0);

        if (calFuture.compareTo(calNow) <= 0) {
            calFuture.add(Calendar.DATE, 1);
        }
        setAlarm(calFuture);
        Toast.makeText(getActivity(), "Alarm set for " + hour + ":" + min, Toast.LENGTH_SHORT).show();
    }

    private void setAlarm(Calendar futureCal) {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), REQUEST_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureCal.getTimeInMillis(), pendingIntent);
    }
}
