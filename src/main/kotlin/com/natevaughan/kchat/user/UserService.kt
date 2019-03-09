package com.natevaughan.kchat.user

import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.api.UserRepo
import javax.inject.Inject
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 12/3/17
 */
class UserService @Inject constructor(val userRepo: UserRepo) {

    fun findByApiKey(key: String): User {
        val user = userRepo.findByApiKey(key)
        if (user != null) {
            return user
        }
        throw NotFoundException("User not found")
    }

    fun findById(id: Long): User {
        val user = userRepo.findById(id)
        if (user != null) {
            return user
        }
        throw NotFoundException("User not found")
    }

    fun save(user: User): User {
        return userRepo.save(user)
    }

    fun delete(user: User): Boolean {
        return userRepo.delete(user)
    }
}