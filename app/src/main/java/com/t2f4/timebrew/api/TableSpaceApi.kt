package com.t2f4.timebrew.api

import okhttp3.Callback
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface TableSpaceApi {
    @POST("/table")
    @Multipart
    fun saveTableSpace(@Query("uid") uid: String, @Part html: MultipartBody.Part):Call<String>
}