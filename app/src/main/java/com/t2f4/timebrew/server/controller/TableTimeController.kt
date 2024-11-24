package com.t2f4.timebrew.server.controller

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.t2f4.timebrew.InitApplication
import com.t2f4.timebrew.IntroPage
import com.t2f4.timebrew.R
import com.t2f4.timebrew.server.RESTManager
import com.t2f4.timebrew.server.service.VibratingBellTimeService
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD
import kotlin.concurrent.thread

class TableTimeController : RouterNanoHTTPD.GeneralHandler(){
    private val vibratingBellTimeService = VibratingBellTimeService();
    override fun get( //남은 시간이 얼마나 남았는지 조회할 때 사용
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        //Todo. Server로 부터 아두이노 남은 시간 가져오기 또는 저장된 아두이노 시간 정보 가져오기
        val bellId = session?.parameters?.get("bellId")
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "exist not bellId");

        val reamingTimePercent = vibratingBellTimeService.reamingTimePercent(bellId[0]);

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain",
            reamingTimePercent.toString())
    }

    override fun post( //특정 아두이노에 시간 할당
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        Log.d("http", "post: " + session?.parameters)

        val bellId = session?.parameters?.get("bellId")
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "exist not bellId");

        InitApplication.getCurrentActivity().runOnUiThread {
            showBuzzerPopup(bellId.toString(), InitApplication.getCurrentActivity());
        }

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "true");
    }

    private fun showBuzzerPopup(bellId: String, context: Context) {
        val dialog2 = Dialog(context)
        dialog2.setContentView(R.layout.buzzer_rec)

        val width: Int = (context.resources.displayMetrics.widthPixels * 0.23).toInt()
        val height: Int = (context.resources.displayMetrics.heightPixels * 0.63).toInt()
        dialog2.window!!.setLayout(width, height)

        val setBtn = dialog2.findViewById<Button>(R.id.bt_set_btn)
        val bellIdTv = dialog2.findViewById<TextView>(R.id.buzzer_num)
        bellIdTv.text = "$bellId 진동벨"

        //Todo. 나중에 RFID랑 bellId랑 매칭시키는 작업이 필요할 것 같음
        //Todo. minute 계산하는 로직 필요

        setBtn.setOnClickListener { showCustomDialog2(bellId, 60, context) }
        dialog2.show()
    }

    private fun showCustomDialog2(bellId: String, minute: Int, context: Context) {
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
            vibratingBellTimeService.setTime(bellId = bellId, minute = minute);
            //Todo. 서버에 전송 필요
            dialog2.dismiss() // 다이얼로그 닫기
        }

        cancelBtn.setOnClickListener {
            dialog2.dismiss() // 다이얼로그 닫기
        }

        dialog2.show() // 다이얼로그 표시
    }
}