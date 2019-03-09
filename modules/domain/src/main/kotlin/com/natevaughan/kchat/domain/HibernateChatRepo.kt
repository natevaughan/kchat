package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.api.User
import javax.inject.Inject
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 12/9/17
 */
class HibernateChatRepo @Inject constructor(val entityManagerFactory: EntityManagerFactory) : ChatRepo {

    override fun findByKey(key: String): Chat? {
        val entityManager = entityManagerFactory.createEntityManager()
//        val builder = entityManager.criteriaBuilder
//        val criteria = builder.createQuery(ChatEntity::class.java)
//        val messageRoot: Root<ChatEntity> = criteria.from(ChatEntity::class.java)
//        criteria.select(messageRoot)
//        val root = messageRoot.get(Hat_.accessKey)
//        val predicate = builder.equal(root, key)
//        criteria.where(predicate)
        val query = "from ChatEntity where accessKey = :key"
        try {
            return entityManager.createQuery(query).setParameter("accessKey", key).singleResult as ChatEntity
//            return entityManager.createQuery( criteria ).singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun findNewForUser(user: User, timestamp: Long): Collection<ChatEntity> {
        throw NotImplementedError()
    }

    override fun listForUser(user: User): Collection<ChatEntity> {
        val entityManager = entityManagerFactory.createEntityManager()
        val query = "select h from ChatEntity h inner join h.participants u where u = :user or h.creator = :user"
        try {
            return entityManager.createQuery(query).setParameter("user", user).resultList as Collection<ChatEntity>
        } catch (e: NoResultException) {
            return emptyList()
        }
    }

    override fun findById(id: Long): ChatEntity? {
        val entityManager = entityManagerFactory.createEntityManager()
//        val builder = entityManager.criteriaBuilder
//        val criteria = builder.createQuery(ChatEntity::class.java)
//        val messageRoot: Root<ChatEntity> = criteria.from(ChatEntity::class.java)
//        criteria.select(messageRoot)
//        val root = messageRoot.get(Hat_.id)
//        val predicate = builder.equal(root, id)
//        criteria.where(predicate)
        val query = "from ChatEntity h where id = :id"
        try {
            return entityManager.createQuery(query).setParameter("id", id).singleResult as ChatEntity
//            return entityManager.createQuery( criteria ).singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun save(entity: Chat): Chat {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.persist(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return entity
    }

    override fun delete(entity: Chat): Boolean {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.remove(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return true
    }

}

@StaticMetamodel(ChatEntity::class)
object Hat_ {
    @Volatile
    var id: SingularAttribute<ChatEntity, Long>? = null

    @Volatile
    var accessKey: SingularAttribute<ChatEntity, String>? = null

    @Volatile
    var creator: SingularAttribute<ChatEntity, User>? = null

    @Volatile
    var participants: SetAttribute<ChatEntity, User>? = null
}