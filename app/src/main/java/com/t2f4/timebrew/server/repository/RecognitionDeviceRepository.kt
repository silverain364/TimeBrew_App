package com.t2f4.timebrew.server.repository

import android.devicelock.DeviceId
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

    fun deleteById(recognitionDeviceId: Integer): RecognitionDeviceDto?{
        return recognitionDeviceStore.remove(recognitionDeviceId)
    }

    fun deleteAll(){
        return recognitionDeviceStore.clear();
    }

    fun save(recognitionDeviceDto: RecognitionDeviceDto): RecognitionDeviceDto? {
        recognitionDeviceStore[recognitionDeviceDto.recognitionDeviceId] = recognitionDeviceDto;
        return recognitionDeviceStore[recognitionDeviceDto.recognitionDeviceId];
    }
}