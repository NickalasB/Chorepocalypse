package com.zonkey.chorepocalypse.adapters.viewHolders;

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

    public void updateDisplay(){
        mChoreTestTextView.setText("Chore number 1");
    }
}
