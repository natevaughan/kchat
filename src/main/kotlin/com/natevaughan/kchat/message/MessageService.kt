package com.natevaughan.kchat.message

import com.natevaughan.kchat.ChatService
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.User
import java.time.Instant
import javax.inject.Inject
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 12/3/17
 */
class MessageService @Inject constructor(val messageRepo: MessageRepo, val hatService: ChatService) {

    fun findAllSinceTimestamp(hatId: Long, timestamp: Long, user: User): Iterable<Message> {
        val hat = hatService.findById(hatId, user)
        return messageRepo.findForChatSinceTimestamp(hat, Instant.ofEpochMilli(timestamp))
    }

    fun findRecent(hatId: Long, count: Int, user: User): Iterable<Message> {
        val hat = hatService.findById(hatId, user)
        return messageRepo.findRecent(hat, count)
    }

    fun create(text: String, hatId: Long, user: User): Message {
        val hat = hatService.findById(hatId, user)
        val message = Message(text, user, hat)
        return messageRepo.save(message)
    }

    fun update(text: String, id: Long, user: User): Message {
        val old = findById(id)
        if (old != null) {
            checkAccess(old, user)
            return messageRepo.save(old.copy(text))
        }
        throw NotFoundException("MessageEntity ${id} not found for user ${user.id}")
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
        throw NotFoundException("MessageEntity ${id} not found for user ${user.id}")
    }

    private fun checkAccess(old: Message, user: User) {
        if (old.author != user) {
            throw NotFoundException("MessageEntity ${old.id} not found for user ${user.id}")
        }
    }
}