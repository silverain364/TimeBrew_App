package com.t2f4.timebrew.server.repository

import java.util.LinkedList
import java.util.Queue

class RecognitionDeviceMessageRepository {
    //레코딩 일련번호로 특정 키를 생성
    companion object {
        private val recognitionDeviceStore:
                HashMap<Integer, Queue<String>> = HashMap();
    }
    fun findById(recognitionDeviceId: Integer): Queue<String>? {
        return recognitionDeviceStore[recognitionDeviceId];
    }

    fun save(recognitionDeviceId: Integer): Queue<String>? {
        recognitionDeviceStore[recognitionDeviceId] = LinkedList()
        return recognitionDeviceStore[recognitionDeviceId]
    }

    fun delete(recognitionDeviceId: Integer){
        recognitionDeviceStore.remove(recognitionDeviceId);
    }
}