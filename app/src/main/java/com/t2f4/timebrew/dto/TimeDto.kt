package com.t2f4.timebrew.dto

data class TimeDto(
    private val timeId: Int,
    private val bellRfid: String,
    private val time: Int,
    private val startTime: String
)
