package com.example.qrcodetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodetest.qrcode.QrcodeConstants;
import com.example.qrcodetest.qrcode.QrcodeImpl;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QrcodeImpl";
    QrcodeImpl qrcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"======================================== onCreate Start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initQrcode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        qrcode.prcsResult(requestCode,resultCode,data);
    }

    /**
     * 액티비티 상태 저장
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG,"======================================== onSaveInstanceState Start");

        super.onSaveInstanceState(outState);
        int size = qrcode.getDataList(null).size();
        ArrayList<Integer> idxList = new ArrayList<>(size);
        ArrayList<String> contentList = new ArrayList<>(size);

        Iterator<? extends Object> srcIdxIt = qrcode.getDataList(QrcodeConstants.DATALIST_ID).iterator();
        Iterator<? extends Object> contentIt = qrcode.getDataList(QrcodeConstants.DATALIST_CONTENT).iterator();

        while(srcIdxIt.hasNext()){
            idxList.add((Integer) srcIdxIt.next());
            contentList.add((String)contentIt.next());
        }

        outState.putIntegerArrayList(QrcodeConstants.DATALIST_ID,idxList);
        outState.putStringArrayList(QrcodeConstants.DATALIST_CONTENT,contentList);
        outState.putInt(QrcodeConstants.TEXT_VIEW_IDX, qrcode.getTextViewIdx());
    }

    /**
     * 액티비티 상태 복원
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG,"======================================== onRestoreInstanceState Start");

        super.onRestoreInstanceState(savedInstanceState);
        initQrcode();
        qrcode.setDataList(
                 savedInstanceState.getIntegerArrayList(QrcodeConstants.DATALIST_ID)
                ,savedInstanceState.getStringArrayList(QrcodeConstants.DATALIST_CONTENT)
        );
        qrcode.setTextViewIdx(savedInstanceState.getInt(QrcodeConstants.TEXT_VIEW_IDX));
    }

    /**
     * onCreate 나 onRestoreInstanceState 시 초기화
     */
    private void initQrcode(){
        if(qrcode==null){
            qrcode = new QrcodeImpl(this);
            qrcode.initButton(this);
        }
    }
}
