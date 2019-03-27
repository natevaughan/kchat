package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.api.ContextualPrivilege
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.NotFoundException
import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.util.logger
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 12/9/17
 */
@Singleton
class DefaultChatService @Inject constructor(val chatRepo: ChatRepo, val userService: UserService): ChatService {

    val log = logger(this)

    override fun listForUserAndSpace(spaceId: String, user: User): Collection<Chat> {
        val chats = chatRepo.listForUserAndSpace(spaceId, user)

        // todo: chat repo populates these from database
        return chats.map { chat ->
            val particp = chat.participants.map { participant -> Participant(userService.findById(participant.user.id), participant.role) }
            chat.copy(participants = particp)
        }
    }

    override fun findById(id: String, requester: User): Chat {
        val chat = chatRepo.findById(id)
		// check access
        if (chat.participants.map { it.user }.contains(requester) || Chat.Type.PUBLIC_CHANNEL == chat.type) {
			return chat
		}
        throw NotFoundException("Chat not found with id $id")
    }

    override fun addUser(chatId: String, requester: User, userId: String): Boolean {
        val privileges = chatRepo.getPrivileges(chatId, userId)
		if (privileges != null && privileges.level >= ContextualPrivilege.ADMIN.level) {
			return chatRepo.addParticipant(chatId, userId)

		}
		throw UnauthorizedException("User not authorized to add users.")
    }

    override fun create(name: String, spaceId: String, user: User, type: Chat.Type, participantIds: Collection<String>): Chat {
        val participants = (participantIds).filter { it != user.id }.map { Participant(userService.findById(it), ContextualPrivilege.PARTICIPANT) }
        return chatRepo.save(Chat(UUID.randomUUID().toString(), Instant.now(), name, Space(spaceId), type, (participants + Participant(user, ContextualPrivilege.CREATOR))))
    }

	override fun checkAccess(chatId: String, user: User): Boolean {
		return chatRepo.getPrivileges(chatId, user.id) != null
	}

    override fun delete(id: String, user: User): Boolean {
		val privileges = chatRepo.getPrivileges(id, user.id)
		if (privileges != null && privileges.level >= ContextualPrivilege.CREATOR.level) {
        	return chatRepo.delete(id)
        }
		val messsage = "User ${user} is not owner of chat ${id}"
		log.warn(messsage)
		throw UnauthorizedException(messsage)
    }
}
