package com.example.bo.todolist;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.Collection;
import java.util.Collections;

public class RecyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private  static final String TAG = "Todo";
    RecyclerView.Adapter mAdapter;
    boolean isSwapEnable;
    boolean isFirstDragUnable;

    public RecyItemTouchHelperCallback(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        isSwapEnable = true;
        isFirstDragUnable = false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipFalgs = 0;
            return makeMovementFlags(dragFlags,swipFalgs);
        }
        else {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipFalgs = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags,swipFalgs);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int adapterPos = viewHolder.getAdapterPosition();
        ((TodoItemAdapter)mAdapter).removeItem(adapterPos);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
        int pos = viewHolder.getAdapterPosition();
            Log.d(TAG, "touchHelper:set to light gray : "+ pos);
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isFirstDragUnable;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwapEnable;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        int fromPos = viewHolder.getAdapterPosition();
        int toPos = viewHolder1.getAdapterPosition();

        if(isFirstDragUnable && toPos == 0) {
            return false;
        }

        if(fromPos < toPos) {
            for(int i = fromPos; i < toPos; i ++) {
                Collections.swap(((TodoItemAdapter)mAdapter).getDataList(), i, i + 1);
            }
        }
        else  {
            for(int i = fromPos; i > toPos; i --) {
                Collections.swap(((TodoItemAdapter)mAdapter).getDataList(), i, i - 1);
            }
        }
        ((TodoItemAdapter)mAdapter).moveItem(fromPos,toPos);
        return true;
    }
}
