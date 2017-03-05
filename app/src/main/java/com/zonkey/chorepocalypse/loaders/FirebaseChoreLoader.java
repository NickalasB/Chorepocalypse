package com.zonkey.chorepocalypse.loaders;

import android.content.Context;
import android.support.v4.content.Loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zonkey.chorepocalypse.models.Chore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 2/18/17.
 */

public class FirebaseChoreLoader extends Loader<List<Chore>> implements ValueEventListener {

    private static final String CHORES = "chores";
    private DatabaseReference mChoreReference;

    public FirebaseChoreLoader(Context context) {
        super(context);
        mChoreReference = FirebaseDatabase.getInstance().getReference(CHORES);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mChoreReference.addListenerForSingleValueEvent(this);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        mChoreReference.removeEventListener(this);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mChoreReference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Chore> choreList = new ArrayList<>();
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            choreList.add(childSnapshot.getValue(Chore.class));
        }
        deliverResult(choreList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        deliverCancellation();
    }
}