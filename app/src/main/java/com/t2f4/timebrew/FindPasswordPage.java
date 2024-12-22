package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.t2f4.timebrew.application.ValidateService;

public class FindPasswordPage extends AppCompatActivity {
    private Button member_info_check_btn;
    private Button emailCheckBtn;
    private EditText emailEt;
    private ValidateService validateService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_page);

        member_info_check_btn = findViewById(R.id.member_info_check_btn);
        emailCheckBtn = findViewById(R.id.SignUp_EmailCheck_btn);
        emailEt = findViewById(R.id.SignUp_Email_edt);
        validateService = new ValidateService();

        
        emailCheckBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            if(email.trim().isEmpty()) {
                Toast.makeText(this, "이메일을 입력주세요!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!validateService.validateEmail(email)){
                Toast.makeText(this, "이메일 형식을 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }


            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this, "해당 이메일로 비밀번호 초기화 메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindPasswordPage.this, LoginPage.class);
                    startActivity(intent);
                    return;
                }

                try{
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e){
                    Toast.makeText(this, "존재하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthRecentLoginRequiredException e){
                    Toast.makeText(this, "최근에 로그인한 기기가 아닙니다.", Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthUserCollisionException e){
                    Toast.makeText(this, "사용자 충돌이 발생했습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "비밀번호 재설정 이메일 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        member_info_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent를 사용하여 LoginPage 액티비티로 이동
                Intent intent = new Intent(FindPasswordPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
}