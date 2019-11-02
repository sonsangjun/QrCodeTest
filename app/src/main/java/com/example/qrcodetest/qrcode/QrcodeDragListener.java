package com.example.qrcodetest.qrcode;

import android.content.Context;

public class QrcodeDragListener {
    public interface OnStartDragListener {
        void onStartDrag(QrcodeRecyclerViewHolder qrcodeRecyclerViewHolder);
    }

    private final Context context;
    private final OnStartDragListener onStartDragListener;
    public QrcodeDragListener(Context context, OnStartDragListener onStartDragListener){
        this.context = context;
        this.onStartDragListener = onStartDragListener;
    }
}
