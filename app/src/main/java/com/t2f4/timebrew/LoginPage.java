package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        TextView signUp_Txt = findViewById(R.id.SignUp_txt);
        Button signInBtn = findViewById(R.id.Login_btn);

        signInBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, Table_settings.class);
            startActivity(intent);
        });

        signUp_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent를 사용하여 JoinPage 액티비티로 이동
                Intent intent = new Intent(LoginPage.this, JoinPage.class);
                startActivity(intent);
            }
        });
    }
}