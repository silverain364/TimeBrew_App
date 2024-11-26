package com.t2f4.timebrew.api

import com.t2f4.timebrew.dto.ManyVibratebellCreateDto
import com.t2f4.timebrew.dto.VibratingbellCreateDto
import com.t2f4.timebrew.dto.VibratingbellReadDto
import com.t2f4.timebrew.dto.VibratingbellUpdateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface BellApi {
    @POST
    fun create(@Body dto: VibratingbellCreateDto): Call<String>

    @POST
    fun createAll(@Body dtoList: ManyVibratebellCreateDto): Call<String>

    @GET("bell")
    fun findByRfid(@Query("rifd") rfid: String): Call<VibratingbellReadDto>

    @GET("bell/uid")
    fun findByUid(@Query("uid") uid: String): Call<List<VibratingbellReadDto>>

    @GET("bell/all") //Test 용도로만 사용
    fun findAll(): Call<List<VibratingbellReadDto>>

    @PUT("bell")
    fun update(@Body dto: VibratingbellUpdateDto): Call<String>

    @DELETE("bell")
    fun delete(@Query("rfid") rfid: String): Call<String>
}