package com.t2f4.timebrew.dto

data class RecognitionDeviceUpdateDto (
    private val id: Long,
    private val deviceId: Int,
    private val tableNumber: Int,
    private val uid: String
)