package com.t2f4.timebrew.server.repository

import android.content.Intent
import android.devicelock.DeviceId

class TableAndRecognitionDevice {
    companion object{
        private val tableAndDevice: HashMap<Integer, Integer> = HashMap()
        private val deviceAndTable: HashMap<Integer, Integer> = HashMap()
    }

    fun save(tableId: Integer, deviceId: Integer){
        tableAndDevice[tableId] = deviceId;
        deviceAndTable[deviceId] = tableId;
    }

    fun removeByTableId(tableId: Integer){
      //  Integer tableAndDevice.remove(tableId);

    }
}