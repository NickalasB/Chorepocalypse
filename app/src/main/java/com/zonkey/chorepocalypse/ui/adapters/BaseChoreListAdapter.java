package com.zonkey.chorepocalypse.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
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
    private DatabaseReference mChoreReference;
    private List<Chore> mChoreList;
    private ArrayList<String> mAllPointsList;
    private int totalPoints;


    public interface ChoreListAdapterInterface {
        void onListChoreSelected(Chore chore);
        void onChorePointsTotaled(int totalChorePoints);
        void onItemCountChange(int itemCount);
    }

    public BaseChoreListAdapter(ChoreListAdapterInterface adapterInterface) {
        super();
        mInterface = adapterInterface;
        mChoreReference = FirebaseDatabase.getInstance().getReference("chores");
        mChoreList = new ArrayList<>();
        mAllPointsList = new ArrayList<>();
    }

    @Override
    public ChoreListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chore_recyclerview_item, viewGroup, false);
        view.setFocusable(true);
        Context context = view.getContext();
        return new ChoreListAdapterViewHolder(view, context, mChoreList);
    }

    @Override
    public void onBindViewHolder(ChoreListAdapterViewHolder viewHolder, int position) {

        viewHolder
                .mChoreListNameTextView.setText(mChoreList.get(position).getChoreName());
        if (mChoreList.get(position).getChoreReward().equals("0")) {
            viewHolder.mChoreListPointsTextView.setText(R.string.detail_no_chore_points);
        } else {
            viewHolder
                    .mChoreListPointsTextView.setText(mChoreList.get(position).getChoreReward());
        }

        String choreTimeString;
        String choreDateString;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_SHOW_DATE;
        choreTimeString = DateUtils.formatDateTime(viewHolder.itemView.getContext(), mChoreList.get(position).getChoreTime(), timeFlag);
        choreDateString = DateUtils.formatDateTime(viewHolder.itemView.getContext(), mChoreList.get(position).getChoreTime(), dateFlag);
        viewHolder.mChoreListDueDateTextView.setText(String.format("%s%s%s%s", viewHolder.itemView.getContext().getString(R.string.detail_due_string), choreDateString, viewHolder.itemView.getContext().getString(R.string.add_chore_at_string), choreTimeString));
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
            mAllPointsList.add(chore.getChoreReward());
            totalPoints = getListTotal();
            mInterface.onChorePointsTotaled(getListTotal());
            Log.v("TOTAL POINTS = ", String.valueOf(totalPoints));
        }
    }


    public int getListTotal(){
        int sum = 0;
        for (String s : mAllPointsList){
            int i = Integer.parseInt(s);
            sum += i;
        }
        return sum;

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
            mInterface.onChorePointsTotaled(getListTotal());
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int childCount = (int) dataSnapshot.getChildrenCount();
        if (childCount == 0) {
            mInterface.onItemCountChange(childCount);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
