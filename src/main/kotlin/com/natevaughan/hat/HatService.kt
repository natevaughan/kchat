package com.natevaughan.hat

import com.natevaughan.hat.framework.UnauthorizedException
import com.natevaughan.hat.user.User
import com.natevaughan.hat.user.UserService
import java.util.*
import javax.inject.Inject
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 12/9/17
 */
interface HatService {
    fun listForUser(user: User): Collection<Hat>
    fun findById(id: Long, user: User): Hat
    fun create(name: String, user: User, participantIds: Collection<Long>): Hat
    fun delete(id: Long, user: User)
}

class HatServiceImpl @Inject constructor(val hatRepo: HatRepo, val userService: UserService) {

    fun listForUser(user: User): Collection<Hat> {
        return hatRepo.listForUser(user)
    }

    fun findById(id: Long, user: User): Hat {
        val hat = hatRepo.findById(id)
        if (hat != null) {
            if (hat.creator == user || hat.participants.contains(user)) {
                return hat
            }
        }
        throw NotFoundException()
    }

    fun create(name: String, user: User, participantIds: Collection<Long>): Hat {
        val participants = participantIds.map { userService.findById(it) }
        val key = UUID.randomUUID().toString()
        return hatRepo.save(Hat(name, key, user, participants))
    }

    fun delete(id: Long, user: User) {
        val hat = findById(id, user)
        if (user != hat.creator) {
            throw UnauthorizedException("User ${user} is not owner of hat ${hat.id}")
        }
        hatRepo.delete(hat)
    }
}
