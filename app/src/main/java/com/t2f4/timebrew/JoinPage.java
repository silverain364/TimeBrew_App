package com.t2f4.timebrew;

import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.application.LoginService;
import com.t2f4.timebrew.application.ValidateService;
import com.t2f4.timebrew.dto.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinPage extends AppCompatActivity {
    private EditText emailEt;
    private EditText idEt;
    private EditText pwEt;
    private EditText pwAgainEt;
    private EditText memberNameEt;
    private EditText phoneEt;
    private EditText cafeNameEt;
    private Button signUpBtn;

    private LoginService loginService;
    private ValidateService validateService;

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
            String id = idEt.getText().toString();
            String pw = pwEt.getText().toString();
            String pwAgain = pwAgainEt.getText().toString();
            String memberName = memberNameEt.getText().toString();
            String phone = phoneEt.getText().toString();
            String cafeName = cafeNameEt.getText().toString();

            List<String> inputList = Arrays.asList(email, id, pw, pwAgain, memberName, phone, cafeName);

            if(inputList.stream().anyMatch(String::isEmpty)){ //공백이 있는 경우
                return;
            }

            if(!pw.equals(pwAgain)){ //check 암호가 같지 않는 경우
                return;
            }

            if(validateService.validateEmail(email)){ //email 검증
                return;
            }

            if(validateService.validatePhone(phone)){ //phone 검증
                return;
            }

            loginService.signOn(email, pw, task -> {
                if(task.isSuccessful()){ //회원가입을 성공한 경우
                    String uid = task.getResult().getUser().getUid();
                    UserDto userDto = new UserDto(uid, email, memberName, cafeName, phone);

                    //Todo. 서버로 회원 정보를 보냄
                }
            });
        });
    }
}