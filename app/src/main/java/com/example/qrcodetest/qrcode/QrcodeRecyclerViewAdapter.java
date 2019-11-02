package com.example.qrcodetest.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodetest.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * RecyclerView는 adapter를 직접구현해야한다.
 */
public class QrcodeRecyclerViewAdapter extends RecyclerView.Adapter{
    private static final String TAG = "QrcodeImpl";
    private Context context;
    private final TextView totByteView;
    private final TextView totCntView;

    private List<QrcodeInfo> dataList;
    private int textViewIdx = 0;

    public QrcodeRecyclerViewAdapter(@NonNull Context context){
        dataList = new LinkedList<>();
        this.context = context;
        totByteView = ((Activity)context).findViewById(R.id.totLengthView);
        totCntView = ((Activity)context).findViewById(R.id.totCntView);
    }

    /***********************************************************************************************/
    /** DataList 관련 메소드 */
    /***********************************************************************************************/
    /**
     * 데이터 리스트 반환
     * @return
     */
    public List<QrcodeInfo> getDataList(){
        return this.dataList;
    }

    /**
     * 데이터 리스트 설정
     * @param list
     */
    public void setDataList(List<QrcodeInfo> list){
        this.dataList = list;
    }

    /**
     * DataList에 데이터 한건 추가
     * @param qrcodeInfo
     */
    public void setDataElement(final QrcodeInfo qrcodeInfo){
        dataList.add(qrcodeInfo);
        notifyDataSetChanged();;
    }

