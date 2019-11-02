package com.example.qrcodetest.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodetest.R;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class QrcodeImpl {
//    private static final String TAG = "QrcodeImpl";
    private static final String fileName = "_QrData.txt";
    private static final String fileDir = Environment.DIRECTORY_DOWNLOADS;

    private final Activity mainActivity;
    private final IntentIntegrator obj;
    private final RecyclerView resultView;
    private final QrcodeRecyclerViewAdapter resultViewAdapter;

    /**
     * 생성자
     * @param activity context
     */
    public QrcodeImpl(Activity activity){
        obj = new IntentIntegrator(activity);
        obj.setBeepEnabled(false);
        mainActivity = activity;
        resultView = mainActivity.findViewById(R.id.resultView);
        resultViewAdapter = new QrcodeRecyclerViewAdapter(mainActivity);
        resultView.setAdapter(resultViewAdapter);
        resultView.setLayoutManager(new LinearLayoutManager(mainActivity));

        QrcodeItemTouchHelper qrcodeItemTouchHelper = new QrcodeItemTouchHelper(resultViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(qrcodeItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(resultView);
    }

    /**
     * Qr코드 결과 처리
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public void prcsResult(int requestCode, int resultCode, Intent data){
        resultViewAdapter.prcsResult(requestCode, resultCode, data);
    }


    /***********************************************************************************************/
    /** Defined Button Event */
    /***********************************************************************************************/
    /**
     * 버튼 초기화
     * @param activity
     */
    public void initButton(Activity activity){
        Button saveBtn =  activity.findViewById(R.id.saveBtn);
        Button resetBtn =  activity.findViewById(R.id.resetBtn);
        Button scanBtn =  activity.findViewById(R.id.scanBtn);

        saveBtn.setOnClickListener(getSaveBtn());
        resetBtn.setOnClickListener(getResetBtn());
        scanBtn.setOnClickListener(getScanBtn());
    }

    /**
     * Save버튼 구현
     * @return
     */
    private View.OnClickListener getSaveBtn(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Iterator<? extends Object> it = getDataList(QrcodeConstants.DATALIST_CONTENT).iterator();
                final String sFileName = getToday()+fileName;
                // 앱 내부전용 파일
//              File file = new File(mainActivity.getFilesDir(), sFileName);

                // 앱 공용저장공간 파일
                File file = new File(Environment.getExternalStoragePublicDirectory(fileDir), sFileName);

                Toast errToast = Toast.makeText(mainActivity,"저장중 에러가 발생하여 저장에 실패했습니다.",Toast.LENGTH_SHORT);
                Toast suuToast = Toast.makeText(mainActivity,"저장에 성공하였습니다. \n경로 : ["+fileDir+"]\n파일명 ["+sFileName+"]",Toast.LENGTH_SHORT);

                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

//                    if(!file.isDirectory()){
//                        Log.i(TAG,"폴더를 생성합니다.");
//                        file.mkdir();
//                    }

                    while (it.hasNext()) {
                        String content =  (String)it.next();
                        bufferedWriter.write(content == null ? "" : content);
                    }

                    if(fileWriter == null || bufferedWriter == null){
                        errToast.show();
                        return ;
                    }

                    bufferedWriter.flush();
                    bufferedWriter.close();

                    fileWriter.close();
                    suuToast.show();

                } catch (IOException e) {
                    errToast.show();
                    e.printStackTrace();
                }
                // TEST 진짜 파일이 있는지 체크
                // 안드로이드 최근버젼은 그냥 휴대폰 연결한다고 해서 파일을 찾아 볼 수 없음. (안슈쓰면 가능은 함... shift 2 -> Device...)
                //
//                try{
//                    File sFile = new File(mainActivity.getFilesDir(),sFileName);
//                    FileReader fileReader = new FileReader(sFile);
//                    BufferedReader bufferedReader = new BufferedReader(fileReader);
//                    if(file.exists()){
//                        Log.i(TAG,"파일이 있습니다.");
//                        Log.i(TAG,bufferedReader.readLine());
//                    }else{
//                        Log.i(TAG,"그딴거 없음.");
//
//                    }
//                    bufferedReader.close();
//                    fileReader.close();
//
//                }catch(Exception e){
//                    e.printStackTrace();;
//                }
            }
        };
    }

    /**
     * getResetBtn 구현
     * @return
     */
    private View.OnClickListener getResetBtn(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                resultViewAdapter.resetDataList();
                Toast.makeText(mainActivity,"데이터 목록이 초기화 되었습니다.",Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * getScanBtn 구현
     * @return
     */
    private View.OnClickListener getScanBtn(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                obj.initiateScan();
            }
        };
    }


    /***********************************************************************************************/
    /** (get DataList) */
    /***********************************************************************************************/
    /**
     * (외부) 데이터List 내보내기
     * @param flag
     * @return
     */
    public List<? extends Object> getDataList(String flag){
        return resultViewAdapter.getDataList(flag);
    }

    /**
     * (외부) textViewIdx 반환
     * @return
     */
    public int getTextViewIdx(){
        return resultViewAdapter.getTextViewIdx();
    }

    /**
     * (외부) 데이터List 가져오기
     * <pre>
     *     dataList를 초기화 후, 새로 덮어쓴다.
     * </pre>
     * @param idxList
     * @param contentList
     */
    public void setDataList(List<Integer> idxList, List<String> contentList){
        resultViewAdapter.setDataList(idxList,contentList);
    }

    /**
     * (외부) textViewIdx 설정
     * @param textViewIdx
     */
    public void setTextViewIdx(int textViewIdx){
        resultViewAdapter.setTextViewIdx(textViewIdx);
    }

    /***********************************************************************************************/
    /** Common */
    /***********************************************************************************************/
    private String getToday(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss", Locale.KOREA);
        return simpleDateFormat.format(new Date());
    }
 }
