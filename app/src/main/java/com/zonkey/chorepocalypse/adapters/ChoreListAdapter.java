package com.zonkey.chorepocalypse.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zonkey.chorepocalypse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickbradshaw on 1/14/17.
 */

public class ChoreListAdapter extends RecyclerView.Adapter<ChoreListAdapter.ChoreListAdapterViewHolder> {

    final private ChoreListAdapterOnClickHandler mClickHandler;
    private LayoutInflater mLayoutInflater;

    public static class ChoreListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.chore_test_textview)
        TextView mChoreTestTextView;

        public ChoreListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("RecyclerView", "CLICK!");
            Toast.makeText(view.getContext(), "You clicked", Toast.LENGTH_SHORT).show();
        }
    }

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
        choreListAdapterViewHolder.mChoreTestTextView.setText("Chore number 1");

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