    /**
     * DataList 초기화
     */
    public void resetDataList(){
        dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * (외부) 데이터List 내보내기
     * @param flag
     * @return
     */
    public List<? extends Object> getDataList(final String flag){
        String sFlag = (flag == null ? "" : flag);

        if(sFlag.equals(QrcodeConstants.DATALIST_ID)){
            List<Integer> rstList = new LinkedList<>();
            Iterator<QrcodeInfo> it = dataList.iterator();
            while(it.hasNext()){
                rstList.add(it.next().getIndex());
            }
            return rstList;
        }

        if(sFlag.equals(QrcodeConstants.DATALIST_CONTENT)){
            List<String> rstList = new LinkedList<>();
            Iterator<QrcodeInfo> it = dataList.iterator();
            while(it.hasNext()){
                rstList.add(it.next().getContent());
            }
            return rstList;
        }

        return dataList;
    }

    /**
     * (외부) textViewIdx 반환
     * @return
     */
    public int getTextViewIdx(){
        return this.textViewIdx;
    }

    /**
     * (외부) 데이터List 가져오기
     * <pre>
     *     dataList를 초기화 후, 새로 덮어쓴다.
     * </pre>
     * @param idxList
     * @param contentList
     */
    public void setDataList(final List<Integer> idxList, final List<String> contentList){
        Log.i(TAG,"==================== Size : idxList "+idxList.size()+", contentList "+contentList.size());

        dataList.clear();

        for(int i=0; i<idxList.size(); i++){
            QrcodeInfo q = new QrcodeInfo();
            q.setIndex(idxList.get(i));
            q.setContent(contentList.get(i));
            dataList.add(q);
            Log.i(TAG,"==================== [setDataList]["+i+"] idx : "+q.getIndex()+"\t,content"+q.getContent());
        }

        notifyDataSetChanged();
    }

    /**
     * (외부) textViewIdx 설정
     * @param textViewIdx
     */
    public void setTextViewIdx(final int textViewIdx){
        this.textViewIdx = textViewIdx;
    }

    /**
     * DataList의 총 String 크기(단위 : Byte)
     */
    public BigDecimal getDataListTotStrByte(){
        Iterator<QrcodeInfo> it = this.dataList.iterator();
        BigDecimal totByte = new BigDecimal(0);
        while(it.hasNext()){
            QrcodeInfo qrcodeInfo = it.next();
            String str = qrcodeInfo.getContent();
            if(str==null) {
                continue;
            }

            totByte = totByte.add(new BigDecimal(str.getBytes().length));
        }

        return totByte;
    }

    /**
     * 크기 단위 변환 ( param값은 byte )
     * (1024단위로 B => K => M)
     * @param bigDecimal
     * @return
     */
    public String castByteToBigByte(final BigDecimal bigDecimal){
        BigDecimal Byte_1024 = new BigDecimal(1024);
        BigDecimal rstValue = new BigDecimal(bigDecimal.toString());
        int castDep = QrcodeConstants.CAST_BYTE;

        while(rstValue.compareTo(Byte_1024) > 0){
            rstValue = bigDecimal.divide(Byte_1024,1,BigDecimal.ROUND_HALF_DOWN);
            castDep++;
        }

        switch (castDep){
            case QrcodeConstants.CAST_BYTE : return (rstValue.toString() + " Byte");
            case QrcodeConstants.CAST_KBYTE : return (rstValue.toString() + " KByte");
            case QrcodeConstants.CAST_MBYTE : return (rstValue.toString() + " MByte");
            default : return (bigDecimal.toString() + " Byte");
        }
    }

    /***********************************************************************************************/
    /** 이벤트 정의 메소드 */
    /***********************************************************************************************/
    /**
     * Qr코드 결과 처리
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public void prcsResult(final int requestCode, final int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result == null || result.getContents() == null) {
            return ;
        }

        QrcodeInfo qrcodeInfo = new QrcodeInfo();
        qrcodeInfo.setIndex(this.textViewIdx++);
        qrcodeInfo.setContent(result.getContents());
        setDataElement(qrcodeInfo);
        updateTotView();
    }

    /**
     * TotTextView에 출력되는 정보를 업데이트 한다.
     */
    public void updateTotView(){
        totByteView.setText(String.format("총 %s ", castByteToBigByte(getDataListTotStrByte())));
        totCntView.setText(String.format("총 %s 건",this.dataList.size()));
    }

    /**
     * 드래그를 통한 아이템 이동
     * @param fromPosition
     * @param toPosition
     * @return
     */
    public boolean onItemMove(final int fromPosition, final int toPosition){
        Log.i(TAG,"onItemMove : fromPos "+fromPosition+"\t toPos "+toPosition+"\tdataList.size "+getItemCount()+"\t "+getDataListWithString());

        if(dataList.size() < 1){
            notifyDataSetChanged();
            return false;
        }

        Collections.swap(dataList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 스와이프를 통한 아이템 삭제
     * @param position
     */
    public void onSwipeDelete(final int position){
        Log.i(TAG,"onSwipeDelete : pos "+position+"\tdataList.size "+getItemCount()+"\t "+getDataListWithString());
        if(dataList.size() < 1){
            notifyDataSetChanged();
            return;
        }

        dataList.remove(position);
        updateTotView();
        notifyItemRemoved(position);
    }

    /***********************************************************************************************/
    /** 필수 구현 메소드 */
    /***********************************************************************************************/
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qrcodeinfo_view, parent, false);
        return new QrcodeRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QrcodeInfo qrcodeInfo = this.dataList.get(position);
        QrcodeRecyclerViewHolder viewHolder = (QrcodeRecyclerViewHolder)holder;

        int index = qrcodeInfo.getIndex();
        String content = qrcodeInfo.getContent();
        String codeByteSize = (content == null ? "0" : castByteToBigByte(new BigDecimal(content.getBytes().length)));

        viewHolder.setTextView(index , content, codeByteSize);
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }


    /***********************************************************************************************/
    /** Test Area */
    /***********************************************************************************************/
    /**
     * String 형태의 데이터 정보 가져오기
     * @return
     */
    public List<String> getDataListWithString(){
        Log.i(TAG,"getDataListWithString dataList.size"+dataList.size());
        List<String> rstList = new LinkedList<>();

        Iterator<QrcodeInfo> it = dataList.iterator();
        while(it.hasNext()){
            QrcodeInfo qrcodeInfo = it.next();
            rstList.add(
                    "[" + qrcodeInfo.getIndex() + "]\n"+qrcodeInfo.getContent()
            );
        }
        return rstList;
    }
}
