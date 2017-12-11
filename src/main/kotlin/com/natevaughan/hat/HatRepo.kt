package com.natevaughan.hat

import com.natevaughan.hat.framework.Repository
import com.natevaughan.hat.user.User

/**
 * Created by nate on 12/9/17
 */
interface HatRepo : Repository<Hat> {
    fun findByKey(key: String): Hat?
    fun findNewForUser(user: User, timestamp: Long): Collection<Hat>
    fun listForUser(user: User): Collection<Hat>
}