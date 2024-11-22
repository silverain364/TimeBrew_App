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
import com.t2f4.timebrew.api.RetrofitSetting;
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
        table_set.loadUrl(RetrofitSetting.FILE_URL + "tmp/table.html");

        return root;
    }


    //    이전 키 눌렀을 때, 뒤로 이동 가능. 뒤로 갈 수 없으면 앱 종료
    public void onBackPassed() {
        if (table_set.canGoBack())
            table_set.goBack();
    }
}
