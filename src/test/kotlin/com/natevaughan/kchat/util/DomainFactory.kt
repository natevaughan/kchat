package com.natevaughan.kchat.util

import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.domain.ChatEntity
import com.natevaughan.kchat.domain.MessageEntity
import com.natevaughan.kchat.domain.UserEntity

/**
 * Created by nate on 11/24/17
 */
object DomainFactory {
    val USER_NAME = "user.name"
    val USER_API_KEY = "user.api.accessKey"
    val USER_ROLE = User.Role.USER

    val MESSAGE_TEXT = "message.text"
    val MESSAGE_TIMESTAMP = 1L


    val HAT_NAME = "hat.name"
    val HAT_ACCESS_KEY = "hat.access.key"

    fun buildValidUser(): UserEntity {
        return UserEntity(
                name = USER_NAME,
                role = USER_ROLE,
                apiKey = USER_API_KEY
        )
    }

    fun buildValidMessage(): MessageEntity {
        return MessageEntity(MESSAGE_TEXT, MESSAGE_TIMESTAMP, buildValidUser(), buildValidHat())
    }


    fun buildValidHat(): ChatEntity {
        return ChatEntity(
                HAT_NAME,
                HAT_ACCESS_KEY,
                buildValidUser(),
                emptyList()
        )
    }
}

