package com.t2f4.timebrew.server.controller

import android.util.Log
import com.t2f4.timebrew.server.dto.RecognitionDeviceDto
import com.t2f4.timebrew.server.repository.BellMessageRepository
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD

class BellController : RouterNanoHTTPD.GeneralHandler() {
    private val bellMessageRepository = BellMessageRepository();
    override fun get(
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        val bellId = session?.parameters?.get("bellId")?.get(0)
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "", "not exist id!");

        Log.d("http", "bell msg : $bellId")

        //Msg Queue가 없으면 생성
        val message = bellMessageRepository.findById(bellId)
            ?: bellMessageRepository.save(bellId);

        return NanoHTTPD.newFixedLengthResponse(
            NanoHTTPD.Response.Status.OK, "text/plain",
            if (message.isNotEmpty()) message.poll() else "empty"
        )
    }
}