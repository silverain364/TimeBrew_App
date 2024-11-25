package com.t2f4.timebrew.api

import com.google.firebase.auth.UserInfo
import com.t2f4.timebrew.dto.UserInfoDto
import com.t2f4.timebrew.dto.UserJoinDto
import com.t2f4.timebrew.dto.UserUpdateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("user")
    fun join(@Body dto: UserJoinDto): Call<String>

    @GET("user")
    fun findUserInfo(@Query("uid") uid: String): Call<UserInfoDto>

    @POST("user/update")
    fun updateUser(@Body dto: UserUpdateDto): Call<String>

    @DELETE("user")
    fun deleteUser(@Query("uid") uid:String): Call<String>

}