package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MemberInfoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_info_page);

        Button member_info_check_btn = findViewById(R.id.member_info_check_btn);

    member_info_check_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MemberInfoPage.this, TemplatePage.class);
            startActivity(intent);
        }
    });
    }
}