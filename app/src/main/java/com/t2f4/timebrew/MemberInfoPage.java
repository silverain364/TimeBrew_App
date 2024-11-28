package com.t2f4.timebrew;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;

public class MemberInfoPage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.member_info_page, container, false);

        // 버튼 참조
        Button memberInfoCheckBtn = root.findViewById(R.id.member_info_check_btn);

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
