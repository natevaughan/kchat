package com.natevaughan.hat.message

import com.natevaughan.hat.HatService
import com.natevaughan.hat.user.User
import javax.inject.Inject
import javax.ws.rs.NotFoundException

/**
 * Created by nate on 12/3/17
 */
class MessageService @Inject constructor(val messageRepo: MessageRepo, val hatService: HatService) {

    fun findAllSinceTimestamp(hatId: Long, timestamp: Long, user: User): Iterable<Message> {
        val hat = hatService.findById(hatId, user)
        return messageRepo.findForHatSinceTimestamp(hat, timestamp)
    }

    fun findRecent(hatId: Long, count: Int, user: User): Iterable<Message> {
        val hat = hatService.findById(hatId, user)
        return messageRepo.findRecent(hat, count)
    }

    fun create(text: String, hatId: Long, user: User): Message {
        val hat = hatService.findById(hatId, user)
        return messageRepo.save(Message(text, System.currentTimeMillis(), user, hat))
    }

    fun update(text: String, id: Long, user: User): Message {
        val old = findById(id)
        if (old != null) {
            checkAccess(old, user)
            return messageRepo.save(old.copy(text = text, lastEdited = System.currentTimeMillis()))
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