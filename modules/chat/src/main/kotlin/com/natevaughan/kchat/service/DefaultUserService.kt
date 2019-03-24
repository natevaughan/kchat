package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.framework.NotFoundException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 12/3/17
 */
@Singleton
class DefaultUserService @Inject constructor(val userRepo: UserRepo, val spaceService: SpaceService): UserService {

    override fun findByApiKey(key: String): User {
        return userRepo.findByApiKey(key)
    }

    override fun findById(id: String): User {
        return userRepo.findById(id)
    }

    override fun save(user: User): User {
        return userRepo.save(user)
    }

    override fun delete(user: User): Boolean {
        return userRepo.delete(user)
    }

    override fun listForSpace(spaceId: String, user: User): Collection<Participant> {
        val authorized = spaceService.checkAccess(spaceId, user.id)
        if (!authorized) {
            throw NotFoundException("Space not found")
        }
        val space = spaceService.findById(spaceId)
		return space.users
    }
}