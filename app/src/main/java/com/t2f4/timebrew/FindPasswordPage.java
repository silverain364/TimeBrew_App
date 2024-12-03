package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FindPasswordPage extends AppCompatActivity {
    private Button member_info_check_btn;
    private Button emailCheckBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_page);

        member_info_check_btn = findViewById(R.id.member_info_check_btn);
        emailCheckBtn = findViewById(R.id.SignUp_EmailCheck_btn); 
        
        emailCheckBtn.setOnClickListener(v -> {
            Toast.makeText(this, "미구현 기능입니다", Toast.LENGTH_SHORT).show();
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