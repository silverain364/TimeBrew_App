package com.t2f4.timebrew.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitSetting {
    companion object{
        const val URL = "http://49.50.164.110:8080/"
        const val FILE_URL = "http://49.50.164.110:8071/test2/"


        fun getRetrofit() : Retrofit{
            return Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder().setLenient().create() //엄격한 규칙을 좀 더 자유롭게해준다.
                ))
                .build()
        }
    }
}