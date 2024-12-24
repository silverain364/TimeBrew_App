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
import com.t2f4.timebrew.server.dto.RecognitionDeviceDto;
import com.t2f4.timebrew.server.dto.TableDto;
import com.t2f4.timebrew.server.repository.RecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableAndRecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableRepository;

import java.io.IOException;

public class IntroPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page);
        FirebaseApp.initializeApp(this);


        setTestData();

        // 3초 후에 JoinPage로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroPage.this, LoginPage.class);
                startActivity(intent);
                finish(); // IntroPage를 종료해서 뒤로가기 시 다시 표시되지 않게 함
            }
        }, 3000); // 3000밀리초 = 3초
    }

    public void setTestData(){
        TableRepository tableRepository = new TableRepository();
        RecognitionDeviceRepository recognitionDeviceRepository = new RecognitionDeviceRepository();
        TableAndRecognitionDeviceRepository tableAndRecognitionDeviceRepository = new TableAndRecognitionDeviceRepository();

        for (int i = 1; i <= 4; i++) {
            tableRepository.save(new TableDto(i, null));
            recognitionDeviceRepository.save(new RecognitionDeviceDto(i));
            tableAndRecognitionDeviceRepository.save(i, i);
        }
    }

}