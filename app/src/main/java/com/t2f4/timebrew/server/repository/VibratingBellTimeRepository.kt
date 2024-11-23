package com.t2f4.timebrew.server.repository

import com.t2f4.timebrew.server.dto.RecognitionDeviceDto
import com.t2f4.timebrew.server.dto.VibratingBellTimeDto

class VibratingBellTimeRepository {
    companion object{
        private val vibratingBellTimeStore: HashMap<String, VibratingBellTimeDto>
                = HashMap()
    }

    fun findById(vibratingBellId: String): VibratingBellTimeDto?{
        return vibratingBellTimeStore[vibratingBellId];
    }

    fun save(vibratingBellTimeDto: VibratingBellTimeDto): VibratingBellTimeDto? {
        vibratingBellTimeStore[vibratingBellTimeDto.bellId] = vibratingBellTimeDto;
        return vibratingBellTimeStore[vibratingBellTimeDto.bellId]
    }

    fun existById(vibratingBellId: String): Boolean{
        return vibratingBellTimeStore.contains(vibratingBellId);
    }


}