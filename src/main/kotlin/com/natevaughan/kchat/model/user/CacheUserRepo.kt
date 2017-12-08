package com.natevaughan.kchat.model.user

import com.natevaughan.kchat.AppCompanion.KEY_DEFAULT_PROPERTIES_FILE
import com.natevaughan.kchat.config.admin
import com.natevaughan.kchat.BadRequestException
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import java.util.concurrent.atomic.AtomicLong
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 11/23/17
 */

class CacheUserRepo private constructor() : UserRepo {

    companion object {

        var inst: CacheUserRepo? = null

        fun getInstance(): CacheUserRepo {
            var snapshot = inst
            if (snapshot == null) {
                snapshot = CacheUserRepo()
                inst = snapshot
            }
            return snapshot
        }
    }

    val counter = AtomicLong()
    val usersByKey = mutableMapOf<String, User>()
    val usersByName = mutableMapOf<String, User>()

    init {
        val appConfig = ConfigurationProperties.systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromResource(KEY_DEFAULT_PROPERTIES_FILE)

        val admin = User(
                name = appConfig.get(admin.name),
                role = Role.ADMIN,
                apiKey = appConfig.get(admin.token)
        )
        usersByKey.put(admin.apiKey, admin)
        usersByName.put(admin.name, admin)
    }

    override fun save(entity: User): User {
        if (usersByName.containsKey(entity.name) || usersByKey.containsKey(entity.apiKey)) {
            throw BadRequestException("User details exist")
        }
        entity.id = counter.incrementAndGet()
        usersByKey.put(entity.apiKey, entity)
        usersByName.put(entity.name, entity)
        return entity
    }

    override fun findByApiKey(key: String): User? {
        val found = usersByKey.get(key)
        if (found != null) {
            return found
        }
        return null
    }

    override fun findById(id: Long): User {
        val found = usersByKey.values.filter { it.id == id }
        if (found.isNotEmpty()) {
            return found.first()
        }
        throw NotFoundException("User not found")
    }

    override fun delete(entity: User): Boolean {
        return usersByName.remove(entity.name) != null && usersByKey.remove(entity.apiKey) != null
    }
}