package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.User
import java.time.Instant
import java.util.Collections.emptyList

/**
 * Created by nate on 11/24/17
 */
object DomainFactory {
    val USER_NAME = "user.name"
    val USER_API_KEY = "user.api.accessKey"
    val USER_ROLE = User.Role.USER

    val MESSAGE_ID = 1L
    val MESSAGE_TEXT = "message.text"
    val MESSAGE_TIMESTAMP = Instant.ofEpochMilli(123L)


    val HAT_NAME = "hat.name"
    val HAT_ACCESS_KEY = "hat.access.key"

    fun buildValidUser(): User {
        return User (
                USER_NAME,
                USER_ROLE,
                USER_API_KEY
        )
    }

    fun buildValidMessage(): Message {
        return Message(MESSAGE_ID, MESSAGE_TIMESTAMP, MESSAGE_TEXT, buildValidUser(), buildValidHat(), MESSAGE_TIMESTAMP)
    }


    fun buildValidHat(): Chat {
        return Chat (
                HAT_NAME,
                HAT_ACCESS_KEY,
                buildValidUser(),
                emptyList()
        )
    }
}

