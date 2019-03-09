package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.framework.UnauthorizedException
import javax.inject.Inject
import javax.persistence.EntityManagerFactory
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 12/2/17
 */
class HibernateUserRepo @Inject constructor(val entityManagerFactory: EntityManagerFactory) : UserRepo {

    override fun save(entity: User): User {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.persist(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return entity
    }

    override fun findByApiKey(key: String): User? {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria: CriteriaQuery<UserEntity> = builder.createQuery( UserEntity::class.java )
        val personRoot: Root<UserEntity> = criteria.from( UserEntity::class.java )
        criteria.select( personRoot )
        val root = personRoot.get(User_.apiKey)
        val predicate = builder.equal( root, key )
        criteria.where( predicate )
        val people: List<User>  = entityManager.createQuery( criteria ).getResultList()
        if (people.isNotEmpty()) {
            return people.first()
        }
        return null
    }

    override fun findById(id: Long): User {
        val entityManager = entityManagerFactory.createEntityManager()
        val builder = entityManager.criteriaBuilder
        val criteria: CriteriaQuery<UserEntity> = builder.createQuery( UserEntity::class.java )
        val personRoot: Root<UserEntity> = criteria.from( UserEntity::class.java )
        criteria.select( personRoot )
        criteria.where( builder.equal( personRoot.get(User_.id), id ) )
        val people: List<User>  = entityManager.createQuery( criteria ).resultList
        if (people.isNotEmpty()) {
            return people.first()
        }
        throw UnauthorizedException("User not found")
    }

    override fun delete(entity: User): Boolean {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        entityManager.remove(entity)
        entityManager.flush()
        entityManager.transaction.commit()
        return true
    }
}

@StaticMetamodel(UserEntity::class)
object User_ {
    @Volatile
    var id: SingularAttribute<UserEntity, Long>? = null
    @Volatile
    var name: SingularAttribute<UserEntity, String>? = null
    @Volatile
    var apiKey: SingularAttribute<UserEntity, String>? = null
}