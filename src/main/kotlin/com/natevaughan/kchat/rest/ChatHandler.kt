package com.natevaughan.kchat.rest

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.CREATED
import com.natevaughan.kchat.framework.NotFoundException
import com.natevaughan.kchat.service.ChatService
import com.natevaughan.kchat.service.MessageService
import com.natevaughan.kchat.service.SpaceService
import java.util.concurrent.TimeUnit
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 3/21/19
 */
class ChatHandler constructor(val spaceId: String, val spaceService: SpaceService, val chatService: ChatService, val messageService: MessageService) {

	companion object {
		const val CHAT = "chat"
		const val CHAT_WITH_ID = "$CHAT/{chatId}"
		const val ADD_USER = "$CHAT_WITH_ID/addUser"
		const val MESSAGE = "$CHAT_WITH_ID/message"
		const val MESSAGE_SINCE = "$MESSAGE/since/{instant}"
		const val MESSAGE_PREVIOUS = "$MESSAGE/previous/{count}"
	}

	val accessibleCache: LoadingCache<String, Boolean> = Caffeine
			.newBuilder()
			.maximumSize(1000L)
			.expireAfterWrite(5L, TimeUnit.MINUTES)
			.build<String, Boolean> { userId ->
				spaceService.checkAccess(spaceId, userId)
			}

	@GET
	@Path(CHAT)
	@Produces(MediaType.APPLICATION_JSON)
	fun listForUser(@Context sc: SecurityContext): Collection<Chat> {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			return chatService.listForUserAndSpace(spaceId, user)
		}
		throw NotFoundException("Space not found with id $spaceId")
	}

	@POST
	@Path(CHAT)
	@Produces(MediaType.APPLICATION_JSON)
	fun create(body: RestChat, @Context sc: SecurityContext): Chat {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			val participants = body.participants ?: emptyList()
			return chatService.create(body.name, spaceId, sc.userPrincipal as User, body.type, participants)
		}
		throw NotFoundException("Space not found $spaceId")
	}

	@GET
	@Path(CHAT_WITH_ID)
	@Produces(MediaType.APPLICATION_JSON)
	fun messages(@PathParam("chatId") chatId: String, @Context sc: SecurityContext): Chat {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			return chatService.findById(chatId, sc.userPrincipal as User)
		}
		throw NotFoundException("Space not found $spaceId")
	}

	@POST
	@Path(ADD_USER)
	@Produces(MediaType.APPLICATION_JSON)
	fun addUser(@PathParam("chatId") chatId: String, addUserRequest: AddUserRequest, @Context sc: SecurityContext): Response {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			val success = chatService.addUser(chatId, sc.userPrincipal as User, addUserRequest.userId)
			if (success) {
				return CREATED
			}
		}
		throw NotFoundException("Space not found $spaceId")
	}

	@POST
	@Path(MESSAGE)
	@Produces(MediaType.APPLICATION_JSON)
	fun createMessages(@PathParam("chatId") chatId: String, message: MessageRequest, @Context sc: SecurityContext): Message {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			return messageService.create(message.text, chatId, sc.userPrincipal as User)
		}
		throw NotFoundException("Space not found $spaceId")
	}

	@GET
	@Path(MESSAGE_SINCE)
	@Produces(MediaType.APPLICATION_JSON)
	fun getMessages(@PathParam("chatId") chatId: String, @PathParam("instant") since: Long, @Context sc: SecurityContext): Iterable<Message> {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			return messageService.findAllSinceTimestamp(chatId, since, sc.userPrincipal as User)
		}
		throw NotFoundException("Space not found $spaceId")
	}

	@GET
	@Path(MESSAGE_PREVIOUS)
	@Produces(MediaType.APPLICATION_JSON)
	fun getLastMessages(@PathParam("chatId") chatId: String, @PathParam("count") count: Int, @Context sc: SecurityContext): Iterable<Message> {
		val user = sc.userPrincipal as User
		if (true == accessibleCache.get(user.id)) {
			return messageService.findRecent(chatId, count, sc.userPrincipal as User)
		}
		throw NotFoundException("Space not found $spaceId")
	}
}