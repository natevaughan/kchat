package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.user.User
import java.util.TreeSet
import javax.inject.Inject
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.criteria.Root

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

    override fun findAllByUser(user: User): Iterable<Message> {
        val entityManager = entityManagerFactory.createEntityManager()
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
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Message::class.java)
        val messageRoot: Root<Message> = criteria.from(Message::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get( Message_.timestamp )
        val predicate = builder.gt(root, timestamp)
        criteria.where(predicate)
        return entityManager.createQuery( criteria ).resultList.toCollection(TreeSet<Message>())
    }

    override fun findById(id: Long): Message? {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Message::class.java)
        val messageRoot: Root<Message> = criteria.from(Message::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get( Message_.id )
        val predicate = builder.gt(root, id)
        criteria.where(predicate)
        try {
            return entityManager.createQuery( criteria ).singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun findRecent(count: Int): Iterable<Message> {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Message::class.java)
        val messageRoot: Root<Message> = criteria.from(Message::class.java)
        criteria.select(messageRoot).orderBy(builder.desc(messageRoot.get( Message_.id )))
        return entityManager.createQuery( criteria ).setMaxResults(count).resultList.toCollection(TreeSet<Message>())
    }
}