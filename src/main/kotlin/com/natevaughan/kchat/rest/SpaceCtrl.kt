package com.natevaughan.kchat.rest

import com.github.benmanes.caffeine.cache.CacheLoader
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.service.ChatService
import com.natevaughan.kchat.service.MessageService
import com.natevaughan.kchat.service.SpaceService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.BadRequestException
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 *
 * Caches handlers for each space
 */
@Singleton
@Path("space")
class SpaceCtrl {

	val spaceService: SpaceService
	val chatService: ChatService
	val messageService: MessageService
	val handlerCache: LoadingCache<String, ChatHandler>

	@Inject constructor(spaceService: SpaceService, chatService: ChatService, messageService: MessageService) {
		this.spaceService = spaceService
		this.chatService = chatService
		this.messageService = messageService
		this.handlerCache = Caffeine.newBuilder()
				.maximumSize(10_000L)
				.expireAfterAccess(5, TimeUnit.HOURS)
				.build(ChatHandlerProvider(this))
	}


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listForUser(@Context sc: SecurityContext): Collection<Space> {
		val user = sc.userPrincipal as User
        return spaceService.listForUser(user)
    }

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	fun create(@Context sc: SecurityContext, restSpace: RestSpace): Space {
		val user = sc.userPrincipal as User
		if (restSpace.name == null || restSpace.name.isEmpty()) {
			throw BadRequestException("Space name must be at least 1 character")
		}
		return spaceService.create(restSpace.name, user)
	}

	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	fun interact(@PathParam("id") id: String): Any {
		return handlerCache.get(id)!!
	}

	class ChatHandlerProvider(val spaceCtrl: SpaceCtrl) : CacheLoader<String, ChatHandler> {
		override fun load(key: String): ChatHandler {
			return ChatHandler(key, spaceCtrl.spaceService, spaceCtrl.chatService, spaceCtrl.messageService)
		}
	}
}