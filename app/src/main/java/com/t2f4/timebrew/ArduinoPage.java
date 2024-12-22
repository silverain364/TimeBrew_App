package com.t2f4.timebrew;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.t2f4.timebrew.dto.ConnectInfoDto;
import com.t2f4.timebrew.server.dto.TableDto;
import com.t2f4.timebrew.server.repository.RecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableAndRecognitionDeviceRepository;
import com.t2f4.timebrew.server.repository.TableRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArduinoPage extends Fragment {

    private RecyclerView uncon_a_list;
    private RecyclerView uncon_t_list;
    private RecyclerView conList;

    private ArduinoAdapter arduinoAdapter;
    private ArduinoAdapter tableAdapter;
    private ConnectedAdapter tmpAdapter;

    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;

    private TextView discTableTv, discArduinoTv;
    private TextView selectConTableTv, selectConArduinoTv;
    private Button disconnectionBtn, conBtn;

    private TableRepository tableRepository = new TableRepository();
    private RecognitionDeviceRepository recognitionDeviceRepository
            = new RecognitionDeviceRepository();
    private TableAndRecognitionDeviceRepository tableAndRecognitionDeviceRepository
            = new TableAndRecognitionDeviceRepository();

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.arduino_page, container, false);

        discArduinoTv = root.findViewById(R.id.disc_arduinoTv);
        discTableTv = root.findViewById(R.id.disc_tableTv);
        selectConArduinoTv = root.findViewById(R.id.select_con_arduinoTv);
        selectConTableTv = root.findViewById(R.id.select_con_tableTv);
        disconnectionBtn = root.findViewById(R.id.disconnectionBtn);
        conBtn = root.findViewById(R.id.con_btn);


        //connect 정보가 없는 TableId를 가져온다.
        List<Integer> disCntTableIds = tableRepository.findAll().stream()
                .filter(dto -> tableAndRecognitionDeviceRepository.findByTableId(dto.getTableId()) == null)
                .map(dto -> dto.getTableId())
                .collect(Collectors.toList());


        //connect 정보가 없는 deviceId를 가져온다.
        List<Integer> disCntDeviceIds = recognitionDeviceRepository.findAll().stream()
                .filter(dto -> tableAndRecognitionDeviceRepository.findByDeviceId(dto.getRecognitionDeviceId()) == null)
                .map(dto -> dto.getRecognitionDeviceId())
                .collect(Collectors.toList());

        //연결 테이블
        conList = root.findViewById(R.id.connectCompleteRv);
        conList.setLayoutManager(new LinearLayoutManager(getActivity()));

        tmpAdapter = new ConnectedAdapter(
                tableAndRecognitionDeviceRepository.findAllTable().stream().map(
                        tableId -> {
                            return new ConnectInfoDto(tableId, tableAndRecognitionDeviceRepository.findByTableId(tableId));
                        }
                ).collect(Collectors.toList())
                , discArduinoTv, discTableTv);
        conList.setAdapter(tmpAdapter);
        conList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));


        // 첫 번째 리스트 설정 (미연결 아두이노)
        uncon_a_list = root.findViewById(R.id.uncon_a_list);
        layoutManager1 = new LinearLayoutManager(getActivity());
        uncon_a_list.setLayoutManager(layoutManager1);
        uncon_a_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        arduinoAdapter = new ArduinoAdapter("DEVICE"
                , disCntDeviceIds
                , selectConArduinoTv);

        uncon_a_list.setAdapter(arduinoAdapter);

        // 두 번째 리스트 설정 (미연결 테이블)
        uncon_t_list = root.findViewById(R.id.uncon_t_list);
        layoutManager2 = new LinearLayoutManager(getActivity());
        uncon_t_list.setLayoutManager(layoutManager2);
        uncon_t_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));


        tableAdapter = new ArduinoAdapter("TABLE"
                , disCntTableIds
                , selectConTableTv); // Table_Adapter를 초기화
        uncon_t_list.setAdapter(tableAdapter);


        disconnectionBtn.setOnClickListener(v -> {
            ConnectInfoDto disConnectDto = tmpAdapter.selectRemove();
            if (disConnectDto == null) {
                Toast.makeText(getActivity(), "선택되지 않있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }


            //Todo. 서버 통신


            //Repository 최신화
            tableAndRecognitionDeviceRepository.deleteByTableId(disConnectDto.getTableId());

            //만약 벨 정보가 있었다면 초기화
            TableDto tableDto = tableRepository.findById(disConnectDto.getTableId());
            tableDto.setBellId(null);

            arduinoAdapter.addItem(disConnectDto.getDeviceId());
            tableAdapter.addItem(disConnectDto.getTableId());
            arduinoAdapter.notifyItemInserted(arduinoAdapter.getItemCount());
            tableAdapter.notifyItemInserted(tableAdapter.getItemCount());
        });

        conBtn.setOnClickListener(v -> {

            if (tableAdapter.getSelectNumber() == -1 || arduinoAdapter.getSelectNumber() == -1) {
                Toast.makeText(getActivity(), "선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer tableId = tableAdapter.removeSelectList();
            Integer arduinoId = arduinoAdapter.removeSelectList();


            //Todo. 서버 통신

            //Repository 최신화
            tableAndRecognitionDeviceRepository.save(tableId, arduinoId);

            tmpAdapter.addItem(new ConnectInfoDto(tableId, arduinoId));
            tmpAdapter.notifyItemInserted(tmpAdapter.getItemCount());
        });

        return root;
    }


    private List<Integer> generateDummyList() {
        List<Integer> dummyList2 = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dummyList2.add(i);
        }
        return dummyList2;
    }

    public List<ConnectInfoDto> generateDuList3() {
        List<ConnectInfoDto> dummyList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            dummyList.add(new ConnectInfoDto(i + 1, i + 1));
        }

        return dummyList;
    }
}

