package com.natevaughan.kchat.model.user

import com.natevaughan.kchat.model.Repository

/**
 * Created by nate on 11/23/17
 */
interface UserRepo : Repository<User> {
    fun findByApiKey(key: String): User?
}