package com.zonkey.chorepocalypse.ui.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zonkey.chorepocalypse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class ChoreListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.chore_list_points_textview)
    public TextView mChoreListPointsTextView;

    @BindView(R.id.chore_list_chore_name_textview)
    public TextView mChoreListNameTextView;

    public ChoreListAdapterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("RecyclerView", "CLICK!");
        Toast.makeText(view.getContext(), "You clicked # " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
    }
}
