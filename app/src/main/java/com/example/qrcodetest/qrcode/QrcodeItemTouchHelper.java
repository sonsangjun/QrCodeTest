package com.example.qrcodetest.qrcode;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class QrcodeItemTouchHelper extends ItemTouchHelper.Callback {
    private static final String TAG = "QrcodeImpl";
    QrcodeRecyclerViewAdapter adapter;

    public QrcodeItemTouchHelper(@NonNull QrcodeRecyclerViewAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        adapter.onItemMove(fromPosition, toPosition);

        Log.i(TAG,"===================== onMove : fromPos "+fromPosition+",\t toPos "+toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onSwipeDelete(viewHolder.getAdapterPosition());

        Log.i(TAG,"===================== onSwiped : position"+viewHolder.getAdapterPosition());
    }
}
