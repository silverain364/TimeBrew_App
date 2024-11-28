package com.t2f4.timebrew.server.repository

import com.t2f4.timebrew.server.dto.RecognitionDeviceDto

class RecognitionDeviceRepository {
    companion object{
        private val recognitionDeviceStore: HashMap<Integer, RecognitionDeviceDto>
            = HashMap()
    }

    fun findById(recognitionDeviceId: Integer): RecognitionDeviceDto?{
        return recognitionDeviceStore[recognitionDeviceId]
    }

    fun findAll(): List<RecognitionDeviceDto>{
        return recognitionDeviceStore.values.toList();
    }


    fun save(recognitionDeviceDto: RecognitionDeviceDto): RecognitionDeviceDto? {
        recognitionDeviceStore[recognitionDeviceDto.recognitionDeviceId] = recognitionDeviceDto;
        return recognitionDeviceStore[recognitionDeviceDto.recognitionDeviceId];
    }
}