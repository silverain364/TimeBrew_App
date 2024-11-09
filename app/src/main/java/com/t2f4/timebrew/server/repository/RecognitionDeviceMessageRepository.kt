package com.t2f4.timebrew.server.repository

import java.util.LinkedList
import java.util.Queue

class RecognitionDeviceMessageRepository {
    //레코딩 일련번호로 특정 키를 생성
    companion object {
        private val recognitionDeviceStore:
                HashMap<String, Queue<String>> = HashMap();
    }
    fun findById(recognitionDeviceId: String): Queue<String>? {
        return recognitionDeviceStore[recognitionDeviceId];
    }

    fun insert(recognitionDeviceId: String) {
        recognitionDeviceStore[recognitionDeviceId] = LinkedList()
    }

    fun delete(recognitionDeviceId: String){
        recognitionDeviceStore.remove(recognitionDeviceId);
    }
}