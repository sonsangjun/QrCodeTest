package com.example.qrcodetest.qrcode;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodetest.R;

/**
 * RecyclerView는 ViewHolder를 직접구현해야한다.
 */
public class QrcodeRecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView index, content, qrcodeByte;

    public QrcodeRecyclerViewHolder(View itemView){
        super(itemView);
        index = itemView.findViewById(R.id.qrcodeIndex);
        content = itemView.findViewById(R.id.qrcodeContent);
        qrcodeByte = itemView.findViewById(R.id.qrcodeByte);
    }

    public void setTextView(final int index, final String content, final String qrcodeByte){
        this.content.setText(content);
        this.index.setText(String.valueOf(index));
        this.qrcodeByte.setText(qrcodeByte);
    }
}
