package com.t2f4.timebrew.dto

data class UserUpdateDto(
    private val uid: String,
    private val email: String,
    private val workplace: String,
    private val phone:String
)
