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
import com.t2f4.timebrew.application.LoginService;
import com.t2f4.timebrew.application.ValidateService;

public class LoginPage extends AppCompatActivity {
    public static boolean TEST = true;
    private EditText emailEt, pwEt;
    private ValidateService validateService;
    private LoginService loginService;
    private boolean loginFrag = false;

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
            //임시코드
            if(TEST) {
                Intent i = new Intent(LoginPage.this, TemplatePage.class);
                startActivity(i);
                return;
            }

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
                    Intent intent = new Intent(LoginPage.this, TableSettingFragment.class);
                    startActivity(intent);
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
}