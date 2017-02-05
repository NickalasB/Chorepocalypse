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
import com.google.firebase.database.ValueEventListener;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class BaseChoreListAdapter extends RecyclerView.Adapter<ChoreListAdapterViewHolder> implements ChildEventListener, ValueEventListener {

    private ChoreListAdapterInterface mInterface;
    private LayoutInflater mLayoutInflater;
    private DatabaseReference mChoreReference;
    private List<Chore> mChoreList;


    public interface ChoreListAdapterInterface {
        void onClick(ChoreListAdapterViewHolder vh);

        void onItemCountChange(int itemCount);
    }

    public BaseChoreListAdapter(Context context, ChoreListAdapterInterface adapterInterface) {
        super();
        mInterface = adapterInterface;
        mLayoutInflater = LayoutInflater.from(context);
        mChoreReference = FirebaseDatabase.getInstance().getReference("chores");
        mChoreList = new ArrayList<>();
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
        mChoreReference.removeEventListener((ChildEventListener) this);

    }

    public void onResume() {
        mChoreReference.addChildEventListener(this);
        mChoreReference.addListenerForSingleValueEvent(this);

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Chore chore = dataSnapshot.getValue(Chore.class);
        int index = mChoreList.size();
        if (!mChoreList.contains(chore)) {
            mChoreList.add(chore);
            notifyItemInserted(index);
            mInterface.onItemCountChange(getItemCount());
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
            mInterface.onItemCountChange(getItemCount());
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int childCount = (int) dataSnapshot.getChildrenCount();
        if (childCount == 0){
            mInterface.onItemCountChange(childCount);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
