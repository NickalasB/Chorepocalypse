package com.zonkey.chorepocalypse.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;
import com.zonkey.chorepocalypse.touchHelper.ItemTouchHelperAdapter;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class BaseChoreListAdapter extends RecyclerView.Adapter<ChoreListAdapterViewHolder> implements ItemTouchHelperAdapter {

    private ChoreListAdapterInterface mInterface;
    private List<Chore> mChoreList;
    private int i;
    int mTotalPoints;

    public void setData(List<Chore> data) {
        mChoreList.clear();
        if (data != null) {
            mChoreList.addAll(data);
        }
        mInterface.onItemCountChange(getItemCount());
        mTotalPoints = getListTotal();
        mInterface.onChorePointsTotaled(mTotalPoints);
        Log.v("TOTAL POINTS = ", String.valueOf(mTotalPoints));
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Chore previousChore = mChoreList.remove(fromPosition);
        mChoreList.add(toPosition > fromPosition ? toPosition -1 : toPosition, previousChore);
        notifyItemRemoved(fromPosition);
    }

    @Override
    public void onItemDismiss(final int position) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query choreQuery = databaseRef.child("chores").child(mChoreList.get(position).getChoreKey());
        choreQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    choreSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("BaseChoreListAdapter", "onCancelled", databaseError.toException());
            }
        });

        mChoreList.remove(position);
        notifyItemRemoved(position);
    }

    public interface ChoreListAdapterInterface {
        void onListChoreSelected(Chore chore);

        void onChorePointsTotaled(int totalChorePoints);

        void onItemCountChange(int itemCount);

    }

    public BaseChoreListAdapter(ChoreListAdapterInterface adapterInterface) {
        super();
        mInterface = adapterInterface;
        mChoreList = new ArrayList<>();
    }

    @Override
    public ChoreListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chore_recyclerview_item, viewGroup, false);
        view.setFocusable(true);
        return new ChoreListAdapterViewHolder(view, mChoreList);
    }

    @Override
    public void onBindViewHolder(final ChoreListAdapterViewHolder viewHolder, final int position) {
        getChoreApprovalStatus(viewHolder, position);
        getChoreName(viewHolder, position);
        getChoreReward(viewHolder, position);
        getChoreDueDate(viewHolder, position);
        viewHolder.mChoreListCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (viewHolder.mChoreListCheckBox.isChecked()) {
                    // TODO: 2/19/17 set the value of i to be the value of the chore points else i = 0
                }
            }
        });
    }

    private void getChoreName(ChoreListAdapterViewHolder viewHolder, int position) {
        viewHolder.mChoreListNameTextView.setText(mChoreList.get(position).getChoreName());
    }

    private void getChoreReward(ChoreListAdapterViewHolder viewHolder, int position) {
        if (mChoreList.get(position).getChoreReward().equals("0")) {
            viewHolder.mChoreListPointsTextView.setText(R.string.detail_no_chore_points);
        } else {
            viewHolder.mChoreListPointsTextView.setText(mChoreList.get(position).getChoreReward());
        }
    }

    private void getChoreApprovalStatus(ChoreListAdapterViewHolder viewHolder, int position) {
        viewHolder.mChoreListCheckBox.setChecked(mChoreList.get(position).getIsChoreApproved());
    }

    private void getChoreDueDate(ChoreListAdapterViewHolder viewHolder, int position) {
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

    private int getListTotal() {
        int sum = 0;
        for (Chore chore : mChoreList) {
            i = Integer.parseInt(chore.getChoreReward());
            sum += i;
        }
        return sum;
    }
}
