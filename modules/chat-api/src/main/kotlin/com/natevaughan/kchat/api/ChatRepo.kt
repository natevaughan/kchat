package com.natevaughan.kchat.api

/**
 * Created by nate on 12/9/17
 */
interface ChatRepo : Repository<Chat> {
	fun findNewForUser(user: User, timestamp: Long?): Collection<Chat>
	fun listForUserAndSpace(spaceId: String, user: User): Collection<Chat>
	fun addParticipant(chatId: String, userId: String): Boolean
	fun getPrivileges(chatId: String, userId: String): ContextualPrivilege?
	fun delete(chatId: String): Boolean
}