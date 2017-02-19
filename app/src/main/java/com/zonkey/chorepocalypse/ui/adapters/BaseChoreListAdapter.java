package com.zonkey.chorepocalypse.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class BaseChoreListAdapter extends RecyclerView.Adapter<ChoreListAdapterViewHolder> {

    private ChoreListAdapterInterface mInterface;
    private List<Chore> mChoreList;

    public void setData(List<Chore> data) {
        mChoreList.clear();
        if (data != null) {
            mChoreList.addAll(data);
        }
        mInterface.onItemCountChange(getItemCount());
        int totalPoints = getListTotal();
        mInterface.onChorePointsTotaled(totalPoints);
        Log.v("TOTAL POINTS = ", String.valueOf(totalPoints));
        notifyDataSetChanged();
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

        viewHolder
                .mChoreListNameTextView.setText(mChoreList.get(position).getChoreName());
        if (mChoreList.get(position).getChoreReward().equals("0")) {
            viewHolder.mChoreListPointsTextView.setText(R.string.detail_no_chore_points);
        } else {
            viewHolder
                    .mChoreListPointsTextView.setText(mChoreList.get(position).getChoreReward());
        }

//        viewHolder.mChoreListCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!viewHolder.mChoreListCheckBox.isChecked())
//                    mChoreList.get(position).setChoreReward(String.valueOf(0));
//            }
//        });

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
            int i = Integer.parseInt(chore.getChoreReward());
            sum += i;
        }
        return sum;
    }
}
