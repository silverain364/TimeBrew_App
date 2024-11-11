package com.t2f4.timebrew.server.controller

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD
import okhttp3.Response

class TableTimeController : RouterNanoHTTPD.GeneralHandler(){
    override fun get( //남은 시간이 얼마나 남았는지 조회할 때 사용
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        //Todo. Server로 부터 아우디노 남은 시간 가져오기 또는 저장된 아두이노 시간 정보 가져오기

        return super.get(uriResource, urlParams, session)
    }

    override fun post( //특정 아두이노에 시간 할당
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        //Todo. 시간 설정 페이지 다이얼 띄우기

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "true");
    }
}