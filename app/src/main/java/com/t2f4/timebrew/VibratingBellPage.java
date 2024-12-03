package com.t2f4.timebrew;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.t2f4.timebrew.application.DialogService;
import com.t2f4.timebrew.server.dto.VibratingBellTimeDto;
import com.t2f4.timebrew.server.repository.BellMessageRepository;
import com.t2f4.timebrew.server.service.VibratingBellTimeService;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class VibratingBellPage extends Fragment {
    private final VibratingBellTimeService bellTimeService = new VibratingBellTimeService();
    private final BellMessageRepository bellMessageRepository = new BellMessageRepository();
    private RecyclerView bellList;
    private BellIdAdapter bellIdAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.vibratingbell_page, container, false);
        bellList = root.findViewById(R.id.vibratingbellList);


        bellList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bellIdAdapter = new BellIdAdapter(
                getActivity(),
                bellTimeService.findByAll(), root,
                bellTimeService, bellMessageRepository
        );

        bellList.setAdapter(bellIdAdapter);


        return root;
    }
}

class BellIdAdapter extends RecyclerView.Adapter<BellIdAdapter.ViewHolder>{
    private List<VibratingBellTimeDto> bellTimeDtoList;
    private final  VibratingBellTimeService vibratingBellTimeService;
    private final BellMessageRepository bellMessageRepository;
    private DialogService dialogService = new DialogService();
    private View parent;
    private EditText enterEt;
    private EditText leftEt;
    private EditText setEt;
    private VibratingBellTimeDto selectBell = null;
    private Button bellRinging;
    private HashMap<String, Boolean> vibeMap = new HashMap();




    public BellIdAdapter(Activity activity, List bellTimeDtoList, View parent,
                         VibratingBellTimeService vibratingBellTimeService, BellMessageRepository bellMessageRepository){
        this.parent = parent;
        this.bellTimeDtoList = bellTimeDtoList;
        this.vibratingBellTimeService = vibratingBellTimeService;
        this.bellMessageRepository = bellMessageRepository;
        bellRinging = parent.findViewById(R.id.vibratebell_ringing);
        vibratingBellTimeService.findByAll().forEach(it -> vibeMap.put(it.getBellId(), false));


        leftEt = parent.findViewById(R.id.left_time);
        enterEt = parent.findViewById(R.id.enter_time);
        setEt = parent.findViewById(R.id.set_time);

        setEt.setOnClickListener(v -> {
            dialogService.timePickerDialog(
                    activity,
                    vibratingBellTimeService,
                    selectBell,
                    () -> {
                        VibratingBellTimeDto changeBell = selectBell;
                        setEt.setText(minuteFormat(changeBell.getMinute()));
                        leftEt.setText(minuteFormat(vibratingBellTimeService.reamingTime(changeBell.getBellId())));
                    });
        });

        bellRinging.setOnClickListener(v -> {
            if(selectBell == null) {
                Toast.makeText(parent.getContext(), "진동벨을 선택해 주세요!", Toast.LENGTH_SHORT).show();
            }
            Queue<String> message = bellMessageRepository.findById(selectBell.getBellId());

            if(message == null) {
                Toast.makeText(parent.getContext(), "진동벨 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean bive = vibeMap.get(selectBell.getBellId());
            if(bive){
                vibeMap.put(selectBell.getBellId(), false);
                message.add("normal");
                Toast.makeText(parent.getContext(), "진동벨 진동을 멈춥니다.", Toast.LENGTH_SHORT).show();

                bellRinging.setText("진동벨 울리기");
            }else{
                vibeMap.put(selectBell.getBellId(), true);
                message.add("bive");
                Toast.makeText(parent.getContext(), "진동벨 진동합니다.", Toast.LENGTH_SHORT).show();

                bellRinging.setText("진동벨 멈추기");
            }
        });

    }

    private String minuteFormat(int minute){
        return String.format("%02d", minute / 60) + ":" +
                String.format("%02d", minute % 60);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vibratingbell_id, viewGroup, false),
                bellTimeDtoList.get(i)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BellIdAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(bellTimeDtoList.get(i));
    }

    @Override
    public int getItemCount() {
        return bellTimeDtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bellIdInfoTv;
        private TextView bellIdTv;
        private EditText enterEt;
        private EditText leftEt;
        private EditText setEt;
        private VibratingBellTimeDto bellTimeDto;

        public ViewHolder(@NonNull @NotNull View itemView, VibratingBellTimeDto vibratingBellTimeDto) {
            super(itemView);
            this.bellTimeDto = vibratingBellTimeDto;
            bellIdTv = itemView.findViewById(R.id.bellIdTv);
            bellIdInfoTv = parent.findViewById(R.id.vibratingbellInfo);


            leftEt = parent.findViewById(R.id.left_time);
            enterEt = parent.findViewById(R.id.enter_time);
            setEt = parent.findViewById(R.id.set_time);



            this.itemView.setOnClickListener(v -> {
                bellIdInfoTv.setText("[" + bellTimeDto.getBellId() + "] 진동벨 정보");
                enterEt.setText(bellTimeDto.getStart().format(DateTimeFormatter.ofPattern("HH:mm")));
                setEt.setText(minuteFormat(bellTimeDto.getMinute()));
                leftEt.setText(minuteFormat(vibratingBellTimeService.reamingTime(bellTimeDto.getBellId())));
                selectBell = bellTimeDto;
            });


        }

        public void bind(VibratingBellTimeDto bellTimeDto){
            bellIdTv.setText("[" + bellTimeDto.getBellId() + "]");
            this.bellTimeDto = bellTimeDto;
        }

        private String minuteFormat(int minute){
            return String.format("%02d", minute / 60) + ":" +
                    String.format("%02d", minute % 60);
        }
    }
}
