package com.zonkey.chorepocalypse.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.loaders.FirebaseChoreLoader;
import com.zonkey.chorepocalypse.models.Chore;
import com.zonkey.chorepocalypse.touchHelper.SimpleItemTouchHelperCallback;
import com.zonkey.chorepocalypse.ui.activities.MainActivity;
import com.zonkey.chorepocalypse.ui.adapters.BaseChoreListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChoreListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Chore>> {

    @BindView(R.id.chore_list_recycler_view)
    RecyclerView mChoreRecyclerView;

    @BindView(R.id.empty_recyclerview)
    TextView mEmptyRecyclerView;

    private BaseChoreListAdapter mChoreListAdapter;

    public ChoreListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chore_list, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mChoreRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mChoreRecyclerView.setHasFixedSize(true);
        mChoreListAdapter = new BaseChoreListAdapter((MainActivity) getActivity(), getContext());
        mChoreRecyclerView.setAdapter(mChoreListAdapter);
        getLoaderManager().initLoader(0, null, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mChoreListAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mChoreRecyclerView);

        return rootView;
    }

    public void onItemCountChange(int itemCount) {
        if (itemCount == 0) {
            mEmptyRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyRecyclerView.setVisibility(View.GONE);
        }
        mChoreListAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<List<Chore>> onCreateLoader(int id, Bundle args) {
        return new FirebaseChoreLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Chore>> loader, List<Chore> data) {
        mChoreListAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Chore>> loader) {
        loader = null;
    }
}