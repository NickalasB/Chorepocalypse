package com.zonkey.chorepocalypse.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.ui.adapters.ChoreListAdapter;
import com.zonkey.chorepocalypse.ui.viewHolders.ChoreListAdapterViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChoreListFragment extends Fragment {

    @BindView(R.id.chore_list_recycler_view)
    RecyclerView mChoreRecyclerView;

    @BindView(R.id.empty_recyclerview)
    TextView mEmptyRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;
    private ChoreListAdapter mChoreListAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChoreListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoreListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoreListFragment newInstance(String param1, String param2) {
        ChoreListFragment fragment = new ChoreListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chore_list, container, false);
        ButterKnife.bind(this, rootView);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mChoreRecyclerView.setLayoutManager(mLinearLayoutManager);
        mChoreRecyclerView.setHasFixedSize(true);

        mChoreListAdapter = new ChoreListAdapter(getActivity(), new ChoreListAdapter.ChoreListAdapterOnClickHandler() {
            @Override
            public void onClick(ChoreListAdapterViewHolder vh) {
            }
        });
        checkAdapterIsEmpty();

        mChoreRecyclerView.setAdapter(mChoreListAdapter);

        return rootView;
    }

    private Boolean checkAdapterIsEmpty() {
        if (mChoreListAdapter.getItemCount() == 0) {
            mEmptyRecyclerView.setVisibility(View.VISIBLE);
            return true;
        } else {
            mEmptyRecyclerView.setVisibility(View.GONE);
        }
        return false;
    }
    
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        mChoreListAdapter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mChoreListAdapter.onResume();
    }
}