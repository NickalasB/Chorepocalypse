package com.zonkey.chorepocalypse.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class ChoreListAdapter extends com.zonkey.chorepocalypse.listeners.ChoreListListener {

    final private ChoreListAdapterOnClickHandler mClickHandler;
    private LayoutInflater mLayoutInflater;
    private DatabaseReference mChoreReference;

    public interface ChoreListAdapterOnClickHandler {
        void onClick(ChoreListAdapterViewHolder vh);
    }

    public ChoreListAdapter(Context context, ChoreListAdapterOnClickHandler clickHandler) {
        super();
        mClickHandler = clickHandler;
        mLayoutInflater = LayoutInflater.from(context);
        mChoreReference = FirebaseDatabase.getInstance().getReference("chores");
    }

    @Override
    public ChoreListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.chore_recyclerview_item, viewGroup, false);
        view.setFocusable(true);
        return new ChoreListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoreListAdapterViewHolder choreListAdapterViewHolder, int position) {

        choreListAdapterViewHolder
                .mChoreListNameTextView.setText(mChoreList.get(position).getChoreName());
        choreListAdapterViewHolder
                .mChoreListPointsTextView.setText(mChoreList.get(position).getChoreReward());
    }

    @Override
    public int getItemCount() {
        return mChoreList.size();
    }

    public void onPause() {
        mChoreReference.removeEventListener(this);

    }

    public void onResume() {
        mChoreReference.addChildEventListener(this);

    }

}
