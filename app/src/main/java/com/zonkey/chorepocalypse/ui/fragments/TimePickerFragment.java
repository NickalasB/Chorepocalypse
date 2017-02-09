package com.zonkey.chorepocalypse.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.receivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by nickbradshaw on 2/8/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    final static int REQUEST_1 = 1;

    static String TIME_PICKER;
    static String DATE_PICKER;
    private Calendar mCalendar;
    public long mMillis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();
        showDatePicker();
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog timePickerDialog = (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag(TIME_PICKER);
        if (timePickerDialog != null) {
            timePickerDialog.setOnTimeSetListener(this);
        }
        DatePickerDialog datePickerDialog = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag(DATE_PICKER);
        if (datePickerDialog != null) datePickerDialog.setOnDateSetListener(this);
    }

    public void showDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                TimePickerFragment.this,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setTitle(getString(R.string.date_picker_title));

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date2.add(Calendar.WEEK_OF_MONTH, -1);
        Calendar date3 = Calendar.getInstance();
        date3.add(Calendar.WEEK_OF_MONTH, 1);
        Calendar[] days = {date1, date2, date3};
        datePickerDialog.setHighlightedDays(days);
        datePickerDialog.show(getActivity().getFragmentManager(), DATE_PICKER);
    }

    public void showTimePickerDialog() {
        Calendar nowCalendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                TimePickerFragment.this,
                nowCalendar.get(Calendar.HOUR_OF_DAY),
                nowCalendar.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.enableSeconds(true);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setTitle(getString(R.string.time_picker_title));
        timePickerDialog.setCancelText(R.string.time_picker_cancel_text);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showDatePicker();
            }
        });
        timePickerDialog.show(getActivity().getFragmentManager(), TIME_PICKER);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        showTimePickerDialog();
    }
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
        setAlarm();
    }

    private void setAlarm() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), REQUEST_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        mMillis = mCalendar.getTimeInMillis();

        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_SHOW_DATE;
        String timeString = DateUtils.formatDateTime(getContext(), mMillis, timeFlag);
        String dateString = DateUtils.formatDateTime(getContext(), mMillis, dateFlag);
        Toast.makeText(getActivity(), "Alarm set for " + dateString + " at " + timeString, Toast.LENGTH_LONG).show();

        alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
    }
}
