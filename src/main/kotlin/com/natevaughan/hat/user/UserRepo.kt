package com.natevaughan.hat.user

import com.natevaughan.hat.framework.Repository

/**
 * Created by nate on 11/23/17
 */
interface UserRepo : Repository<User> {
    fun findByApiKey(key: String): User?
}