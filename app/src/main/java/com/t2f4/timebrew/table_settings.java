package com.t2f4.timebrew;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class table_settings extends Activity {

    public WebView table_set = null;
    private List<Integer> tableNumberList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_settings);

        table_set = findViewById(R.id.table_set);

//      웹뷰의 설정을 통해 JS 사용을 허용하도록 변경
        WebSettings settings = table_set.getSettings();
        settings.setJavaScriptEnabled(true);

        //      웹 분서가 열릴 때 기본적으로 내 웹 뷰가 아닌
//      새로운 웹 뷰를 열어주는 방식사용 -> 현재 웹 뷰 안에 웹 문서가 열리도록 설정
        table_set.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript("javascript:getTableNumbers()", value -> {
                    Log.d("javascript", value.toString());
                    Toast.makeText(table_settings.this, value.toString(), Toast.LENGTH_SHORT).show();

                    try {
                        Log.d("javascript", value.toString());
                        Toast.makeText(table_settings.this, value.toString(), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = new JSONArray(value.toString());

                        for (int i = 0; i < jsonArray.length(); i++)
                            tableNumberList.add(jsonArray.getInt(i));

                        Log.d("javascript", "convert : " + tableNumberList);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });


//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1500);
//                    runOnUiThread(() -> {
//                        table_set.evaluateJavascript("javascript:getTableNumbers()", value ->{
//                            try {
//                                Log.d("javascript", value.toString());
//                                Toast.makeText(table_settings.this,  value.toString(), Toast.LENGTH_SHORT).show();
//
//                                JSONArray jsonArray = new JSONArray(value.toString());
//                                List<Integer> tableNumberList = new ArrayList<>();
//
//                                for (int i = 0; i < jsonArray.length(); i++)
//                                    tableNumberList.add(jsonArray.getInt(i));
//
//                                Log.d("javascript", "convert : " + tableNumberList.toArray().toString());
//                            } catch (JSONException e) {throw new RuntimeException(e);}
//
//                        });
//                    });
//
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        }.start();


        // alert(), confirm() 같은 팝업 기능의 Js 코드 사용 (혹시 몰라 사용 할 수 있도록 코드 작성)
        table_set.setWebChromeClient(new WebChromeClient());

        table_set.addJavascriptInterface(this, "AndroidInterface");


        //웹 뷰가 보여줄 웹 문서 로드
        table_set.loadUrl("file:///android_asset/drag_and_drop2.html");


    }

    //    이전 키 눌렀을 때, 뒤로 이동 가능. 뒤로 갈 수 없으면 앱 종료
    public void onBackPassed() {
        if (table_set.canGoBack())
            table_set.goBack();
        else
            finish();
    }

    @JavascriptInterface
    public void selectTable(int tableId){
        Log.d("javascript", "selectTable: " + tableId);
        //Todo. 다이얼로그 띄우기
    }

}
