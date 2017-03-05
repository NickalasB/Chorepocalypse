package com.zonkey.chorepocalypse.touchHelper;

/**
 * Created by nickbradshaw on 3/4/17.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int adapterPosition, int adapterPosition1);

    void onItemDismiss(int adapterPosition);
}