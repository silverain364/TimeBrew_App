package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.api.*;
import com.t2f4.timebrew.application.LoginService;
import com.t2f4.timebrew.application.ValidateService;
import com.t2f4.timebrew.dto.DiningTableDto;
import com.t2f4.timebrew.dto.RecognitionDeviceDto;
import com.t2f4.timebrew.dto.TimeDto;
import com.t2f4.timebrew.dto.VibratingbellReadDto;
import com.t2f4.timebrew.server.RESTManager;
import com.t2f4.timebrew.server.dto.TableDto;
import com.t2f4.timebrew.server.dto.VibratingBellTimeDto;
import com.t2f4.timebrew.server.repository.RecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableAndRecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableRepository;
import com.t2f4.timebrew.server.repository.VibratingBellTimeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class LoginPage extends AppCompatActivity {
    private EditText emailEt, pwEt;
    private ValidateService validateService;
    private LoginService loginService;
    private boolean loginFrag = false;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    //Api 객체 생성
    private Retrofit retrofit = RetrofitSetting.Companion.getRetrofit();
    private BellApi bellApi = retrofit.create(BellApi.class);
    private DiningTableApi diningTableApi = retrofit.create(DiningTableApi.class);
    private RecognitionDeviceApi recognitionDeviceApi = retrofit.create(RecognitionDeviceApi.class);
    private TimeApi timeApi = retrofit.create(TimeApi.class);

    //Repository 객체 생성
    private RecognitionDeviceRepository recognitionDeviceRepository = new RecognitionDeviceRepository();
    private TableAndRecognitionDeviceRepository tableAndRecognitionDeviceRepository = new TableAndRecognitionDeviceRepository();
    private TableRepository tableRepository = new TableRepository();
    private VibratingBellTimeRepository vibratingBellTimeRepository = new VibratingBellTimeRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        TextView signUp_Txt = findViewById(R.id.SignUp_txt);
        TextView FindPs_txt = findViewById(R.id.FindPs_txt);
        Button signInBtn = findViewById(R.id.Login_btn);

        emailEt = findViewById(R.id.login_Id_edt);
        pwEt = findViewById(R.id.login_Passwd_edt);

        validateService = new ValidateService();
        loginService = new LoginService(FirebaseAuth.getInstance());


        signInBtn.setOnClickListener(view -> {
            if(loginFrag) return; //로그인 중일 때 다시 시도 못 하도록 방지
            loginFrag = true;

            String email = emailEt.getText().toString();
            String pw = pwEt.getText().toString();

            if(!validateService.validateEmail(email)){
                Toast.makeText(LoginPage.this, "잘못된 이메일 형식입니다.", Toast.LENGTH_SHORT);
                return;
            }

            loginService.signIn(email, pw, task -> {
                if (task.isSuccessful()) { //로그인 성공시
                    Intent intent = new Intent(LoginPage.this, TemplatePage.class);
                    startActivity(intent);

                    RESTManager restManager = RESTManager.Companion.getInstace(8080, this.getApplicationContext());
                    try {
                        if(!restManager.isAlive())
                            restManager.start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    finish();
                } else {
                    Toast.makeText(LoginPage.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }

                loginFrag = false;
            });
        });

        signUp_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent를 사용하여 JoinPage 액티비티로 이동
                Intent intent = new Intent(LoginPage.this, JoinPage.class);
                startActivity(intent);
            }
        });

        FindPs_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent를 사용하여 FindPasswordPage 액티비티로 이동
                Intent intent = new Intent(LoginPage.this, FindPasswordPage.class);
                startActivity(intent);
            }
        });
    }

    List<VibratingbellReadDto> vibratingbellReadDtoList;
    String uid = firebaseAuth.getUid();

    //진동벨 데이터 초기화
    private void loadBell(int level){
        if(level == 0){ //bell 정보 가져오기
            bellApi.findByUid(uid).enqueue(new Callback<List<VibratingbellReadDto>>() {
                @Override
                public void onResponse(Call<List<VibratingbellReadDto>> call, Response<List<VibratingbellReadDto>> response) {
                    if(response.body() == null) return;
                    vibratingbellReadDtoList = response.body();
                    loadBell(level + 1);
                }
                @Override
                public void onFailure(Call<List<VibratingbellReadDto>> call, Throwable throwable) {}
            });
        }

        if(level == 1){ //bell에 부여되어 있는 시간 가져오기
            vibratingbellReadDtoList.forEach(vibratingbellReadDto -> {
                timeApi.findByBellRfid(vibratingbellReadDto.getRfid()).enqueue(new Callback<TimeDto>() {
                    @Override
                    public void onResponse(Call<TimeDto> call, Response<TimeDto> response) {
                        if(response.body() == null) return;
                        TimeDto timeDto = response.body();
                        vibratingBellTimeRepository.save(new VibratingBellTimeDto(
                                LocalDateTime.parse(timeDto.getStartTime()),
                                timeDto.getTimeId(),
                                LocalDateTime.parse(timeDto.getStartTime()).plusMinutes(timeDto.getTimeId()),
                                timeDto.getBellRfid()
                        ));
                    }
                    @Override
                    public void onFailure(Call<TimeDto> call, Throwable throwable) {}
                });
            });
        }
    }


    private void tableLoad(int level){ //테이블 정보 로드
        if(level == 0){
            //tableRepository 정보 최신화
            diningTableApi.findByUid(uid).enqueue(new Callback<List<DiningTableDto>>() {
                @Override
                public void onResponse(Call<List<DiningTableDto>> call, Response<List<DiningTableDto>> response) {
                    if(response.body() == null) return;
                    response.body().forEach(diningTableDto -> {
                        tableRepository.save(new TableDto(
                                diningTableDto.getTableNumber(), null
                        ));
                    });

                    tableLoad(level + 1);
                }
                @Override
                public void onFailure(Call<List<DiningTableDto>> call, Throwable throwable) {}
            });
        }

        if(level == 1){
            tableRepository.findAll().forEach( tableDto -> { //table과 연결되어 있는 연결정보 가져오기
                recognitionDeviceApi.findByUidAndTableNumber(uid, tableDto.getTableId()).enqueue(new Callback<RecognitionDeviceDto>() {
                    @Override
                    public void onResponse(Call<RecognitionDeviceDto> call, Response<RecognitionDeviceDto> response) {
                        RecognitionDeviceDto recognitionDeviceDto = response.body();
                        if(recognitionDeviceDto == null) return;
                        //인식장치 정보 저장
                        recognitionDeviceRepository.save(new com.t2f4.timebrew.server.dto.RecognitionDeviceDto(
                                recognitionDeviceDto.getDeviceId()
                        ));

                        //인식장치와 테이블 연결 정보 저장
                        tableAndRecognitionDeviceRepository.save(
                                tableDto.getTableId(), //Todo. 나중에 잘 동작되는지 확인 필요
                                recognitionDeviceDto.getDeviceId());
                    }

                    @Override
                    public void onFailure(Call<RecognitionDeviceDto> call, Throwable throwable) {}
                });
            });
        }
    }

    public void loadData(){
        //기존에 다른 데이터가 있으면 삭제한다.
        recognitionDeviceRepository.deleteAll();
        tableAndRecognitionDeviceRepository.deleteAll();
        tableRepository.deleteAll();
        vibratingBellTimeRepository.deleteAll();

        //새로운 데이터를 받아온다.
        loadBell(0); //bellTimeData 최신화


    }
}