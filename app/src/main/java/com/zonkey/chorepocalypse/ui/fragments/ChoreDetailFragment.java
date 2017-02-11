package com.zonkey.chorepocalypse.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChoreDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChoreDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoreDetailFragment extends Fragment {

    @BindView(R.id.detail_chore_title)
    TextView mChoreTitle;

    @BindView(R.id.detail_chore_pic)
    RoundedImageView mChorePic;

    @BindView(R.id.detail_current_chore_points)
    TextView mCurrentChorePoints;

    @BindView(R.id.detail_total_chore_points)
    TextView mTotalPoints;

    @BindView(R.id.detail_chore_due_date)
    TextView mDueDate;

    @BindView(R.id.detail_chore_status_textview)
    TextView mApprovalStatus;

    @BindView(R.id.detail_chore_checkbox)
    CheckBox mChoreCheckBox;


    private OnFragmentInteractionListener mListener;

    public ChoreDetailFragment() {
        // Required empty public constructor
    }

    public static ChoreDetailFragment newInstance() {
        ChoreDetailFragment fragment = new ChoreDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        super.onCreate(savedInstanceState);
        if (args != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchChoreData();
    }

    public void fetchChoreData() {
        FirebaseDatabase choreDatabase = FirebaseDatabase.getInstance();
        DatabaseReference choreDatabaseReference = choreDatabase.getReference("chores");
        Query singleChoreQuery = choreDatabaseReference.limitToLast(1);
        singleChoreQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                        Chore chore = choreSnapshot.getValue(Chore.class);
                        mChoreTitle.setText(chore.getChoreName());
                        if (!chore.getChoreReward().equals("")) {
                            mCurrentChorePoints.setText(chore.getChoreReward());
                        } else {
                            mCurrentChorePoints.setText(R.string.detail_no_chore_points);
                        }
                        loadChorePhoto(chore);
                        setChoreDueTimeText(chore);
                    }
                }
            }

            void loadChorePhoto(Chore chore) {
                if (chore.getChorePhotoUrl() != null) {
                    Glide.with(ChoreDetailFragment.this)
                            .load(chore.getChorePhotoUrl())
                            .into(mChorePic);
                } else {
                    mChorePic.setImageResource(R.drawable.sink);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setChoreDueTimeText(Chore chore) {
        String choreTimeString;
        String choreDateString;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_SHOW_DATE;
        choreTimeString = DateUtils.formatDateTime(getActivity(), chore.getChoreTime(), timeFlag);
        choreDateString = DateUtils.formatDateTime(getActivity(), chore.getChoreTime(), dateFlag);
        mDueDate.setText(String.format("%s%s%s%s", getString(R.string.detail_due_string), choreDateString, getString(R.string.add_chore_at_string), choreTimeString));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chore_detail, container, false);
        ButterKnife.bind(this, rootView);
        setUpCheckBox();
        return rootView;
    }

    private void setUpCheckBox() {
        mChoreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getActivity(), "Check works", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
