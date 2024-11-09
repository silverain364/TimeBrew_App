package com.t2f4.timebrew.server.controller

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD

class TableSetController: RouterNanoHTTPD.GeneralHandler() {
    override fun get(
        uriResource: RouterNanoHTTPD.UriResource?,
        urlParams: MutableMap<String, String>?,
        session: NanoHTTPD.IHTTPSession?
    ): NanoHTTPD.Response {
        //Todo. Setting 구현 및 등록 구현

        return super.get(uriResource, urlParams, session)
    }
}