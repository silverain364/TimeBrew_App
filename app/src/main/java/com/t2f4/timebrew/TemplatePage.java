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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.api.RetrofitSetting;
import com.t2f4.timebrew.api.UserApi;
import com.t2f4.timebrew.dto.UserInfoDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemplatePage extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private TableSettingFragment tableSettingFragment;
    private MemberInfoPage memberInfoPage;
    private ArduinoPage arduinoPage;
    private TableViewFragment tableViewFragment;
    private VibratingBellPage vibratingBellPage;
    private String username = "아잇코";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserApi userApi = RetrofitSetting.Companion.getRetrofit().create(UserApi.class);



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
        vibratingBellPage = new VibratingBellPage();


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
                            .replace(R.id.frameLayoutContainer, memberInfoPage)
                            .addToBackStack(null)
                            .commit();
                } else if (item.getItemId() == R.id.nav_table_settings_page) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutContainer, tableSettingFragment)
                            .addToBackStack(null)
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
                } else if (item.getItemId() == R.id.nav_vibratingbell_page) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutContainer, vibratingBellPage)
                            .addToBackStack(null)
                            .commit();
                }
                // Drawer 닫기
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
        // `MainPageFragment`를 `frameLayoutContainer`에 추가
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContainer, new MainFragment())
                .commit();

        userApi.findUserInfo(auth.getUid()).enqueue(new Callback<UserInfoDto>() {
            @Override
            public void onResponse(Call<UserInfoDto> call, Response<UserInfoDto> response) {
                if(response.body() != null)
                 navigationView.getMenu().findItem(R.id.nav_app_info).setTitle(response.body().getUserName());
            }

            @Override
            public void onFailure(Call<UserInfoDto> call, Throwable throwable) {}
        });

    }

    public Fragment getNowFragment(){
        return getSupportFragmentManager().getFragments().get(0);
    }
}