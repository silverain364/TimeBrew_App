package com.t2f4.timebrew.server.controller

import com.t2f4.timebrew.server.dto.RecognitionDeviceDto
import com.t2f4.timebrew.server.repository.RecognitionDeviceMessageRepository
import com.t2f4.timebrew.server.repository.RecognitionDeviceRepository
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD

class TableSetController: RouterNanoHTTPD.GeneralHandler() {
    //나중에 의존성 주입으로 받을 거임
    //서비스 만들 수도 있음
    private val recognitionDeviceRepository = RecognitionDeviceRepository();
    private val recognitionDeviceMessageRepository = RecognitionDeviceMessageRepository();
    override fun get(
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        //Todo. Setting 구현 및 등록 구현

        //인식장치 id 받기
        val recognitionDeviceId = urlParams?.get("id")
            ?: return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "", "not exist id!");

        //인식장치 id로 dto조회 없으면 생성
        val recognitionDeviceDto =  recognitionDeviceRepository.findById(recognitionDeviceId)
            ?: recognitionDeviceRepository.save(RecognitionDeviceDto(recognitionDeviceId, null));

        //객체가 안 만들어지면 예외처리
        recognitionDeviceDto ?: throw Exception();

        val message = recognitionDeviceMessageRepository.findById(recognitionDeviceDto.recognitionDeviceId)
            ?: recognitionDeviceMessageRepository.save(recognitionDeviceDto.recognitionDeviceId);

        //메시지가 안 만들어지면 예외처리
        message ?: throw Exception();

        return return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain",
            if(message.isNotEmpty()) message.poll() else "empty")
    }
}