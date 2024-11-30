package com.t2f4.timebrew;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.api.RetrofitSetting;
import com.t2f4.timebrew.api.TableSpaceApi;
import com.t2f4.timebrew.server.RESTManager;
import com.t2f4.timebrew.server.dto.TableDto;
import com.t2f4.timebrew.server.repository.TableAndRecognitionDevice;
import com.t2f4.timebrew.server.repository.TableRepository;
import com.t2f4.timebrew.util.CustomCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TableSettingFragment extends Fragment {

    private WebView table_set;
    private TableRepository tableRepository = new TableRepository();
    private TableAndRecognitionDevice tableAndRecognitionDevice = new TableAndRecognitionDevice();
    private TableSpaceApi tableSpaceApi = RetrofitSetting.Companion.getRetrofit().create(TableSpaceApi.class);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AlertDialog loadingDialog;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.table_settings, container, false);

        RESTManager.context = getActivity();

        table_set = root.findViewById(R.id.table_set);
        loadingDialog = loadingDialog();

//      웹뷰의 설정을 통해 JS 사용을 허용하도록 변경
        WebSettings settings = table_set.getSettings();
        settings.setJavaScriptEnabled(true);

        //      웹 분서가 열릴 때 기본적으로 내 웹 뷰가 아닌
//      새로운 웹 뷰를 열어주는 방식사용 -> 현재 웹 뷰 안에 웹 문서가 열리도록 설정
        table_set.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript("javascript:loadSettingJs()", null); //setting.js를 로드함.
            }
        });

        table_set.addJavascriptInterface(this, "AndroidInterface");


        // alert(), confirm() 같은 팝업 기능의 Js 코드 사용 (혹시 몰라 사용 할 수 있도록 코드 작성)
        table_set.setWebChromeClient(new WebChromeClient());


        //웹 뷰가 보여줄 웹 문서 로드
        table_set.loadUrl(RetrofitSetting.FILE_URL + "/" + auth.getUid() + "/table.html");
        return root;
    }

    @JavascriptInterface
    public void showToast(String msg) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    //변경 사항을 저장할 때
    @JavascriptInterface
    public void saveTable(String jsonArrayRemovedTableIds, String jsonArrayAddedTableIds) {
        List<Integer> addedTableIds = new ArrayList<>();
        List<Integer> removedTableIds = new ArrayList<>();

        //로딩 다이얼로그 띄우기
        //UiThread로 해야됨
        getActivity().runOnUiThread(() -> {
            loadingDialog.setCancelable(false);
            loadingDialog.show();
        });


        Log.d("javascript", "added table : " + jsonArrayRemovedTableIds);
        Log.d("javascript", "removed table : " + jsonArrayAddedTableIds);


        try {
            JSONArray jsonArray;
            jsonArray = new JSONArray(jsonArrayAddedTableIds);
            for (int i = 0; i < jsonArray.length(); i++)
                addedTableIds.add(jsonArray.getInt(i));


            jsonArray = new JSONArray(jsonArrayRemovedTableIds);
            for (int i = 0; i < jsonArray.length(); i++)
                removedTableIds.add(jsonArray.getInt(i));


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        //1. Repository에 저장
        addedTableIds.forEach(tableId ->
                tableRepository.save(new TableDto(tableId)));

        removedTableIds.forEach(tableId -> {
            tableRepository.delete(tableId);
            //만약 연결된 정보도 있었다면 같이 삭제한다.
            //Service를 만들어서 처리하는게 원래는 맞겠지만 나중에 시간되면 수정
            tableAndRecognitionDevice.deleteByTableId(tableId);
        });

        //Todo.2. 서버에 갱신된 HTML 파일 저장
        getActivity().runOnUiThread(() -> {
            Log.d("javascript", "start!");
            table_set.evaluateJavascript("javascript:removeSettingJs()", result -> { //현재  실행되고 있는 js 날림
                Log.d("javascript", "removeSettingJs");
                table_set.evaluateJavascript("javascript:getHtml()", base64Html -> { //html 정보를 가져옴
                    Log.d("javascript", "getHtml : " + base64Html.getBytes().length);
                    uploadHtml(decodeBase64Html(base64Html)); //html 저장
                });
            });
        });


        //Todo.3. 서버에 테이블 id정보 저장
    }

    private String decodeBase64Html(String base64Html){
        try{
            byte[] decodeBytes = Base64.decode(base64Html, Base64.DEFAULT);
            return new String(decodeBytes, StandardCharsets.UTF_8);
        }catch (Exception e){
            Log.d("javascript", "decodeBase64Html: " + e.getMessage());
            return "";
        }
    }

    public File tmpSaveHtml(String html) {
        //임시 파일을 생성한다.
        File tmpHtml = new File(getContext().getCacheDir(), "tmpHtml.html");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(tmpHtml);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            writer.write(html);
            writer.close();
            Log.d("javascript", "tmp html saved! : " + tmpHtml.getAbsolutePath());

            return tmpHtml;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MultipartBody.Part fileToMultipart(File file, String mimeType, String fieldName) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(mimeType),
                file
        );

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                fieldName, //서버가 받을 필드 이름
                file.getName(),
                requestBody //파일 내용
        );

        return filePart;
    }

    public void uploadHtml(String html) {
        File file = tmpSaveHtml(html);
        MultipartBody.Part filePart = fileToMultipart(file, "text/html", "html");
        tableSpaceApi.saveTableSpace(auth.getUid(), filePart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                table_set.reload(); //저장후 reload
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    Toast.makeText(getActivity(), "저장에 성공했습니다!", Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                //Todo. 실패시 Js 좌우 컴포넌트 보이게 수정필요!
                getActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    Toast.makeText(getActivity(), "저장에 실패했습니다!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    //이전 키 눌렀을 때, 뒤로 이동 가능. 뒤로 갈 수 없으면 앱 종료
    public void onBackPassed() {
        if (table_set.canGoBack())
            table_set.goBack();
    }

    public AlertDialog loadingDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_loading_save_table, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(dialogView);

        return dialogBuilder.create();
    }
}
