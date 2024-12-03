package com.t2f4.timebrew.application;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.t2f4.timebrew.R;
import com.t2f4.timebrew.server.dto.VibratingBellTimeDto;
import com.t2f4.timebrew.server.service.VibratingBellTimeService;
import com.t2f4.timebrew.util.CustomCallback;

public class DialogService {
    public void timePickerDialog(Activity activity,
                                 VibratingBellTimeService vibratingBellTimeService,
                                 VibratingBellTimeDto bell,
                                 CustomCallback customCallback){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_timepicker_timeset);

        Button okBtn = dialog.findViewById(R.id.tp_ok);
        Button cancelBtn = dialog.findViewById(R.id.tp_cancle);

        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(bell.getMinute() / 60);
        timePicker.setMinute(bell.getMinute() % 60);

        okBtn.setOnClickListener(v -> {
            customCallback.call(); //잔여시간 최쇤화

            int changeMinute = timePicker.getHour() * 60 + timePicker.getMinute();
            int reamingTime = vibratingBellTimeService.reamingTime(bell.getBellId());

            if(changeMinute < reamingTime) {
                Toast.makeText(activity, "잔여시간보다 더 적은 시간을 설정할 수 없습니다.(잔여시간 : " + reamingTime + "분)", Toast.LENGTH_SHORT).show();
                return;
            }

            bell.setMinute(changeMinute);
            bell.setEnd(bell.getStart().plusMinutes(changeMinute));
            customCallback.call(); //자연시간 최신화

            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> {
            customCallback.call(); //자연시간 최신화
            dialog.dismiss();
        });

        dialog.show();
    }

}
