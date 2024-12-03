package com.t2f4.timebrew.server.repository

import java.util.*
import kotlin.collections.HashMap

class BellMessageRepository {
    companion object {
        private val bellMessageStore:
                HashMap<String, Queue<String>> = HashMap();
    }
    fun findById(bellId: String): Queue<String>? {
        return bellMessageStore[bellId];
    }

    fun save(bellId: String): Queue<String> {
        bellMessageStore[bellId] = LinkedList()
        return bellMessageStore[bellId]!!
    }

    fun delete(bellId: String){
        bellMessageStore.remove(bellId);
    }
}