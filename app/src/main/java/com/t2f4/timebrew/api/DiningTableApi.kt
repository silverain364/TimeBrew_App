package com.t2f4.timebrew.api

import com.t2f4.timebrew.dto.DiningTableCreateDto
import com.t2f4.timebrew.dto.DiningTableDto
import com.t2f4.timebrew.dto.DiningTableUpdateDto
import com.t2f4.timebrew.dto.ManyDiningTableCreateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface DiningTableApi {
    @POST("diningTable")
    fun create(@Body diningTableCreateDto: DiningTableCreateDto): Call<String>

    @POST("diningTable/all")
    fun createAll(@Body manyDiningTableCreateDto: ManyDiningTableCreateDto)

    @GET("diningTable")
    fun findById(@Query("id") id: Long): Call<DiningTableDto>

    @GET("diningTable/all")
    fun findByUid(@Query("uid") uid:String): Call<List<DiningTableDto>>

    @PUT("diningTable")
    fun update(@Body diningTableUpdateDto: DiningTableUpdateDto): Call<String>

    @DELETE("diningTable")
    fun delete(@Query("id") id: Long): Call<String>

    @DELETE("diningTable/all")
    fun deleteAll(@Query("uid") uid: String): Call<String>
}