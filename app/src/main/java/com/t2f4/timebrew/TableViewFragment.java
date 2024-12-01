package com.t2f4.timebrew;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.api.RetrofitSetting;
import com.t2f4.timebrew.server.dto.VibratingBellTimeDto;
import com.t2f4.timebrew.server.repository.RecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableAndRecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableRepository;
import com.t2f4.timebrew.server.service.VibratingBellTimeService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TableViewFragment extends Fragment {
    private ScheduledExecutorService scheduledExecutorService;

    public WebView table_view;
    private Button table_check;
    private List<Integer> tableNumberList = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private TableRepository tableRepository = new TableRepository();
    private TableAndRecognitionDeviceRepository tableAndRecognitionDeviceRepository = new TableAndRecognitionDeviceRepository();
    private RecognitionDeviceRepository recognitionDeviceRepository = new RecognitionDeviceRepository();
    private VibratingBellTimeService vibratingBellTimeService = new VibratingBellTimeService();

    //화면 재실행 시 reload!
    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        table_view.reload();
    }

    @Override
    public void onPause() {
        super.onPause();
        scheduledExecutorService.shutdown();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.table_view, container, false);

        table_view = root.findViewById(R.id.table_view);
        table_check = root.findViewById(R.id.table_check);

//      웹뷰의 설정을 통해 JS 사용을 허용하도록 변경
        WebSettings settings = table_view.getSettings();
        settings.setJavaScriptEnabled(true);

        //      웹 분서가 열릴 때 기본적으로 내 웹 뷰가 아닌
//      새로운 웹 뷰를 열어주는 방식사용 -> 현재 웹 뷰 안에 웹 문서가 열리도록 설정
        table_view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.evaluateJavascript("javascript:loadViewJs()", null);
            }
        });


        // alert(), confirm() 같은 팝업 기능의 Js 코드 사용 (혹시 몰라 사용 할 수 있도록 코드 작성)
        table_view.setWebChromeClient(new WebChromeClient());

        table_view.addJavascriptInterface(this, "AndroidInterface");

        //웹 뷰가 보여줄 웹 문서 로드
        table_view.loadUrl(RetrofitSetting.FILE_URL + "/" + auth.getUid() + "/table.html");


        // table_check 버튼 클릭 시 팝업 다이얼로그 표시
        table_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        return root;
    }


    //    이전 키 눌렀을 때, 뒤로 이동 가능. 뒤로 갈 수 없으면 앱 종료
    public void onBackPassed() {
        if (table_view.canGoBack())
            table_view.goBack();
        else
            return;
        // ();
    }

    @JavascriptInterface
    public void selectTable(int tableId) {
        Log.d("javascript", "selectTable: " + tableId);
        showPopup();
    }

    @JavascriptInterface
    public void loadCompleteJavascript() {
        getActivity().runOnUiThread(() -> {
            table_view.evaluateJavascript("javascript:getTableNumbers()", value -> {
                Log.d("javascript", "source : " + value.toString());

                try {
                    if (value.toString().equals("null")) return;

                    JSONArray jsonArray = new JSONArray(value.toString());
                    for (int i = 0; i < jsonArray.length(); i++)
                        tableNumberList.add(jsonArray.getInt(i));

                    Log.d("javascript", "convert : " + tableNumberList);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            scheduledExecutorService = new ScheduledThreadPoolExecutor(1); //셧다운 후에 재사용이 불가능함으로 필요할 때마다 새로인 인스턴스를 생성시킴
            tableTimeUpdateStart();
        });
    }

    public void tableTimeUpdateStart(){
        scheduledExecutorService.scheduleAtFixedRate(() ->{
            try {
                Log.d("thread", "table update!");
                getActivity().runOnUiThread(() -> {
                    tableRepository.findAll().stream().forEach(dto -> {
                        setTableTime(dto.getTableId(),
                                dto.getBellId() != null ? vibratingBellTimeService.reamingTime(dto.getBellId()) : 0);
                    });
                });
            }catch (Exception e){
                Log.d("thread", "perhaps move page?");
            }
        }, 0, 1, TimeUnit.MINUTES); //1분 간격으로 실행
    }

    public void setTableTime(int tableId, int minute){
        table_view.evaluateJavascript("javascript:setTableTime("+ tableId + " , " + minute + ")", null);
    }

    // 팝업 다이얼로그 표시 메서드
    private void showPopup() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.table_info);

        // 팝업 다이얼로그 크기 설정
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.52);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);
        dialog.getWindow().setLayout(width, height);

        // table_info 팝업 안의 time_retouch 버튼 찾기
        Button time_retouch = dialog.findViewById(R.id.time_retouch);

        // time_retouch 버튼 클릭 시 customDialog 표시
        time_retouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        dialog.show();
    }

    // customDialog 표시 메서드
    private void showCustomDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dlg);

        // 다이얼로그 크기 조정
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.35); // 화면 너비의 85%
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.2); // 화면 높이의 50%
        dialog.getWindow().setLayout(width, height);

        // 커스텀 다이얼로그 내의 버튼 가져오기 (예: 확인 버튼, 취소 버튼 등)
        Button dlg_ok_btn = dialog.findViewById(R.id.dlg_ok_btn);
        Button dlg_cancle_btn = dialog.findViewById(R.id.dlg_cancle_btn);

        dlg_ok_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dialog.dismiss();  // 다이얼로그 닫기
                    v.performClick();  // 경고 해결을 위해 performClick 호출
                }
                return true;
            }
        });

        dlg_cancle_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dialog.dismiss();  // 다이얼로그 닫기
                    v.performClick();  // 경고 해결을 위해 performClick 호출
                }
                return true;
            }
        });

        dialog.show();  // 다이얼로그 표시
    }


}
