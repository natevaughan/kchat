package com.natevaughan.kchat.model.message.user

import com.natevaughan.kchat.api.BadRequestException
import com.natevaughan.kchat.api.UnauthorizedException
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
        val nate = User(name = "nate", role = Role.ADMIN, apiKey = "fafca0b3-8834-4bef-bad7-4d79aa9c9074")
        usersByKey.put(nate.apiKey, nate)
        usersByName.put(nate.name, nate)
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

    override fun findByApiKey(key: String): User {
        val found = usersByKey.get(key)
        if (found != null) {
            return found
        }
        throw UnauthorizedException("User not found")
    }

    override fun findById(id: Long): User {
        val found = usersByKey.values.filter { it.id == id }
        if (found.isNotEmpty()) {
            return found.first()
        }
        throw NotFoundException("User not found")
    }
}