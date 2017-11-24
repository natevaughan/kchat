package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.message.user.User
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by nate on 11/23/17
 */
class CacheMessageRepo : MessageRepo {
    val counter = AtomicLong()
    val messages = arrayListOf<Message>()

    override fun save(entity: Message) {
        entity.id = counter.incrementAndGet()
        messages.add(entity)
    }

    override fun findAllByChatId(chatId: String): Iterable<Message> {
        throw NotImplementedError()
    }

    override fun findAllByUser(user: User): Iterable<Message> {
        return messages.filter { it.author == user }
    }

    override fun findAllSinceTimestamp(timestamp: Long): Iterable<Message> {
        return messages.filter { it.timestamp > timestamp }
    }

    override fun findById(id: Long): Message {
        throw NotImplementedError()
    }
}