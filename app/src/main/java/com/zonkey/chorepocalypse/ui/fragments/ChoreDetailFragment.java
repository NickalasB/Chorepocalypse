package com.zonkey.chorepocalypse.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoreDetailFragment extends Fragment {

    @BindView(R.id.detail_chore_title)
    TextView mChoreTitle;

    @BindView(R.id.detail_chore_pic)
    RoundedImageView mChorePic;

    @BindView(R.id.detail_current_chore_points)
    TextView mCurrentChorePoints;

    @BindView(R.id.detail_total_chore_points)
    TextView mTotalPointsTextView;

    @BindView(R.id.detail_chore_due_date)
    TextView mDueDate;

    @BindView(R.id.detail_chore_status_textview)
    TextView mApprovalStatus;

    boolean mIsChoreApprovalRequired;
    boolean mIsChoreApproved;

    public ChoreDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chore_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchLastChoreAddedData();
    }

    public void fetchLastChoreAddedData() {
        FirebaseDatabase choreDatabase = FirebaseDatabase.getInstance();
        DatabaseReference choreDatabaseReference = choreDatabase.getReference("chores");
        Query singleChoreQuery = choreDatabaseReference.limitToLast(1);
        singleChoreQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                        Chore chore = choreSnapshot.getValue(Chore.class);
                        displayChoreDetails(chore);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void displayChoreDetails(Chore chore) {
        mChoreTitle.setText(chore.getChoreName());
        loadChorePoints(chore);
        loadChorePhoto(chore);
        loadChoreDueTimeText(chore);
        loadChoreApprovalStatus(chore);
    }

    public void loadChorePoints(Chore chore) {
        if (!chore.getChoreReward().equals("")) {
            mCurrentChorePoints.setText(chore.getChoreReward());
        } else {
            mCurrentChorePoints.setText(R.string.detail_no_chore_points);
        }
    }

    public void loadChorePhoto(Chore chore) {
        if (chore.getChorePhotoUrl() != null) {
            Glide.with(ChoreDetailFragment.this)
                    .load(chore.getChorePhotoUrl())
                    .into(mChorePic);
        } else {
            mChorePic.setImageResource(R.drawable.sink);
        }
    }

    public void loadChoreDueTimeText(Chore chore) {
        String choreTimeString;
        String choreDateString;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_SHOW_DATE;
        choreTimeString = DateUtils.formatDateTime(getActivity(), chore.getChoreTime(), timeFlag);
        choreDateString = DateUtils.formatDateTime(getActivity(), chore.getChoreTime(), dateFlag);
        mDueDate.setText(String.format("%s%s%s%s", getString(R.string.detail_due_string), choreDateString, getString(R.string.add_chore_at_string), choreTimeString));
    }

    public void loadChoreApprovalStatus(Chore chore) {
        mIsChoreApprovalRequired = chore.getIsChoreApprovalRequired();
        mIsChoreApproved = chore.getIsChoreApproved();
        if (mIsChoreApprovalRequired) {
            mApprovalStatus.setText(R.string.detail_awaiting_approval);
        } else {
            mApprovalStatus.setText(R.string.detail_approved);
        }
    }

    public void updateChoreBasedOnListSelection(Chore chore) {
        displayChoreDetails(chore);
    }

    public void onChorePointsTotaled(int totalChorePoints) {
        mTotalPointsTextView.setText(String.valueOf(totalChorePoints));
    }

}
