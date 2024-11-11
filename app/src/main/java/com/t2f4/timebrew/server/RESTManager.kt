package com.t2f4.timebrew.server

import com.t2f4.timebrew.server.controller.TableSetController
import com.t2f4.timebrew.server.controller.TableTimeController
import fi.iki.elonen.router.RouterNanoHTTPD

//싱글톤
class RESTManager(private val port: Int): RouterNanoHTTPD(port) {
    companion object {
        private var server: RESTManager? = null;

        fun getInstace(port: Int): RESTManager{
            //이 함수를 잠금
            server = server ?: synchronized(this){
                server ?: RESTManager(port);
            }

            return server!!;
        }
    }

    init {
        addRoute("/time", TableTimeController::class.java)
        addRoute("/msg", TableSetController::class.java)
    }

}