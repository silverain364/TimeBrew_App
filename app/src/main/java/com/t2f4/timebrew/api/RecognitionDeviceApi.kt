package com.t2f4.timebrew.api

import com.t2f4.timebrew.dto.RecognitionDeviceCreateDto
import com.t2f4.timebrew.dto.RecognitionDeviceDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface RecognitionDeviceApi {
    @POST("recognitionDevice")
    fun create(@Body dto: RecognitionDeviceCreateDto): Call<String>

    @GET("recognitionDevice/table-id")
    fun findByTableId(@Query("tableId") tableId: Long): Call<RecognitionDeviceDto>

    @GET("recognitionDevice/uid/tableNumber")
    fun findByUidAndTableNumber(@Query("uid") uid:String, @Query("tableNumber") tableNumber: Int):
            Call<RecognitionDeviceDto>

    @GET("recognitionDevice")
    fun findById(@Query("id") id:Long): Call<RecognitionDeviceDto>

    @PUT("recognitionDevice")
    fun update(@Body dto: RecognitionDeviceCreateDto): Call<String>

    @DELETE("recognitionDevice")
    fun delete(@Query("id") id: Long): Call<String>
}