package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.NotFoundException
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 12/3/17
 */
@Singleton
class DefaultMessageService @Inject constructor(val messageRepo: MessageRepo, val chatService: ChatService): MessageService {

	override fun findAllSinceTimestamp(chatId: String, timestamp: Long, user: User): Iterable<Message> {
        val hat = chatService.findById(chatId, user)
        return messageRepo.findForChatSinceTimestamp(hat, Instant.ofEpochMilli(timestamp))
    }

    override fun findRecent(chatId: String, count: Int, user: User): Iterable<Message> {
        val hat = chatService.findById(chatId, user)
        return messageRepo.findRecent(hat, count)
    }

    override fun create(text: String, chatId: String, user: User): Message {
        val chat = chatService.findById(chatId, user)
        val message = Message(UUID.randomUUID().toString(), Instant.now(), text, user, chat, Instant.now())
        return messageRepo.save(message)
    }

    override fun update(text: String, id: String, user: User): Message {
        val old = findById(id)
        if (old != null) {
            checkAccess(old, user)
            return messageRepo.save(old.copy(text))
        }
        throw NotFoundException("MessageEntity ${id} not found for user ${user.id}")
    }

    override fun findById(id: String): Message? {
        return messageRepo.findById(id)
    }

    override fun delete(id: String, user: User): Boolean {
        val message = messageRepo.findById(id)
        checkAccess(message, user)
        return messageRepo.delete(message)
    }

    private fun checkAccess(old: Message, user: User) {
        if (old.author != user) {
            throw NotFoundException("MessageEntity ${old.id} not found for user ${user.id}")
        }
    }
}