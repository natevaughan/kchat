package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.message.user.User
import com.natevaughan.kchat.persistence.Identifieable
import com.natevaughan.kchat.persistence.impl.EntityManagerContainer

/**
 * Created by nate on 11/23/17
 */

interface MessageRepo: Identifieable<Message> {
    fun findAllByChatId(chatId: String): Iterable<Message>
    fun findAllByUser(user: User): Iterable<Message>
    fun findAllByUserSinceTimestamp(user: User, timestamp: Long): Iterable<Message>
}

class HibernateMessageRepo : MessageRepo {

    override fun save(entity: Message) {
        EntityManagerContainer.entityManager.transaction.begin()
        EntityManagerContainer.entityManager.persist(entity)
        EntityManagerContainer.entityManager.flush()
        EntityManagerContainer.entityManager.transaction.commit()
    }

    override fun findAllByChatId(chatId: String): Iterable<Message> {
        throw NotImplementedError()
    }

    override fun findAllByUser(user: User): Iterable<Message> {
        throw NotImplementedError()
    }

    override fun findAllByUserSinceTimestamp(user: User, timestamp: Long): Iterable<Message> {
        throw NotImplementedError()
    }

    override fun findById(id: Long): Message {
        throw NotImplementedError()
    }
}