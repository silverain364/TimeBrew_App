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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.t2f4.timebrew.server.RESTManager;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TableSettingFragment extends Fragment {

    public WebView table_set;
    private Button table_check;
    private List<Integer> tableNumberList = new ArrayList<>();

    private Button arduino_check;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.table_settings, container, false);

        RESTManager.context = getActivity();

        table_set = root.findViewById(R.id.table_set);
        table_check = root.findViewById(R.id.table_check);
        arduino_check = root.findViewById(R.id.arduino_check);

//      웹뷰의 설정을 통해 JS 사용을 허용하도록 변경
        WebSettings settings = table_set.getSettings();
        settings.setJavaScriptEnabled(true);

        //      웹 분서가 열릴 때 기본적으로 내 웹 뷰가 아닌
//      새로운 웹 뷰를 열어주는 방식사용 -> 현재 웹 뷰 안에 웹 문서가 열리도록 설정
        table_set.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript("javascript:loadSettingJs()", null);
            }
        });


        // alert(), confirm() 같은 팝업 기능의 Js 코드 사용 (혹시 몰라 사용 할 수 있도록 코드 작성)
        table_set.setWebChromeClient(new WebChromeClient());


        //웹 뷰가 보여줄 웹 문서 로드
        table_set.loadUrl("file:///android_asset/setting/table.html");

        // table_check 버튼 클릭 시 팝업 다이얼로그 표시
        table_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        arduino_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuzzerPopup();
            }
        });


        return root;
    }


    //    이전 키 눌렀을 때, 뒤로 이동 가능. 뒤로 갈 수 없으면 앱 종료
    public void onBackPassed() {
        if (table_set.canGoBack())
            table_set.goBack();
        else
            return;
        // ();
    }



    // 팝업 다이얼로그 표시 메서드
    private void showPopup() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.table_info);

        // 팝업 다이얼로그 크기 설정
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.46);
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

    private void showBuzzerPopup() {
        Dialog dialog2 = new Dialog(requireContext());
        dialog2.setContentView(R.layout.buzzer_rec);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.23);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.63);
        dialog2.getWindow().setLayout(width, height);

        Button bt_set_btn = dialog2.findViewById(R.id.bt_set_btn);

        bt_set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog2();
            }
        });

        dialog2.show();
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

        dlg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // 다이얼로그 닫기
            }
        });

        dlg_cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // 다이얼로그 닫기
            }
        });

        dialog.show();  // 다이얼로그 표시
    }


    private void showCustomDialog2() {
        Dialog dialog2 = new Dialog(getContext());
        dialog2.setContentView(R.layout.custom_dlg);

        // 다이얼로그 크기 조정
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.35); // 화면 너비의 85%
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.2); // 화면 높이의 50%
        dialog2.getWindow().setLayout(width, height);

        // 커스텀 다이얼로그 내의 버튼 가져오기 (예: 확인 버튼, 취소 버튼 등)
        Button dlg_ok_btn = dialog2.findViewById(R.id.dlg_ok_btn);
        Button dlg_cancle_btn = dialog2.findViewById(R.id.dlg_cancle_btn);

        dlg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();  // 다이얼로그 닫기
            }
        });

        dlg_cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();  // 다이얼로그 닫기
            }
        });

        dialog2.show();  // 다이얼로그 표시
    }
}
