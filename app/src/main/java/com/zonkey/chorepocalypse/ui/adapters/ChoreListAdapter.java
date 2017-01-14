package com.zonkey.chorepocalypse.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class ChoreListAdapter extends RecyclerView.Adapter<ChoreListAdapterViewHolder> {

    final private ChoreListAdapterOnClickHandler mClickHandler;
    private LayoutInflater mLayoutInflater;


    public interface ChoreListAdapterOnClickHandler {
        void onClick(ChoreListAdapterViewHolder vh);
    }

    public ChoreListAdapter(Context context, ChoreListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mLayoutInflater = LayoutInflater.from(context);
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
        return 5;
    }
}
