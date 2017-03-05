package com.zonkey.chorepocalypse.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zonkey.chorepocalypse.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChoreFullScreenDetailActivity extends AppCompatActivity {
    public static final String NAME = "NAME";
    public static final String REWARD = "REWARD";
    public static final String DATE = "DATE";
    public static final String APPROVAL_STATUS = "APPROVAL_STATUS";
    public static final String PHOTO = "PHOTO";

    /*
       FSD = FullScreenDetail
     */

    @BindView(R.id.full_screen_detail_chore_title)
    TextView mFSDChoreTitle;

    @BindView(R.id.full_screen_detail_chore_pic)
    RoundedImageView mFSDChorePic;

    @BindView(R.id.full_screen_detail_current_chore_points)
    TextView mFSDChorePoints;

    @BindView(R.id.full_screen_detail_chore_due_date)
    TextView mFSDChoreDueDate;

    @BindView(R.id.full_screen_approval_status)
    TextView mFSDChoreApprovalStatus;

    Intent mIntent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_detail_full_screen);
        ButterKnife.bind(this);
        mIntent = getIntent();
        loadChoreTextDetails();
        loadChorePhoto();
    }

    private void loadChoreTextDetails() {
        if (mIntent.hasExtra(NAME)) {
            mFSDChoreTitle.setText(mIntent.getStringExtra(NAME));
            mFSDChorePoints.setText(mIntent.getStringExtra(REWARD));
            mFSDChoreDueDate.setText(mIntent.getStringExtra(DATE));

            boolean approved = mIntent.getBooleanExtra(APPROVAL_STATUS, true);
            if (approved) {
                mFSDChoreApprovalStatus.setText(R.string.fsd_approved);
            } else {
                mFSDChoreApprovalStatus.setText(R.string.fsd_awaiting_approval);
            }
        }
    }

    public void loadChorePhoto() {
        if (mIntent.getStringExtra(PHOTO) != null) {
            Glide.with(ChoreFullScreenDetailActivity.this)
                    .load(mIntent.getStringExtra(PHOTO))
                    .into(mFSDChorePic);
        } else {
            mFSDChorePic.setImageResource(R.drawable.sink);
        }
    }
}