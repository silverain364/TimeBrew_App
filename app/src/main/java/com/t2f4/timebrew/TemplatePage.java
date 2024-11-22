package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;

public class TemplatePage extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private TableSettingFragment tableSettingFragment;
    private MemberInfoPage memberInfoPage;
    private ArduinoPage arduinoPage;
    private TableViewFragment tableViewFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_page);

        drawerLayout = findViewById(R.id.drawerLayout);
        ImageButton hamburgerButton = findViewById(R.id.Hamburger_Button_img);  // 햄버거 버튼 ID 맞추기
        NavigationView navigationView = findViewById(R.id.navigationView);

        fragmentManager = getSupportFragmentManager();
        tableSettingFragment = new TableSettingFragment();
        memberInfoPage = new MemberInfoPage();
        arduinoPage = new ArduinoPage();
        tableViewFragment = new TableViewFragment();


        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView); // Drawer 닫기
                } else {
                    drawerLayout.openDrawer(navigationView); // Drawer 열기
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_member_info_page) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutContainer, memberInfoPage) // fragment_container는 Fragment를 표시할 레이아웃 ID입니다.
                            .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아가도록 설정
                            .commit();
                } else if (item.getItemId() == R.id.nav_table_settings_page) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutContainer, tableSettingFragment) // fragment_container는 Fragment를 표시할 레이아웃 ID입니다.
                            .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아가도록 설정
                            .commit();
                } else if (item.getItemId() == R.id.nav_arduino_page) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutContainer, arduinoPage)
                            .addToBackStack(null)
                            .commit();
                } else if (item.getItemId() == R.id.nav_table_view_page) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutContainer, tableViewFragment)
                            .addToBackStack(null)
                            .commit();
                }
                // Drawer 닫기
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
        ;
        // `MainPageFragment`를 `frameLayoutContainer`에 추가
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContainer, new MainFragment())
                .commit();
    }
}