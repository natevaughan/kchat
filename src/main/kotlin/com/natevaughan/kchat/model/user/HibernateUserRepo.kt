package com.natevaughan.kchat.model.user

import com.natevaughan.kchat.config.admin
import com.natevaughan.kchat.UnauthorizedException
import com.natpryce.konfig.Configuration
import javax.inject.Inject
import javax.persistence.EntityManagerFactory
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

/**
 * Created by nate on 12/2/17
 */
class HibernateUserRepo @Inject constructor(appConfig: Configuration, val entityManagerFactory: EntityManagerFactory) : UserRepo {

    init {
        val existingAdmin = findByApiKey(appConfig.get(admin.token))

        if (existingAdmin == null) {
            val admin = User(
                    name = appConfig.get(admin.name),
                    role = Role.ADMIN,
                    apiKey = appConfig.get(admin.token)
            )
            save(admin)
        }
    }

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
        val criteria: CriteriaQuery<User> = builder.createQuery( User::class.java )
        val personRoot: Root<User> = criteria.from( User::class.java )
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
        val criteria: CriteriaQuery<User> = builder.createQuery( User::class.java )
        val personRoot: Root<User> = criteria.from( User::class.java )
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
