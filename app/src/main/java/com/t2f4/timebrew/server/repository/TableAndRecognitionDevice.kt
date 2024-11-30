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

    fun deleteByTableId(tableId: Integer){
        val deviceId = tableAndDevice.remove(tableId)
            ?: return

        deviceAndTable.remove(deviceId);
    }

    fun deleteByDeviceId(deviceId: Integer){
        val tableId = deviceAndTable.remove(deviceId)
            ?: return

        tableAndDevice.remove(tableId)
    }

    fun findByDeviceId(deviceId: Integer): Integer?{
        return deviceAndTable[deviceId]
    }

    fun findByTableId(tableId: Integer): Integer?{
        return tableAndDevice[tableId]
    }

    fun deleteAll(){
        deviceAndTable.clear()
        tableAndDevice.clear()
    }

    fun findAllTable(): List<Integer> {
        return deviceAndTable.values.toList()
    }

    fun findAllDevice(): List<Integer> {
        return tableAndDevice.values.toList()
    }

}