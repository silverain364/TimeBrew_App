package com.t2f4.timebrew.server.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class VibratingBellTimeDto (
    var start: LocalDateTime,
    var minute: Int,
    var end: LocalDateTime,
    var bellId: String
)