package com.zonkey.chorepocalypse.listeners;

import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.zonkey.chorepocalypse.data.Chore;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 1/21/17.
 */
public abstract class ChoreListListener extends RecyclerView.Adapter<ChoreListAdapterViewHolder> implements ChildEventListener {
    protected List<Chore> mChoreList;

    public ChoreListListener() {
        mChoreList = new ArrayList<>();
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
