package com.natevaughan.kchat.model.message.user

import com.natevaughan.kchat.api.UnauthorizedException
import com.natevaughan.kchat.persistence.Identifieable
import com.natevaughan.kchat.persistence.impl.EntityManagerContainer
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

/**
 * Created by nate on 11/23/17
 */
interface UserRepo : Identifieable<User> {
    fun findByApiKey(key: String): User
}

class HibernateUserRepo : UserRepo {

    override fun save(entity: User) {
        EntityManagerContainer.entityManager.transaction.begin()
        EntityManagerContainer.entityManager.persist(entity)
        EntityManagerContainer.entityManager.flush()
        EntityManagerContainer.entityManager.transaction.commit()
    }

    override fun findByApiKey(key: String): User {
        val builder = EntityManagerContainer.entityManager.criteriaBuilder
        val criteria: CriteriaQuery<User> = builder.createQuery( User::class.java )
        val personRoot: Root<User> = criteria.from( User::class.java )
        criteria.select( personRoot )
        val root = personRoot.get( User_.apiKey )
        val predicate = builder.equal( root, key )
        criteria.where( predicate )
        val people: List<User>  = EntityManagerContainer.entityManager.createQuery( criteria ).getResultList()
        if (people.size > 0) {
            return people.first()
        }
        throw UnauthorizedException("User not found")
    }

    override fun findById(id: Long): User {
        val builder = EntityManagerContainer.entityManager.criteriaBuilder
        val criteria: CriteriaQuery<User> = builder.createQuery( User::class.java )
        val personRoot: Root<User> = criteria.from( User::class.java )
        criteria.select( personRoot )
        criteria.where( builder.equal( personRoot.get( User_.id ), id ) )
        val people: List<User>  = EntityManagerContainer.entityManager.createQuery( criteria ).getResultList()
        if (people.size > 0) {
            return people.first()
        }
        throw UnauthorizedException("User not found")
    }
}