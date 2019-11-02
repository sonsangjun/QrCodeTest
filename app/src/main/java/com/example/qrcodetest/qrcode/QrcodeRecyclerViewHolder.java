package com.example.qrcodetest.qrcode;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodetest.R;

/**
 * RecyclerView는 ViewHolder를 직접구현해야한다.
 */
public class QrcodeRecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView index, content;

    public QrcodeRecyclerViewHolder(View itemView){
        super(itemView);
        index = itemView.findViewById(R.id.qrcodeIndex);
        content = itemView.findViewById(R.id.qrcodeContent);
    }

    public void setTextView(int index, String content){
        this.content.setText(content);
        this.index.setText(String.valueOf(index));
    }
}
