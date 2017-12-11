package com.natevaughan.hat

import com.natevaughan.hat.user.User
import javax.inject.Inject
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.criteria.Root
import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 12/9/17
 */
class HibernateHatRepo @Inject constructor(val entityManagerFactory: EntityManagerFactory) : HatRepo {

    override fun findByKey(key: String): Hat? {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Hat::class.java)
        val messageRoot: Root<Hat> = criteria.from(Hat::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get(Hat_.accessKey)
        val predicate = builder.equal(root, key)
        criteria.where(predicate)
        try {
            return entityManager.createQuery( criteria ).singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun findNewForUser(user: User, timestamp: Long): Collection<Hat> {
        throw NotImplementedError()
    }

    override fun listForUser(user: User): Collection<Hat> {
        val entityManager = entityManagerFactory.createEntityManager()
        val query = "select h from Hat h inner join h.participants u where u = :user or h.creator = :user"
        try {
            return entityManager.createQuery(query).setParameter("user", user).resultList as Collection<Hat>
        } catch (e: NoResultException) {
            return emptyList()
        }
    }

    override fun findById(id: Long): Hat? {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria = builder.createQuery(Hat::class.java)
        val messageRoot: Root<Hat> = criteria.from(Hat::class.java)
        criteria.select(messageRoot)
        val root = messageRoot.get(Hat_.id)
        val predicate = builder.equal(root, id)
        criteria.where(predicate)
        try {
            return entityManager.createQuery( criteria ).singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun save(entity: Hat): Hat {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.persist(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return entity
    }

    override fun delete(entity: Hat): Boolean {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.remove(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return true
    }

}

@StaticMetamodel(Hat::class)
object Hat_ {
    @Volatile
    var id: SingularAttribute<Hat, Long>? = null

    @Volatile
    var accessKey: SingularAttribute<Hat, String>? = null

    @Volatile
    var creator: SingularAttribute<Hat, User>? = null

    @Volatile
    var participants: SetAttribute<Hat, User>? = null
}