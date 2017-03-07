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
public class WidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String CHORES = "chores";
    private static final String NAME = "NAME";
    private static final String DATE = "DATE";
    private static final String REWARD = "REWARD";
    private static final String APPROVAL_STATUS = "APPROVAL_STATUS";
    private static final String PHOTO = "PHOTO";

    private ArrayList<Chore> mChoreList = new ArrayList<>();
    private Context mContext = null;
    private Intent mIntent;

    WidgetListRemoteViewsFactory(Context context, Intent intent) {
        this.mIntent = intent;
        this.mContext = context;
    }

    private void populateChoreWidgetList() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(CHORES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChoreList.clear();
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    mChoreList.add(choreSnapshot.getValue(Chore.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() {
        return mChoreList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.chore_widget_item);

        String choreWidgetChoreReward = mChoreList.get(position).getChoreReward();
        boolean choreWidgetApprovalStatus = mChoreList.get(position).getIsChoreApproved();
        String choreWidgetPhotoUrl = mChoreList.get(position).getChorePhotoUrl();
        String choreWidgetChoreName = mChoreList.get(position).getChoreName();
        String formattedDueDate = getFormattedDueDate(position);

        remoteView.setTextViewText(R.id.chore_widget_chore_name_textview, choreWidgetChoreName);
        remoteView.setTextViewText(R.id.chore_widget_chore_due_date_textview, formattedDueDate);

        mIntent.putExtra(NAME, choreWidgetChoreName);
        mIntent.putExtra(DATE, formattedDueDate);
        mIntent.putExtra(REWARD, choreWidgetChoreReward);
        mIntent.putExtra(APPROVAL_STATUS, choreWidgetApprovalStatus);
        mIntent.putExtra(PHOTO, choreWidgetPhotoUrl);

        remoteView.setOnClickFillInIntent(R.id.widget_single_chore_linear_layout, mIntent);
        return remoteView;
    }

    private String getFormattedDueDate(int position) {
        String choreTimeString;
        String choreDateString;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_NUMERIC_DATE;
        choreTimeString = DateUtils.formatDateTime(mContext, mChoreList.get(position).getChoreTime(), timeFlag);
        choreDateString = DateUtils.formatDateTime(mContext, mChoreList.get(position).getChoreTime(), dateFlag);
        return String.format("%s%s%s%s", mContext.getString(R.string.detail_due_string), choreDateString, mContext.getString(R.string.add_chore_at_string), choreTimeString);
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
        populateChoreWidgetList();
    }

    @Override
    public void onDestroy() {

    }
}