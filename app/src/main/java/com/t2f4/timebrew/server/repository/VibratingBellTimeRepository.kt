package com.t2f4.timebrew.server.repository

import com.t2f4.timebrew.server.dto.RecognitionDeviceDto

class VibratingBellTimeRepository {
    companion object{
        private val VibratingBellTimeStore: HashMap<Integer, VibratingBellTimeRepository>
                = HashMap()
    }

}