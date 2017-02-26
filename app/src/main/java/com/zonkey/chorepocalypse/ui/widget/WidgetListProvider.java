package com.zonkey.chorepocalypse.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;

import java.util.ArrayList;

/**
 * Created by nickbradshaw on 2/25/17.
 */
public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Chore> choreList = new ArrayList<>();
    private Context context = null;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;
//        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateChoreWidgetList();
    }

    private void populateChoreWidgetList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            Chore chore;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chore = new Chore();
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    Chore chore = choreSnapshot.getValue(Chore.class);
                    chore.getChoreName();
                    chore.getChoreTime();
                    choreList.add(chore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() {
        return choreList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.chore_widget_item);
        Chore chore = choreList.get(position);
        remoteView.setTextViewText(R.id.chore_widget_chore_name_textview, chore.getChoreName());
        String formattedChoreTime = formatChoreTime(chore);
        remoteView.setTextViewText(R.id.chore_widget_chore_due_date_textview, formattedChoreTime);

        return remoteView;
    }

    private String formatChoreTime(Chore chore) {
        String choreTimeString;
        String choreDateString;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_SHOW_DATE;
        choreTimeString = DateUtils.formatDateTime(context, chore.getChoreTime(), timeFlag);
        choreDateString = DateUtils.formatDateTime(context, chore.getChoreTime(), dateFlag);
        return String.format("%s%s%s%s", context.getString(R.string.detail_due_string), choreDateString, context.getString(R.string.add_chore_at_string), choreTimeString);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        populateChoreWidgetList();
    }

    @Override
    public void onDestroy() {
    }

}