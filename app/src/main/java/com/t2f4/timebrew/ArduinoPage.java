package com.t2f4.timebrew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ArduinoPage extends AppCompatActivity {

    private RecyclerView uncon_a_list;
    private RecyclerView uncon_t_list;

    private Arduino_Adapter Arduino_Adapter;
    private Arduino_Adapter Table_Adapter;

    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arduino_page);

        // 첫 번째 리스트 설정 (미연결 아두이노)
        uncon_a_list = findViewById(R.id.uncon_a_list);
        layoutManager1 = new LinearLayoutManager(this);
        uncon_a_list.setLayoutManager(layoutManager1);

        List<String> dummyList1 = generateDummyList1();
        Arduino_Adapter = new Arduino_Adapter(dummyList1);
        uncon_a_list.setAdapter(Arduino_Adapter);

        // 두 번째 리스트 설정 (미연결 테이블)
        uncon_t_list = findViewById(R.id.uncon_t_list);
        layoutManager2 = new LinearLayoutManager(this);
        uncon_t_list.setLayoutManager(layoutManager2);

        List<String> dummyList2 = generateDummyList2();
        Table_Adapter = new Arduino_Adapter(dummyList2); // Table_Adapter를 초기화
        uncon_t_list.setAdapter(Table_Adapter);
    }

    private List<String> generateDummyList1() {
        List<String> dummyList1 = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dummyList1.add("ADINO " + i);
        }
        return dummyList1;
    }

    private List<String> generateDummyList2() {
        List<String> dummyList2 = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dummyList2.add("Table " + i);
        }
        return dummyList2;
    }
}
