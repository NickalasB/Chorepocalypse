package com.zonkey.chorepocalypse.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.data.Chore;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class ChoreListAdapter extends RecyclerView.Adapter<ChoreListAdapterViewHolder> implements ChildEventListener {

    final private ChoreListAdapterOnClickHandler mClickHandler;
    private LayoutInflater mLayoutInflater;
    private List<Chore> mChoreList;
    private DatabaseReference mChoreReference;

    public interface ChoreListAdapterOnClickHandler {
        void onClick(ChoreListAdapterViewHolder vh);
    }

    public ChoreListAdapter(Context context, ChoreListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mLayoutInflater = LayoutInflater.from(context);
        mChoreList = new ArrayList<>();
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
        choreListAdapterViewHolder.updateDisplay();
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

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Chore chore = dataSnapshot.getValue(Chore.class);
        int index = mChoreList.size();
        if (!mChoreList.contains(chore)) {
            mChoreList.add(chore);
            notifyItemInserted(index);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Chore chore = dataSnapshot.getValue(Chore.class);
        int index = mChoreList.indexOf(chore);
        if (mChoreList.remove(chore)) {
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
