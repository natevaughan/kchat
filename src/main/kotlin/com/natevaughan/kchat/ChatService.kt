package com.natevaughan.kchat

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.domain.ChatEntity
import com.natevaughan.kchat.domain.UserEntity
import com.natevaughan.kchat.user.UserService
import java.util.*
import javax.inject.Inject
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 12/9/17
 */
open class ChatService @Inject constructor(val hatRepo: ChatRepo, val userService: UserService) {

    fun listForUser(user: User): Collection<Chat> {
        return hatRepo.listForUser(user).toCollection(TreeSet())
    }

    fun findById(id: Long, user: User): Chat {
        val hat = hatRepo.findById(id)
        if (hat != null) {
            if (hat.creator == user || hat.participants.contains(user)) {
                return hat
            }
        }
        throw NotFoundException()
    }

    fun create(name: String, user: User, participantIds: Collection<Long>): Chat {
        val participants = participantIds.map { userService.findById(it) as UserEntity }
        val key = UUID.randomUUID().toString()
        if (user is UserEntity) {
            return hatRepo.save(ChatEntity(name, key, user, participants))
        }
        throw Exception("error creating chat")
    }

    fun delete(id: Long, user: User) {
        val hat = findById(id, user)
        if (user != hat.creator) {
            throw UnauthorizedException("User ${user} is not owner of hat ${hat.id}")
        }
        hatRepo.delete(hat)
    }
}
