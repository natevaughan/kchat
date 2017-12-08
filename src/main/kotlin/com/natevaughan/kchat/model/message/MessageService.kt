package com.natevaughan.kchat.model.message

import com.google.inject.Inject
import com.natevaughan.kchat.model.user.User
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 12/3/17
 */
class MessageService @Inject constructor(val messageRepo: MessageRepo) {

    fun findAllSinceTimestamp(timestamp: Long): Iterable<Message> {
        return messageRepo.findAllSinceTimestamp(timestamp)
    }

    fun findRecent(count: Int): Iterable<Message> {
        return messageRepo.findRecent(count)
    }

    fun create(message: Message): Message {
        return messageRepo.save(message)
    }

    fun update(message: Message, id: Long, user: User): Message {
        val old = findById(id)
        if (old != null) {
            checkAccess(old, user)
            return messageRepo.save(old.copy(text = message.text))
        }
        throw NotFoundException("Message ${id} not found for user ${user.id}")
    }

    fun findById(id: Long): Message? {
        return messageRepo.findById(id)
    }

    fun delete(id: Long, user: User) {
        val message = messageRepo.findById(id)
        if (message != null) {
            checkAccess(message, user)
            messageRepo.delete(message)
            return
        }
        throw NotFoundException("Message ${id} not found for user ${user.id}")
    }

    private fun checkAccess(old: Message, user: User) {
        if (old.author != user) {
            throw NotFoundException("Message ${old.id} not found for user ${user.id}")
        }
    }
}