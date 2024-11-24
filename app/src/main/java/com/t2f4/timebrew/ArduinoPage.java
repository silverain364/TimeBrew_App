package com.t2f4.timebrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.t2f4.timebrew.dto.ConnectInfoDto;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArduinoPage extends Fragment {

    private RecyclerView uncon_a_list;
    private RecyclerView uncon_t_list;
    private RecyclerView conList;

    private Arduino_Adapter Arduino_Adapter;
    private Arduino_Adapter Table_Adapter;
    private ConnectedAdapter tmpAdapter;

    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;

    private TextView discTableTv , discArduinoTv;
    private TextView selectConTableTv, selectConArduinoTv;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.arduino_page, container, false);

        discArduinoTv = root.findViewById(R.id.disc_arduinoTv);
        discTableTv = root.findViewById(R.id.disc_tableTv);
        selectConArduinoTv = root.findViewById(R.id.select_con_arduinoTv);
        selectConTableTv = root.findViewById(R.id.select_con_tableTv);


        //연결 테이블
        conList = root.findViewById(R.id.connectCompleteRv);
        conList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        tmpAdapter = new ConnectedAdapter(generateDuList3(), selectConArduinoTv, selectConTableTv);
        conList.setAdapter(tmpAdapter);
        conList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));


        // 첫 번째 리스트 설정 (미연결 아두이노)
        uncon_a_list = root.findViewById(R.id.uncon_a_list);
        layoutManager1 = new LinearLayoutManager(getActivity());
        uncon_a_list.setLayoutManager(layoutManager1);
        uncon_a_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        List<String> dummyList1 = generateDummyList1();
        Arduino_Adapter = new Arduino_Adapter(dummyList1);
        uncon_a_list.setAdapter(Arduino_Adapter);

        // 두 번째 리스트 설정 (미연결 테이블)
        uncon_t_list = root.findViewById(R.id.uncon_t_list);
        layoutManager2 = new LinearLayoutManager(getActivity());
        uncon_t_list.setLayoutManager(layoutManager2);
        uncon_t_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));


        List<String> dummyList2 = generateDummyList2();
        Table_Adapter = new Arduino_Adapter(dummyList2); // Table_Adapter를 초기화
        uncon_t_list.setAdapter(Table_Adapter);

        return root;
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

    public List<ConnectInfoDto> generateDuList3(){
        List<ConnectInfoDto> dummyList = new ArrayList<>();
        for(int i = 1; i <= 20; i++){
            dummyList.add(new ConnectInfoDto("AFEFD", i + 1, i + 1));
        }

        return dummyList;
    }
}

class ConnectedAdapter extends RecyclerView.Adapter<ConnectedAdapter.ViewHolder>{
    private List<ConnectInfoDto> connectInfoDtoList;
    private TextView selectTableTv, selectAdinoTv;

    public ConnectedAdapter(List connectInfoDtoList, TextView selectAdinoTv, TextView selectTableTv){
        this.connectInfoDtoList = connectInfoDtoList;
        this.selectAdinoTv = selectAdinoTv;
        this.selectTableTv = selectTableTv;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.arduino_con_item, viewGroup, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int i) {
        viewHolder.bind(connectInfoDtoList.get(i));
    }

    @Override
    public int getItemCount() {
        return connectInfoDtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tableTv, adinoTv;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tableTv = itemView.findViewById(R.id.tableTv);
            adinoTv = itemView.findViewById(R.id.adinoTv);
        }

        public void bind(ConnectInfoDto connectInfoDto){
            tableTv.setText("TABLE : " + connectInfoDto.getTableId());
            adinoTv.setText("ADINO : " + connectInfoDto.getBellNumber());

            itemView.setOnClickListener(v -> {
                selectTableTv.setText(tableTv.getText());
                selectAdinoTv.setText(adinoTv.getText());
            });
        }
    }

}
