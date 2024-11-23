package com.t2f4.timebrew.server.service

import com.t2f4.timebrew.server.dto.VibratingBellTimeDto
import com.t2f4.timebrew.server.repository.VibratingBellTimeRepository
import java.time.Duration
import java.time.LocalDateTime

class VibratingBellTimeService {
    private val vibratingBellTimeRepository = VibratingBellTimeRepository();

    fun setTime(bellId: String, minute: Int){
        vibratingBellTimeRepository.save(VibratingBellTimeDto(
            LocalDateTime.now(), minute, LocalDateTime.now().plusMinutes(minute as Long), bellId
        ))
    }

    //Todo. 추후 구현 예정
    fun addTime(bellId: String, minute: Int){

    }

    fun reamingTime(bellId: String): Int{
        if(!vibratingBellTimeRepository.existById(bellId))
            return -1;

        val vibratingBellTimeDto = vibratingBellTimeRepository.findById(bellId)!!
        return Duration.between(vibratingBellTimeDto.end, LocalDateTime.now()).toMinutes() as Int;
    }

    fun reamingTimePercent(bellId: String): Float {
        if(!vibratingBellTimeRepository.existById(bellId))
            return -1f;

        val vibratingBellTimeDto = vibratingBellTimeRepository.findById(bellId)!!

        return Duration.between(vibratingBellTimeDto.end, LocalDateTime.now())
            .toMinutes() as Float / vibratingBellTimeDto.minute;
    }
}