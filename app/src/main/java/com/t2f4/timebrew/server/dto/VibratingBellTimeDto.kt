package com.t2f4.timebrew.server.dto

import java.time.LocalDate

data class VibratingBellTimeDto (
    var start: LocalDate,
    var end: LocalDate,
    var bellId: Int
)