package com.zonkey.chorepocalypse.ui.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zonkey.chorepocalypse.receivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by nickbradshaw on 2/8/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    final static int REQUEST_1 = 1;
    static String TIME_PICKER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar now = Calendar.getInstance();
        showTimePickerDialog(now);
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog timePickerDialog = (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag(TIME_PICKER);
        if (timePickerDialog != null) {
            timePickerDialog.setOnTimeSetListener(this);
        }
    }

    public void showTimePickerDialog(Calendar now) {
        TimePickerDialog mTimePickerDialog = TimePickerDialog.newInstance(
                TimePickerFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(getActivity())
        );
        mTimePickerDialog.dismissOnPause(true);
        mTimePickerDialog.enableSeconds(true);
        mTimePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        mTimePickerDialog.setTitle("Chore Due By...");
        mTimePickerDialog.show(getActivity().getFragmentManager(), TIME_PICKER);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Calendar calNow = Calendar.getInstance();
        Calendar calFuture = (Calendar) calNow.clone();
        calFuture.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calFuture.set(Calendar.MINUTE, minute);
        calFuture.set(Calendar.SECOND, second);

        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;

        if (calFuture.compareTo(calNow) <= 0) {
            calFuture.add(Calendar.DATE, 1);
        }
        setAlarm(calFuture);
        Toast.makeText(getActivity(), "Alarm set for " + hourString + ":" + minuteString + ":" + secondString, Toast.LENGTH_SHORT).show();
    }

    private void setAlarm(Calendar futureCal) {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), REQUEST_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureCal.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

}
