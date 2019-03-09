package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.User
import java.util.TreeSet
import javax.inject.Inject
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.criteria.Root
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 12/2/17
 */
class HibernateMessageRepo @Inject constructor(val entityManagerFactory: EntityManagerFactory) : MessageRepo {

    override fun delete(entity: Message): Boolean {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.remove(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return true
    }

    override fun save(entity: Message): Message {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.persist(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return entity
    }


    override fun findById(id: Long): Message? {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(MessageEntity::class.java)
        val messageRoot: Root<MessageEntity> = criteria.from(MessageEntity::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get(Message_.id)
        val predicate = builder.equal(root, id)
        criteria.where(predicate)
        try {
            return entityManager.createQuery( criteria ).singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun findForHatSinceTimestamp(hat: Chat, timestamp: Long): Iterable<Message> {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(MessageEntity::class.java)
        val messageRoot: Root<MessageEntity> = criteria.from(MessageEntity::class.java)
        criteria.select(messageRoot)
        val timestampPath = messageRoot.get(Message_.timestamp)
        val timestampPredicate = builder.gt(timestampPath, timestamp)
        val hatPath = messageRoot.get(Message_.hat)
        val hatPredicate = builder.equal(hatPath, hat)
        criteria.where(timestampPredicate, hatPredicate)
        return entityManager.createQuery( criteria ).resultList.toCollection(TreeSet<Message>())
    }

    override fun findRecent(hat: Chat, count: Int): Iterable<Message> {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(MessageEntity::class.java)
        val messageRoot: Root<MessageEntity> = criteria.from(MessageEntity::class.java)
        val hatPath = messageRoot.get(Message_.hat)
        val hatPredicate = builder.equal(hatPath, hat)
        criteria.select(messageRoot).where(hatPredicate).orderBy(builder.desc(messageRoot.get(Message_.id)))
        return entityManager.createQuery( criteria ).setMaxResults(count).resultList.toCollection(TreeSet<Message>())
    }
}


@StaticMetamodel(MessageEntity::class)
object Message_ {
    @Volatile
    var id: SingularAttribute<Message, Long>? = null
    @Volatile
    var author: SingularAttribute<Message, User>? = null
    @Volatile
    var timestamp: SingularAttribute<Message, Long>? = null
    @Volatile
    var hat: SingularAttribute<Message, ChatEntity>? = null
}