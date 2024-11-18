package com.t2f4.timebrew;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.t2f4.timebrew.server.RESTManager;

import java.io.IOException;

public class IntroPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page);
        FirebaseApp.initializeApp(this);

        RESTManager restManager = RESTManager.Companion.getInstace(8080, this.getApplicationContext());

        try {
            restManager.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // 3초 후에 JoinPage로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroPage.this, TemplatePage.class);
                startActivity(intent);
                finish(); // IntroPage를 종료해서 뒤로가기 시 다시 표시되지 않게 함
            }
        }, 3000); // 3000밀리초 = 3초
    }



}