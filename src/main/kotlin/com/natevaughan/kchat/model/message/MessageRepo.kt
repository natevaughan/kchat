package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.message.user.User
import com.natevaughan.kchat.persistence.Identifieable
import com.natevaughan.kchat.persistence.impl.EntityManagerContainer.entityManager
import javax.persistence.criteria.Root

/**
 * Created by nate on 11/23/17
 */

interface MessageRepo: Identifieable<Message> {
    fun findAllByChatId(chatId: String): Iterable<Message>
    fun findAllByUser(user: User): Iterable<Message>
    fun findAllSinceTimestamp(timestamp: Long): Iterable<Message>
}

class HibernateMessageRepo : MessageRepo {

    override fun save(entity: Message) {
        entityManager.transaction.begin()
        entityManager.persist(entity)
        entityManager.flush()
        entityManager.transaction.commit()
    }

    override fun findAllByChatId(chatId: String): Iterable<Message> {
        throw NotImplementedError()
    }

    override fun findAllByUser(user: User): Iterable<Message> {
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Message::class.java)
        val messageRoot: Root<Message> = criteria.from(Message::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get( Message_.author )
        val predicate = builder.equal(root, user)
        criteria.where(predicate)
        return entityManager.createQuery( criteria ).resultList
    }

    override fun findAllSinceTimestamp(timestamp: Long): Iterable<Message> {
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Message::class.java)
        val messageRoot: Root<Message> = criteria.from(Message::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get( Message_.timestamp )
        val predicate = builder.gt(root, timestamp)
        criteria.where(predicate)
        return entityManager.createQuery( criteria ).resultList
    }

    override fun findById(id: Long): Message {
        throw NotImplementedError()
    }
}