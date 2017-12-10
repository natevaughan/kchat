package com.natevaughan.hat.message

import java.util.concurrent.atomic.AtomicLong

/**
 * Created by nate on 11/23/17
 */
class CacheMessageRepo : MessageRepo {

    val counter = AtomicLong()
    val messages = mutableMapOf<Long, Message>()

    override fun save(entity: Message): Message {
        val id = entity.id
        if (id == null) {
            val newId = counter.incrementAndGet()
            entity.id = newId
            messages.put(newId, entity)
        } else {
            messages.put(id, entity)
        }
        return entity
    }

    override fun findAllSinceTimestamp(timestamp: Long): Iterable<Message> {
        return messages.values.filter { it.timestamp > timestamp }
    }

    override fun findRecent(count: Int): Iterable<Message> {
        return messages.values.sortedBy { it.timestamp }.takeLast(count)
    }

    override fun findById(id: Long): Message? {
        return messages.get(id)
    }

    override fun delete(entity: Message): Boolean {
        return messages.remove(entity.id) != null
    }
}
