package com.t2f4.timebrew.server.controller

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import com.t2f4.timebrew.InitApplication
import com.t2f4.timebrew.R
import com.t2f4.timebrew.TableViewFragment
import com.t2f4.timebrew.TemplatePage
import com.t2f4.timebrew.server.repository.RecognitionDeviceRepository
import com.t2f4.timebrew.server.repository.TableAndRecognitionDeviceRepository
import com.t2f4.timebrew.server.repository.TableRepository
import com.t2f4.timebrew.server.service.VibratingBellTimeService
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD

class TableTimeController : RouterNanoHTTPD.GeneralHandler(){
    private val vibratingBellTimeService = VibratingBellTimeService();
    private val recognitionDeviceRepository = RecognitionDeviceRepository();
    private val tableAndRecognitionDevice = TableAndRecognitionDeviceRepository()
    private val tableRepository = TableRepository();

    override fun get( //남은 시간이 얼마나 남았는지 조회할 때 사용
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        Log.d("http", session?.parameters.toString());

        //Todo. Server로 부터 아두이노 남은 시간 가져오기 또는 저장된 아두이노 시간 정보 가져오기
        val bellId = session?.parameters?.get("bellId") //bellId가 없다면
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "not exist bellId");

        val deviceIdString = session?.parameters?.get("deviceId") //deviceId가 없다면
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "not exist deviceId");

        val deviceId = Integer.valueOf(deviceIdString[0]) as Integer

        val device = recognitionDeviceRepository.findById(deviceId) //device가 메모리에 등록되어 있지 않다면
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain",  "not exist device")


        val tableId = tableAndRecognitionDevice.findByDeviceId(deviceId)

        //tableId가 설정되어 있지 않다면
        tableId ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.FORBIDDEN, "text/plain", "disConnect tableId")
        val table = tableRepository.findById(tableId!!);
        table ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.FORBIDDEN, "text/plain", "disConnect table")

        //time이 존재하지 않는다면 - 진동벨에 시간이 부여되지 않았다면
        Log.d("http", "bellId : " + bellId[0]);
        val time = vibratingBellTimeService.findById(bellId[0])
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "-2.0")

        //좌석 최신화
        val leaveTable = tableRepository.findByBellId(bellId[0]) //기존에 테이블 연결은 삭제
        if (leaveTable != null) {
            leaveTable?.bellId = null;
            tableRepository.save(leaveTable)
        };


        table.bellId = bellId[0]
        tableRepository.save(table) //세로운 테이블 저장


        val templatePage = InitApplication.getCurrentActivity();
        if(templatePage is TemplatePage){ //template page라면
            val viewFragment = templatePage.nowFragment
            if(viewFragment is TableViewFragment) //TableViewFragmetn라면
                templatePage.runOnUiThread {
                    viewFragment.setTableTime(table.tableId.toInt(), //table 최신화
                        vibratingBellTimeService.reamingTime(bellId[0]))
                }
        }

        //남은 시간을 percent로 받음
        val reamingTimePercent = vibratingBellTimeService.reamingTimePercent(bellId[0]);


        //남은 시간을 반환
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain",
            reamingTimePercent.toString())
    }

    override fun delete(
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        val deviceIdString = session?.parameters?.get("deviceId") //deviceId가 없다면
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "not exist deviceId");

        val deviceId = Integer.valueOf(deviceIdString[0]) as Integer

        val tableId = tableAndRecognitionDevice.findByDeviceId(deviceId) //device와 연결된  table이 없는 경우
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "dis connect table");

        val table = tableRepository.findById(tableId)
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "not find table");

        table?.bellId = null
        tableRepository.save(table)
        val templatePage = InitApplication.getCurrentActivity();
        if(templatePage is TemplatePage){ //template page라면
            val viewFragment = templatePage.nowFragment
            if(viewFragment is TableViewFragment) //TableViewFragmetn라면
                templatePage.runOnUiThread {
                    viewFragment.setTableTime(table.tableId.toInt(), 0)
                }
        }

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "ok")
    }

    override fun post( //특정 아두이노에 시간 할당
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        Log.d("http", "post: " + session?.parameters)

        val bellId = session?.parameters?.get("bellId")
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "exist not bellId");

        val activity = InitApplication.getCurrentActivity();
        if(activity is TemplatePage){ //template 페이지만 보여준다.
            activity.runOnUiThread {
                showBuzzerPopup(bellId[0], activity)
            }
        }

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "true");
    }

    private fun showBuzzerPopup(bellId: String, context: Context) {
        val dialog2 = Dialog(context)
        dialog2.setContentView(R.layout.buzzer_rec)
        val timePicker = dialog2.findViewById<TimePicker>(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.hour = 1;
        timePicker.minute = 0;

        //화면 잘림 이슈로 주석처리 했음
//        val width: Int = (context.resources.displayMetrics.widthPixels * 0.23).toInt()
//        val height: Int = (context.resources.displayMetrics.heightPixels * 0.63).toInt()
//        dialog2.window!!.setLayout(width, height)

        val setBtn = dialog2.findViewById<Button>(R.id.bt_set_btn)
        val bellIdTv = dialog2.findViewById<TextView>(R.id.buzzer_num)
        bellIdTv.text = "$bellId 진동벨"

        //Todo. 나중에 RFID랑 bellId랑 매칭시키는 작업이 필요할 것 같음


        setBtn.setOnClickListener {
            //minute 계산하는 로직
            val minute = timePicker.hour * 60 + timePicker.minute
            showCustomDialog2(bellId, minute, context, dialog2)
        }
        dialog2.show()
    }

    private fun showCustomDialog2(bellId: String, minute: Int, context: Context, parent: Dialog) {
        val dialog2 = Dialog(context)
        dialog2.setContentView(R.layout.custom_dlg)

        // 다이얼로그 크기 조정
        val width: Int = (context.resources.displayMetrics.widthPixels * 0.35).toInt() // 화면 너비의 85%
        val height: Int = (context.resources.displayMetrics.heightPixels * 0.2).toInt() // 화면 높이의 50%
        dialog2.window!!.setLayout(width, height)

        // 커스텀 다이얼로그 내의 버튼 가져오기 (예: 확인 버튼, 취소 버튼 등)
        val okBtn = dialog2.findViewById<Button>(R.id.dlg_ok_btn)
        val cancelBtn = dialog2.findViewById<Button>(R.id.dlg_cancle_btn)

        okBtn.setOnClickListener {
            //만약에 이전에 정보가 있으면 덮어쓰기 됨.
            vibratingBellTimeService.setTime(bellId = bellId, minute = minute);


            //Todo. 서버에 전송 필요
            dialog2.dismiss() // 다이얼로그 닫기
            parent.dismiss();
        }

        cancelBtn.setOnClickListener {
            dialog2.dismiss() // 다이얼로그 닫기
        }

        dialog2.show() // 다이얼로그 표시
    }
}