package com.t2f4.timebrew;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;

public class table_settings extends Activity {

    public WebView table_set = null;
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
        table_set.setWebViewClient(new WebViewClient());
//        alert(), confirm() 같은 팝업 기능의 Js 코드 사용 (혹시 몰라 사용 할 수 있도록 코드 작성)
        table_set.setWebChromeClient(new WebChromeClient());

//웹 뷰가 보여줄 웹 문서 로드
        table_set.loadUrl("file:///android_asset/table_settings.html");
    }
//    이전 키 눌렀을 때, 뒤로 이동 가능. 뒤로 갈 수 없으면 앱 종료
    public void onBackPassed(){
        if(table_set.canGoBack())
            table_set.goBack();
        else
            finish();
    }
}
