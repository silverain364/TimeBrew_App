package com.t2f4.timebrew;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;

public class TableInfoPage extends Activity {

    Button time_retouch;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_info);

        time_retouch = findViewById(R.id.time_retouch);

        // 버튼 터치 시 커스텀 다이얼로그 창을 표시
        time_retouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showCustomDialog();
                    // 경고 해결을 위해 performClick 호출
                    v.performClick();
                }
                return true;
            }
        });
    }

    // 커스텀 다이얼로그 창 표시 메소드
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