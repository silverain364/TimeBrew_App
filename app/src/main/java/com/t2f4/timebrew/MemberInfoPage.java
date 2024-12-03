package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.t2f4.timebrew.api.RetrofitSetting;
import com.t2f4.timebrew.api.UserApi;
import com.t2f4.timebrew.dto.UserInfoDto;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberInfoPage extends Fragment {
    private UserApi userApi = RetrofitSetting.Companion.getRetrofit().create(UserApi.class);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.member_info_page, container, false);

        // 버튼 참조
        Button memberInfoCheckBtn = root.findViewById(R.id.member_info_check_btn);
        TextView nameTv = root.findViewById(R.id.member_info_name_txt);
        TextView cafeNameTv = root.findViewById(R.id.member_info_cafename_txt);
        TextView emailTv = root.findViewById(R.id.member_info_email_txt);
        TextView phoneTv = root.findViewById(R.id.member_info_PhNum_txt);


        //user 정보 불러오복 보여주기
        userApi.findUserInfo(auth.getUid()).enqueue(new Callback<UserInfoDto>() {
            @Override
            public void onResponse(Call<UserInfoDto> call, Response<UserInfoDto> response) {
                UserInfoDto userInfoDto = response.body();
                Log.d("server", userInfoDto.toString());
                if(userInfoDto == null) return;

                nameTv.setText(userInfoDto.getUserName());
                cafeNameTv.setText(userInfoDto.getWorkplace());
                phoneTv.setText(userInfoDto.getPhone());
                emailTv.setText(userInfoDto.getEmail());
            }
            @Override
            public void onFailure(Call<UserInfoDto> call, Throwable throwable) {
                Toast.makeText(getActivity(), "통신에 실패했습니다 : " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        // 버튼에 클릭 리스너 설정
        memberInfoCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent를 사용해 TemplatePage로 이동
                Intent intent = new Intent(getActivity(), TemplatePage.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
