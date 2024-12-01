package com.t2f4.timebrew.server.repository

import com.t2f4.timebrew.server.dto.TableDto

class TableRepository {
    companion object {
        private val tableNumberStore:
                HashMap<Integer, TableDto> = HashMap()
        private val bellIdIndexStore: //db index 느낌으로 만들어봄
                HashMap<String, TableDto> = HashMap();
    }

    fun findById(tableId: Integer): TableDto? {
        return tableNumberStore[tableId];
    }

    fun findByBellId(bellId: String): TableDto? {
        return bellIdIndexStore[bellId]
    }

    fun findAllAndNotNullBellId(): List<TableDto> {
        return bellIdIndexStore.values.toList()
    }

    fun save(tableDto: TableDto) : TableDto? {
        tableNumberStore[tableDto.tableId] = tableDto
        if(tableDto .bellId != null)
            bellIdIndexStore[tableDto.bellId!!] = tableDto
        return tableNumberStore[tableDto.tableId]
    }

    fun findAll(): List<TableDto> {
        return tableNumberStore.values.toList();
    }

    fun delete(tableId: Integer){
        val dto = tableNumberStore[tableId]
        bellIdIndexStore.remove(dto?.bellId)
        tableNumberStore.remove(tableId)
    }

    fun deleteByBellId(bellId: String){
        val dto = bellIdIndexStore[bellId]
        bellIdIndexStore.remove(bellId)
        tableNumberStore.remove(dto?.tableId)
    }

    fun deleteAll(){
        tableNumberStore.clear();
    }
}