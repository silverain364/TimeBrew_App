package com.t2f4.timebrew;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;

public class TemplatePage extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_page);

        drawerLayout = findViewById(R.id.drawerLayout);
        ImageButton hamburgerButton = findViewById(R.id.Hamburger_Button_img);  // 햄버거 버튼 ID 맞추기
        NavigationView navigationView = findViewById(R.id.navigationView);

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
                // if-else로 변경
                if (item.getItemId() == R.id.nav_first) {
                    Toast.makeText(TemplatePage.this, "First Item clicked", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_second) {
                    Toast.makeText(TemplatePage.this, "Second Item clicked", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_third) {
                    Toast.makeText(TemplatePage.this, "Third Item clicked", Toast.LENGTH_SHORT).show();
                }

                // Drawer 닫기
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
        ;
        // `MainPageFragment`를 `frameLayoutContainer`에 추가
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContainer, new MainPage())
                .commit();
    }
}