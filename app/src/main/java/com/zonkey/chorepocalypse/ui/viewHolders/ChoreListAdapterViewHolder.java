package com.zonkey.chorepocalypse.ui.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;
import com.zonkey.chorepocalypse.ui.adapters.BaseChoreListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class ChoreListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final List<Chore> mChoreList;
    private BaseChoreListAdapter.ChoreListAdapterInterface mchoreListAdapterInterface;

    @BindView(R.id.chore_list_points_textview)
    public TextView mChoreListPointsTextView;

    @BindView(R.id.chore_list_chore_name_textview)
    public TextView mChoreListNameTextView;

    @BindView(R.id.chore_list_due_date_textview)
    public TextView mChoreListDueDateTextView;

    @BindView(R.id.chore_list_checkbox)
    public CheckBox mChoreListCheckBox;

    @BindView(R.id.chore_list_approval_status_textview)
    public TextView mChoreListApprovalStatusTextView;

    public ChoreListAdapterViewHolder(View itemView, List<Chore> mChoreList) {
        super(itemView);
        this.mChoreList = mChoreList;
        ButterKnife.bind(this, itemView);
        mchoreListAdapterInterface = (BaseChoreListAdapter.ChoreListAdapterInterface) itemView.getContext();
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("RecyclerView", "CLICK!");
        getAdapterPosition();
        mchoreListAdapterInterface.onListChoreSelected(mChoreList.get(getAdapterPosition()));
    }
}