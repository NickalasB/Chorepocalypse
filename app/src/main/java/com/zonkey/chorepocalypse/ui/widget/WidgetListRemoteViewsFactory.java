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
class WidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String CHORES = "chores";
    private static final String NAME = "NAME";
    private static final String DATE = "DATE";
    private static final String REWARD = "REWARD";
    private static final String APPROVAL_STATUS = "APPROVAL_STATUS";
    private static final String PHOTO = "PHOTO";

    private ArrayList<Chore> choreList = new ArrayList<>();
    private Context context = null;

    WidgetListRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    private void populateChoreWidgetList() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(CHORES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    choreList.add(choreSnapshot.getValue(Chore.class));
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

        String choreWidgetChoreReward = choreList.get(position).getChoreReward();
        boolean choreWidgetApprovalStatus = choreList.get(position).getIsChoreApproved();
        String choreWidgetPhotoUrl = choreList.get(position).getChorePhotoUrl();
        String choreWidgetChoreName = choreList.get(position).getChoreName();
        String formattedDueDate = getFormattedDueDate(position);

        remoteView.setTextViewText(R.id.chore_widget_chore_name_textview, choreWidgetChoreName);
        remoteView.setTextViewText(R.id.chore_widget_chore_due_date_textview, formattedDueDate);

        final Intent intent = new Intent();
        intent.putExtra(NAME, choreWidgetChoreName);
        intent.putExtra(DATE, formattedDueDate);
        intent.putExtra(REWARD, choreWidgetChoreReward);
        intent.putExtra(APPROVAL_STATUS, choreWidgetApprovalStatus);
        intent.putExtra(PHOTO, choreWidgetPhotoUrl);

        remoteView.setOnClickFillInIntent(R.id.widget_single_chore_linear_layout, intent);
        return remoteView;
    }

    private String getFormattedDueDate(int position) {
        String choreTimeString;
        String choreDateString;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_NUMERIC_DATE;
        choreTimeString = DateUtils.formatDateTime(context, choreList.get(position).getChoreTime(), timeFlag);
        choreDateString = DateUtils.formatDateTime(context, choreList.get(position).getChoreTime(), dateFlag);
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
        populateChoreWidgetList();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
    }
}