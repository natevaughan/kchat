package com.natevaughan.kchat.util

import com.natevaughan.kchat.model.message.Message
import com.natevaughan.kchat.model.message.user.Role
import com.natevaughan.kchat.model.message.user.User

/**
 * Created by nate on 11/24/17
 */
object DomainFactory {
    val USER_NAME = "user.name"
    val USER_API_KEY = "user.api.key"
    val USER_ROLE = Role.USER

    val MESSAGE_TEXT = "message.text"
    val MESSAGE_TIMESTAMP = 1L

    fun buildValidUser(): User {
        return User(
                name = USER_NAME,
                role = USER_ROLE,
                apiKey = USER_API_KEY
        )
    }

    fun buildValidMessage(): Message {
        return Message(text = MESSAGE_TEXT, author = buildValidUser(), timestamp = MESSAGE_TIMESTAMP)
    }
}

