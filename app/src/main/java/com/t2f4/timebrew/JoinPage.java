package com.t2f4.timebrew;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.api.RetrofitSetting;
import com.t2f4.timebrew.api.UserApi;
import com.t2f4.timebrew.application.LoginService;
import com.t2f4.timebrew.application.ValidateService;
import com.t2f4.timebrew.dto.UserInfoDto;
import com.t2f4.timebrew.dto.UserJoinDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.Arrays;
import java.util.List;

public class JoinPage extends AppCompatActivity {
    private EditText emailEt;
    private EditText pwEt;
    private EditText pwAgainEt;
    private EditText memberNameEt;
    private EditText phoneEt;
    private EditText cafeNameEt;
    private Button signUpBtn;

    private LoginService loginService;
    private ValidateService validateService;
    private Retrofit retrofit = RetrofitSetting.Companion.getRetrofit();
    private UserApi userApi = retrofit.create(UserApi.class); 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        // Et 요소들을 Java 변수에 할당
        emailEt = findViewById(R.id.SignUp_Email_edt);
        pwEt = findViewById(R.id.SignUp_Passwd_edt);
        pwAgainEt = findViewById(R.id.SignUp_PasswdAgain_edt);
        memberNameEt = findViewById(R.id.SignUp_MemberName_edt);
        phoneEt = findViewById(R.id.SignUp_PhNum_edt);
        cafeNameEt = findViewById(R.id.SignUp_CafeName_edt);


        signUpBtn = findViewById(R.id.SignUp_register_btn);
        loginService = new LoginService(FirebaseAuth.getInstance());
        validateService = new ValidateService();


        signUpBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String pw = pwEt.getText().toString();
            String pwAgain = pwAgainEt.getText().toString();
            String memberName = memberNameEt.getText().toString();
            String phone = phoneEt.getText().toString();
            String cafeName = cafeNameEt.getText().toString();

            List<String> inputList = Arrays.asList(email, pw, pwAgain, memberName, phone, cafeName);

            if(inputList.stream().anyMatch(String::isEmpty)){ //공백이 있는 경우
                Toast.makeText(JoinPage.this, "모든 항목이 있어야 합니다", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!pw.equals(pwAgain)){ //check 암호가 같지 않는 경우
                Toast.makeText(JoinPage.this, "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!validateService.validateEmail(email)){ //email 검증
                Toast.makeText(JoinPage.this, "이메일 형식이 잘못되어 있습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!validateService.validatePhone(phone)){ //phone 검증
                Toast.makeText(JoinPage.this, "휴대폰번호 형식이 잘못되어 습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            loginService.signOn(email, pw, task -> {
                if(task.isSuccessful()){ //회원가입을 성공한 경우
                    String uid = task.getResult().getUser().getUid();
                    UserJoinDto userDto = new UserJoinDto(uid, email, memberName, cafeName, phone);

                    //Todo. 서버로 회원 정보를 보냄
                    userApi.join(userDto).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(JoinPage.this, response.body().replaceAll("\\n", ""), Toast.LENGTH_SHORT).show();
                            //임시로 메인으로 이동시킴
                            startActivity(new Intent(JoinPage.this, TemplatePage.class));
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable throwable){
                            Toast.makeText(JoinPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}