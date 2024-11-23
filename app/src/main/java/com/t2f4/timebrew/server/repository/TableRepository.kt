package com.t2f4.timebrew.server.repository

import com.t2f4.timebrew.server.dto.TableDto

class TableRepository {
    companion object {
        //private val tableRe
        private val tableNumberStore:
                HashMap<Integer, TableDto> = HashMap()
    }

    fun findById(tableId: Integer): TableDto? {
        return tableNumberStore[tableId];
    }

    fun save(tableDto: TableDto) : TableDto? {
        tableNumberStore[tableDto.tableId] = tableDto
        return tableNumberStore[tableDto.tableId]
    }

    fun findAll(): List<TableDto> {
        return tableNumberStore.values.toList();
    }

    fun delete(tableId: Integer){
        tableNumberStore.remove(tableId)
    }

    fun deleteAll(){
        tableNumberStore.clear();
    }
}