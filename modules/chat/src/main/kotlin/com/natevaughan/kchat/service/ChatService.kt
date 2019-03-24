package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.User

/**
 * Created by nate on 3/22/19
 */
interface ChatService {

	fun listForUserAndSpace(spaceId: String, user: User): Collection<Chat>

	fun findById(id: String, requester: User): Chat

	fun addUser(chatId: String, requester: User, userId: String): Boolean

	fun create(name: String, spaceId: String, user: User, type: Chat.Type, participantIds: Collection<String>): Chat

	fun checkAccess(chatId: String, user: User): Boolean

	fun delete(id: String, user: User): Boolean
}
