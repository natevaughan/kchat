package com.natevaughan.hat.message

import com.natevaughan.hat.framework.Repository

/**
 * Created by nate on 11/23/17
 */
interface MessageRepo: Repository<Message> {
    fun findAllSinceTimestamp(timestamp: Long): Iterable<Message>
    fun findRecent(count: Int): Iterable<Message>
}