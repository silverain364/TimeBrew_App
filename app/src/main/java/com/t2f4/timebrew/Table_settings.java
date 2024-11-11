package com.t2f4.timebrew;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;

public class Table_settings extends Activity {

    public WebView table_set = null;
    private Button table_check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_settings);

        table_set = findViewById(R.id.table_set);
        table_check = findViewById(R.id.table_check);

        // 웹뷰의 설정을 통해 JS 사용을 허용하도록 변경
        WebSettings settings = table_set.getSettings();
        settings.setJavaScriptEnabled(true);

        // 웹 문서가 현재 웹 뷰 안에서 열리도록 설정
        table_set.setWebViewClient(new WebViewClient());
        table_set.setWebChromeClient(new WebChromeClient());

        // 웹 문서 로드
        table_set.loadUrl("file:///android_asset/drag_and_drop2.html");

        // table_check 버튼 클릭 시 팝업 다이얼로그 표시
        table_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    // 뒤로 가기 기능
    public void onBackPressed() {
        if (table_set.canGoBack()) {
            table_set.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // 팝업 다이얼로그 표시 메서드
    private void showPopup() {
        Dialog dialog = new Dialog(this);
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
        Dialog dialog = new Dialog(this);
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