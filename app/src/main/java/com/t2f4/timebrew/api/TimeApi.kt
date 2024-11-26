package com.t2f4.timebrew.api

import com.t2f4.timebrew.dto.TimeCreateDto
import com.t2f4.timebrew.dto.TimeDto
import com.t2f4.timebrew.dto.TimeUpdateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TimeApi {
    @POST("time")
    fun create(@Body dto: TimeCreateDto): Call<String>

    @GET("time")
    fun findById(@Query("id") id: Integer): Call<TimeDto>

    @GET("time/bell-rfid")
    fun findByBellRfid(@Query("time/bell-rfid") bellRfid: String): Call<TimeDto>

    @PUT("time")
    fun update(@Body dto: TimeUpdateDto): Call<String>


    @DELETE("time")
    fun deleteById(@Query("id") id: Integer): Call<String>

    @DELETE("time/bell-rfid")
    fun deleteByBellRfid(@Query("bellRfid") bellRfid: String): Call<String>
}