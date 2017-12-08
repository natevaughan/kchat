package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.user.User
import com.natevaughan.kchat.model.Repository
/**
 * Created by nate on 11/23/17
 */

interface MessageRepo: Repository<Message> {
    fun findAllByUser(user: User): Iterable<Message>
    fun findAllSinceTimestamp(timestamp: Long): Iterable<Message>
    fun findRecent(count: Int): Iterable<Message>
}