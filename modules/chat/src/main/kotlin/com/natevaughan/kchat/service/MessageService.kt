package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.User

/**
 * Created by nate on 3/22/19
 */
interface MessageService {

    fun findAllSinceTimestamp(chatId: String, timestamp: Long, user: User): Iterable<Message>

    fun findRecent(chatId: String, count: Int, user: User): Iterable<Message>

    fun create(text: String, chatId: String, user: User): Message

    fun update(text: String, id: String, user: User): Message

    fun findById(id: String): Message?

    fun delete(id: String, user: User): Boolean
}