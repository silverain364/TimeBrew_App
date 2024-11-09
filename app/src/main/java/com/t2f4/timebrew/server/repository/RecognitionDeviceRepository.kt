package com.t2f4.timebrew.server.repository

import com.t2f4.timebrew.server.dto.RecognitionDeviceDto

class RecognitionDeviceRepository {
    companion object{
        private val recognitionDeviceStore: HashMap<String, RecognitionDeviceDto>
            = HashMap()
    }

    fun findById(recognitionDeviceId: String): RecognitionDeviceDto?{
        return recognitionDeviceStore[recognitionDeviceId]
    }

    fun update(recognitionDeviceDto: RecognitionDeviceDto): RecognitionDeviceDto?{
        recognitionDeviceStore[recognitionDeviceDto.recognitionDeviceId] = recognitionDeviceDto
        return  recognitionDeviceDto;
    }
}