class ConnectedAdapter extends RecyclerView.Adapter<ConnectedAdapter.ViewHolder> {
    private List<ConnectInfoDto> connectInfoDtoList;
    private TextView selectTableTv, selectAdinoTv;
    private int selectListNumber = -1;
    private View selectItemView = null;

    public ConnectedAdapter(List connectInfoDtoList, TextView selectAdinoTv, TextView selectTableTv) {
        this.connectInfoDtoList = connectInfoDtoList;
        this.selectAdinoTv = selectAdinoTv;
        this.selectTableTv = selectTableTv;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.arduino_con_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int i) {
        viewHolder.bind(connectInfoDtoList.get(i));
    }

    public ConnectInfoDto selectRemove() {
        if (selectListNumber == -1)
            return null;


        ConnectInfoDto removeDto =
                connectInfoDtoList.remove(selectListNumber);

        notifyItemRemoved(selectListNumber);
        selectItemView.setBackgroundColor(Color.rgb(255,255,255));


        selectItemView = null;
        selectListNumber = -1;

        return removeDto;
    }

    public void addItem(ConnectInfoDto connectInfoDto) {
        connectInfoDtoList.add(connectInfoDto);
    }


    @Override
    public int getItemCount() {
        return connectInfoDtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tableTv, adinoTv;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tableTv = itemView.findViewById(R.id.tableTv);
            adinoTv = itemView.findViewById(R.id.adinoTv);
        }

        public void bind(ConnectInfoDto connectInfoDto) {
            tableTv.setText("TABLE : " + connectInfoDto.getTableId());
            adinoTv.setText("DEVICE : " + connectInfoDto.getDeviceId());

            itemView.setOnClickListener(v -> {
                if (selectItemView != null)
                    selectItemView.setBackgroundColor(Color.rgb(255, 255, 255));

                itemView.setBackgroundColor(Color.rgb(240, 240, 240));
                selectItemView = itemView;
                selectListNumber = getAdapterPosition();

                selectTableTv.setText(tableTv.getText());
                selectAdinoTv.setText(adinoTv.getText());
            });
        }
    }

}
