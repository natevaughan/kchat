package com.natevaughan.hat.message

import com.natevaughan.hat.Hat
import com.natevaughan.hat.framework.Repository

/**
 * Created by nate on 11/23/17
 */
interface MessageRepo: Repository<Message> {
    fun findForHatSinceTimestamp(hat: Hat, timestamp: Long): Iterable<Message>
    fun findRecent(hat: Hat, count: Int): Iterable<Message>
}