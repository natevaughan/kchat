package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.message.user.User
import java.util.concurrent.atomic.AtomicLong
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 11/23/17
 */
class CacheMessageRepo : MessageRepo {

    val counter = AtomicLong()
    val messages = mutableMapOf<Long, Message>()

    override fun save(entity: Message): Message {
        val id = counter.incrementAndGet()
        entity.id = id
        messages.put(id, entity)
        return entity
    }

    override fun findAllByUser(user: User): Iterable<Message> {
        return messages.values.filter { it.author == user }
    }

    override fun findAllSinceTimestamp(timestamp: Long): Iterable<Message> {
        return messages.values.filter { it.timestamp > timestamp }
    }

    override fun findRecent(count: Int): Iterable<Message> {
        return messages.values.sortedByDescending { it.timestamp }.take(count)
    }

    override fun findById(id: Long): Message {
        val msg = messages.get(id)
        if (msg == null) {
            throw NotFoundException()
        }
        return msg
    }
}