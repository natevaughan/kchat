package com.natevaughan.kchat.util

import com.natevaughan.hat.Hat
import com.natevaughan.hat.message.Message
import com.natevaughan.hat.user.Role
import com.natevaughan.hat.user.User

/**
 * Created by nate on 11/24/17
 */
object DomainFactory {
    val USER_NAME = "user.name"
    val USER_API_KEY = "user.api.accessKey"
    val USER_ROLE = Role.USER

    val MESSAGE_TEXT = "message.text"
    val MESSAGE_TIMESTAMP = 1L


    val HAT_NAME = "hat.name"
    val HAT_ACCESS_KEY = "hat.access.key"

    fun buildValidUser(): User {
        return User(
                name = USER_NAME,
                role = USER_ROLE,
                apiKey = USER_API_KEY
        )
    }

    fun buildValidMessage(): Message {
        return Message(MESSAGE_TEXT, MESSAGE_TIMESTAMP, buildValidUser(), buildValidHat())
    }


    fun buildValidHat(): Hat {
        return Hat(
                HAT_NAME,
                HAT_ACCESS_KEY,
                buildValidUser(),
                emptyList()
        )
    }
}

