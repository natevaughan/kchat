package com.natevaughan.kchat.model.message.user

import com.natevaughan.kchat.api.UnauthorizedException
import java.util.concurrent.atomic.AtomicLong
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 11/23/17
 */

class CacheUserRepo : UserRepo {
    val counter = AtomicLong()
    val users = arrayListOf<User>(
    )

    override fun save(entity: User) {
        entity.id = counter.incrementAndGet()
        users.add(entity)
    }

    override fun findByApiKey(key: String): User {
        val found = users.filter { it.apiKey == key }
        if (found.isNotEmpty()) {
            return found.first()
        }
        throw UnauthorizedException("User not found")
    }

    override fun findById(id: Long): User {
        val found = users.filter { it.id == id }
        if (found.isNotEmpty()) {
            return found.first()
        }
        throw NotFoundException("User not found")
    }
}